package ua.ucu.edu

import akka.actor._

import ua.ucu.edu.kafka.DummyDataProducer
import ua.ucu.edu.actor.PlantManagerActor
import ua.ucu.edu.model.Location

object Main extends App {
  implicit val system: ActorSystem = ActorSystem()
  system.actorOf(Props(classOf[PlantManagerActor], "plant1", Location(0, 0)), "plant1-manager")

  DummyDataProducer.pushTestData()
}