package volkovandr.AkkaStreamsExample

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Balance, Flow, GraphDSL, Keep, Merge, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, ClosedShape, KillSwitches, OverflowStrategy}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContextExecutor, Future}

object Main extends App with LazyLogging {
  val conf = ConfigFactory.load()
  val noProcessors = conf.getInt("processing.no-of-processors")
  val totalAmount = conf.getInt("processing.total-amount")
  var bufferSize = conf.getInt("processing.buffer-size")

  implicit val system: ActorSystem = ActorSystem("Main")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  Metrics.initMetrics()

  val source: Source[String, NotUsed] = Source[String](Sender(totalAmount))
    .buffer(bufferSize, OverflowStrategy.backpressure)
  val processors = for(i <- 1 to noProcessors) yield Flow[String].map(Processor.process).async
  val receiver: Sink[Int, Future[Done]] = Sink.foreach(Receiver.receive)

  val runnable = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
    import GraphDSL.Implicits._

    val balance = builder.add(Balance[String](noProcessors))
    val merge = builder.add(Merge[Int](noProcessors))

                                    source ~> balance
    processors.foreach(processor =>           balance ~> processor ~> merge)
                                                                      merge ~> receiver

    ClosedShape
  })
  runnable.run()
  logger.info("Processing started")

  def shutdown(): Unit = {
    system.terminate()
    Metrics.shutdownMetrics()
  }

  val hook = sys.addShutdownHook(shutdown())

}



