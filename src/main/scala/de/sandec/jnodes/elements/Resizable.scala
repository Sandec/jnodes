package de.sandec.jnodes.elements

import simplefx.core._
import simplefx.all._

class ResizableGroup extends Group with Resizable
trait Resizable extends Node {

  override def isResizable() = true
  override def prefHeight(width : Double) = height
  override def prefWidth (height: Double) = width

  @Bind var width : Double  = 0
  @Bind var height: Double  = 0
  @Bind var wh    : Double2 = <->(width,height)

  @Bind var x : Double  = 0
  @Bind var y : Double  = 0
  @Bind var xy: Double2 = <->(x,y)

  override def resize(width: Double, height: Double): Unit = {
    wh = (width,height)
  }
  override def relocate(x: Double, y: Double): Unit = {
    xy = (x,y)
  }
}
