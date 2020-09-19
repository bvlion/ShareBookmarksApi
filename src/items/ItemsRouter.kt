package net.ambitious.sharebookmarks.items

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject

object ItemsRouter {
  @KtorExperimentalAPI
  fun Routing.items(key: AttributeKey<Int>) {
    val controller: ItemsController by inject()

    route("/items") {

      get("/list") {
        call.respond(transaction { controller.getItems(call.attributes[key]) })
      }

      put("/parents") {
        call.attributes[key].run {
          call.receive<Array<ItemsModel.PostParents>>().let {
            call.respond(transaction {
              controller.setParentIds(it)
            })
          }
        }
      }

      post("/save") {
        call.receive<Array<ItemsModel.PostItem>>().let {
          call.respond(transaction {
            controller.saveItems(
              call.attributes[key],
              it
            )
          })
        }
      }

      delete("/delete") {
        call.attributes[key].run {
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