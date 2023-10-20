package module1.homework.futures

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success}

object task_futures_sequence {

  /**
   * В данном задании Вам предлагается реализовать функцию fullSequence,
   * похожую на Future.sequence, но в отличии от нее,
   * возвращающую все успешные и не успешные результаты.
   * Возвращаемое тип функции - кортеж из двух списков,
   * в левом хранятся результаты успешных выполнений,
   * в правово результаты неуспешных выполнений.
   * Не допускается использование методов объекта Await и мутабельных переменных var
   */
  /**
   * @param futures список асинхронных задач
   * @return асинхронную задачу с кортежом из двух списков
   */
  def fullSequence[A](futures: List[Future[A]])
                     (implicit ex: ExecutionContext): Future[(List[A], List[Throwable])] = {
    val p = Promise[(List[A], List[Throwable])];

    def process(featuresForProcess: List[Future[A]], successful: List[A], unsuccessful: List[Throwable]): Unit = featuresForProcess match {
      case Nil => p.success(successful.reverse, unsuccessful.reverse)
      case ::(head, tail) => head.onComplete({
        case Failure(exception) => process(tail, successful, exception :: unsuccessful)
        case Success(value) => process(tail, value :: successful, unsuccessful)
      })
    }

    process(futures, List.empty[A], List.empty[Throwable]);
    p.future;
  }

}
