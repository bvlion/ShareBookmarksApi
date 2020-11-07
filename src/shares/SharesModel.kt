package net.ambitious.sharebookmarks.shares

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import net.ambitious.sharebookmarks.Util
import net.ambitious.sharebookmarks.users.UsersDao
import org.jetbrains.exposed.sql.ResultRow

object SharesModel {
  data class GetList(
    val shares: List<Share>
  )

  data class Share(
    @JsonProperty("remote_id") val id: Int,
    @JsonProperty("folder_id") val itemsId: Int,
    @JsonProperty("user_email") val userEmail: String,
    @JsonProperty("owner_type") val ownerType: Int,
    val updated: String
  )

  data class PostShare(
    @JsonAlias("local_id") val localId: Int,
    @JsonAlias("remote_id") val id: Int?,
    @JsonAlias("folder_id") val itemsId: Int,
    @JsonAlias("user_email") val userEmail: String,
    @JsonAlias("owner_type") val ownerType: Int,
    val updated: String
  )

  data class PostResponseList(
    val shares: List<PostResponse>
  )

  data class PostResponse(
    @JsonProperty("local_id") val localId: Int,
    @JsonProperty("remote_id") val id: Int
  )

  data class DeleteRequest(@JsonAlias("delete_id") val id: Int)

  data class DeleteResponse(@JsonProperty("delete_count") val count: Int)

  fun entityToModel(entity: ResultRow) =
    Share(
      entity[SharesDao.id].value,
      entity[SharesDao.itemsId],
      entity[UsersDao.email],
      entity[SharesDao.ownerType],
      Util.datetimeFormat(entity[SharesDao.updated].millis)
    )
}