package ua.ucu.edu.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.{AskTimeoutException, ask}
import akka.util.Timeout
import ua.ucu.edu.kafka.KafkaProducerActor
import ua.ucu.edu.model.{Location, ReadMeasurement, RespondMeasurement, SensorTypes}

import scala.concurrent.ExecutionContext.Implicits._
import scala.collection.mutable
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.{Await, Future}
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
    (for (i <- 1 to Config.SensorsCount)
      yield "Sensor" + i -> context.actorOf(Props(classOf[SensorActor], "sensor" + i, sensorTypes(i-1)))).toMap

  private val kafkaProducerActor = "producer1" -> context.actorOf(Props(classOf[KafkaProducerActor], "producer1"))

  override def preStart(): Unit = {
    log.info(s"========== $panelId starting ===========")
    super.preStart()

    // todo - schedule measurement reads
    context.system.scheduler.schedule(5 second, 5 seconds, self, ReadMeasurement)(
      context.dispatcher, self)
  }

//  import context.dispatcher

  override def receive: Receive = {
    case ReadMeasurement =>
      implicit val timeout: Timeout = 5.seconds
      log.info("Received schedule trigger")
      Future.sequence(deviceToActorRef.values.map(_ ? ReadMeasurement))
        .mapTo[List[RespondMeasurement]]
        .onComplete {
          case Success(results) => log.info(s"$results")
          case Failure(exception) => log.info(s"Received exception, $exception")
        }
//      for (i <- 1 to Config.SensorsCount) {
//        ask(deviceToActorRef(s"Sensor$i"), ReadMeasurement).mapTo[RespondMeasurement].onComplete{
//          case Success(value: RespondMeasurement) =>
//            log.info(s"Received respond, ${value.deviceId}, ${value.value}, ${value.sensorType}")
//            kafkaProducerActor._2 ! value
//          case Failure(exception) =>
//            log.info(s"Received exception, $exception")
//        }
//      }
    // todo handle measurement respond and push it to kafka
  }
}