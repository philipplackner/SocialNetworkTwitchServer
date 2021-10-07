package com.plcoding.di

import com.google.gson.Gson
import com.plcoding.data.models.Post
import com.plcoding.data.models.Skill
import com.plcoding.data.repository.activity.ActivityRepository
import com.plcoding.data.repository.activity.ActivityRepositoryImpl
import com.plcoding.data.repository.comment.CommentRepository
import com.plcoding.data.repository.comment.CommentRepositoryImpl
import com.plcoding.data.repository.follow.FollowRepository
import com.plcoding.data.repository.follow.FollowRepositoryImpl
import com.plcoding.data.repository.likes.LikeRepository
import com.plcoding.data.repository.likes.LikeRepositoryImpl
import com.plcoding.data.repository.post.PostRepository
import com.plcoding.data.repository.post.PostRepositoryImpl
import com.plcoding.data.repository.skill.SkillRepository
import com.plcoding.data.repository.skill.SkillRepositoryImpl
import com.plcoding.data.repository.user.UserRepository
import com.plcoding.data.repository.user.UserRepositoryImpl
import com.plcoding.service.*
import com.plcoding.util.Constants
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    single<CommentRepository> {
        CommentRepositoryImpl(get())
    }
    single<ActivityRepository> {
        ActivityRepositoryImpl(get())
    }
    single<SkillRepository> {
        SkillRepositoryImpl(get())
    }
    single { UserService(get(), get()) }
    single { FollowService(get()) }
    single { PostService(get()) }
    single { LikeService(get(), get(), get()) }
    single { CommentService(get()) }
    single { ActivityService(get(), get(), get()) }
    single { SkillService(get()) }

    single { Gson() }
}