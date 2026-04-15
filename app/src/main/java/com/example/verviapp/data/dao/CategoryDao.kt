package com.example.verviapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.verviapp.data.entity.CategoryEntity
import com.example.verviapp.data.entity.UserCategoryCrossRef

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserCategories(userCategories: List<UserCategoryCrossRef>)

    @Query("DELETE FROM user_categories WHERE userId = :userId")
    suspend fun deleteUserCategoriesByUserId(userId: Int)

    @Query("SELECT * FROM categories WHERE name IN (:names)")
    suspend fun getCategoriesByNames(names: List<String>): List<CategoryEntity>

    @Query("SELECT name FROM categories ORDER BY name ASC")
    suspend fun getCategoryNames(): List<String>
}
