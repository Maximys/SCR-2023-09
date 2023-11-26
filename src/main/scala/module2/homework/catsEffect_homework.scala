package module2.homework


import cats.Functor.ops.toAllFunctorOps
import cats.effect.{ExitCode, IO, IOApp}
import module2.homework.catsEffectHomework.guessProgram

import scala.io.StdIn
import scala.language.higherKinds
import scala.util.Try

object catsEffectHomework {

  /**
   * Тайп класс для генерации псевдо случайных чисел
   *
   * @tparam F
   */
  trait Random[F[_]] {
    /** *
     *
     * @param min значение от (включительно)
     * @param max значение до (исключается)
     * @return псевдо случайное число в заданном диапазоне
     */
    def nextIntBetween(min: Int, max: Int): F[Int]
  }


  object Random {
    /**
     * 1. реализовать сумонер метод для класса Random, в последствии он должен позволить
     * использовать Random например вот так для IO:
     * Random[IO].nextIntBetween(1, 10)
     *
     * @return Random[F]
     */
    def apply[F[_]](implicit value: Random[F]): Random[F] = value;


    /**
     * 2. Реализовать инстанс тайп класса для IO
     */
    implicit val ioRandom = new Random[IO] {
      val random = scala.util.Random;

      override def nextIntBetween(min: Int, max: Int): IO[Int] = {
        IO.fromEither(
          if (max <= min) {
            Left(new IllegalArgumentException("Значение \"max\" должно быть больше \"min\"."));
          } else {
            Right(random.nextInt(max - min) + min);
          })
      }
    }
  }

  /**
   * Тайп класс для совершения операций с консолью
   *
   * @tparam F
   */
  trait Console[F[_]] {
    def printLine(str: String): F[Unit]

    def readLine(): F[String]
  }

  object Console {
    /**
     * 3. реализовать сумонер метод для класса Console, в последствии он должен позволить
     * использовать Console например вот так для IO:
     * Console[IO].printLine("Hello")
     *
     * @return Console[F]
     */
    def apply[F[_]](implicit value: Console[F]): Console[F] = value;

    /**
     * 4. Реализовать инстанс тайп класса для IO
     */
    implicit val ioConsole = new Console[IO] {
      override def printLine(str: String): IO[Unit] = {
        IO(println(str));
      }

      override def readLine(): IO[String] = {
        IO(StdIn.readLine());
      }
    }
  }

  val leftInclusiveRandomBorder = 1
  val rightInclusiveRandomBorder = 3

  /**
   * 5.
   * Используя Random и Console для IO, напишите консольную программу которая будет предлагать пользователю угадать число от 1 до 3
   * и печатать в консоль угадал или нет. Программа должна выполняться до тех пор, пока пользователь не угадает.
   * Подумайте, на какие наиболее простые эффекты ее можно декомпозировать.
   */

  val guessProgram: IO[Boolean] = {
    doWhile(
      for {
        hiddenNumber <- Random[IO].nextIntBetween(leftInclusiveRandomBorder, rightInclusiveRandomBorder)
        _ <- Console[IO].printLine(s"Введите число от ${leftInclusiveRandomBorder} до ${rightInclusiveRandomBorder}")
        rawInputValue <- Console[IO].readLine()
        intValue <- IO.delay(Try(rawInputValue.toInt).getOrElse(0))
        equals <- IO.delay(hiddenNumber == intValue)
        _ <- if (equals){
          Console[IO].printLine("Поздравляем Вас! Загаданное число и число, введенное Вами, равны")
        }
        else{
          Console[IO].printLine("Сожалеем, но Вы не угадали число")
        }
      } yield equals)((isSuccessful: Boolean) => isSuccessful)
  }



  /**
   * 6. реализовать функцию doWhile (общего назначения) для IO, которая будет выполнять эффект до тех пор, пока его значение в условии не даст true
   * Подумайте над сигнатурой, вам нужно принимать эффект и условие относительно его значения, для того чтобы повторять либо заканчивать выполнение.
   */

  def doWhile[A](repeatableFunc: IO[A])(check: A => Boolean): IO[A] =
    for {
      functionResult <- repeatableFunc
      _ <- if (check(functionResult)){
        IO.delay(functionResult)
      } else {
        doWhile(repeatableFunc)(check)
      }
    } yield functionResult;

}

/**
 * 7. Превратите данный объект в исполняемую cats effect программу, которая будет запускать
 * guessProgram
 */
object HomeworkApp extends IOApp{

  override def run(args: List[String]): IO[ExitCode] = {
    guessProgram.as(ExitCode.Success)
  }

}