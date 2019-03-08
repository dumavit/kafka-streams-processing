package ua.ucu.edu.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import ua.ucu.edu.model.Location

import scala.collection.mutable

/**
  * This actor manages solar plant, holds a list of panels and knows about its location
  * todo - the main purpose right now to initialize panel actors
  */
class PlantManagerActor(
  plantName: String
) extends Actor with ActorLogging {

  val locations: List[Location] =
    (for (_ <- 1 to Config.PanelCount) yield Location(1,1)).toList

  // todo - populate a list of panels on this plant
  val panelToActorRef: Map[String, ActorRef] =
    (for (i <- 1 to Config.PanelCount)
      yield "SolarPanel" + i -> context.actorOf(
        Props(classOf[SolarPanelActor], "panel" + i, locations(i - 1))
      )).toMap

  override def preStart(): Unit = {
    log.info(s"========== Solar Plant Manager starting ===========")
    super.preStart()
  }

  override def receive: Receive = {
    case _ => ???
  }
}

object Config {
  val SensorsCount = 3
  val PanelCount = 50
  val Locations = List(
    Location(20, 20), Location(30, 30), Location(40, 40), Location(50, 50), Location(60, 60))
}