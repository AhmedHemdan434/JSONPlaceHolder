package com.hemdan.jsonplaceholder.presentation.work/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.hemdan.jsonplaceholder.repository.PostRepository
import com.hemdan.jsonplaceholder.repository.database.getPostsDatabase
import com.hemdan.jsonplaceholder.repository.database.model.PostState
import retrofit2.HttpException

class SyncDataWorker(appContext: Context, params: WorkerParameters):
        CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "com.hemdan.jsonplaceholder.presentation.work.RefreshDataWorker"
    }

    /**
     * A coroutine-friendly method to do your work.
     * Note: In recent work version upgrade, 1.0.0-alpha12 and onwards have a breaking change.
     * The doWork() function now returns Result instead of Payload because they have combined Payload into Result.
     * Read more here - https://developer.android.com/jetpack/androidx/releases/work#1.0.0-alpha12
     */
    override suspend fun doWork(): Result {
        val database = getPostsDatabase(applicationContext)
        val repository = PostRepository(database)
        return try {
            if (database.postsDao.countPosts() == 0) {
                repository.fetchPostsAndInsert()
            } else {
                // Sync posts from the network
                database.postsDao.getNeedToSyncPosts().forEach() { post ->
                    when (post.state) {
                        PostState.CREATED -> repository.createPost(post)
                        PostState.EDITED -> repository.editPost(post)
                        PostState.DELETED -> repository.deletePost(post)
                        else -> {}
                    }
                    post.state = PostState.SYNCED
                    database.postsDao.updatePost(post)
                }
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
