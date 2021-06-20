package net.ambitious.sharebookmarks.shares

import org.jetbrains.exposed.dao.IntIdTable

object SharesDao : IntIdTable("share") {
  val itemsId = integer("items_id")
  val ownerUserId = integer("owner_user_id")
  val shareUserId = integer("share_user_id")
  val ownerType = integer("owner_type")
  val parentId = integer("parent_id")
  val created = datetime("created")
  val updated = datetime("updated")
  val deleted = datetime("deleted").nullable()
}