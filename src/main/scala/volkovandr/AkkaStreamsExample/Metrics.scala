package volkovandr.AkkaStreamsExample

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import io.prometheus.client.exporter.HTTPServer

object Metrics extends LazyLogging {

  private var prometheusHttpServer: HTTPServer = _

  def initMetrics(): Unit = {
    val conf = ConfigFactory.load()
    val metricsPort = conf.getInt("metrics.port")
    prometheusHttpServer = new HTTPServer(metricsPort)
    logger.info(s"Metrics initialized and available on port $metricsPort")
  }

  def shutdownMetrics(): Unit = {
    if(prometheusHttpServer != null)
      prometheusHttpServer.stop()
    logger.info("Metrics shut down")
  }

}