package ua.ucu.edu.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import ua.ucu.edu.Config

/**
  * This actor manages solar plant, holds a list of panels and knows about its location
  * todo - the main purpose right now to initialize panel actors
  */
class PlantManagerActor(
  val plantName: String
) extends Actor with ActorLogging {

  // todo - populate a list of panels on this plant
  val panelToActorRef: Map[String, ActorRef] =
    (for (i <- 1 to ActorConfig.PanelCount)
      yield plantName + ":SolarPanel" + i -> context.actorOf(
        Props(
          classOf[SolarPanelActor],
          plantName + ":SolarPanel" + i,
          Config.Locations((i - 1) % Config.Locations.length))
      )).toMap

  override def preStart(): Unit = {
    log.info(s"========== Solar Plant Manager starting ===========")
    super.preStart()
  }

  override def receive: Receive = {
    case _ => log.info(s"$plantName received unexpected message")
  }
}