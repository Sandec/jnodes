package de.sandec.jnodes.device

import de.sandec.jnodes.elements.Resizable
import simplefx.all._
import simplefx.core._

class DeviceScene(_root: Resizable with Parent, _width: Double, _height: Double)
  extends Scene(_root, _width: Double, _height: Double) { THIS =>

  @Bind var device = <*> (deviceNode.device)
  @Bind val deviceNode = new Device(_root)
  @Bind var content = <*>(deviceNode.content)
  root = deviceNode

  fill = Color.TRANSPARENT
}
