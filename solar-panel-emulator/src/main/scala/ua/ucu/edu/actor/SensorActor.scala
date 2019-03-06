package ua.ucu.edu.actor

import akka.actor.Actor
import ua.ucu.edu.device.{SensorApi, SensorGenerator}
import ua.ucu.edu.model.{ReadMeasurement, RespondMeasurement}

import scala.language.postfixOps

class SensorActor(
  val deviceId: String,
  sensorType: String
) extends Actor {

  val api: SensorApi = new SensorGenerator

  override def receive: Receive = {
    case _ => ???
    // todo respond with measurements to parent panel actor
  }
}
