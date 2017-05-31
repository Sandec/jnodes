package de.sandec.jnodes.device

import de.sandec.jnodes.elements.AdaptiveImageHorizontal
import simplefx.all._
import simplefx.core._
import simplefx.experimental._

class DemoWrapper(_content: Node, _mode: Device.Device) extends HBox(10){

  @Bind var toggle: Boolean = false

  @Bind var imgURL: String = <-- (if(toggle) "de/sandec/jnodes/elements/general/zoom_out.png" else "de/sandec/jnodes/elements/general/zoom_in.png")

  val minSize: Double = 15.0
  val maxSize: Double = 30.0

  val minmaxPane = new StackPane{
    alignment = Pos.CENTER
    <++ (new Circle{
      style = "-fx-fill: linear-gradient(from 0% 0% to 0% 100%, #FFFFFF 0%, #c1c1c1 100%);"
      radius <-- (if(toggle) minSize else maxSize)
    })
    <++ (new ImageView{
      image <-- (Image.cached(imgURL))
      fitWH <-- (if(toggle) minSize.to2D else maxSize.to2D)
    })
    scaleOnHover = (1.2, (0.2 s))
  }

  val contentPane = new StackPane{
    alignment = Pos.CENTER
    <++ (new Device(_content){
      device = _mode
      prefWidthProp   = if(_mode == Device.imac_monitor) 350.0 else 200.0
      prefHeightProp  = 300.0
    })
  }

  <++ (contentPane)
  <++ (minmaxPane)
}

class DemoContainer(landscape_content: Node, portrait_content1: Node, portrait_content2: Node, portrait_content3: Node, portrait_content4: Node) extends Pane { PANE =>

  @Bind var imageLocation = ""

  imageLocation ==> { loc =>
    if(!loc.isEmpty){
      PANE.style = "-fx-background-image: url('" + loc + "'); -fx-background-repeat: stretch; -fx-background-size: 100% 100%;"
    }
  }

  this.padding = Insets(10)

  val offsetWH = PANE.labWH * (0.2, 0.2)

  val blurPane: StackPane = new StackPane{ BLUR =>
    this.prefWH <-- (PANE.wh)
  }

  PANE <++ blurPane

  def createDevice(_content: Node, _mode: Device.Device, posXY: (Double, Double)) = new DemoWrapper(_content, _mode){ NODE =>

    val scaleMax: Dbl = 2.5
    val scaleMin: Dbl = 1.0
    val scaleIn: Time    = (0.2 s)

    @Bind var animation = 0.0

    def startPos      = (PANE.layoutXY + PANE.labWH * posXY)
    def fullscreenPos = ((PANE.labWH - NODE.labWH) /2)

    def realPos       = (animation * fullscreenPos + (1 - animation) * startPos)

    layoutXY <-- realPos

    when(toggle)  ==> {
      blurPane.style = "-fx-background-color: rgba(0,0,0,0.6);"
      blurPane.toFront()
      animation := 1.0 in (scaleIn) using Interpolator.EASE_OUT
      scaleXY :=  (scaleMax.to2D)   in (scaleIn) using Interpolator.EASE_OUT
      NODE.toFront()
    } otherwise {
      blurPane.style = "-fx-background-color: transparent;"
      blurPane.toBack()
      scaleXY :=  (scaleMin.to2D)   in (scaleIn) using Interpolator.EASE_OUT
      animation := 0.0 in (scaleIn) using Interpolator.EASE_OUT
    }

    this.minmaxPane.onClick --> {
      toggle := !toggle
    }

  }

  def offset : (Double, Double)       = (0.2, 0.4)
  def startingPoint: (Double, Double) = (0.1, 0.15)

  def position(row: Int, column: Int) = (startingPoint + (row.toDouble, column.toDouble) * offset)

  @Bind var device2 : DemoWrapper = createDevice(portrait_content1, Device.iphone,         position(0,0))
  @Bind var device3 : DemoWrapper = createDevice(portrait_content2, Device.ipad,           position(1,0))
  @Bind var device4 : DemoWrapper = createDevice(portrait_content3, Device.samsung_galaxy, position(0,1))
  @Bind var device5 : DemoWrapper = createDevice(portrait_content4, Device.samsung_tablet, position(1,1))

  @Bind var device1 : DemoWrapper = createDevice(landscape_content, Device.imac_monitor,   position(2,0))

  <++ (device1)
  <++ (device2)
  <++ (device3)
  <++ (device4)
  <++ (device5)
}
