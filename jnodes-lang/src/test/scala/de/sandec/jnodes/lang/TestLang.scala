package de.sandec.jnodes.lang

import com.typesafe.config.ConfigFactory
import org.junit._
import java.util.Locale

class TestLang {

  @Test
  def testStringRetireval = {
    val config =  """
                    |{
                    |en {
                    |key1 = "evalue1",
                    |key2 = "evalue2"
                    |}
                    |de {
                    |key1 = "dvalue1",
                    |key2 = "dvalue2"
                    |}
                    |}
                  """.stripMargin

    val Messages = new LangConfig(ConfigFactory.parseString(config))

    implicit val lang = new Lang(Locale.ENGLISH, Locale.GERMAN)
    val str1 = Messages("key1")
    val str2 = Messages("key2")

    println(str1 + " " + str2)

    assert(str1.equals("dvalue1"), "str1 contains " + str1 + " instead of " + "dvalue1")
    assert(str2.equals("dvalue2"), "str2 contains " + str2 + " instead of " + "dvalue2")
  }

  @Test
  def testStringRetirevalwithParameters = {
    val config =  """
                    |{
                    |en {
                    |key1 = "I am {0}",
                    |key2 = "You are {0} and he is {1}"
                    |}
                    |de {
                    |key1 = "Ich bin {0}",
                    |key2 = "Du bist {0} und er ist {1}"
                    |}
                    |}
                  """.stripMargin

    val Messages = new LangConfig(ConfigFactory.parseString(config))

    implicit val lang = new Lang(Locale.GERMAN, Locale.ENGLISH)
    val str1 = Messages("key1", "A")
    val str2 = Messages("key2", "B" , "C")

    println(str1 + " \n " + str2)

    assert(str1.equals("I am A"), "str1 contains " + str1 + " instead of " + "dvalue1")
    assert(str2.equals("You are B and he is C"), "str2 contains '" + str2 + "' instead of " + "'You are B and he is C'")
  }

  @Test
  def testStringRetirevalwithFallback = {
    val config =  """
                    |{
                    |en {
                    |key1 = "I am {0}",
                    |key2 = "You are {0} and he is {1}"
                    |}
                    |de {
                    |key1 = "Ich bin {0}",
                    |key2 = "Du bist {0} und er ist {1}"
                    |}
                    |}
                  """.stripMargin

    val Messages = new LangConfig(ConfigFactory.parseString(config))

    implicit val lang = new Lang(Locale.ENGLISH, Locale.FRENCH)
    val str1 = Messages("key1", "A")
    val str2 = Messages("key2", "B" , "C")

    println(str1 + " \n " + str2)

    assert(str1.equals("I am A"), "str1 contains " + str1 + " instead of " + "dvalue1")
    assert(str2.equals("You are B and he is C"), "str2 contains '" + str2 + "' instead of " + "'You are B and he is C'")
  }
}
