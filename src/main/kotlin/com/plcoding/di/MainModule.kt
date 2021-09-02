package com.plcoding.di

import com.plcoding.data.repository.follow.FollowRepository
import com.plcoding.data.repository.follow.FollowRepositoryImpl
import com.plcoding.data.repository.likes.LikeRepository
import com.plcoding.data.repository.likes.LikeRepositoryImpl
import com.plcoding.data.repository.post.PostRepository
import com.plcoding.data.repository.post.PostRepositoryImpl
import com.plcoding.data.repository.user.UserRepository
import com.plcoding.data.repository.user.UserRepositoryImpl
import com.plcoding.service.FollowService
import com.plcoding.service.LikeService
import com.plcoding.service.PostService
import com.plcoding.service.UserService
import com.plcoding.util.Constants
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase(Constants.DATABASE_NAME)
    }
    single<UserRepository> {
        UserRepositoryImpl(get())
    }
    single<FollowRepository> {
        FollowRepositoryImpl(get())
    }
    single<PostRepository> {
        PostRepositoryImpl(get())
    }
    single<LikeRepository> {
        LikeRepositoryImpl(get())
    }
    single { UserService(get()) }
    single { FollowService(get()) }
    single { PostService(get()) }
    single { LikeService(get()) }
}