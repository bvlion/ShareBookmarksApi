package net.ambitious.sharebookmarks.index

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import net.ambitious.sharebookmarks.Util

object IndexRouter {
  @KtorExperimentalAPI
  fun Routing.index() {
    route("/") {
      get {
        call.respond(Util.datetimeFormat(null))
      }
    }
  }
}