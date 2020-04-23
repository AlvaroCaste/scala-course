package exercise5

import cats._ // this import brings all typeclasses
import exercise3._
import scala.annotation.tailrec

object typeclasses {

  /**
    * create a Functor instance for our binary tree
    */
  implicit val treeFunctor: Functor[Tree] = new Functor[Tree] {
    def map[A, B](fa: Tree[A])(f: A => B): Tree[B] =
      exercise3.Tree.map(fa)(f)
  }

  /**
    * create a Foldable instance for our binary tree
    */
  implicit val treeFoldable: Foldable[Tree] = new Foldable[Tree] {
    def foldLeft[A, B](fa: Tree[A], b: B)(f: (B, A) => B): B = fa match {
      case Empty() => b
      case Node(l, a, r) => foldLeft(r, foldLeft(l, f(b, a))(f))(f)
    }

    def foldRight[A, B](fa: Tree[A],lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = fa match {
      case Empty() => lb
      case Node(l, a, r) => foldRight(l, foldRight(r, f(a, lb))(f))(f)
    }
  }

  /**
    * create a Monad instance for Maybe
    */
  sealed trait Maybe[A]
  case class Nothing[A]() extends Maybe[A]
  case class Just[A](a: A) extends Maybe[A]

  implicit val maybeMonad: Monad[Maybe] = new Monad[Maybe] {
    def pure[A](x: A): Maybe[A] = ???

    def flatMap[A, B](fa: Maybe[A])(f: A => Maybe[B]): Maybe[B] = ???


    // This method was not originally part of the Monad typeclass, is added
    // here for performance
    @tailrec
    def tailRecM[A, B](a: A)(f: A => Maybe[Either[A, B]]): Maybe[B] =
      f(a) match {
        case Nothing() => Nothing()
        case Just(Left(a1)) => tailRecM(a1)(f)
        case Just(Right(b)) => Just(b)
      }
  }

}
