package de.sandec.jnodes.context

import simplefx.core._
import simplefx.all._
import simplefx.util.Predef.extension

class ContextManager[A] {
  @extension class ExtWithSession(x: Node) {
    @Bind private[context] var context: A = null
    private[context] def getContext: A = {
      if(context != null) context
      else {
        if(x.getStyleableParent != null &&  x.getStyleableParent.isInstanceOf[Node]) {
          x.getStyleableParent.asInstanceOf[Node].getContext
        } else {
          assert(false,"Couldn't find SessionManager!")
          ???
        }
      }
    }
  }

  def setContext(x: Node, y: A): Unit = x.context = y
  def getContext(x: Node): A = x.getContext
}
