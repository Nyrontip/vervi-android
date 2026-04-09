package com.example.verviapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.verviapp.data.entity.ConversationEntity
import com.example.verviapp.data.entity.MessageEntity
import com.example.verviapp.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Query(
        """
        SELECT id FROM users
        WHERE email = :email
        LIMIT 1
        """
    )
    suspend fun getUserIdByEmail(email: String): Int?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Query(
        """
        SELECT id FROM conversations
        WHERE (participantAUserId = :userAId AND participantBUserId = :userBId)
           OR (participantAUserId = :userBId AND participantBUserId = :userAId)
        LIMIT 1
        """
    )
    suspend fun getConversationId(userAId: Int, userBId: Int): Int?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertConversation(conversation: ConversationEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertConversations(conversations: List<ConversationEntity>)

    @Query(
        """
        SELECT * FROM messages
        WHERE conversationId = :conversationId
        ORDER BY sentAt ASC
        """
    )
    fun observeMessages(conversationId: Int): Flow<List<MessageEntity>>

    @Query(
        """
        SELECT * FROM messages
        WHERE conversationId = :conversationId
        ORDER BY sentAt ASC
        """
    )
    suspend fun getMessages(conversationId: Int): List<MessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Update
    suspend fun updateMessage(message: MessageEntity)

    @Query("SELECT COUNT(*) FROM messages WHERE conversationId = :conversationId")
    suspend fun countMessages(conversationId: Int): Int
}


