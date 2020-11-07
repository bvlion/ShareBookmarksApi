package net.ambitious.sharebookmarks.shares

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import net.ambitious.sharebookmarks.Util
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject

object SharesRouter {
  fun Routing.shares() {
    val controller: SharesController by inject()

    route("/shares") {
      authenticate {
        get("/list") {
          call.respond(transaction { controller.getShares(call.principal<Util.AuthUser>()!!.id) })
        }

        post("/save") {
          call.receive<Array<SharesModel.PostShare>>().let {
            call.respond(transaction {
              controller.saveShares(
                call.principal<Util.AuthUser>()!!.id,
                it
              )
            })
          }
        }

        delete("/delete") {
          call.principal<Util.AuthUser>()!!.run {
            call.receive<Array<SharesModel.DeleteRequest>>().let {
              call.respond(transaction {
                controller.deleteShares(it)
              })
            }
          }
        }
      }
    }
  }
}