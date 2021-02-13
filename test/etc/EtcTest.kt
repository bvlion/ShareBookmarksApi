package etc

import TestBase
import io.ktor.http.*
import io.ktor.server.testing.*
import net.ambitious.sharebookmarks.etc.EtcDao
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.json.simple.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test

class EtcTest : TestBase() {

  @Test
  fun indexOnly() {
    with(engine) {
      handleRequest(HttpMethod.Get, "/test").response.apply {
        assertEquals(HttpStatusCode.OK, status())
      }
    }
  }

  @Test
  fun index() {
    with(engine) {
      handleRequest(HttpMethod.Get, "/").response.apply {
        assertEquals(HttpStatusCode.OK, status())
      }
    }
  }

  @Test
  fun jaFaq() {
    transaction {
      EtcDao.Faq.update({ EtcDao.Faq.id eq 1 }) {
        it[answer] = "answer"
        it[question] = "question"
      }
    }
    with(engine) {
      handleRequest(HttpMethod.Get, "/etc/ja/faq").response.apply {
        assertEquals(
          JSONObject(
            mapOf("faq" to listOf(
              mapOf("answer" to "answer", "question" to "question")
            ))
          ).toJSONString(),
          content
        )
        assertEquals(HttpStatusCode.OK, status())
      }
    }
  }
}