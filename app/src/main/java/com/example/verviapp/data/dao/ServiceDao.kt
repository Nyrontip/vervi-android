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

