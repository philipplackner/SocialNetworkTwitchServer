package com.plcoding.routes

import com.plcoding.service.ActivityService
import com.plcoding.util.Constants
import com.plcoding.util.QueryParams
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getActivities(
    activityService: ActivityService,
) {
    authenticate {
        get("/api/activity/get") {
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?:
            Constants.DEFAULT_PAGE_SIZE

            val activities = activityService.getActivitiesForUser(call.userId, page, pageSize)
            call.respond(
                HttpStatusCode.OK,
                activities
            )
        }
    }
}
