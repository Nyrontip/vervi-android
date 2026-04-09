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