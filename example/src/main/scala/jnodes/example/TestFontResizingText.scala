package jnodes.example

import simplefx.core._
import simplefx.all._
import de.sandec.jnodes.config.Configurable._
import de.sandec.jnodes.config.ConfigurableExtended._
import de.sandec.jnodes.css.DynamicCSS._
import de.sandec.jnodes.elements.FontResizingText

object TestFontResizingText extends App
@SimpleFXApp class TestFontResizingText { THIS =>

  @Bind var size: Int = 20
  @Bind var text: String = "I'm a lot of text!"

  @Bind var font = <--(new Font(size))

  val pin = new BorderPane {
    left = new VBox {
      this <++ config(size)
      this <++ new TextArea {
        this.text <-> THIS.text
      }
    }
    center = new VBox {
      padding = Insets(10)
      spacing = 10
      def add(x: Int) = this <++ new FontResizingText {
        this.font <-- THIS.font
        this.text <-- THIS.text
        styleClass ::= "text1"
        maxHeightProp = x
        prefHeightProp = x
        minHeightProp = x
      }
      add(20)
      add(50)
      add(100)
      add(180)
      //add(300)
      this <++ new VBox {
        styleClass ::= "box2"
        prefHeightProp = 300
        this <++ new StackPane {
          styleClass ::= "box1"
          //prefHeightProp = 200
          this <++ new FontResizingText {
            this.font <-- THIS.font
            this.text <-- THIS.text
            styleClass ::= "text1"
            minHeightProp = 100
          }
        }
      }
    }
  }

  scene = new Scene(pin, 800,800) {
    this.cssString <--
      s"""
         |.text1 {
         |  -fx-background-color : yellow;
         |  -fx-border-width : 1px;
         |  -fx-border-color : black;
         |}
         |.box2 {
         |  -fx-background-color : blue;
         |  -fx-border-width : 1px;
         |  -fx-border-color : black;
         |}
         |.box1 {
         |  -fx-background-color : green;
         |  -fx-border-width : 1px;
         |  -fx-border-color : black;
         |}
    """.stripMargin
  }
}
