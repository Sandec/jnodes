package jnodes.example

import de.sandec.jnodes.elements.{AdaptiveImageHorizontal, Carousel}
import simplefx.all._
import simplefx.core._
import simplefx.experimental._

object TestCarousel extends App
@SimpleFXApp class TestCarousel { THIS =>

  root = new VBox {
    fillWidth = true
    this <++ new Carousel {
      prefWH = (300,300)
      content <++ new Label("element a") {prefWH = (200,200)}
      content <++ new Label("element b")
      content <++ new Label("element c")
    }
    this <++ new Carousel {
      prefWH = (300,300)
      content <++ new AdaptiveImageHorizontal(Image.cached("/test/image/1.png"))
      content <++ new AdaptiveImageHorizontal(Image.cached("/test/image/2.png"))
      content <++ new AdaptiveImageHorizontal(Image.cached("/test/image/3.png"))
      content <++ new AdaptiveImageHorizontal(Image.cached("/test/image/4.png"))
      content <++ new AdaptiveImageHorizontal(Image.cached("/test/image/5.png"))
    }
  }
}
