package net.ambitious.sharebookmarks.items

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import io.ktor.util.*
import net.ambitious.sharebookmarks.Util
import org.jetbrains.exposed.sql.ResultRow

object ItemsModel {
  data class GetList(val items: List<Item>)

  data class Item(
    @JsonProperty("remote_id") val id: Int,
    @JsonProperty("parent_id") val parentId: Int,
    @JsonProperty("name") val name: String,
    @JsonProperty("url") val url: String?,
    @JsonProperty("orders") val order: Int,
    val updated: String
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
    @JsonAlias("parent_id") val parentId: Int
  )

  data class ParentsSetResponse(@JsonProperty("result_count") val count: Int)

  data class DeleteRequest(@JsonAlias("delete_id") val id: Int)

  data class DeleteResponse(@JsonProperty("delete_count") val count: Int)

  @KtorExperimentalAPI
  fun entityToModel(entity: ResultRow, parentId: Int? = null) =
    Item(
      entity[ItemsDao.id].value,
      parentId ?: entity[ItemsDao.parentId],
      entity[ItemsDao.name],
      entity[ItemsDao.url],
      entity[ItemsDao.orders],
      Util.datetimeFormat(entity[ItemsDao.updated].millis)
    )
}