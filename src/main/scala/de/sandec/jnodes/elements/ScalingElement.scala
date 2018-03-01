package de.sandec.jnodes.elements

import simplefx.all._
import simplefx.core._

class ScalingElement(node: Node) extends StackPane { THIS =>

  def scaleTo = 1.12
  def scaleIn = (0.4 s)

  this <++ new StackPane {
    updated {
      when( hover) --> { scaleXY :=  (scaleTo.to2D) in (scaleIn) using Interpolator.EASE_OUT}
      when(!hover) --> { scaleXY :=  (1.0    .to2D) in (scaleIn) using Interpolator.EASE_OUT}
    }
    this <++ node
  }

  clip = new Rectangle {
    this.wh <-- THIS.wh
  }
}