package net.ambitious.sharebookmarks.notifications

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class NotificationsEntity(id: EntityID<Int>) : IntEntity(id) {
 companion object : IntEntityClass<NotificationsEntity>(NotificationsDao)

 var title by NotificationsDao.title
 var subject by NotificationsDao.subject
 var url by NotificationsDao.url
 var targetDate by NotificationsDao.targetDate
}