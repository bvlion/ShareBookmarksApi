package net.ambitious.sharebookmarks

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.*
import net.ambitious.sharebookmarks.index.IndexRouter.index
import net.ambitious.sharebookmarks.items.ItemsRouter.items
import net.ambitious.sharebookmarks.notifications.NotificationsRouter.notifications
import net.ambitious.sharebookmarks.shares.SharesRouter.shares
import net.ambitious.sharebookmarks.users.UsersRouter.users
import org.koin.ktor.ext.Koin
import org.jetbrains.exposed.sql.Database

@KtorExperimentalLocationsAPI
@KtorExperimentalAPI
@Suppress("unused")
fun Application.module() {
  install(Koin) {
    modules(listOf(Modules.modules))
  }
  install(ContentNegotiation) {
    jackson {
      enable(SerializationFeature.INDENT_OUTPUT)
    }
  }
  install(StatusPages) {
    exception<IllegalStateException> { cause ->
      log.warn("StatusPages Error", cause)
      call.respond(HttpStatusCode.NotFound)
    }
  }
  install(ProjectAuthentication) {
    routing {
      index()
      users()
      notifications(userIdKey)
      shares(userIdKey)
      items(userIdKey)
    }
  }

  Database.connect(
    url = environment.config.property("app.database.url").getString(),
    user = environment.config.property("app.database.user").getString(),
    password = environment.config.property("app.database.password").getString(),
    driver = "com.mysql.jdbc.Driver"
  )
}

fun main(args: Array<String>) {
  embeddedServer(Netty, commandLineEnvironment(args)).start()
}