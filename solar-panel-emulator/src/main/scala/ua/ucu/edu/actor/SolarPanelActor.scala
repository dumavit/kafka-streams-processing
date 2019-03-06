package ua.ucu.edu.actor

import akka.actor.{Actor, ActorRef}
import ua.ucu.edu.model.ReadMeasurement

import scala.collection.mutable
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Keeps a list of device sensor actors, schedules sensor reads and pushes updates into sensor data topic
  */
class SolarPanelActor(
  val panelId: String
) extends Actor {

  // todo - initialize device actors
  val deviceToActorRef: mutable.Map[String, ActorRef] = ???

  override def preStart(): Unit = {
    super.preStart()

    // todo - schedule measurement reads
    context.system.scheduler.schedule(5 second, 5 seconds, self, ReadMeasurement)(
      context.dispatcher, self)
  }

  override def receive: Receive = {
    case _ => ???
    // todo handle measurement respond and push it to kafka
  }
}
