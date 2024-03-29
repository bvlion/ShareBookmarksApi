package net.ambitious.sharebookmarks.notifications

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.datetime

object NotificationsDao : IntIdTable("notifications") {
  val title = text("title")
  val subject = text("subject")
  val url = text("url").nullable()
  val targetDate = datetime("target_date").nullable()
  val targetPremium = integer("target_premium")
  val deleted = datetime("deleted").nullable()
}

