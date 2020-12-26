package net.ambitious.sharebookmarks.notifications

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.routing.get
import net.ambitious.sharebookmarks.Util
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject

object NotificationsRouter {
  fun Routing.notifications() {
    val controller: NotificationsController by inject()

    route("/notifications") {
      get("/list") {
        call.respond(transaction { controller.getList(null) })
      }
      authenticate {
        get("/auth/list") {
          call.respond(transaction { controller.getList(call.principal<Util.AuthUser>()!!.id) })
        }
      }
    }
  }
}