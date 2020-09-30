package net.ambitious.sharebookmarks.etc

import net.ambitious.sharebookmarks.Util
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select

class EtcController {
  fun message(lang: String?, type: Int) = EtcDao.select {
    (EtcDao.lang eq (lang ?: "ja")) and (EtcDao.type eq type)
  }.map { mapOf(Pair("message", it[EtcDao.message])) }.first()

  fun getHttpOgpImage(url: String?) = EtcRouter.OgpGet(
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
}