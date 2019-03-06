package ua.ucu.edu.kafka

import java.util.Properties

import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import org.slf4j.{Logger, LoggerFactory}

// delete_me - for testing purposes
object DummyDataProducer {

  val logger: Logger = LoggerFactory.getLogger(getClass)

  def pushTestData(): Unit = {
    val BrokerList: String = System.getenv(Config.KafkaBrokers)
    val Topic = "weather_data"
    val props = new Properties()
    props.put("bootstrap.servers", BrokerList)
    props.put("client.id", "weather-provider")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    logger.info("initializing producer")

    val producer = new KafkaProducer[String, String](props)

    val testMsg = "hot weather"

    while (true) {
      Thread.sleep(1000)
      logger.info(s"[$Topic] $testMsg")
      val data = new ProducerRecord[String, String](Topic, testMsg)
      producer.send(data, (metadata: RecordMetadata, exception: Exception) => {
        logger.info(metadata.toString, exception)
      })
    }

    producer.close()
  }
}

object Config {
  val KafkaBrokers = "KAFKA_BROKERS"
}