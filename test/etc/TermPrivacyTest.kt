package etc

import JsonObject
import TestBase
import io.ktor.http.*
import io.ktor.server.testing.*
import net.ambitious.sharebookmarks.etc.EtcDao
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert
import org.junit.Test

class TermPrivacyTest : TestBase() {
  @Test
  fun termOfUse() {
    termTestBefore()
    with(engine) {
      handleRequest(HttpMethod.Get, "/etc/terms_of_use").response.run {
        Assert.assertEquals(
          JsonObject(mapOf("message" to "terms_of_use")).toString(),
          content
        )
        Assert.assertEquals(HttpStatusCode.OK, status())
      }
      termTestAfter()
    }
  }

  @Test
  fun privacyPolicy() {
    privacyTestBefore()
    with(engine) {
      handleRequest(HttpMethod.Get, "/etc/privacy_policy").response.run {
        Assert.assertEquals(
          JsonObject(mapOf("message" to "privacy_policy")).toString(),
          content
        )
        Assert.assertEquals(HttpStatusCode.OK, status())
      }
    }
    termTestAfter()
  }

  private fun termTestBefore() = transaction {
    EtcDao.Term.insert {
      it[id] = EntityID(1, EtcDao.Term)
      it[message] = "terms_of_use"
    }
  }

  private fun privacyTestBefore() = transaction {
    EtcDao.Term.insert {
      it[id] = EntityID(2, EtcDao.Term)
      it[message] = "privacy_policy"
    }
  }

  private fun termTestAfter() = transaction {
    EtcDao.Term.deleteAll()
  }
}