package volkovandr.AkkaStreamsExample

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import io.prometheus.client.Counter

import scala.util.Random

class Sender(val totalAmount: Int = 100) extends scala.collection.immutable.Iterable[String] {
  import Sender._
  override def iterator: Iterator[String] = new SenderIterator(totalAmount)
}

object Sender extends LazyLogging {

  private def conf = ConfigFactory.load()
  private val logEvery = conf.getInt("logging.sender-log-every")

  def apply(totalAmount: Int): Sender = {
    logger.info(s"Initializing a SampleSource with totalAmount = $totalAmount")
    new Sender(totalAmount)
  }

  private val subjects = Vector("human", "robot", "dog", "house", "pensil", "table", "whale", "hero", "jedi", "ninja")
  private val actions = Vector("eats", "catches", "sees", "likes", "hates", "drinks", "smells", "copies", "loses", "admires")
  private val objects = Vector("banana", "coconut", "brick", "wall", "computer", "cup", "car", "panda", "pony", "stone", "cow")

  class SenderIterator(val totalAmount: Int) extends Iterator[String] with LazyLogging {
    private var count: Int = 0
    override def hasNext: Boolean = count < totalAmount
    private val rnd = new Random()
    override def next(): String = {
      count += 1
      val s = s"$count: A ${subjects(rnd.nextInt(subjects.size))} ${actions(rnd.nextInt(actions.size))} a ${objects(rnd.nextInt(objects.size))}"
      SourceCounter.inc()
      if(count % logEvery == 0) logger.info(s"Generated $count messages")
      s
    }
  }

  val SourceCounter: Counter = Counter.build()
    .name("source_messages_total")
    .help("The total number of messages created by SampleSource")
    .register()
}
