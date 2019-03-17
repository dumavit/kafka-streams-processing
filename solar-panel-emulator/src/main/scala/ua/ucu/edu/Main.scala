package ua.ucu.edu

import akka.actor._
import ua.ucu.edu.actor.{ActorConfig, PlantManagerActor}

object Main extends App {
  implicit val system: ActorSystem = ActorSystem()
  for (i <- 1 to ActorConfig.PlantCount)
    system.actorOf(Props(classOf[PlantManagerActor], "Plant" + i), "Plant" + i)
}