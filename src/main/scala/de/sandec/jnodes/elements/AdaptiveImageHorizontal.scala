package de.sandec.jnodes.elements

import simplefx.all._
import simplefx.core._

class AdaptiveImageHorizontal(image: Image) extends Pane { PANE =>

  def this(imagePath: String) = this(Image.cached(imagePath))
  val img = image

  override def getContentBias = Orientation.HORIZONTAL

  def computeHeight(width: Double): Double = width * img.height / (img.width)

  override def computeMinHeight (width: Double1 ): Double1 = { if(width <= 0.0) Double.MinValue else computeHeight(width) }
  override def computePrefHeight(width: Double1 ): Double1 = { if(width <= 0.0) 1               else computeHeight(width) }
  override def computeMaxHeight (width: Double1 ): Double1 = { if(width <= 0.0) Double.MaxValue else computeHeight(width) }

  def BackgroundImagePosition = new BackgroundSize(1.0,1.0, true, true, true, false)

  this.background <-- new Background(
    new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
      BackgroundPosition.CENTER, BackgroundImagePosition))
}
