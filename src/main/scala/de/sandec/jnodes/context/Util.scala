package de.sandec.jnodes.context

import simplefx.core._
import simplefx.all._

object Util {
  implicit class PageAndPopupOps(inject: Node) {
    def setPage(page: Node) = {
      PageContext.getContext(inject).children = List(page)
    }
    def setPopup(popup: Node) = {
      PageContext.getContext(inject) <++ popup
    }
    def removePopup(popup: Node) = {
      PageContext.getContext(inject).children = PageContext.getContext(inject).children.filter(_ != popup)
    }
  }
}
