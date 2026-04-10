package com.example.verviapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.verviapp.data.entity.CategoryEntity
import com.example.verviapp.data.entity.UserWithCategories
import com.example.verviapp.data.entity.UserCategoryCrossRef
import com.example.verviapp.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): UserEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserCategories(userCategories: List<UserCategoryCrossRef>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM user_categories WHERE userId = :userId")
    suspend fun deleteUserCategoriesByUserId(userId: Int)

    @Query("SELECT * FROM categories WHERE name IN (:names)")
    suspend fun getCategoriesByNames(names: List<String>): List<CategoryEntity>

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserWithCategoriesById(userId: Int): UserWithCategories?

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun observeUserWithCategoriesById(userId: Int): Flow<UserWithCategories?>

    @Query("SELECT name FROM categories ORDER BY name ASC")
    suspend fun getCategoryNames(): List<String>

    @Transaction
    @Query(
        """
        SELECT * FROM users
        WHERE users.isProvider = 1
          AND (
              :text = ''
              OR LOWER(users.name) LIKE '%' || LOWER(:text) || '%'
              OR LOWER(users.bio) LIKE '%' || LOWER(:text) || '%'
              OR LOWER(users.location) LIKE '%' || LOWER(:text) || '%'
              OR EXISTS (
                  SELECT 1
                  FROM user_categories uc
                  INNER JOIN categories c ON c.id = uc.categoryId
                  WHERE uc.userId = users.id
                    AND LOWER(c.name) LIKE '%' || LOWER(:text) || '%'
              )
          )
          AND (
              :category = 'Todos'
              OR EXISTS (
                  SELECT 1
                  FROM user_categories uc
                  INNER JOIN categories c ON c.id = uc.categoryId
                  WHERE uc.userId = users.id
                    AND c.name = :category
              )
          )
        ORDER BY users.rating DESC, users.reviewCount DESC, users.name ASC
        """
    )
    fun observeProviders(text: String, category: String): Flow<List<UserWithCategories>>
}
