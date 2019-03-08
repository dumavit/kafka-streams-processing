package ua.ucu.edu.kafka

import java.util.Properties

import akka.actor.{Actor, ActorLogging}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.slf4j.{Logger, LoggerFactory}
import ua.ucu.edu.model.RespondMeasurement

class KafkaProducerActor(val producerId: String) extends Actor with ActorLogging {
  val brokerList: String = "127.0.0.1:9201"//System.getenv(Config.KafkaBrokers)
  val topic = "sensor-data"
  val props = new Properties()
  props.put("bootstrap.servers", brokerList)
  props.put("client.id", "solar-panel-1")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  val producer = new KafkaProducer[String, String](props)

  override def preStart(): Unit = {
    log.info(s"========== $producerId starting ===========")
    super.preStart()
  }

  override def receive: Receive = {
    case RespondMeasurement(id, value, sensorType) =>
      log.info(s"Received respond for producer, $id, $value, $sensorType")
      producer.send(new ProducerRecord[String, String](topic, id, value))
      log.info(s"Message sent")
  }
}

object Config {
  val KafkaBrokers = "127.0.0.1:9201"//"KAFKA_BROKERS"
}