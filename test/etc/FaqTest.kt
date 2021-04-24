package etc

import JsonObject
import TestBase
import io.ktor.http.*
import io.ktor.server.testing.*
import net.ambitious.sharebookmarks.etc.EtcDao
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert
import org.junit.Test

class FaqTest : TestBase() {
  @Test
  fun jaFaq() {
    faqTestBefore()
    with(engine) {
      handleRequest(HttpMethod.Get, "/etc/ja/faq").response.run {
        Assert.assertEquals(
          JsonObject(
            mapOf(
              "faq" to listOf(
                mapOf("answer" to "answer_ja_2", "question" to "question_ja_2"),
                mapOf("answer" to "answer_ja_1", "question" to "question_ja_1")
              )
            )
          ).toString(),
          content
        )
        Assert.assertEquals(HttpStatusCode.OK, status())
      }
    }
    faqTestAfter()
  }

  @Test
  fun enFaq() {
    faqTestBefore()
    with(engine) {
      handleRequest(HttpMethod.Get, "/etc/en/faq").response.run {
        Assert.assertEquals(
          JsonObject(
            mapOf(
              "faq" to listOf(
                mapOf("answer" to "answer_en_2", "question" to "question_en_2"),
                mapOf("answer" to "answer_en_1", "question" to "question_en_1")
              )
            )
          ).toString(),
          content
        )
        Assert.assertEquals(HttpStatusCode.OK, status())
      }
    }
    faqTestAfter()
  }

  private fun faqTestBefore() = transaction {
    repeat(2) { times ->
      EtcDao.Faq.insert {
        it[lang] = "ja"
        it[answer] = "answer_ja_${times + 1}"
        it[question] = "question_ja_${times + 1}"
      }
      EtcDao.Faq.insert {
        it[lang] = "en"
        it[answer] = "answer_en_${times + 1}"
        it[question] = "question_en_${times + 1}"
      }
    }
  }

  private fun faqTestAfter() = transaction {
    EtcDao.Faq.deleteAll()
  }
}