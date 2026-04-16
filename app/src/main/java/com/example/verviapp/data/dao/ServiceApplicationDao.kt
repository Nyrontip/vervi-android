package com.example.verviapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.verviapp.data.entity.ServiceApplicationEntity

@Dao
interface ServiceApplicationDao {

    @Insert
    suspend fun insertApplication(application: ServiceApplicationEntity): Long

    @Query(
        """
        UPDATE requests
        SET applicationCount = applicationCount + 1,
            applications = CAST(applicationCount + 1 AS TEXT) || ' Postulaciones',
            updatedAt = :updatedAt
        WHERE id = :requestId
        """
    )
    suspend fun increaseRequestApplications(requestId: Int, updatedAt: Long): Int

    @Transaction
    suspend fun submitApplication(application: ServiceApplicationEntity): Boolean {
        val insertedId = insertApplication(application)
        val updatedRows = increaseRequestApplications(
            requestId = application.requestId,
            updatedAt = System.currentTimeMillis()
        )
        return insertedId > 0L && updatedRows > 0
    }
}

