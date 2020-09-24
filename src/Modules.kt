package net.ambitious.sharebookmarks

import io.ktor.locations.*
import io.ktor.util.*
import net.ambitious.sharebookmarks.etc.EtcController
import net.ambitious.sharebookmarks.items.ItemsController
import net.ambitious.sharebookmarks.notifications.NotificationsController
import net.ambitious.sharebookmarks.shares.SharesController
import net.ambitious.sharebookmarks.users.UsersController
import org.koin.dsl.module

object Modules {
  @KtorExperimentalAPI
  @KtorExperimentalLocationsAPI
  val modules = module {
    single { NotificationsController() }
    single { UsersController() }
    single { SharesController() }
    single { ItemsController() }
    single { EtcController() }
  }
}