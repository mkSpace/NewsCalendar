package com.mkspace.newscalendar.data

import androidx.room.Dao
import com.mkspace.newscalendar.data.vo.Article

@Dao
abstract class ArticleDao : BaseDao<Article>()