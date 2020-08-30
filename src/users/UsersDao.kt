package net.ambitious.sharebookmarks.users

import org.jetbrains.exposed.dao.IntIdTable

object UsersDao : IntIdTable("users") {
  val email = text("email")
  val fcmToken = text("fcm_token")
  val premium = integer("premium")
}