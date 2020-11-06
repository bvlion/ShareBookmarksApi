package net.ambitious.sharebookmarks.shares

import io.ktor.util.*
import net.ambitious.sharebookmarks.Util
import net.ambitious.sharebookmarks.users.UsersDao
import net.ambitious.sharebookmarks.users.UsersEntity
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime

class SharesController {

  fun saveShares(userId: Int, shares: Array<SharesModel.PostShare>) = SharesModel.PostResponseList(
    shares.map { share ->
      // シェア対象ユーザーがいなければ登録する
      UsersEntity.find { UsersDao.email eq share.userEmail }.firstOrNull().let { dbData ->
        dbData?.id ?: UsersDao.insert {
          it[email] = share.userEmail
        } get UsersDao.id
      }.value.let { shareUser ->
        SharesModel.PostResponse(
          share.localId,
          if (share.id == null) {
            (SharesDao.insert {
              it[itemsId] = share.itemsId
              it[ownerUserId] = userId
              it[shareUserId] = shareUser
              it[ownerType] = share.ownerType
              it[parentId] = 0
              it[created] = Util.datetimeParse(share.updated)
              it[updated] = Util.datetimeParse(share.updated)
            } get SharesDao.id).value
          } else {
            SharesDao.update({ SharesDao.id eq share.id }) {
              it[itemsId] = share.itemsId
              it[ownerUserId] = userId
              it[shareUserId] = shareUser
              it[ownerType] = share.ownerType
              it[updated] = Util.datetimeParse(share.updated)
            }
            share.id
          }
        )
      }
    }
  )

  fun deleteShares(shareIds: Array<SharesModel.DeleteRequest>) = SharesModel.DeleteResponse(
    shareIds.map { EntityID(it.id, SharesDao) }.let { idList ->
      SharesDao.update({ SharesDao.id.inList(idList) }) {
        it[deleted] = DateTime(Util.DATETIME_ZONE)
      }
    }
  )

  @KtorExperimentalAPI
  fun getShares(userId: Int) = SharesModel.GetList(
      SharesDao.join(UsersDao, JoinType.INNER, additionalConstraint = { SharesDao.shareUserId eq UsersDao.id })
        .slice(SharesDao.id, SharesDao.itemsId, UsersDao.email, SharesDao.ownerType, SharesDao.updated)
        .select { (SharesDao.ownerUserId eq userId) and SharesDao.deleted.isNull() }
        .orderBy(SharesDao.id to SortOrder.DESC)
        .map { SharesModel.entityToModel(it) }
  )
}