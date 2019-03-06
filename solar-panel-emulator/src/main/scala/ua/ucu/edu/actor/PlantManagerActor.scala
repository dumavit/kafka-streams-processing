package ua.ucu.edu.actor

import akka.actor.{Actor, ActorLogging, ActorRef}
import ua.ucu.edu.model.Location

import scala.collection.mutable

/**
  * This actor manages solar plant, holds a list of panels and knows about its location
  * todo - the main purpose right now to initialize panel actors
  */
class PlantManagerActor(
  plantName: String,
  location: Location
) extends Actor with ActorLogging {

  // todo - populate a list of panels on this plant
  lazy val panelToActorRef: mutable.Map[String, ActorRef] = ???

  override def preStart(): Unit = {
    log.info(s"========== Solar Plant Manager starting ===========")
    super.preStart()
  }

  override def receive: Receive = {
    case _ => ???
  }
}
