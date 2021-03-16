package com.mkspace.newscalendar.data.vo.remotekey

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article_remote_keys")
data class ArticleRemoteKey(
    @PrimaryKey @ColumnInfo(name = "query_date") val queryDate: String,
    val after: Int?
)