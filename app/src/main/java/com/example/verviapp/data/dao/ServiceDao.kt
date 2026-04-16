package com.example.verviapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.verviapp.data.entity.ReviewEntity
import com.example.verviapp.data.entity.ServiceEntity
import com.example.verviapp.data.entity.ServiceEvidenceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {

    @Query(
        """
        SELECT
            s.id AS serviceId,
            s.title AS title,
            provider.name AS counterpartName,
            COALESCE(s.completedAt, s.updatedAt, s.createdAt) AS dateMillis,
            s.totalPriceCop AS totalPriceCop,
            (
                SELECT se.imageUrl
                FROM service_evidence se
                WHERE se.serviceId = s.id
                ORDER BY se.sortOrder ASC, se.id ASC
                LIMIT 1
            ) AS imageUrl,
            (
                SELECT r.rating
                FROM reviews r
                WHERE r.serviceId = s.id
                  AND r.reviewerUserId = :userId
                ORDER BY r.createdAt DESC, r.id DESC
                LIMIT 1
            ) AS rating
        FROM services s
        INNER JOIN users provider ON provider.id = s.providerUserId
        WHERE s.clientUserId = :userId
        ORDER BY dateMillis DESC
        """
    )
    fun observeHistoryAsClient(userId: Int): Flow<List<ServiceHistoryRow>>

    @Query(
        """
        SELECT
            s.id AS serviceId,
            s.title AS title,
            client.name AS counterpartName,
            COALESCE(s.completedAt, s.updatedAt, s.createdAt) AS dateMillis,
            s.totalPriceCop AS totalPriceCop,
            (
                SELECT se.imageUrl
                FROM service_evidence se
                WHERE se.serviceId = s.id
                ORDER BY se.sortOrder ASC, se.id ASC
                LIMIT 1
            ) AS imageUrl,
            (
                SELECT r.rating
                FROM reviews r
                WHERE r.serviceId = s.id
                  AND r.reviewerUserId = :userId
                ORDER BY r.createdAt DESC, r.id DESC
                LIMIT 1
            ) AS rating
        FROM services s
        INNER JOIN users client ON client.id = s.clientUserId
        WHERE s.providerUserId = :userId
        ORDER BY dateMillis DESC
        """
    )
    fun observeHistoryAsProvider(userId: Int): Flow<List<ServiceHistoryRow>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<ServiceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServiceEvidence(evidence: List<ServiceEvidenceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<ReviewEntity>)

    @Query(
        """
        SELECT
            s.id AS serviceId,
            counterpart.id AS counterpartUserId,
            counterpart.name AS counterpartName,
            counterpart.photoUrl AS counterpartAvatarUrl,
            review.rating AS existingRating,
            review.comment AS existingComment,
            review.evidenceImageUrl AS existingEvidenceImageUrl
        FROM services s
        INNER JOIN users counterpart
            ON counterpart.id = CASE
                WHEN s.clientUserId = :reviewerUserId THEN s.providerUserId
                ELSE s.clientUserId
            END
        LEFT JOIN reviews review
            ON review.id = (
                SELECT r.id
                FROM reviews r
                WHERE r.serviceId = s.id
                  AND r.reviewerUserId = :reviewerUserId
                ORDER BY r.createdAt DESC, r.id DESC
                LIMIT 1
            )
        WHERE s.id = :serviceId
          AND (s.clientUserId = :reviewerUserId OR s.providerUserId = :reviewerUserId)
        LIMIT 1
        """
    )
    suspend fun getRateServiceRow(serviceId: Int, reviewerUserId: Int): ServiceRateRow?

    @Query(
        """
        SELECT id
        FROM reviews
        WHERE serviceId = :serviceId
          AND reviewerUserId = :reviewerUserId
        ORDER BY createdAt DESC, id DESC
        LIMIT 1
        """
    )
    suspend fun getLatestReviewId(serviceId: Int, reviewerUserId: Int): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity): Long

    @Query(
        """
        SELECT
            s.id AS serviceId,
            s.title AS title,
            s.summary AS summary,
            s.location AS location,
            s.totalPriceCop AS totalPriceCop,
            COALESCE(s.completedAt, s.updatedAt, s.createdAt) AS dateMillis,
            s.status AS status,
            CASE
                WHEN s.providerUserId = :viewerUserId THEN 'Como: Prestador'
                ELSE 'Como: Cliente'
            END AS roleLabel,
            counterpart.name AS counterpartName,
            counterpart.rating AS counterpartRating,
            counterpart.location AS counterpartLocation,
            counterpart.photoUrl AS counterpartAvatarUrl,
            (
                SELECT c.lastMessagePreview
                FROM conversations c
                WHERE c.serviceId = s.id
                ORDER BY COALESCE(c.lastMessageAt, c.createdAt) DESC
                LIMIT 1
            ) AS chatPreview
        FROM services s
        INNER JOIN users counterpart
            ON counterpart.id = CASE
                WHEN s.clientUserId = :viewerUserId THEN s.providerUserId
                ELSE s.clientUserId
            END
        WHERE s.id = :serviceId
        LIMIT 1
        """
    )
    suspend fun getServiceDetailsRow(serviceId: Int, viewerUserId: Int): ServiceDetailsRow?

    @Query(
        """
        SELECT imageUrl
        FROM service_evidence
        WHERE serviceId = :serviceId
        ORDER BY sortOrder ASC, id ASC
        """
    )
    suspend fun getServiceEvidenceUrls(serviceId: Int): List<String>
}

data class ServiceHistoryRow(
    val serviceId: Int,
    val title: String,
    val counterpartName: String,
    val dateMillis: Long,
    val totalPriceCop: Long,
    val imageUrl: String?,
    val rating: Int?
)

data class ServiceRateRow(
    val serviceId: Int,
    val counterpartUserId: Int,
    val counterpartName: String,
    val counterpartAvatarUrl: String?,
    val existingRating: Int?,
    val existingComment: String?,
    val existingEvidenceImageUrl: String?
)

data class ServiceDetailsRow(
    val serviceId: Int,
    val title: String,
    val summary: String,
    val location: String,
    val totalPriceCop: Long,
    val dateMillis: Long,
    val status: String,
    val roleLabel: String,
    val counterpartName: String,
    val counterpartRating: Float,
    val counterpartLocation: String,
    val counterpartAvatarUrl: String?,
    val chatPreview: String?
)

