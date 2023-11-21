package module1.homework.cats

object cats_homework {
  trait Show[T] {
    def myToString(x: T): String;
  }

  object Show {
    def apply[T](implicit value: Show[T]): Show[T] = value;

    def fromFunction[T](func: T => String): Show[T] = (value: T) => func(value);

    def fromJvm[T]: Show[T] = (value: T) => value.toString

    implicit val stringShow = new Show[String] {
      override def myToString(x: String): String = s"MyToString_Result: $x";
    }

    implicit val booleanShow = new Show[Boolean] {
      override def myToString(x: Boolean): String = x.toString;
    }

    implicit val intShow = new Show[Int] {
      override def myToString(x: Int): String = x.toString;
    }

    implicit def listShow[A](implicit valueShow: Show[A]): Show[List[A]] = Show.fromFunction[List[A]] {
      case Nil => "[MyEmptyList]"
      case ::(head, tail) =>
        tail.foldLeft(s"[MyList]" +
          s" [${valueShow.myToString(head)}")((str: String, value: A) =>
          s"${str}; ${valueShow.myToString(value)}") + "]"
    }

    implicit class ShowSyntax[T](v: T) {
      def myToString(implicit vs: Show[T]): String = vs.myToString(v)
    }
  }

  trait Monad[F[_]] {
    def flatMap[T1, T2](f1: F[T1])(f2: T1 => F[T2]): F[T2];
    def flatten[T](f: F[F[T]]): F[T] = flatMap(f)(x => x);
  }

  object Monad {
    def apply[F[_]](implicit value: Monad[F]): Monad[F] = value;

    implicit def listMonad: Monad[List] = new Monad[List] {
      def flatMap[T1, T2](f1: List[T1])(f2: T1 => List[T2]): List[T2] = f1.flatMap(f2);
    }

    implicit def optionMonad: Monad[Option] = new Monad[Option] {
      def flatMap[T1, T2](f1: Option[T1])(f2: T1 => Option[T2]): Option[T2] = f1 match {
        case Some(x) => f2(x)
        case None => None
      }
    }

    implicit def setMonad: Monad[Set] = new Monad[Set] {
      def flatMap[T1, T2](f1: Set[T1])(f2: T1 => Set[T2]): Set[T2] = f1.flatMap(f2);
    }
  }
}