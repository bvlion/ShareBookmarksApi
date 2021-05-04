package net.ambitious.sharebookmarks

import com.auth0.jwt.JWT
import com.fasterxml.jackson.databind.SerializationFeature
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.*
import net.ambitious.sharebookmarks.etc.EtcRouter.etc
import net.ambitious.sharebookmarks.etc.EtcRouter.index
import net.ambitious.sharebookmarks.items.ItemsRouter.items
import net.ambitious.sharebookmarks.notifications.NotificationsRouter.notifications
import net.ambitious.sharebookmarks.shares.SharesRouter.shares
import net.ambitious.sharebookmarks.users.UsersDao
import net.ambitious.sharebookmarks.users.UsersEntity
import net.ambitious.sharebookmarks.users.UsersRouter.users
import org.koin.ktor.ext.Koin
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

@KtorExperimentalAPI
@Suppress("unused")
fun Application.module() {
  install(Koin) {
    modules(listOf(Modules.modules))
  }
  install(ContentNegotiation) {
    jackson {
      if (environment.config.property("app.pretty_print").getString() == "true") {
        enable(SerializationFeature.INDENT_OUTPUT)
      }
    }
  }
  install(StatusPages) {
    exception<IllegalStateException> { cause ->
      log.warn("StatusPages Error", cause)
      call.respond(HttpStatusCode.NotFound)
    }
  }
  install(Authentication) {
    jwt {
      realm = javaClass.packageName
      verifier(
        JWT.require(Util.getAlgorithm(environment))
          .withAudience(Util.getAudience(environment))
          .withIssuer(Util.JWT_ISSUER)
          .build()
      )
      validate {
        it.payload.subject?.let { hash ->
          transaction {
            UsersEntity.find { UsersDao.hash eq hash}.firstOrNull()?.let { db ->
              Util.AuthUser(db.id.value)
            }
          }
        }
      }
    }
  }
  routing {
    index()
    users()
    notifications()
    shares()
    items()
    etc()
  }

  Database.connect(HikariDataSource(
    HikariConfig().apply {
      driverClassName = "com.mysql.jdbc.Driver"
      jdbcUrl = environment.config.property("app.database.url").getString()
      username = environment.config.property("app.database.user").getString()
      password = environment.config.property("app.database.password").getString()
      connectionTestQuery = "SELECT 1"
      minimumIdle = 5
      maximumPoolSize = 10
      poolName = "ConnectionPool"
      leakDetectionThreshold = 5000
      isAutoCommit = false

      val cloudSqlInstance = environment.config.property("app.database.cloud_sql_instance").getString()
      if (cloudSqlInstance.isNotEmpty()) {
        addDataSourceProperty("socketFactory", "com.google.cloud.sql.mysql.SocketFactory")
        addDataSourceProperty("cloudSqlInstance", cloudSqlInstance)
      }
      addDataSourceProperty("useSSL", "false")
      validate()
    }
  ))
}

fun main(args: Array<String>) {
  embeddedServer(Netty, commandLineEnvironment(args)).start()
}