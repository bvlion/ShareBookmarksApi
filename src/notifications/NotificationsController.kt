package net.ambitious.sharebookmarks.notifications

import net.ambitious.sharebookmarks.Util
import net.ambitious.sharebookmarks.users.UsersEntity
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.joda.time.DateTime

class NotificationsController {
  fun getList(userId: Int?) = NotificationsModel.entityToModel(
    NotificationsEntity.find {
      (NotificationsDao.targetPremium.eq(0) or
          NotificationsDao.targetPremium.eq(
            userId?.let { UsersEntity.findById(it)?.premium ?: 0 } ?: 0
          )
          ) and
          NotificationsDao.targetDate.isNotNull() and
          NotificationsDao.targetDate.less(DateTime(Util.DATETIME_ZONE)) and
          NotificationsDao.deleted.isNull()
    }.orderBy(NotificationsDao.targetDate to SortOrder.DESC)
      .limit(20)
  )
}