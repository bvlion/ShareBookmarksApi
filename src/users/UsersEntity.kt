package net.ambitious.sharebookmarks.users

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class UsersEntity(id: EntityID<Int>) : IntEntity(id) {
  companion object : IntEntityClass<UsersEntity>(UsersDao)

  var premium by UsersDao.premium
}