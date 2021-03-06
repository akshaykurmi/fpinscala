package fpinscala.datastructures

sealed trait List[+A]

// `List` data type, parameterized on a type, `A`
case object Nil extends List[Nothing]

// A `List` data constructor representing the empty list
/* Another data constructor, representing nonempty lists. Note that `tail` is another `List[A]`,
which may be `Nil` or another `Cons`.
 */
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List {
    // `List` companion object. Contains functions for creating and working with lists.
    def sum(ints: List[Int]): Int = ints match {
        // A function that uses pattern matching to add up a list of integers
        case Nil => 0 // The sum of the empty list is 0.
        case Cons(x, xs) => x + sum(xs) // The sum of a list starting with `x` is `x` plus the sum of the rest of the list.
    }

    def product(ds: List[Double]): Double = ds match {
        case Nil => 1.0
        case Cons(0.0, _) => 0.0
        case Cons(x, xs) => x * product(xs)
    }

    def apply[A](as: A*): List[A] = // Variadic function syntax
        if (as.isEmpty) Nil
        else Cons(as.head, apply(as.tail: _*))

    val x = List(1, 2, 3, 4, 5) match {
        case Cons(x, Cons(2, Cons(4, _))) => x
        case Nil => 42
        case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
        case Cons(h, t) => h + sum(t)
        case _ => 101
    }

    def append[A](a1: List[A], a2: List[A]): List[A] =
        a1 match {
            case Nil => a2
            case Cons(h, t) => Cons(h, append(t, a2))
        }

    def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B = // Utility functions
        as match {
            case Nil => z
            case Cons(x, xs) => f(x, foldRight(xs, z)(f))
        }

    def sum2(ns: List[Int]) =
        foldRight(ns, 0)((x, y) => x + y)

    def product2(ns: List[Double]) =
        foldRight(ns, 1.0)(_ * _) // `_ * _` is more concise notation for `(x,y) => x * y`; see sidebar

    //----------------------------------------------------------------------------------------------------------------//

    // 3.2
    def tail[A](l: List[A]): List[A] = l match {
        case Nil => Nil
        case Cons(_, t) => t
    }

    // 3.3
    def setHead[A](l: List[A], h: A): List[A] = l match {
        case Nil => Nil
        case Cons(_, t) => Cons(h, t)
    }

    // 3.4
    def drop[A](l: List[A], n: Int): List[A] = (l, n) match {
        case (Nil, _) => Nil
        case (list, 0) => list
        case (list, num) => drop(tail(list), num - 1)
    }

    // 3.5
    def dropWhile[A](l: List[A], f: A => Boolean): List[A] = l match {
        case Cons(h, t) if f(h) => dropWhile(t, f)
        case _ => l
    }

    // 3.6
    def init[A](l: List[A]): List[A] = l match {
        case Nil => Nil
        case Cons(_, Nil) => Nil
        case Cons(h, t) => Cons(h, init(t))
    }

    // 3.7
    def productShortCircuit(ns: List[Double]): Double =
        foldRight(ns, 1.0)((a, b) => {
            if (b == 0) return 0
            a * b
        })

    // 3.9
    def length[A](l: List[A]): Int =
        foldRight(l, 0)((_, b) => b + 1)

    // 3.10
    def foldLeft[A, B](l: List[A], z: B)(f: (B, A) => B): B = l match {
        case Nil => z
        case Cons(h, t) => foldLeft(t, f(z, h))(f)
    }

    // 3.11
    def sumFoldLeft(ns: List[Int]) =
        foldLeft(ns, 0)(_ + _)

    def productFoldLeft(ns: List[Double]) =
        foldLeft(ns, 1.0)(_ * _)

    def lengthFoldLeft[A](l: List[A]): Int =
        foldLeft(l, 0)((a, _) => a + 1)

    // 3.12
    def reverse[A](l: List[A]): List[A] =
        foldLeft(l, List[A]())((a, b) => Cons(b, a))

    // 3.13
    def foldLeftUsingFoldRight[A, B](l: List[A], z: B)(f: (B, A) => B): B = ???

    def foldRightUsingFoldLeft[A, B](l: List[A], z: B)(f: (A, B) => B): B = ???

    // 3.14
    def appendFoldRight[A](l: List[A], z: List[A]): List[A] =
        foldRight(l, z)((a, b) => Cons(a, b))

    // 3.15
    def concat[A](l: List[List[A]]): List[A] =
        foldLeft(l, List[A]())((a, b) => append(a, b))

    // 3.16
    def transformAdd1(l: List[Int]): List[Int] =
        foldRight(l, List[Int]())((a, b) => Cons(a + 1, b))

    // 3.17
    def transformDoubleToString(l: List[Double]): List[String] =
        foldRight(l, List[String]())((a, b) => Cons(a.toString, b))

    // 3.18
    def map[A, B](l: List[A])(f: A => B): List[B] =
        foldRight(l, List[B]())((a, b) => Cons(f(a), b))

    // 3.19
    def filter[A](l: List[A])(f: A => Boolean): List[A] =
        foldRight(l, List[A]())((a, b) => {
            if (f(a)) Cons(a, b)
            else b
        })

    // 3.20
    def flatMap[A, B](as: List[A])(f: A => List[B]): List[B] =
        concat(map(as)(f))

    // 3.21
    def filterFlatMap[A](as: List[A])(f: A => Boolean): List[A] =
        flatMap(as)(a => {
            if (f(a)) List(a)
            else Nil
        })

    // 3.22
    def addPairwise(a: List[Int], b: List[Int]): List[Int] = (a, b) match {
        case (Cons(h1, t1), Cons(h2, t2)) => Cons(h1 + h2, addPairwise(t1, t2))
        case (Nil, _) => Nil
        case (_, Nil) => Nil
    }

    // 3.23
    def zipWith[A, B, C](a: List[A], b: List[B])(f: (A, B) => C): List[C] = (a, b) match {
        case (Cons(h1, t1), Cons(h2, t2)) => Cons(f(h1, h2), zipWith(t1, t2)(f))
        case (Nil, _) => Nil
        case (_, Nil) => Nil
    }

    // 3.24
    def take[A](ls: List[A], n: Int): List[A] = {
        if (length(ls) < n) return Nil
        (ls, n) match {
            case (_, x) if x <= 0 => Nil
            case (Cons(h, t), x) => Cons(h, take(t, x - 1))
        }
    }

    def sliding[A](ls: List[A], n: Int): List[List[A]] = {
        if (n <= 0) return Nil
        ls match {
            case l if length(l) < n => Nil
            case l if length(l) == n => Cons(l, Nil)
            case Cons(_, t) => Cons(take(ls, n), sliding(t, n))
        }
    }

    def hasSubsequence[A](sup: List[A], sub: List[A]): Boolean =
        foldLeft(sliding(sup, length(sub)), false)((acc, a) => acc || a == sub)
}
