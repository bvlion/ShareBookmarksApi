package net.ambitious.sharebookmarks.notifications

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.routing.get
import io.ktor.util.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject

object NotificationsRouter {
  @KtorExperimentalAPI
  @KtorExperimentalLocationsAPI
  fun Routing.notifications() {
    val controller: NotificationsController by inject()

    route("/notifications") {
      get("/list") {
        call.respond(transaction {
          controller.getList(
            call.request.parseAuthorizationHeader(),
            application.environment
          )
        })
      }
    }
  }
}