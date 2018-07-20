package de.sandec.jnodes.css

import java.io.{FileNotFoundException, InputStream, StringBufferInputStream, StringReader}
import java.net.{URL, URLConnection, URLStreamHandler, URLStreamHandlerFactory}

import simplefx.core._
import simplefx.all._
import simplefx.util.Predef._

object DynamicCSS {

  @extension class AddCSSStringParent(parent: Parent) {
    def isShowing = parent.scene != null && parent.scene.window != null && parent.scene.window.showing
    @Bind private var showingCSS = <--((this.isShowing,cssString))
    @Bind var cssString: String = "" <> {
      var previousURL: URL = null
      updated(showingCSS --> { x =>
        val (showing: Boolean, cssString: String) = x
        if(previousURL != null) {
          parent.getStylesheets().remove(previousURL.toString)
          runLater {
            InMemoryURL.unregister(previousURL.getHost)
            previousURL = null
          }
        }
        if(showing) {
          previousURL = InMemoryURL.genDynamicContent(cssString)
          parent.getStylesheets().add(previousURL.toString)
        }
      })
    }
  }

  @extension class AddCSSStringScene(scene: Scene) {
    def isShowing = scene.window != null && scene.window.showing
    @Bind private var showingCSS = <--((this.isShowing,cssString))
    @Bind var cssString: String = "" <> {
      var previousURL: URL = null
      updated(showingCSS --> { x =>
        val (showing: Boolean, cssString: String) = x
        if(previousURL != null) {
          scene.getStylesheets().remove(previousURL.toString)
          runLater {
            InMemoryURL.unregister(previousURL.getHost)
            previousURL = null
          }
        }
        if(showing) {
          previousURL = InMemoryURL.genDynamicContent(cssString)
          scene.getStylesheets().add(previousURL.toString)
        }
      })

    }
  }


}


object InMemoryURL {

  def log = false

  val inmemoryProtocol = "inmemory"

  var contentMap: Map[String,String] = Map()
  var counter = 0

  def genDynamicContent(content: String): URL = synchronized {
    counter += 1
    registerContent(s"content$counter", content)
  }

  def registerContent(x: String, content: String): URL = synchronized {
    if(log) println("registering content: " + (x -> content))
    contentMap += (x -> content)
    new URL(inmemoryProtocol, x, "")
  }

  def unregister(x: String) = synchronized {
    if(log) println("unregister: " + x)
    assert(contentMap.contains(x))
    contentMap -= x
    if(log) println("keys in map: " + contentMap.keys.toList)
  }

  def getContent(x: String) = synchronized {
    contentMap(x)
  }

  URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory {
    val streamHandler = new URLStreamHandler() {
      override protected def openConnection(url: URL): URLConnection = {
        if (url.getProtocol == inmemoryProtocol) {
          return new StringURLConnection(url, getContent(url.getHost))
        } else {
          throw new FileNotFoundException
        }
      }
    }

    override def createURLStreamHandler(protocol: String): URLStreamHandler = {
      if (inmemoryProtocol == protocol) return streamHandler
      null
    }
  })

  class StringURLConnection(url: URL, content: String) extends URLConnection(url) {
    override def connect(): Unit = ()
    override def getInputStream = new StringBufferInputStream(content)
  }

}


