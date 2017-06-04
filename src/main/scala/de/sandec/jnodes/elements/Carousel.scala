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

  @Bind val rightButton = new Region{r => javafx.scene.layout.StackPane.setAlignment(r, Pos.CENTER_RIGHT); styleClass ::= STYLE_NAME + "-right"}
  @Bind val leftButton  = new Region{r => javafx.scene.layout.StackPane.setAlignment(r, Pos.CENTER_LEFT ); styleClass ::= STYLE_NAME + "-left" }

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

  this.clip = new Rectangle {
    this.wh <-- THIS.wh
  }

  this <++ rightButton
  this <++ leftButton


  rightButton.onMouseClicked --> { e => if(e.isStillSincePress) onLeft  }
  leftButton .onMouseClicked --> { e => if(e.isStillSincePress) onRight }

  this.onMousePressed --> { e =>
    val start = (e.getX, e.getY)
    val startT = systemTime
    var disp: Disposer = null
    disp = onMouseReleased --> { (e2: MouseEvent) =>
      val end = (e2.getX, e2.getY)
      val endT = systemTime

      def change = (end - start)

      def cond1 = !e2.isStillSincePress
      def cond2 = (end - start).length > 20

      def condLeft  = change._1 < 20
      def condRight = change._1 > 20

      def condHorizontal = change._1.abs > change._2.abs

      def isLeft = cond1 && cond2 && condLeft && condHorizontal
      def isRight = cond1 && cond2 && condRight && condHorizontal

      if(isLeft)  onLeft
      if(isRight) onRight
      disp.dispose
    }

  }

  this.onSwipeLeft  --> { onLeft  }
  this.onSwipeRight --> { onRight }
}
