package net.ambitious.sharebookmarks.notifications

import com.fasterxml.jackson.annotation.JsonProperty
import net.ambitious.sharebookmarks.Util
import org.jetbrains.exposed.sql.SizedIterable

object NotificationsModel {
  data class NotificationList(
    val notifications: List<Notification>
  )

  data class Notification(
    @JsonProperty("target_date") val targetDate: String,
    val title: String,
    val subject: String,
    val url: String?
  )

  fun entityToModel(entity: SizedIterable<NotificationsEntity>) =
    NotificationList(entity.map { Notification(Util.datetimeFormat(it.targetDate?.millis), it.title, it.subject, it.url) }).apply {
      check(notifications.isNotEmpty()) { "list is empty" }
    }
}