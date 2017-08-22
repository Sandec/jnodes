package de.sandec.jnodes.lang

import java.util.Locale

object Lang {

  private var DEFAULT = Locale.getDefault

  def setDefaultLocale(locale: Locale) = DEFAULT = locale

  def getDefaultLocale(locale: Locale): Locale = DEFAULT

  def apply(locale: Locale): Locale = {
    DEFAULT = locale
    DEFAULT
  }

}
