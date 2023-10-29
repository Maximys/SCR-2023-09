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
      override def myToString(x: Boolean): String = x.toString.myToString;
    }

    implicit val intShow = new Show[Int] {
      override def myToString(x: Int): String = x.toString.myToString;
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
}
