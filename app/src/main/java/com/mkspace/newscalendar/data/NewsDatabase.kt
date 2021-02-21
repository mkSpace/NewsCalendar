package com.mkspace.newscalendar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.funin.base.funinbase.BuildConfig
import com.mkspace.newscalendar.data.vo.Article

@Database(
    entities = [Article::class],
    version = 1
)
abstract class NewsDatabase : RoomDatabase() {

    companion object {
        private const val IS_CLEAR_ALL = false

        private const val DM_NAME = "news_calendar.db"

        @Volatile
        private var INSTANCE: NewsDatabase? = null

        fun getInstance(context: Context): NewsDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context): NewsDatabase =
            Room.databaseBuilder(context, NewsDatabase::class.java, DM_NAME)
                .fallbackToDestructiveMigration()
                .apply {
                    if (BuildConfig.DEBUG && IS_CLEAR_ALL) {
                        addCallback(CALLBACK_CLEAR_ALL)
                    }
                }
                .build()

        private val CALLBACK_CLEAR_ALL = object : Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // TODO
            }
        }
    }

    abstract fun articleDao(): ArticleDao
}