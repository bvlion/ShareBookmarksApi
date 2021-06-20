package net.ambitious.sharebookmarks.items

import net.ambitious.sharebookmarks.Util
import net.ambitious.sharebookmarks.shares.SharesDao
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime

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

  fun setParentIds(parents: Array<ItemsModel.PostParents>, userId: Int): ItemsModel.ParentsSetResponse {
    // 親フォルダと id の Map
    val idMap = mutableMapOf<Int, ArrayList<Int>>()
    var count = 0

    parents.forEach { item ->
      // 所有者が自身ではない場合は shares を更新する
      if (item.isShareFolder) {
        count += SharesDao.update({
          (SharesDao.shareUserId eq userId) and (SharesDao.itemsId eq item.id)
        }) {
          it[parentId] = item.parentId
        }
      } else {
        if (idMap.contains(item.parentId)) {
          idMap[item.parentId]!!.add(item.id)
        } else {
          idMap[item.parentId] = arrayListOf(item.id)
        }
      }
    }

    idMap.forEach { ids ->
      count += ItemsDao.update({ ItemsDao.id inList ids.value }) {
        it[parentId] = ids.key
      }
    }
    idMap.clear()

    // 全アイテムの所有者情報を親フォルダの owner で上書きする
    ItemsDao
      .select { ItemsDao.url.isNull() }
      .forEach {
        if (idMap.contains(it[ItemsDao.ownerUserId])) {
          idMap[it[ItemsDao.ownerUserId]]!!.add(it[ItemsDao.id].value)
        } else {
          idMap[it[ItemsDao.ownerUserId]] = arrayListOf(it[ItemsDao.id].value)
        }
      }
    idMap.forEach { ids ->
      count += ItemsDao.update({ ItemsDao.parentId inList ids.value }) {
        it[ownerUserId] = ids.key
      }
    }

    return ItemsModel.ParentsSetResponse(count)
  }

  fun deleteItems(itemIds: Array<ItemsModel.DeleteRequest>) = ItemsModel.DeleteResponse(
    itemIds.map { EntityID(it.id, ItemsDao) }.let { idList ->
      ItemsDao.update({ ItemsDao.id.inList(idList) }) {
        it[deleted] = DateTime(Util.DATETIME_ZONE)
      }
    }
  )

  fun getItems(userId: Int, lastUpdate: String?) = ItemsModel.GetList(
      ArrayList(
        ItemsDao
          .select { ItemsDao.ownerUserId eq userId }
          .map { ItemsModel.entityToModel(it, 0) }
      ).apply {
        val list = arrayListOf<ItemsModel.Item>()
        val idAlias = SharesDao.id.max().alias("id")
        // 同じフォルダを再共有すると削除と混同するため最新のみとする
        val maxIdList = SharesDao.slice(idAlias)
          .select { SharesDao.shareUserId eq userId }
          .groupBy(SharesDao.itemsId)
          .map { it[idAlias]!!.value }
          .toList()
        SharesDao.select { (SharesDao.shareUserId eq userId) and SharesDao.id.inList(maxIdList) }
          .forEach {
          getItemsFromFolderId(
            list,
            it[SharesDao.itemsId],
            it[SharesDao.ownerType],
            it[SharesDao.parentId],
            it[SharesDao.updated],
            it[SharesDao.deleted] != null
          )
        }
        addAll(list)
      }.filter {
        Util.datetimeParse(it.updated).isAfter(Util.datetimeParse(lastUpdate ?: Util.LAST_UPDATE_DEFAULT))
      }
    )

  private fun getItemsFromFolderId(
    list: ArrayList<ItemsModel.Item>,
    folderId: Int,
    ownerType: Int,
    parentId: Int,
    updated: DateTime,
    deleted: Boolean
  ) {
    // フォルダ自身を追加
    val folders = ItemsDao
      .select { ItemsDao.id eq folderId }
      .map { ItemsModel.entityToModel(it, ownerType, parentId, updated, deleted) }
    if (folders.isEmpty()) {
      return
    }

    list.addAll(folders)

    // 紐づくブックマークを取得
    ItemsDao
      .select { ItemsDao.parentId eq folderId }
      .map { ItemsModel.entityToModel(it, ownerType, updated = updated, deleted = deleted) }
      .forEach {
        // フォルダであれば再起的に取得する
        if (it.url.isNullOrEmpty()) {
          getItemsFromFolderId(list, it.id, ownerType, it.parentId, updated, deleted)
        } else {
          list.add(it)
        }
      }
  }
}