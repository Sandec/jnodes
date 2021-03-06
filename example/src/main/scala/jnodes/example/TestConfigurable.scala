package jnodes.example

import simplefx.all._
import simplefx.core._
import de.sandec.jnodes.config.Configurable._
import de.sandec.jnodes.config.ConfigurableExtended._

object TestConfigurable extends App
@SimpleFXApp class TestConfigurable { THIS =>


  root = new VBox {
    spacing = 10
    maxWidthProp = 300
    @Bind var a = ""
    @Bind var b = 1
    @Bind var c: Option[Color] = null //Some(Color.BLUE)
    @Bind var list: List[(Int,String)] = null
    @Bind var list2: List[(List[Int],String)] = Nil
    this <++ config(a)
    this <++ configureMultiLineString.createNode(a)
    this <++ config(b)
    this <++ config(c)
    this <++ config(list)
  }

}
