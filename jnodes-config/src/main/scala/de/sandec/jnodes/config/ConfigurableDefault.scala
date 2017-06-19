package de.sandec.jnodes.config

import java.time.{LocalDateTime, ZoneId}
import java.util.Date
import de.sandec.jnodes.config.Configurable._
import de.sandec.jnodes.config.DefaultValue._
import simplefx.core._
import simplefx.all._

object ConfigurableExtended extends ConfigurableExtended
trait ConfigurableExtended extends ConfigurableDefault {

  implicit def configureList[A: Configurable: DefaultValue] = new Configurable[List[A]] {
    def createNode(x: B[List[A]]) = new VBox {
      @Bind var content: List[A] = <*>(x)
      @Bind var toAdd: A = getDefault[A]
      def getIndex: Int = {
        val model = listView.selectionModel
        model.selectedIndex
      }
      @Bind var selectedIndex = <--(getIndex)
      @Bind var selectedElement: A = <--(if(selectedIndex == -1) getDefault[A] else {
        content(selectedIndex)
      })
      @Bind var editingElement = <--(selectedElement)
      selectedIndex --> { println("selectedIndex: " + selectedIndex)}

      @Bind val listView = new ListView[A] {
        prefHeightProp = 220
        items <-- content
      }
      @Bind val editArea = config(editingElement)
      @Bind val saveButton = new Button("save") {
        onAction --> {
          if(selectedIndex != -1 && selectedElement != editingElement) {
            content = content.patch(selectedIndex, List(editingElement), 1)
          }
        }
      }

      @Bind val configToAdd = config(toAdd)
      @Bind val deleteButton = new Button("delete") {
        onAction --> {
          if(selectedIndex != -1) {
            content = content.patch(selectedIndex, Nil, 1)
          }
        }
      }
      @Bind val moveUp = new Button("move up") {
        onAction --> {
          if(selectedIndex != -1) {
            val index = selectedIndex
            val toMove = content(index)
            content = content.patch(index, Nil, 1)
            content = content.patch(index - 1, List(toMove), 0)
          }
        }
      }
      @Bind val moveDown = new Button("move down") {
        onAction --> {
          if(selectedIndex != -1) {
            val index = selectedIndex
            val toMove = content(index)
            content = content.patch(index, Nil, 1)
            content = content.patch(index + 1, List(toMove), 0)
          }
        }
      }
      @Bind val addButton = new Button("add") {
        onAction --> {
          if(content == null) content = Nil
          content ::= toAdd
          toAdd = getDefault[A]
        }
      }

      this <++ new HBox(listView, new VBox {
        this <++ deleteButton
        this <++ moveUp
        this <++ moveDown
        this <++ editArea
        this <++ saveButton
      })
      this <++ new HBox(configToAdd,addButton)
    }
  }
}

object ConfigurableDefault extends ConfigurableDefault
class ConfigurableDefault {


  implicit def configureTupleHBox[X: Configurable, Y: Configurable]: Configurable[(X,Y)] = new Configurable[(X,Y)] {
    override def createNode(xy: B[(X,Y)]): Node = {
      @Bind var x = xy._1
      @Bind var y = xy._2
      xy <-> (x,y)
      new HBox {
        this <++ config(x)
        this <++ config(y)
      }
    }
  }
  def configureTupleVBox[X: Configurable, Y: Configurable]: Configurable[(X,Y)] = new Configurable[(X,Y)] {
    override def createNode(xy: B[(X,Y)]): Node = {
      @Bind var x = xy._1
      @Bind var y = xy._2
      xy <-> (x,y)
      new VBox {
        this <++ config(x)
        this <++ config(y)
      }
    }
  }

  val configureSingleLineString: Configurable[String] = new Configurable[String] {
    override def createNode(x: B[String]): Node = {
      new TextField {
        text <-> x
      }
    }
  }

  val configureMultiLineString: Configurable[String] = new Configurable[String] {
    override def createNode(x: B[String]): Node = {
      new TextArea {
        text <-> x
      }
    }
  }

  implicit val configurableString: Configurable[String] = new Configurable[String] {
    override def createNode(x: B[String]): Node = {

      if(x.v eq null) x := "" // Workaround

      @Bind val textSize = <--(if(x.v eq null) 0 else x.get.length)
      @Bind val useArea = <--((x.get ne null) && (textSize > 30 || x.get.contains("\n")))

      new StackPane {
        @Bind val field = new TextField{text <-> x}
        @Bind val area = new TextArea{text <-> x}
        @Bind var currentNode: TextInputControl = null
        @Bind val preferedNode = <--(if(useArea) area else field)

        preferedNode --> {
          if(preferedNode != currentNode) {
            if(currentNode != null) {
              val wasFocused = currentNode.focused
              val caretPos = currentNode.caretPosition

              if(wasFocused) {
                onceWhen(preferedNode.scene != null) --> {
                  preferedNode.requestFocus
                }
              }
              preferedNode.positionCaret(caretPos)
            }

            currentNode = preferedNode
            children = List(currentNode)
          }
        }
      }
    }
  }

  implicit val configurableInt: Configurable[Int] = new Configurable[Int] {
    override def createNode(x: B[Int]): Node = {
      var rb1 = new NumberTextField {
        text  <-- (""+x.v)
        x     <-- (if(text.v.isEmpty) 0 else text.v.toInt)
      }
      rb1
    }

    class NumberTextField extends TextField {

      //TODO filter players

      override def replaceText(start: Int, end: Int, text: String) {
        if (validate(text)) {
          super.replaceText(start, end, text)
        }
      }


      override def replaceSelection(text: String) {
        if (validate(text)) {
          super.replaceSelection(text)
        }
      }

      def validate (text: String):Boolean ={
        text.matches("-*[0-9]*")
      }
    }
  }

  implicit val configurableBoolean: Configurable[Boolean] = new Configurable[Boolean] {
    override def createNode(x: B[Boolean]): Node = {
      var rb1 = new CheckBox{
        selected <-> x
      }
      rb1
    }
  }


  implicit val configurableDatePicker: Configurable[java.util.Date] = new Configurable[java.util.Date] {
    override def createNode(x: B[java.util.Date]): Node = {
      //def localDTtoUtilDate(localDate: LocalDateTime): Date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant())

      //def combineLocalDT(localDate: LocalDate, localTime: LocalTime): LocalDateTime = LocalDateTime.of(localDate, localTime)

      val localDateTime = LocalDateTime.ofInstant((if(x.get == null) time.toDate else x.get).toInstant, ZoneId.systemDefault())

      @Bind var localDate = localDateTime.toLocalDate
      @Bind var localTime = localDateTime.toLocalTime

      def setDateBindable = x := Date.from(LocalDateTime.of(localDate, localTime).atZone(ZoneId.systemDefault()).toInstant())

      localDate --> setDateBindable
      localTime --> setDateBindable

      new VBox {
        <++ (new DatePicker{
          valueProperty.toBindable <-> localDate
        })
        //<++ (new DatePicker{
        //  this.setShowTime(true)
        //  timeProperty.toBindable <-> localTime
        //})
      }
    }
  }

  implicit val configurableColorPicker: Configurable[Color] = new Configurable[Color] {
    override def createNode(x: B[Color]): Node = {
      var colorPicker = new ColorPicker {
       value <-> x
      }
      colorPicker
    }
  }
}
