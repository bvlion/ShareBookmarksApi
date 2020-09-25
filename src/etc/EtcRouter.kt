package net.ambitious.sharebookmarks.etc

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import net.ambitious.sharebookmarks.Util
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject

object EtcRouter {
  @KtorExperimentalAPI
  fun Routing.index() {
    route("/") {
      get {
        call.respond(Util.datetimeFormat(null))
      }
    }
  }

  fun Routing.etc() {
    val controller: EtcController by inject()
    route("/etc") {
      get("/{lang}/terms_of_use") {
        call.respond(transaction { controller.message(call.parameters["lang"], 1) })
      }
      get("/{lang}/privacy_policy") {
        call.respond(transaction { controller.message(call.parameters["lang"], 2) })
      }
    }
  }
}