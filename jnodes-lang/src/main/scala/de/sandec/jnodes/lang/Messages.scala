package de.sandec.jnodes.lang

import java.text.MessageFormat
import java.util.Locale
import com.typesafe.config.{Config, ConfigException, ConfigFactory}

class Lang(val fallback: Locale, val prefered: Locale)

class LangConfig(val langSource: Config) {

  def getString(msg: String)(args: List[Any])(implicit lang: Lang): String = {

    val prefKey  =  lang.prefered.getLanguage + "." + msg
    val fallKey  =  lang.fallback.getLanguage + "." + msg

    if(langSource.hasPath(prefKey)) {
      langSource.getString(prefKey)
    } else if(langSource.hasPath(fallKey)) {
      langSource.getString(fallKey)
    } else {
      msg + " NOT FOUND"
    }

  }

  def getFormattedString(input: String, args: List[Any])(locale: Locale): String = {
    //args.map(x => println(x))
    new MessageFormat(input, locale).format(args.map(_.asInstanceOf[java.lang.Object]).toArray)
  }

  def apply(msg: String, args: Any*)(implicit lang: Lang): String = {
    getString(msg)(args.toList)
  }

}

object DefaultLangConfig extends LangConfig(ConfigFactory.load("lang"))
