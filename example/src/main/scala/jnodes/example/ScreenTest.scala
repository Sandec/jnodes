package jnodes.example

import de.sandec.jnodes.device.DemoContainer
import de.sandec.jnodes.device.{DemoContainer}
import simplefx.all._
import simplefx.core._

object ScreenTest extends App
@SimpleFXApp
class ScreenTest { PARENT =>
    title = "Some demo"

    scene = new Scene(new StackPane(new DemoContainer(new StackPane, new StackPane, new StackPane, new StackPane, new StackPane){
      @Bind override var imageLocation = "/test/image/flowers.jpg"
    }){padding = Insets(20)}, 1200, 600)

}
