package net.ambitious.sharebookmarks.items

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import net.ambitious.sharebookmarks.Util
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime

object ItemsModel {
  data class GetList(val items: List<Item>)

  data class Item(
    @JsonProperty("remote_id") val id: Int,
    @JsonProperty("remote_parent_id") val parentId: Int,
    @JsonProperty("name") val name: String,
    @JsonProperty("url") val url: String?,
    @JsonProperty("orders") val order: Int,
    @JsonProperty("owner_type") val ownerType: Int,
    val updated: String,
    val deleted: Boolean
  )

  data class PostItem(
    @JsonAlias("local_id") val localId: Int,
    @JsonAlias("remote_id") val id: Int?,
    @JsonAlias("name") val name: String,
    @JsonAlias("url") val url: String?,
    @JsonAlias("orders") val order: Int,
    val updated: String
  )

  data class PostResponseList(val items: List<PostResponse>)

  data class PostResponse(
    @JsonProperty("local_id") val localId: Int,
    @JsonProperty("remote_id") val id: Int
  )

  data class PostParents(
    @JsonAlias("remote_id") val id: Int,
    @JsonAlias("parent_id") val parentId: Int,
    @JsonAlias("is_share_folder") val isShareFolder: Boolean
  )

  data class ParentsSetResponse(@JsonProperty("result_count") val count: Int)

  data class DeleteRequest(@JsonAlias("delete_id") val id: Int)

  data class DeleteResponse(@JsonProperty("delete_count") val count: Int)

  fun entityToModel(
    entity: ResultRow,
    ownerType: Int,
    parentId: Int? = null,
    updated: DateTime? = null,
    deleted: Boolean = false
  ) = Item(
    entity[ItemsDao.id].value,
    parentId ?: entity[ItemsDao.parentId],
    entity[ItemsDao.name],
    entity[ItemsDao.url],
    entity[ItemsDao.orders],
    ownerType,
    if (updated != null && updated.isAfter(entity[ItemsDao.updated])) {
      Util.datetimeFormat(updated.millis)
    } else {
      Util.datetimeFormat(entity[ItemsDao.updated].millis)
    },
    if (deleted) true else entity[ItemsDao.deleted] != null
  )
}