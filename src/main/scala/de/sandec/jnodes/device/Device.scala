package de.sandec.jnodes.device

import simplefx.all._
import simplefx.core._
import simplefx.experimental._

class Device(_content: Node) extends Pane {  THIS =>
  object NoDevice extends Device.Mode {
    def factor:    Double  = 1.0
    def image:     Image   = null
    def imageWH:   Double2 = THIS.wh
    def contentXY: Double2 = (0,0)
    def contentWH: Double2 = imageWH
  }

  @Bind var device: Device.Mode = NoDevice.asInstanceOf[Device.Mode]
  @Bind val factor      = <--(device.factor)
  @Bind var content     = _content
  @Bind var defaultWidth  = 600.0
  @Bind var defaultHeight = 950.0
  @Bind val contentZoom = <--(Math.sqrt(device.contentWH.y * device.contentWH.x / (defaultWidth * defaultHeight)) * factor)

  @Bind var forcePortrait = false

  @Bind val rotateScene: Boolean = <--(if(forcePortrait) device.contentWH.x > device.contentWH.y else false)


  style = "-fx-background-color: transparent;"
  this <++ new SimpleFXParent {
    translateXY <-- (THIS.wh - device.imageWH * factor) / 2
    def scale1 = (THIS.width  / (device.imageWH._1 * factor))
    def scale2 = (THIS.height / (device.imageWH._2 * factor))
    scaleXY <-- (mini(scale1,scale2)).to2D
    this <++ new ImageView {
      image        <-- device.image
      fitWidth     <-- device.imageWH._1 * factor
      preserveRatio =  true
    }
    this <++ new SimpleFXParent {
      def rotate = if (rotateScene) Translate(0, newWH.y) * Rotate(-90) else Transform.IDENTITY

      transform <-- Scale(contentZoom, contentZoom) * rotate
      children <-- content :: Nil
      clip = new Rectangle {
        this.xy <-- newXY
        this.wh <-- rotatedWH
      }
    }
  }

  def newXY = factor * device.contentXY / contentZoom
  def newWH = factor * device.contentWH / contentZoom
  def rotatedWH = if(rotateScene) newWH.swap else newWH
  @Bind val posi = <--(content,newXY,rotatedWH)
  posi --> { case (content,newXY, rotatedWH) =>
    content.resizeRelocate(newXY.x, newXY.y, rotatedWH.x, rotatedWH.y)
  }

}

object Device {

  abstract class Mode {
    def factor:    Double
    def image:     Image
    def imageWH:   Double2
    def contentXY: Double2
    def contentWH: Double2
  }
  case class Device(val image: Image, val imageWH: Double2, val contentXY: Double2, val contentWH: Double2, val factor: Double) extends Mode

  lazy val iphone = Device(Image.cached("de/sandec/jnodes/devices/iphone.png"), (768,1491), ( 66, 267), (646, 962), 0.51)
  lazy val samsung_tablet = Device(Image.cached("de/sandec/jnodes/devices/samsungtab.png"), (443.0,758.0) + (100,0), ( 54,30), (443.0,708.0), 1.0)
  lazy val ipad = Device(Image.cached("de/sandec/jnodes/devices/ipad.png"), (1000.0,1385.0), ( 54,97), (895.0,1192.0), 0.55)
  lazy val samsung_galaxy = Device(Image.cached("de/sandec/jnodes/devices/galaxyS7.png"), (496.0,1000.0), ( 273,150), (452.0,770.0), 0.5)
  lazy val imac_monitor = Device(Image.cached("de/sandec/jnodes/devices/imac.png"),(1122.0 , 904.0),(47,42),(1030,588), 0.83)

  //lazy val nophone = Device(null, (768,1491), ( 66, 267), (646, 962))

}