package de.sandec.jnodes.config

import simplefx.core._
import simplefx.all._

trait Configurable[T] {
  def createNode(x: B[T]): Node
}
object Configurable {
  def config[T](x: B[T])(implicit configurable: Configurable[T]): Node = configurable.createNode(x)
}


