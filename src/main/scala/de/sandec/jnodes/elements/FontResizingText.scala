package de.sandec.jnodes.elements

import com.sun.javafx.scene.control.skin.UtilsPublic
import simplefx.all._
import simplefx.core._

class FontResizingText extends Pane { THIS =>

  styleClass ::= "font-resiszing-text"

  @Bind var font = <*>(content.font)
  @Bind var text: String = <*>(content.text)

  def log = false

  override def getContentBias = Orientation.HORIZONTAL

  def computeHeight(width: Double): Double = {
    100
  }
  @Bind val content = new Text {
    styleClass ::= "font-resiszing-text-text"
    styleClass ::= "text"
    //this.text <-> THIS.text
    //this.font <-> THIS.font
    this.textAlignment = TextAlignment.CENTER
    boundsType = TextBoundsType.VISUAL
  }

  children <-- List(container)
  object container extends Pane {
    //managed = false
    this <++ content

    font --> {
      this.requestLayout()
    }
    content.text --> {
      this.requestLayout()
    }
    THIS.wh --> {
      this.requestLayout()
    }
    override def layoutChildren(): Unit = {
      if(log) println("Font text: " + content.font)
      if(log) println("Input WH: " + THIS.wh)
      val lineHeight = UtilsPublic.getLineHeight(font,TextBoundsType.LOGICAL)
      var lines = (THIS.height / lineHeight).floor
      if(log) println("LINEHEIGHT" + lineHeight)

      if(UtilsPublic.computeTextHeight(font,text,THIS.width,TextBoundsType.LOGICAL) <= THIS.height) {
        //println("fine")
        this.transforms = Nil
        content.setWrappingWidth(THIS.width)
        if(log) println(s"simple resizeRelocate(0,0,${THIS.width}, ${THIS.height})")
        this.layoutInArea(content, 0, 0, THIS.width, THIS.height, 0, Insets(0), true, true, HPos.CENTER, VPos.CENTER)
      } else {
        //println("downscaling case: " + THIS.wh)
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
          if(log) println(s"TEXTHEIGHT($font,$text,$availableWidth)" + textHeight)
          //println("loop bloop, height: " + lineHeight)
        } while (textHeight > availableHeight)
        content.setWrappingWidth(availableWidth)
        this.transform = Scale(scale.to2D)
        if(log) println(s"complex resizeRelocate(0,0,${availableWidth}, ${availableHeight})")
        this.layoutInArea(content, 0, 0, availableWidth, availableHeight, 0, Insets(0), true, true, HPos.CENTER, VPos.CENTER)
      }

    }
  }

  override def computeMinHeight (width: Double1 ): Double1 = 1
  override def computePrefHeight(width: Double1 ): Double1 = UtilsPublic.computeTextHeight(font,text,width,TextBoundsType.LOGICAL)
  override def computeMaxHeight (width: Double1 ): Double1 = Double.MaxValue

  override def computeMinWidth (height: Double1 ): Double1 = 1
  override def computePrefWidth(height: Double1 ): Double1 = UtilsPublic.computeTextWidth(font,text,0.0)
  override def computeMaxWidth (height: Double1 ): Double1 = Double.MaxValue
}
