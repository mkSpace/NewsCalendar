package com.mkspace.newscalendar.data.vo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "article_query_relations",
    primaryKeys = ["relation_query_date", "relation_article_original_link"],
    foreignKeys = [
        ForeignKey(
            entity = Article::class,
            parentColumns = ["article_original_link"],
            childColumns = ["relation_article_original_link"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("relation_query_date")]
)
data class ArticleQueryDateRelation(
    @ColumnInfo(name = "relation_article_original_link") val articleOriginalLink: String,
    @ColumnInfo(name = "relation_query_date") val queryDate: String
)