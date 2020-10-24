package net.ambitious.sharebookmarks.etc

import net.ambitious.sharebookmarks.Util
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select

class EtcController {
  fun message(lang: String?, type: Int) = EtcDao.Term.select {
    (EtcDao.Term.lang eq (lang ?: "ja")) and (EtcDao.Term.type eq type)
  }.map { mapOf(Pair("message", it[EtcDao.Term.message])) }.first()

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
      EtcDao.Faq.select {
        (EtcDao.Faq.lang eq (lang ?: "ja")) and (EtcDao.Faq.deleted eq false)
      }.orderBy(EtcDao.Faq.id to SortOrder.DESC)
        .map {
          HashMap<String, String>().apply {
            put("answer", it[EtcDao.Faq.answer])
            put("question", it[EtcDao.Faq.question])
          }
        })
  )

  fun getTime() = Util.datetimeFormat(null).apply {
    EtcDao.Faq.select { EtcDao.Faq.id eq 1 }
  }

  data class OgpGet(val url: String?)
}