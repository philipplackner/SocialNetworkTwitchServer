package com.plcoding.util

object Constants {

    const val DATABASE_NAME = "social_network_twitch"

    const val DEFAULT_PAGE_SIZE = 15
    const val DEFAULT_ACTIVITY_PAGE_SIZE = 15

    const val MAX_COMMENT_LENGTH = 2000

    const val BASE_URL = "http://192.168.0.3:8001/"
    const val PROFILE_PICTURE_PATH = "build/resources/main/static/profile_pictures/"
    const val BANNER_IMAGE_PATH = "build/resources/main/static/banner_images/"
    const val POST_PICTURE_PATH = "build/resources/main/static/post_pictures/"
    const val DEFAULT_PROFILE_PICTURE_PATH = "${BASE_URL}profile_pictures/avatar.svg"
    const val DEFAULT_BANNER_IMAGE_PATH = "${BASE_URL}profile_pictures/defaultbanner.png"
}