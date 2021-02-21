package com.mkspace.newscalendar.data.vo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey @SerializedName("originallink") @ColumnInfo(name = "article_original_link") val originalLink: String,
    @SerializedName("title") @ColumnInfo(name = "article_title") val title: String,
    @SerializedName("link") @ColumnInfo(name = "article_link") val link: String,
    @SerializedName("description") @ColumnInfo(name = "article_description") val description: String,
    @SerializedName("pubDate") @ColumnInfo(name = "article_publish_date") val publishDate: String,
    @ColumnInfo(name = "article_created_at") val createdAt: Long
)