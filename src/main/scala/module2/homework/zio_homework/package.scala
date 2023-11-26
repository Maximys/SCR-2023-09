package module2.homework

import module2.homework.zio_homework.utils.printEffectRunningTime
import zio.{Has, Schedule, Task, ULayer, ZIO, ZLayer}
import zio.clock.{Clock, currentTime, sleep}
import zio.console._
import zio.duration.durationInt
import zio.macros.accessible
import zio.random._

import java.io.IOException
import java.util.concurrent.TimeUnit
import scala.io.StdIn
import scala.language.postfixOps
import scala.util.Try

package object zio_homework {
  /**
   * 1.
   * Используя сервисы Random и Console, напишите консольную ZIO программу которая будет предлагать пользователю угадать число от 1 до 3
   * и печатать в консоль угадал или нет. Подумайте, на какие наиболее простые эффекты ее можно декомпозировать.
   */

  //сгенерировать случайное число
  //позволить пользователю ввести число
  //сравнить сгенерированное число и ввод пользователя
  //вывести результат сравнения

  val leftInclusiveRandomBorder = 1;
  val rightExclusiveRandomBorder = 4;

  def requestNumberInTheRange(leftBorder: Int, rightBorder: Int) =
    for {
      _ <- putStrLn(s"Введите число от ${leftBorder} до ${rightBorder}")
      rawInputValue <- getStrLn
      intValue <- ZIO.effect(Try(rawInputValue.toInt).getOrElse(0))
    } yield intValue

  lazy val guessProgram = for {
    hiddenNumber: Int <- nextIntBetween(leftInclusiveRandomBorder, rightExclusiveRandomBorder)
    inputValue <- requestNumberInTheRange(leftInclusiveRandomBorder, rightExclusiveRandomBorder - 1)
    equals <- ZIO.succeed(hiddenNumber == inputValue)
    _ <- if (equals) {
      putStrLn("Поздравляем Вас! Загаданное число и число, введенное Вами, равны");
    } else {
      putStrLn("Сожалеем, но Вы не угадали число");
    }
  } yield equals

  /**
   * 2. реализовать функцию doWhile (общего назначения), которая будет выполнять эффект до тех пор, пока его значение в условии не даст true
   * 
   */

  def doWhile[R, E, A](effect: ZIO[R, E, A])(condition: A => Boolean): ZIO[R, E, A] =
    for {
      intermediateResult <- effect
      returnValue <- if (condition(intermediateResult)) {
        ZIO.succeed(intermediateResult)
      } else {
        doWhile(effect)(condition)
      }
    } yield returnValue


  /**
   * 3. Следуйте инструкциям ниже для написания 2-х ZIO программ,
   * обратите внимание на сигнатуры эффектов, которые будут у вас получаться,
   * на изменение этих сигнатур
   */


  /**
   * 3.1 Создайте эффект, который будет возвращать случайным образом выбранное число от 0 до 10 спустя 1 секунду
   * Используйте сервис zio Random
   */
  val leftInclusiveRandomBorderForEff = 1;
  val rightExclusiveRandomBorderForEff = 11;

  lazy val eff =
    for {
      _ <- ZIO.sleep(1 second)
      returnValue <- nextIntBetween(leftInclusiveRandomBorderForEff, rightExclusiveRandomBorderForEff)
    } yield returnValue

  /**
   * 3.2 Создайте коллекцию из 10 выше описанных эффектов (eff)
   */
  lazy val effects = List.range(0, 10).map(r => eff)

  
  /**
   * 3.3 Напишите программу которая вычислит сумму элементов коллекци "effects",
   * напечатает ее в консоль и вернет результат, а также залогирует затраченное время на выполнение,
   * можно использовать ф-цию printEffectRunningTime, которую мы разработали на занятиях
   */

  lazy val app = printEffectRunningTime(
    for {
      _ <- putStrLn("Начинаем расчет")
      sum <- ZIO.reduceAll(ZIO.succeed(0), effects)((accum, currentValue) => accum + currentValue)
      _ <- putStrLn(s"Вычисленная сумма равна ${sum}")
    } yield ()
  )


  /**
   * 3.4 Усовершенствуйте программу 3.3 так, чтобы минимизировать время ее выполнения
   */

  lazy val appSpeedUp = ZIO.reduceAllPar(ZIO.succeed(0), effects)((accum, currentValue) => accum + currentValue);


  /**
   * 4. Оформите ф-цию printEffectRunningTime разработанную на занятиях в отдельный сервис, так чтобы ее
   * молжно было использовать аналогично zio.console.putStrLn например
   */
  object Stopwatch {
    type StopwatchEnvironment = Has[Stopwatch.StopwatchService]

    trait StopwatchService {
      def start(): ZIO[Any, Throwable, Unit]

      def stop(): ZIO[Any, IOException, Unit]
    }

    val live: ZLayer[Console with Clock, Nothing, StopwatchEnvironment] =
      ZLayer.fromServices[Console.Service, Clock.Service, Stopwatch.StopwatchService] {
        (console, clock) =>
          new StopwatchService {
            var startTime: Long = 0

            override def start(): ZIO[Any, Throwable, Unit] = for {
              currentTime <- clock.currentTime(TimeUnit.MILLISECONDS)
              _ <- Task {
                startTime = currentTime
              }
            } yield ()


            override def stop(): ZIO[Any, IOException, Unit] = {
              for {
                currentTime <- clock.currentTime(TimeUnit.MILLISECONDS)
                _ <- console.putStrLn(s"Эффект исполнялся ${currentTime - startTime} миллисекунд")
              } yield ()

            }
          }
      }

    def start(): ZIO[StopwatchEnvironment, Throwable, Unit] = ZIO.accessM(_.get.start())

    def stop(): ZIO[StopwatchEnvironment, Throwable, Unit] = ZIO.accessM(_.get.stop())
  }

   /**
     * 5.
     * Воспользуйтесь написанным сервисом, чтобы созадть эффект, который будет логировать время выполнения прогаммы из пункта 3.4
     *
     * 
     */

  lazy val appWithTimeLogg = for {
    _ <- Stopwatch.start()
    v <- appSpeedUp
    _ <- Stopwatch.stop()
  } yield v

  /**
    * 
    * Подготовьте его к запуску и затем запустите воспользовавшись ZioHomeWorkApp
    */

  lazy val runApp = for {
    l <- appWithTimeLogg
    _ <- putStrLn(l.toString)
  } yield l

}
