package ua.ucu.edu.kafka

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.slf4j.{Logger, LoggerFactory}

// delete_me - for testing purposes
object DummyDataProducer {

  val logger: Logger = LoggerFactory.getLogger(getClass)

  // This is just for testing purposes
  def pushTestData(): Unit = {
    val BrokerList: String = System.getenv(Config.KafkaBrokers)
    val Topic = "sensor-data"

    val props = new Properties()
    props.put("bootstrap.servers", BrokerList)
    props.put("client.id", "solar-panel-1")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    logger.info("initializing producer")

    val producer = new KafkaProducer[String, String](props)

    val testMsg = "12.3702"

    while (true) {
      Thread.sleep(10000)
      logger.info(s"[$Topic] $testMsg")
      val data = new ProducerRecord[String, String](Topic, testMsg)
      producer.send(data)
    }

    producer.close()
  }
}

object Config {
  val KafkaBrokers = "KAFKA_BROKERS"
}