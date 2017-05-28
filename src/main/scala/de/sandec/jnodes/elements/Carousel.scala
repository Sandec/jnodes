package de.sandec.jnodes.elements

import simplefx.all._
import simplefx.core._
import simplefx.experimental._

class Carousel extends StackPane { THIS =>
  stylesheets ::= "de/sandec/jnodes/elements/carousel/carousel.css"

  val STYLE_NAME = "carousel"
  styleClass ::= STYLE_NAME

  @Bind var currentTarget = 0
  @Bind var currentPlace = 0.0
  def numberElements = content.length

  @Bind var content: List[Node] = Nil

  def onLeft  = {
    currentTarget += 1
    currentTarget = (currentTarget+numberElements) % numberElements
    currentPlace := currentTarget in (1 s) using Interpolator.EASE_BOTH
  }
  def onRight = {
    currentTarget -= 1
    currentTarget = (currentTarget+numberElements) % numberElements
    currentPlace := currentTarget in (1 s) using Interpolator.EASE_BOTH
  }

  @Bind val rightButton = new Region{r => javafx.scene.layout.StackPane.setAlignment(r, Pos.CENTER_RIGHT); styleClass ::= STYLE_NAME + "-right"; this.onClick --> onLeft }
  @Bind val leftButton  = new Region{r => javafx.scene.layout.StackPane.setAlignment(r, Pos.CENTER_LEFT ); styleClass ::= STYLE_NAME + "-left" ; this.onClick --> onRight}



  def manageElem(node: Node, place: Int) = {
    node.visible <-- (((place-1) < currentPlace) && (place+1 > currentPlace))
    node.translateX <-- ((place-currentPlace)*THIS.width)
  }


  this <++ new StackPane {
    children <-- THIS.content
    THIS.content ==> {
      content.zipWithIndex.foreach { case (node,i) =>
        manageElem(node,i)
      }
    }
  }

  this <++ rightButton
  this <++ leftButton
}
