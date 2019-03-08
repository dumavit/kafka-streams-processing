package ua.ucu.edu

import akka.actor._

import ua.ucu.edu.actor.PlantManagerActor

object Main extends App {
  implicit val system: ActorSystem = ActorSystem()
  system.actorOf(Props(classOf[PlantManagerActor], "plant1"), "plant1-manager")
}