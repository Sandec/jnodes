package jnodes.example

import simplefx.all._
import simplefx.core._
import de.sandec.jnodes.css.DynamicCSS._

object StartTestCSS extends App {
  javafx.application.Application.launch(classOf[TestCSS])
}

object TestCSS extends App
@SimpleFXApp class TestCSS { THIS =>

  @Bind var i = 20

  lazy val pin = new VBox {
    this <++ new TextField
    this <++ new TextField
    this <++ new TextField {
      text = i.toString
      text --> {
        i = text.toInt
      }
    }
  }

  scene = new Scene(pin,400,400)

  scene.cssString <--
    s"""
       |* {
       |  -fx-padding : $i;
       |}
    """.stripMargin
}
