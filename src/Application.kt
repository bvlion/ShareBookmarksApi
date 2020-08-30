package net.ambitious.sharebookmarks

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.util.*
import net.ambitious.sharebookmarks.notifications.NotificationsRouter.notifications
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
  routing {
    users()
    notifications()
  }

  Database.connect(
    url = environment.config.property("app.database.url").getString(),
    user = environment.config.property("app.database.user").getString(),
    password = environment.config.property("app.database.password").getString(),
    driver = "com.mysql.jdbc.Driver"
  )
}

