package net.ambitious.sharebookmarks.items

import org.jetbrains.exposed.dao.IntIdTable

object ItemsDao : IntIdTable("items") {
  val ownerUserId = integer("owner_user_id")
  val parentId = integer("parent_id")
  val name = text("name")
  val url = text("url").nullable()
  val orders = integer("orders")
  val created = datetime("created")
  val updated = datetime("updated")
  val deleted = datetime("deleted")
}