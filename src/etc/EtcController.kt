package net.ambitious.sharebookmarks.etc

import net.ambitious.sharebookmarks.Util
import org.jetbrains.exposed.sql.*

class EtcController {
  fun message(id: Int) = EtcDao.Term.selectAll().where {
    EtcDao.Term.id eq id
  }.map {
    mapOf(Pair("message", it[EtcDao.Term.message]))
  }.first()

  fun getHttpOgpImage(url: String?) = OgpGet(
    url?.let {
      Util.getOgpImage(url)?.let {
        if (it.startsWith("https")) {
          it
        } else {
          null
        }
      }
    }
  )

  fun faq(lang: String?) = mapOf(
    Pair("faq",
      EtcDao.Faq.selectAll().where {
        (EtcDao.Faq.lang eq (lang ?: "ja")) and (EtcDao.Faq.deleted eq false)
      }.orderBy(EtcDao.Faq.id to SortOrder.DESC)
        .map {
          HashMap<String, String>().apply {
            put("answer", it[EtcDao.Faq.answer])
            put("question", it[EtcDao.Faq.question])
          }
        })
  )

  fun getTime() = EtcDao.Faq.selectAll().where { EtcDao.Faq.id eq 1 }.count().let {
    Util.datetimeFormat(null)
  }

  fun getTimeOnly() = Util.datetimeFormat(null)

  data class OgpGet(val url: String?)
}