package de.sandec.jnodes.elements

import com.sun.javafx.scene.control.skin.UtilsPublic
import simplefx.all._
import simplefx.core._

class FontResizingText extends Pane { THIS =>

  styleClass ::= "font-resiszing-text"

  @Bind var font = new Font(20)
  @Bind var text: String = "asdf"

  override def getContentBias = Orientation.HORIZONTAL

  def computeHeight(width: Double): Double = {
    100
  }

  //override def layoutChildren(): Unit = {
  //  val lineHeight = UtilsPublic.getLineHeight(baseFont,TextBoundsType.TextBoundsType.LOGICAL)
  //}
  @Bind val content = new Text {
    styleClass ::= "font-resiszing-text-text"
    this.text <-> THIS.text
    this.font <-> THIS.font
    this.textAlignment = TextAlignment.CENTER
  }

  children <-- List(container)
  object container extends AnchorPane {
    this <++ content

    content.font --> this.requestLayout()
    content.text --> this.requestLayout()
    THIS.wh --> this.requestLayout()
    override def layoutChildren(): Unit = {
      val lineHeight = UtilsPublic.getLineHeight(font,TextBoundsType.LOGICAL)
      var lines = (THIS.height / lineHeight).floor

      if(UtilsPublic.computeTextHeight(font,text,THIS.width,TextBoundsType.LOGICAL) <= THIS.height) {
        println("fine")
        this.transforms = Nil
        content.setWrappingWidth(THIS.width)
        this.layoutInArea(content, 0, 0, THIS.width, THIS.height, 0, Insets(0), true, true, HPos.CENTER, VPos.CENTER)
      } else {
        println("downscaling case: " + THIS.wh)
        var scale = 1.0
        var availableHeight = 0.0
        var availableWidth = 0.0
        var textHeight = 0.0
        assert(lineHeight > 0.0)
        do {
          lines += 1
          availableHeight = lineHeight * lines
          // THIS.height = textHeight * scale
          scale = THIS.height / availableHeight
          availableWidth = THIS.width / scale
          textHeight = UtilsPublic.computeTextHeight(font,text,availableWidth,TextBoundsType.LOGICAL)
          println("loop bloop, height: " + lineHeight)
        } while (!(textHeight <= availableHeight))
        content.setWrappingWidth(availableWidth)
        this.transform = Scale(scale.to2D)
        this.layoutInArea(content, 0, 0, availableWidth, availableHeight, 0, Insets(0), true, true, HPos.CENTER, VPos.CENTER)
      }

    }
  }

  override def computeMinHeight (width: Double1 ): Double1 = 1
  override def computePrefHeight(width: Double1 ): Double1 = content.prefHeight(width)
  override def computeMaxHeight (width: Double1 ): Double1 = Double.MaxValue


}
