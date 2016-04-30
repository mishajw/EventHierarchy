package bandhierarchy.front

import org.singlespaced.d3js.{Selection, d3}

import scala.scalajs.js.UndefOr

package object view {
  def mkFunction[T, R](sel: Selection[T], f: (T, Int) => R) = {
    new scala.scalajs.js.Function3[T, Int, scala.scalajs.js.UndefOr[Int], scala.scalajs.js.|[scala.scalajs.js.|[Double, String], Boolean]] {
      override def apply(t: T, i: Int, j: UndefOr[Int]) = f(t, i).asInstanceOf[d3.Primitive]
    }.asInstanceOf[sel.DatumFunction[d3.Primitive]]
  }
}
