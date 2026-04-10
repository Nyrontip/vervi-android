package com.example.verviapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.verviapp.data.entity.ConversationEntity
import com.example.verviapp.data.entity.RequestAttachmentEntity
import com.example.verviapp.data.entity.RequestEntity
import com.example.verviapp.data.entity.UserEntity

@Dao
interface RequestDetailsDao {

    @Query("SELECT * FROM requests WHERE id = :requestId LIMIT 1")
    suspend fun getRequestById(requestId: Int): RequestEntity?

    @Query(
        """
        SELECT * FROM request_attachments
        WHERE requestId = :requestId
        ORDER BY sortOrder ASC, id ASC
        """
    )
    suspend fun getAttachmentsByRequestId(requestId: Int): List<RequestAttachmentEntity>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): UserEntity?

    @Query(
        """
        SELECT * FROM conversations
        WHERE requestId = :requestId
        ORDER BY COALESCE(lastMessageAt, createdAt) DESC
        LIMIT 1
        """
    )
    suspend fun getConversationByRequestId(requestId: Int): ConversationEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAttachments(attachments: List<RequestAttachmentEntity>)
}

