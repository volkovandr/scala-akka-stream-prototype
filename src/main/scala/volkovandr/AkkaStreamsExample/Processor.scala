package volkovandr.AkkaStreamsExample

import com.typesafe.scalalogging.LazyLogging
import io.prometheus.client.Counter

object Processor extends LazyLogging {

  val ProcessorCounter: Counter = Counter.build()
    .name("processed_messages_total")
    .help("The total number of messages processed by Processor")
    .register()

  def process(str: String): Int = {
    val numberOfLetters = str.length
    Thread.sleep(20)
    ProcessorCounter.inc()
    numberOfLetters
  }
}
