package de.sandec.jnodes.css

import java.io.{FileNotFoundException, InputStream, StringBufferInputStream, StringReader}
import java.net.{URL, URLConnection, URLStreamHandler, URLStreamHandlerFactory}

import simplefx.core._
import simplefx.all._
import simplefx.util.Predef._
import java.io.PrintWriter
import java.io.File

object DynamicCSS {

  @extension class AddCSSStringParent(parent: Parent) {
    def isShowing = parent.scene != null && parent.scene.window != null && parent.scene.window.showing
    @Bind private var showingCSS = <--((this.isShowing,cssString))
    @Bind var cssString: String = "" <> {
      var previousURL: URL = null
      var previousKey: String = null
      updated(showingCSS --> { x =>
        val (showing: Boolean, cssString: String) = x
        if(previousURL != null) {
          parent.getStylesheets().remove(previousURL.toString)

          val pKey = previousKey
          previousURL = null
          previousKey = null
          runLater {
            InMemoryURL.unregister(pKey)
          }
        }
        if(showing) {
          val r = InMemoryURL.genDynamicContent(cssString)
          previousKey = r._1
          previousURL = r._2
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
      var previousKey: String = null
      updated(showingCSS --> { x =>
        val (showing: Boolean, cssString: String) = x
        if(previousURL != null) {
          scene.getStylesheets().remove(previousURL.toString)
          val pKey = previousKey
          previousURL = null
          previousKey = null
          runLater {
            InMemoryURL.unregister(pKey)
          }
        }
        if(showing) {
          val r = InMemoryURL.genDynamicContent(cssString)
          previousKey = r._1
          previousURL = r._2
          scene.getStylesheets().add(previousURL.toString)
        }
      })

    }
  }


}


object InMemoryURL {

  def log = false

  var contentMap: Map[String,String] = Map()
  var counter = 0

  var files = java.io.File.createTempFile("cssfiles","")
  println("files" + files.exists())
  println("files" + files.isDirectory)
  files.delete()
  assert(files.mkdirs)
  files.deleteOnExit

  def genDynamicContent(content: String): (String,URL) = synchronized {
    counter += 1
    val key = s"content$counter"
    (key, registerContent(key, content))
  }

  def getFile(x: String) = new File(files,x+".css")
  def registerContent(x: String, content: String): URL = synchronized {
    if(log) println("registering content: " + (x -> content))
    contentMap += (x -> content)
    val f = getFile(x)
    new PrintWriter(f) { write(content); close }
    f.toURI.toURL
  }

  def unregister(x: String): Unit = synchronized {
    if(log) println("unregister: " + x)
    assert(contentMap.contains(x))
    contentMap -= x
    getFile(x).delete()
    if(log) println("keys in map: " + contentMap.keys.toList)
  }

  def getContent(x: String) = synchronized {
    contentMap(x)
  }

}


