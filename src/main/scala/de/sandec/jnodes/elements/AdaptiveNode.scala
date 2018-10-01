package de.sandec.jnodes.elements


import simplefx.all._
import simplefx.core._

class AdaptiveNode(x: Node) extends Pane {THIS =>

  @Bind var defaultWidth = 1000
  @Bind var content: Region = x.asInstanceOf[Region]

  override def getContentBias = Orientation.HORIZONTAL

  def factor(width: Double) = {
    width / defaultWidth
  }

  override def layoutChildren(): Unit = {
    container.layoutChildren()
  }

  children <-- List(container)
  object container extends AnchorPane {
    children <-- List(content)

    THIS.width --> this.requestLayout()
    override def layoutChildren(): Unit = {
      val width = THIS.width
      val targetWH = (THIS.width / factor(width), THIS.height / factor(width))
      this.layoutInArea(content, 0, 0, targetWH._1, targetWH._2, 0, Insets(0), true, true, HPos.RIGHT, VPos.CENTER)
      this.transform = Scale(factor(width).to2D)
    }
  }

  override def computeMinHeight (width: Double1 ): Double1 = {/*println("min " + width);*/ if(width <= 0.0) 1.0                    else content. minHeight(width / factor(width)) * factor(width) }
  override def computePrefHeight(width: Double1 ): Double1 = {/*println("pref" + width);*/ if(width <= 0.0) content.prefHeight(-1) else content.prefHeight(width / factor(width)) * factor(width) }
  override def computeMaxHeight (width: Double1 ): Double1 = {/*println("max " + width);*/ if(width <= 0.0) Double.MaxValue        else content. maxHeight(width / factor(width)) * factor(width) }

  override def computeMinWidth (height: Double1 ): Double1 = {/*println("min " + width);*/ if(height <= 0.0) 1.0                                else content. minWidth(height) * factor(-1) }
  override def computePrefWidth(height: Double1 ): Double1 = {/*println("pref" + width);*/ if(height <= 0.0) content.prefWidth(-1) * factor(-1) else content.prefWidth(height) * factor(-1) }
  override def computeMaxWidth (height: Double1 ): Double1 = {/*println("max " + width);*/ if(height <= 0.0) Double.MaxValue                    else content. maxWidth(height) * factor(-1) }
}