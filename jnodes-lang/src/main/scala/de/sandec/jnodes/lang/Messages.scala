package de.sandec.jnodes.lang

import java.text.MessageFormat
import java.util.Locale

import com.typesafe.config.{Config, ConfigException, ConfigFactory}

object Messages {
  val notfound = "NOT FOUND"

  var langSource: Config = ConfigFactory.load("lang")

  def getString(msg: String, getPrefered: Boolean = true)(args: List[Any])(implicit lang: Lang): String = {

    val locale = getPrefered match {
      case true  => lang.prefered
      case false => lang.fallback
    }

    val key  =  locale.getLanguage + "." + msg

    try{
      getFormattedString(langSource.getString(key), args)(locale)
    }catch{
      case e: ConfigException =>
        getPrefered match {
          case true  => getString(msg, false)(args)
          case false => notfound
        }

      case e: Exception => "EXCEPTION"
    }
  }

  def getFormattedString(input: String, args: List[Any])(locale: Locale): String = {
    args.map(x => println(x))
    new MessageFormat(input, locale).format(args.map(_.asInstanceOf[java.lang.Object]).toArray)
  }

  def apply(msg: String, args: Any*)(implicit lang: Lang): String = {
    getString(msg)(args.toList)
  }
}

