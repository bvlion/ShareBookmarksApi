package net.ambitious.sharebookmarks.items

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import net.ambitious.sharebookmarks.Util
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject

object ItemsRouter {
  @KtorExperimentalAPI
  fun Routing.items() {
    val controller: ItemsController by inject()

    route("/items") {

      get("/list") {
        call.respond(transaction { controller.getItems(call.principal<Util.AuthUser>()!!.id) })
      }

      put("/parents") {
        call.receive<Array<ItemsModel.PostParents>>().let {
          call.respond(transaction {
            controller.setParentIds(it, call.principal<Util.AuthUser>()!!.id)
          })
        }
      }

      post("/save") {
        call.receive<Array<ItemsModel.PostItem>>().let {
          call.respond(transaction {
            controller.saveItems(
              call.principal<Util.AuthUser>()!!.id,
              it
            )
          })
        }
      }

      delete("/delete") {
        call.principal<Util.AuthUser>()!!.run {
          call.receive<Array<ItemsModel.DeleteRequest>>().let {
            call.respond(transaction {
              controller.deleteItems(it)
            })
          }
        }
      }
    }
  }
}