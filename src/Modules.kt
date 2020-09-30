package net.ambitious.sharebookmarks

import net.ambitious.sharebookmarks.etc.EtcController
import net.ambitious.sharebookmarks.items.ItemsController
import net.ambitious.sharebookmarks.notifications.NotificationsController
import net.ambitious.sharebookmarks.shares.SharesController
import net.ambitious.sharebookmarks.users.UsersController
import org.koin.dsl.module

object Modules {
  val modules = module {
    single { NotificationsController() }
    single { UsersController() }
    single { SharesController() }
    single { ItemsController() }
    single { EtcController() }
  }
}