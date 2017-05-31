package jnodes.example

import de.sandec.jnodes.device.{DemoContainer, DeviceContainer}
import simplefx.all._
import simplefx.core._

object ScreenTest extends App
@SimpleFXApp
class ScreenTest { PARENT =>
    title = "Some demo"

    scene = new Scene(new DemoContainer(new StackPane, new StackPane, new StackPane, new StackPane, new StackPane){
      @Bind override var imageLocation = "/test/image/flowers.jpg"
    }, 1200, 600)

}
