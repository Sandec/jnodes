package de.sandec.jnodes.elements

import simplefx.all._
import simplefx.core._

class ScalingElement(node: Node) extends StackPane { THIS =>
  this <++ node

  def scaleTo = 1.12
  def scaleIn = (0.4 s)
  updated {
    node.onMouseEntered --> { scaleXY :=  (scaleTo.to2D)   in (scaleIn) using Interpolator.EASE_OUT}
    node.onMouseExited  --> { scaleXY :=  (1.0      .to2D) in (scaleIn) using Interpolator.EASE_OUT}
  }

  clip = new Rectangle {
    this.wh <-- THIS.wh
  }
}