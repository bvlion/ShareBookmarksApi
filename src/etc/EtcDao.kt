package net.ambitious.sharebookmarks.etc

import org.jetbrains.exposed.dao.IntIdTable

object EtcDao : IntIdTable("term") {
  val type = integer("type")
  val lang = text("lang")
  val message = text("message")
}