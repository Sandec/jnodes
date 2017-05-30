package de.sandec.jnodes.device

import simplefx.all._
import simplefx.core._
import simplefx.experimental._

class DemoContainer(landscape_content: Node, portrait_content1: Node, portrait_content2: Node, portrait_content3: Node, portrait_content4: Node) extends Pane { PANE =>

  this.padding = Insets(10)

  val offsetWH = PANE.labWH * (0.2, 0.2)

  def createDevice(_content: Node, _mode: Device.Device, posXY: (Double, Double)) = new Device(_content){ NODE =>
    device = _mode
    @Bind var toggle: Boolean = false
    prefHeightProp <-- (PANE.height * 0.25)

    val scaleMax: Double = 3.0
    val scaleMin: Double = 1.0
    val scaleIn: Time    = (0.2 s)

    @Bind var animation = 0.0

    def startPos      = (PANE.layoutXY + PANE.labWH * posXY)
    def fullscreenPos = ((PANE.labWH - NODE.labWH) /2)

    def realPos       = (animation * fullscreenPos + (1 - animation) * startPos)

    layoutXY <-- realPos

    when(toggle)  ==> {
      NODE.toFront()
      animation := 1.0 in (scaleIn) using Interpolator.EASE_OUT
      scaleXY :=  (scaleMax.to2D)   in (scaleIn) using Interpolator.EASE_OUT
    } otherwise {
      scaleXY :=  (scaleMin.to2D)   in (scaleIn) using Interpolator.EASE_OUT
      animation := 0.0 in (scaleIn) using Interpolator.EASE_OUT
    }

    this.onClick --> (toggle := !toggle)

  }


  def offset : (Double, Double)       = (0.2, 0.4)
  def startingPoint: (Double, Double) = (0.1, 0.15)

  def position(row: Int, column: Int) = (startingPoint + (row.toDouble, column.toDouble) * offset)

  @Bind var device2 : Device = createDevice(portrait_content1, Device.iphone,         position(0,0))
  @Bind var device3 : Device = createDevice(portrait_content2, Device.ipad,           position(1,0))
  @Bind var device4 : Device = createDevice(portrait_content3, Device.iphone,         position(0,1))
  @Bind var device5 : Device = createDevice(portrait_content4, Device.samsung_tablet, position(1,1))

  @Bind var device1 : Device = createDevice(landscape_content, Device.imac_monitor,   position(2,0))

  <++ (device1)
  <++ (device2)
  <++ (device3)
  <++ (device4)
  <++ (device5)

}
