package com.example.verviapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.verviapp.data.dao.UserDao
import com.example.verviapp.data.dao.ChatDao
import com.example.verviapp.data.dao.RequestDetailsDao
import com.example.verviapp.data.database.AppDatabase
import com.example.verviapp.data.dao.RequestDao
import com.example.verviapp.data.repository.SampleData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        AppDatabase.DATABASE_NAME
    )
    .addCallback(object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Se ejecuta UNA SOLA VEZ cuando se crea la BD por primera vez
            // Inserta datos de muestra en background
            GlobalScope.launch(Dispatchers.IO) {
                val database = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    AppDatabase.DATABASE_NAME
                ).build()
                database.userDao().insertUsers(SampleData.sampleUsers)
                database.requestDao().insertRequests(SampleData.sampleRequests)
                database.requestDetailsDao().insertAttachments(SampleData.sampleRequestAttachments)
                database.chatDao().insertConversations(SampleData.sampleConversations)
                database.chatDao().insertMessages(SampleData.sampleMessages)
                database.close()
            }
        }
    })
    .build()

    @Singleton
    @Provides
    fun provideRequestDao(database: AppDatabase): RequestDao = database.requestDao()

    @Singleton
    @Provides
    fun provideRequestDetailsDao(database: AppDatabase): RequestDetailsDao = database.requestDetailsDao()

    @Singleton
    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Singleton
    @Provides
    fun provideChatDao(database: AppDatabase): ChatDao = database.chatDao()
}
