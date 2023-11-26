package module2.homework.zio_homework

import zio.clock.Clock
import zio.console.Console
import zio.random.Random
import zio.{ExitCode, URIO, ZIO}

object ZioHomeWorkApp extends zio.App {
  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] = {
    val layer = Random.live ++ Console.live ++ Clock.live ++ Stopwatch.live;
    runApp.provideLayer(layer)
      .catchAll(_ => ZIO.succeed(ExitCode.failure))
      .map(_ => ExitCode.success)
  }
}
