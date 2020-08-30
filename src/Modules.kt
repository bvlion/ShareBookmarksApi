package net.ambitious.sharebookmarks

import io.ktor.locations.*
import net.ambitious.sharebookmarks.notifications.NotificationsController
import net.ambitious.sharebookmarks.users.UsersController
import org.koin.dsl.module

object Modules {
  @KtorExperimentalLocationsAPI
  val modules = module {
    single { NotificationsController() }
    single { UsersController() }
  }
}