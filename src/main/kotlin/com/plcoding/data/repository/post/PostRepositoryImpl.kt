package com.plcoding.data.repository.post

import com.plcoding.data.models.Following
import com.plcoding.data.models.Like
import com.plcoding.data.models.Post
import com.plcoding.data.models.User
import com.plcoding.data.responses.PostResponse
import com.plcoding.util.Constants
import org.litote.kmongo.`in`
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.inc

class PostRepositoryImpl(
    db: CoroutineDatabase
) : PostRepository {

    private val posts = db.getCollection<Post>()
    private val following = db.getCollection<Following>()
    private val users = db.getCollection<User>()
    private val likes = db.getCollection<Like>()

    override suspend fun createPost(post: Post): Boolean {
        return posts.insertOne(post).wasAcknowledged().also { wasAcknowledged ->
            if(wasAcknowledged) {
                users.updateOneById(
                    post.userId,
                    inc(User::postCount, 1)
                )
            }
        }
    }

    override suspend fun deletePost(postId: String) {
        posts.findOneById(postId)?.also {
            users.updateOneById(
                it.userId,
                inc(User::postCount, -1)
            )
        }
        posts.deleteOneById(postId)
    }

    override suspend fun getPostsByFollows(
        ownUserId: String,
        page: Int,
        pageSize: Int
    ): List<PostResponse> {
        val userIdsFromFollows = following.find(Following::followingUserId eq ownUserId)
            .toList()
            .map {
                it.followedUserId
            }
        return posts.find(Post::userId `in` userIdsFromFollows)
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Post::timestamp)
            .toList()
            .map { post ->
                val isLiked = likes.findOne(and(
                    Like::parentId eq post.id,
                    Like::userId eq ownUserId
                )) != null
                val user = users.findOneById(post.userId)
                PostResponse(
                    id = post.id,
                    userId = ownUserId,
                    username = user?.username ?: "",
                    imageUrl = post.imageUrl,
                    profilePictureUrl = user?.profileImageUrl ?: "",
                    description = post.description,
                    likeCount = post.likeCount,
                    commentCount = post.commentCount,
                    isLiked = isLiked,
                    isOwnPost = ownUserId == post.userId
                )
            }
    }

    override suspend fun getPostsForProfile(ownUserId: String, userId: String, page: Int, pageSize: Int): List<PostResponse> {
        val user = users.findOneById(userId) ?: return emptyList()
        return posts.find(Post::userId eq userId)
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Post::timestamp)
            .toList()
            .map { post ->
                val isLiked = likes.findOne(and(
                    Like::parentId eq post.id,
                    Like::userId eq ownUserId
                )) != null
                PostResponse(
                    id = post.id,
                    userId = userId,
                    username = user.username,
                    imageUrl = post.imageUrl,
                    profilePictureUrl = user.profileImageUrl,
                    description = post.description,
                    likeCount = post.likeCount,
                    commentCount = post.commentCount,
                    isLiked = isLiked,
                    isOwnPost = ownUserId == post.userId
                )
            }
    }

    override suspend fun getPost(postId: String): Post? {
        return posts.findOneById(postId)
    }

    override suspend fun getPostDetails(userId: String, postId: String): PostResponse? {
        val isLiked = likes.findOne(Like::userId eq userId) != null
        val post =  posts.findOneById(postId) ?: return null
        val user = users.findOneById(post.userId) ?: return null
        return PostResponse(
            id = post.id,
            userId = user.id,
            username = user.username,
            imageUrl = post.imageUrl,
            profilePictureUrl = user.profileImageUrl,
            description = post.description,
            likeCount = post.likeCount,
            commentCount = post.commentCount,
            isLiked = isLiked,
            isOwnPost = userId == post.userId
        )
    }
}