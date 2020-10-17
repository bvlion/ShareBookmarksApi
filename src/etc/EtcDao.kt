package net.ambitious.sharebookmarks.etc

import org.jetbrains.exposed.dao.IntIdTable

object EtcDao {
  object Term: IntIdTable("term") {
    val type = integer("type")
    val lang = text("lang")
    val message = text("message")
  }

  object Faq: IntIdTable("faq") {
    val lang = text("lang")
    val question = text("question")
    val answer = text("answer")
    val deleted = bool("deleted")
  }
}