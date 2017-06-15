package de.sandec.jnodes.config




case class DefaultValue[A](value: A)

object DefaultValue {
  def defineDefault[A](x: A) = DefaultValue(x)
  def getDefault[A](implicit default: DefaultValue[A]): A = default.value

  implicit def tpl2Default[A: DefaultValue,B: DefaultValue] = defineDefault((getDefault[A],getDefault[B]))
  implicit def tpl3Default[A: DefaultValue,B: DefaultValue,C: DefaultValue] = defineDefault((getDefault[A],getDefault[B],getDefault[C]))
  implicit def defaultInt = defineDefault(0)
  implicit def defaultString = defineDefault("")
  implicit def defaultList[A]: DefaultValue[List[A]] = defineDefault(Nil)
}
