package net.ambitious.sharebookmarks.etc

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import net.ambitious.sharebookmarks.Util
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject

object EtcRouter {
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

      get("/ogp") {
        call.respond(controller.getHttpOgpImage(call.request.queryParameters["url"]))
      }

      get("/{lang}/faq") {
        call.respond(transaction { controller.faq(call.parameters["lang"]) })
      }
    }
  }
}