package com.plcoding.data.repository.comment

import com.plcoding.data.models.Comment
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class CommentRepositoryImpl(
    db: CoroutineDatabase
) : CommentRepository {

    val comments = db.getCollection<Comment>()

    override suspend fun createComment(comment: Comment) {
        comments.insertOne(comment)
    }

    override suspend fun deleteComment(commentId: String): Boolean {
        val deleteCount = comments.deleteOneById(commentId).deletedCount
        return deleteCount > 0
    }

    override suspend fun getCommentsForPost(postId: String): List<Comment> {
        return comments.find(Comment::postId eq postId).toList()
    }

    override suspend fun getComment(commentId: String): Comment? {
        return comments.findOneById(commentId)
    }
}