package ua.ucu.edu.actor

import akka.actor.{Actor, ActorLogging}
import ua.ucu.edu.device.{SensorApi, SensorGenerator}
import ua.ucu.edu.model.{ReadMeasurement, RespondMeasurement}

class SensorActor(
  val deviceId: String,
  val sensorType: String
) extends Actor with ActorLogging {

  val api: SensorApi = new SensorGenerator

  override def preStart(): Unit = {
    log.info(s"========== $deviceId with type $sensorType starting ===========")
    super.preStart()
  }

  override def receive: Receive = {
    case ReadMeasurement =>
      log.info("Received Read Measurement message")
      sender() ! RespondMeasurement(deviceId, api.readCurrentValue, sensorType)
    case _ => log.info(s"$deviceId received unexpected message")
  }
}
