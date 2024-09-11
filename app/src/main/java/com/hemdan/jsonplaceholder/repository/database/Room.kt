package com.hemdan.jsonplaceholder.repository.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.hemdan.jsonplaceholder.repository.database.model.Post
import com.hemdan.jsonplaceholder.repository.database.model.PostStateConverter

@Dao
interface PostsDao {
    @Transaction
    @Query("select * from Post where state != 'DELETED'")
    fun getPosts(): LiveData<List<Post>>

    @Transaction
    @Query("select * from Post where state != 'SYNCED'")
    fun getNeedToSyncPosts(): List<Post>

    @Transaction
    @Query("SELECT COUNT(*) FROM post")
    fun countPosts(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts: List<Post>)

    @Update
    fun updatePost(post: Post)

    @Delete
    fun deletePost(post: Post)
}

@Database(entities = [Post::class], version = 1)
@TypeConverters(PostStateConverter::class)
abstract class PostDatabase : RoomDatabase() {
    abstract val postsDao: PostsDao
}

private lateinit var INSTANCE: PostDatabase

fun getPostsDatabase(context: Context): PostDatabase {
    synchronized(PostDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                PostDatabase::class.java,
                "post_DB").build()
        }
    }
    return INSTANCE
}