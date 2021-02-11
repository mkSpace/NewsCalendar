package com.mkspace.newscalendar.data.vo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "article_id") val id: Int = 0,
    @SerializedName("title") @ColumnInfo(name = "article_title") val title: String,
    @SerializedName("link") @ColumnInfo(name = "article_link") val link: String,
    @SerializedName("originallink") @ColumnInfo(name = "article_original_link") val originalLink: String,
    @SerializedName("description") @ColumnInfo(name = "article_description") val description: String,
    @SerializedName("pubDate") @ColumnInfo(name = "article_publish_date") val publishDate: String
)