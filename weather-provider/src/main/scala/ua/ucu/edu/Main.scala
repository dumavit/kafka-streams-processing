package ua.ucu.edu

import akka.actor.ActorSystem
import org.slf4j.LoggerFactory
import ua.ucu.edu.model._
import ua.ucu.edu.provider.WeatherProvider

import scala.concurrent.{Future, duration}
import scala.language.postfixOps
import scala.util.{Failure, Success}
import ua.ucu.edu.kafka.WeatherDataKafkaProducer


object Main extends App {

  val logger = LoggerFactory.getLogger(getClass)

  logger.info("======== Weather Provider App Init ========")

  val system = ActorSystem()

  import system.dispatcher

  import duration._

  system.scheduler.schedule(5 seconds, 30 seconds, new Runnable {
    override def run(): Unit = {
      Config.Locations.foreach(location => {
        val weatherResult: Future[WeatherRecord] = WeatherProvider.weatherAtLocation(location)

        weatherResult onComplete {
          case Success(record: WeatherRecord) =>
            logger.info(s"Schedule triggered, push record $record")
            WeatherDataKafkaProducer.pushData(record)
          case Failure(error) =>
            logger.error("An error has occurred: " + error.getMessage)
        }
      })
    }
  })
}