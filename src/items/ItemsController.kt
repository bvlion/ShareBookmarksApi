package net.ambitious.sharebookmarks.items

import io.ktor.util.*
import net.ambitious.sharebookmarks.Util
import net.ambitious.sharebookmarks.shares.SharesDao
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime

@KtorExperimentalAPI
class ItemsController {
  fun saveItems(userId: Int, items: Array<ItemsModel.PostItem>) = ItemsModel.PostResponseList(
    items.map { item ->
      ItemsModel.PostResponse(
        item.localId,
        if (item.id == null) {
          (ItemsDao.insert {
            it[ownerUserId] = userId
            it[parentId] = 0
            it[name] = item.name
            it[url] = item.url
            it[orders] = item.order
            it[created] = Util.datetimeParse(item.updated)
            it[updated] = Util.datetimeParse(item.updated)
          } get ItemsDao.id).value
        } else {
          ItemsDao.update({ ItemsDao.id eq item.id }) {
            it[name] = item.name
            it[url] = item.url
            it[orders] = item.order
            it[updated] = Util.datetimeParse(item.updated)
          }
          item.id
        }
      )
    }
  )

  fun setParentIds(parents: Array<ItemsModel.PostParents>, userId: Int) = ItemsModel.ParentsSetResponse(
    parents.map { item ->
      // 所有者が自身ではない場合は shares を更新する
      if (item.isShareFolder) {
        SharesDao.update({
          (SharesDao.shareUserId eq userId) and (SharesDao.itemsId eq item.id)
        }) {
          it[parentId] = item.parentId
        }
      } else {
        ItemsDao.update({ ItemsDao.id eq item.id }) {
          it[parentId] = item.parentId
        }
      }
    }.size
  )

  fun deleteItems(itemIds: Array<ItemsModel.DeleteRequest>) = ItemsModel.DeleteResponse(
    itemIds.map { EntityID(it.id, ItemsDao) }.let { idList ->
      ItemsDao.update({ ItemsDao.id.inList(idList) }) {
        it[deleted] = DateTime(Util.DATETIME_ZONE)
      }
    }
  )

  fun getItems(userId: Int) = ItemsModel.GetList(
      ArrayList(
        ItemsDao
          .select { (ItemsDao.ownerUserId eq userId) and ItemsDao.deleted.isNull() }
          .map { ItemsModel.entityToModel(it) }
      ).apply {
        val list = arrayListOf<ItemsModel.Item>()
        SharesDao.select { SharesDao.shareUserId eq userId }.forEach {
          getItemsFromFolderId(list, it[SharesDao.itemsId], it[SharesDao.parentId])
        }
        addAll(list)
      }
    )

    private fun getItemsFromFolderId(list: ArrayList<ItemsModel.Item>, folderId: Int, parentId: Int) {
      // フォルダ自身を追加
      val folders = ItemsDao
        .select { (ItemsDao.id eq folderId) and ItemsDao.deleted.isNull() }
        .map { ItemsModel.entityToModel(it, parentId) }
      if (folders.isEmpty()) {
        return
      }
      list.addAll(folders)

      // 紐づくブックマークを取得
      ItemsDao
        .select { (ItemsDao.parentId eq folderId) and ItemsDao.deleted.isNull() }
        .map { ItemsModel.entityToModel(it, parentId) }
        .forEach {
          // フォルダであれば再起的に取得する
          if (it.url.isNullOrEmpty()) {
            getItemsFromFolderId(list, it.id, it.id)
          } else {
            list.add(it)
          }
        }
    }
}