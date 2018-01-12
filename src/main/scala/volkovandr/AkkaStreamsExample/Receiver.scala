package volkovandr.AkkaStreamsExample

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import io.prometheus.client.Counter

object Receiver extends LazyLogging {

  private val conf = ConfigFactory.load()
  private val logEvery = conf.getInt("logging.receiver-log-every")
  private var count: Int = 0
  private var totalCount: Long = 0

  val ReceiverCounter: Counter = Counter.build()
    .name("received_messages_total")
    .help("The total number of messages received by Receiver")
    .register()

  def receive(numberOfLetters: Int): Unit = {
    totalCount += numberOfLetters
    count += 1
    if(count % logEvery == 0) logger.info(s"Received $count messages")
    ReceiverCounter.inc()
  }

}
