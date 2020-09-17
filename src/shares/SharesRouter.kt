package net.ambitious.sharebookmarks.shares

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject

object SharesRouter {
  @KtorExperimentalAPI
  fun Routing.shares(key: AttributeKey<Int>) {
    val controller: SharesController by inject()

    route("/shares") {
      get("/list") {
        call.respond(transaction { controller.getShares(call.attributes[key]) })
      }

      post("/save") {
        call.receive<Array<SharesModel.PostShare>>().let {
          call.respond(transaction {
            controller.saveShares(
              call.attributes[key],
              it
            )
          })
        }
      }

      delete("/delete") {
        call.attributes[key].run {
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