package ua.ucu.edu.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import ua.ucu.edu.kafka.SolarPanelKafkaProducer
import ua.ucu.edu.model._

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Keeps a list of device sensor actors, schedules sensor reads and pushes updates into sensor data topic
  */
class SolarPanelActor(
  val panelId: String,
  val location: Location
) extends Actor with ActorLogging {

  private val sensorTypes: List[String] = List(SensorTypes.WIND_SPEED, SensorTypes.EFFICIENCY, SensorTypes.SOLAR_FACTOR)
  // todo - initialize device actors
  private val deviceToActorRef: Map[String, ActorRef] =
    (for (i <- 1 to ActorConfig.SensorsCount)
      yield "Sensor" + i -> context.actorOf(Props(classOf[SensorActor], "Sensor" + i, sensorTypes(i-1)))).toMap

  override def preStart(): Unit = {
    log.info(s"========== $panelId with location $location starting ===========")
    super.preStart()

    // todo - schedule measurement reads
    context.system.scheduler.schedule(5 second, 5 seconds, self, ReadMeasurement)(
      context.dispatcher, self)
  }

  override def receive: Receive = {
    case ReadMeasurement =>
      implicit val timeout: Timeout = 5.seconds
      log.info(s"$panelId received schedule trigger")
      Future.sequence(deviceToActorRef.values.map(_ ? ReadMeasurement))
        .mapTo[List[RespondMeasurement]]
        .onComplete {
          case Success(results: List[RespondMeasurement]) =>
            log.info(s"$panelId received success response from all sensors: $results")
            SolarPanelKafkaProducer.pushData(
              SensorRecord(panelId, location, results.map(v => v.sensorType -> v.value).toMap)
            )
          case Failure(exception) => log.info(s"$panelId received exception, $exception")
        }
    case _ => log.info(s"$panelId received unexpected message")
  }
}