package module2.homework.zio_homework

import zio.{Has, Task, ZIO, ZLayer}
import zio.clock.{Clock, currentTime}
import zio.console.{Console, putStrLn}

import java.io.IOException
import java.util.concurrent.TimeUnit

package object utils {
  def printEffectRunningTime[R, E, A](effect: ZIO[R, E, A]) = for {
    startTime <- currentTime(TimeUnit.MILLISECONDS)
    _ <- effect
    endTime <- currentTime(TimeUnit.MILLISECONDS)
    _ <- putStrLn(s"Эффект исполнялся ${endTime - startTime} миллисекунд")
  } yield ()
}
