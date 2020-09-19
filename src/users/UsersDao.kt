package net.ambitious.sharebookmarks.users

import org.jetbrains.exposed.dao.IntIdTable

object UsersDao : IntIdTable("users") {
  val email = text("email")
  val uid = text("uid").nullable()
  val fcmToken = text("fcm_token").nullable()
  val premium = integer("premium")
}