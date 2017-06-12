package de.sandec.jnodes.device

import de.sandec.jnodes.elements.Resizable
import simplefx.all._
import simplefx.core._

class DeviceScene(_root: Node, _width: Double, _height: Double)
  extends Scene(new Group, _width: Double, _height: Double) { THIS =>

  @Bind var device = <*> (deviceNode.device)
  @Bind val deviceNode = new Device(_root)
  @Bind var content = <*>(deviceNode.content)
  root = deviceNode

  fill = Color.TRANSPARENT
}
