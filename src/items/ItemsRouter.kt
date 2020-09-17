package net.ambitious.sharebookmarks.items

import io.ktor.routing.*
import io.ktor.util.*
import org.koin.ktor.ext.inject

object ItemsRouter {
  @KtorExperimentalAPI
  fun Routing.items(key: AttributeKey<Int>) {
    val controller: ItemsController by inject()

    route("/items") {
      get("/list") {
      }
      post("/save") {
      }
      delete("/delete") {
      }
    }
  }
}