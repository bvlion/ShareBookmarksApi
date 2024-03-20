package net.ambitious.sharebookmarks.etc

import org.jetbrains.exposed.dao.id.IntIdTable

object EtcDao {
  object Term: IntIdTable("term") {
    val message = text("message")
  }

  object Faq: IntIdTable("faq") {
    val lang = text("lang")
    val question = text("question")
    val answer = text("answer")
    val deleted = bool("deleted")
  }
}