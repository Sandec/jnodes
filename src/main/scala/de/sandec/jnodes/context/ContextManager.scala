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
        assert((x.parent != null),"Couldn't find SessionManager!")
        x.parent.getContext
      }
    }
  }

  def setContext(x: Node, y: A): Unit = x.context = y
  def getContext(x: Node): A = x.getContext
}
