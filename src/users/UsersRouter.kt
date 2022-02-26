package net.ambitious.sharebookmarks.users

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.routing.post
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject

object UsersRouter {

  fun Routing.users() {
    val controller: UsersController by inject()

    route("/users") {
      post("/auth") {
        call.receive<UsersModel.UserRequest>().let {
          call.respond(transaction { controller.auth(it, application.environment) })
        }
      }
    }
  }

}

