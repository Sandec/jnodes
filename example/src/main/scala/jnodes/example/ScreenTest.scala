package jnodes.example

import de.sandec.jnodes.device.{DemoContainer, DeviceContainer}
import simplefx.all._
import simplefx.core._

object ScreenTest extends App
@SimpleFXApp
class ScreenTest { PARENT =>
    title = "Some demo"

    scene = new Scene(new DemoContainer, 1200, 600)

}
