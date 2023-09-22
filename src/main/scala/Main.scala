import module1.homework.collections.laboratoryWork3.bucket
import module1.threads.{Thread1, ToyFuture, getRatesLocation1, getRatesLocation2, getRatesLocation3, getRatesLocation4, printRunningTime}
import module1.{executor, hof, lazyOps, list, type_system}


object Main {

  def main(args: Array[String]): Unit = {
    println("Hello, World!" +
      s" thread - ${Thread.currentThread().getName}" )

    val bucketsCount = 10000;

    val buckets: IndexedSeq[bucket] = (1 to bucketsCount).map(_ => new bucket());
    val experimentalResult = buckets.map(b => b.isWhite);
    val isWhiteCount = experimentalResult.count(r => r);
    val probabilisticDistributionOfWhiteBall = isWhiteCount / bucketsCount.toFloat;
    println(s"Рассчитанная вероятность получения белых шаров из ${bucketsCount} корзин равна ${probabilisticDistributionOfWhiteBall}");
  }
}