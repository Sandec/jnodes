package de.sandec.jnodes.device

import de.sandec.jnodes.elements.AdaptiveImageHorizontal
import simplefx.all._
import simplefx.core._
import simplefx.experimental._

class DemoWrapper(_content: Node, _mode: Device.Device) extends HBox(10){ BOX =>

  @Bind var toggle: Boolean = false

  val minSize: Double = 15.0
  val maxSize: Double = 30.0

  val contentPane = new StackPane{
    alignment = Pos.CENTER
    <++ (new Device(_content){
      device = _mode
      prefWidthProp   = if(_mode == Device.imac_monitor) 350.0 else 200.0
      prefHeightProp  = 300.0
    })
  }

  BOX <++ (contentPane)
}

class DemoContainer(landscape_content: Node, portrait_content1: Node, portrait_content2: Node, portrait_content3: Node, portrait_content4: Node) extends Pane { PANE =>

  @Bind var scalingTime            : Time   = (0.3 s)
  @Bind var scalingFactor          : Double = (2.5)

  @Bind var position_imac          : (Double, Double) = position(2,0)
  @Bind var position_iphone        : (Double, Double) = position(0,0)
  @Bind var position_ipad          : (Double, Double) = position(1,0)
  @Bind var position_galaxyTab     : (Double, Double) = position(1,1)
  @Bind var position_galaxyPhone   : (Double, Double) = position(0,1)

  @Bind var imageLocation          : String = ""

  def getPosition(mode: Device.Device): (Double, Double) = mode match{
    case Device.imac_monitor   => position_imac
    case Device.iphone         => position_iphone
    case Device.ipad           => position_ipad
    case Device.samsung_galaxy => position_galaxyPhone
    case Device.samsung_tablet => position_galaxyTab
    case other                 => (0.0,0.0)
  }

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

  def createDevice(_content: Node, _mode: Device.Device) = new DemoWrapper(_content, _mode){ NODE =>

    @Bind var animation = 0.0
    @Bind var posXY     = <-- (getPosition(_mode))

    def startPos      = (PANE.layoutXY + PANE.labWH * posXY)
    def fullscreenPos = ((PANE.labWH - NODE.labWH) /2)

    def realPos       = (animation * fullscreenPos + (1 - animation) * startPos)

    layoutXY <-- realPos

    when(toggle)  ==> {
      blurPane.style = "-fx-background-color: rgba(0,0,0,0.4);"
      blurPane.toFront()
      animation := 1.0 in (scalingTime) using Interpolator.EASE_OUT
      scaleXY :=  (scalingFactor.to2D)   in (scalingTime) using Interpolator.EASE_OUT
      NODE.toFront()
    } otherwise {
      blurPane.style = "-fx-background-color: transparent;"
      blurPane.toBack()
      scaleXY :=  (1.0,1.0)   in (scalingTime) using Interpolator.EASE_OUT
      animation := 0.0 in (scalingTime) using Interpolator.EASE_OUT
    }

    this.contentPane.onClick --> {
      if(!toggle)toggle := true
    }

    blurPane.onClick --> {
      if(toggle)toggle := false
    }

  }

  def offset : (Double, Double)       = (0.2, 0.4)
  def startingPoint: (Double, Double) = (0.1, 0.15)

  def position(row: Int, column: Int) = (startingPoint + (row.toDouble, column.toDouble) * offset)

  @Bind var device2 : DemoWrapper = createDevice(portrait_content1, Device.iphone        )
  @Bind var device3 : DemoWrapper = createDevice(portrait_content2, Device.ipad          )
  @Bind var device4 : DemoWrapper = createDevice(portrait_content3, Device.samsung_galaxy)
  @Bind var device5 : DemoWrapper = createDevice(portrait_content4, Device.samsung_tablet)

  @Bind var device1 : DemoWrapper = createDevice(landscape_content, Device.imac_monitor  )

  <++ (device1)
  <++ (device2)
  <++ (device3)
  <++ (device4)
  <++ (device5)
}
