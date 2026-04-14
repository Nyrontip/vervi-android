package com.example.verviapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.verviapp.data.entity.RequestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RequestDao {

    @Query("SELECT * FROM requests")
    fun getAllRequests(): Flow<List<RequestEntity>>

    @Query("SELECT * FROM requests WHERE isActive = :active")
    fun getRequestsByStatus(active: Boolean): Flow<List<RequestEntity>>

    @Query(
        """
        SELECT
            r.id AS id,
            r.title AS title,
            r.location AS location,
            r.budgetCop AS budgetCop,
            r.applicationCount AS applicationCount,
            r.isUrgent AS isUrgent,
            r.imageUrl AS imageUrl,
            c.name AS categoryName
        FROM requests r
        LEFT JOIN categories c ON c.id = r.categoryId
        WHERE r.isActive = 1
          AND (
            :text = ''
            OR LOWER(r.title) LIKE '%' || LOWER(:text) || '%'
            OR LOWER(r.description) LIKE '%' || LOWER(:text) || '%'
            OR LOWER(r.location) LIKE '%' || LOWER(:text) || '%'
          )
          AND (
            :category = 'Todos'
            OR c.name = :category
          )
        ORDER BY COALESCE(r.requiredDateMillis, r.createdAt) DESC, r.id DESC
        """
    )
    fun observeHomeRequests(text: String, category: String): Flow<List<HomeRequestRow>>

    @Query("SELECT * FROM requests WHERE id = :id")
    suspend fun getRequestById(id: Int): RequestEntity?

    @Insert
    suspend fun insertRequest(request: RequestEntity): Long

    @Insert
    suspend fun insertRequests(requests: List<RequestEntity>)

    @Update
    suspend fun updateRequest(request: RequestEntity)

    @Delete
    suspend fun deleteRequest(request: RequestEntity)

    @Query("DELETE FROM requests")
    suspend fun deleteAllRequests()
}

data class HomeRequestRow(
    val id: Int,
    val title: String,
    val location: String,
    val budgetCop: Long?,
    val applicationCount: Int,
    val isUrgent: Boolean,
    val imageUrl: String,
    val categoryName: String?
)
