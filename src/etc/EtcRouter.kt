package net.ambitious.sharebookmarks.etc

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject

object EtcRouter {
  fun Routing.index() {
    val controller: EtcController by inject()

    route("/test") {
      get {
        call.respond(controller.getTimeOnly())
      }
    }

    route("/") {
      get {
        call.respond(transaction { controller.getTime() })
      }
    }
  }

  fun Routing.etc() {
    val controller: EtcController by inject()
    route("/etc") {

      get("/terms_of_use") {
        call.respond(transaction { controller.message(1) })
      }

      get("/privacy_policy") {
        call.respond(transaction { controller.message(2) })
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
