package de.sandec.jnodes.lang

import java.text.MessageFormat
import java.util.Locale

import com.typesafe.config.{ConfigException, ConfigFactory}

object Messages {
  val notfound = "NOT FOUND"

  val langConf = ConfigFactory.load("lang")

  def getString(msg: String)(implicit locale: Locale): String = {
    val key = locale.getLanguage + "." + msg
    try{
      langConf.getString(key)
    }catch{
      case e: ConfigException =>
        e.printStackTrace()
        println(s"Couldn't find the key: $key")
        notfound

      case e: Exception => "EXCEPTION"
    }
  }

  def apply(msg: String, args: Any*)(implicit locale: Locale): String = {
    new MessageFormat(getString(msg), locale).format(args.map(_.asInstanceOf[java.lang.Object]).toArray)
  }
}

