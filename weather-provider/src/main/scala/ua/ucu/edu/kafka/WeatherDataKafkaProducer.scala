package ua.ucu.edu.kafka

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.{Logger, LoggerFactory}
import ua.ucu.edu.model.{WeatherRecord, WeatherRecordSerializer}


object WeatherDataKafkaProducer {

  val logger: Logger = LoggerFactory.getLogger(getClass)
  logger.info("Initialize weather producer")

  val brokerList: String = System.getenv(Config.KafkaBrokers)
  val topic = "weather-data"
  val props = new Properties()

  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList)
  props.put(ProducerConfig.CLIENT_ID_CONFIG, "weather-provider")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getCanonicalName)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[WeatherRecordSerializer].getCanonicalName)
  props.put(ProducerConfig.RETRIES_CONFIG, "5")

  val producer = new KafkaProducer[String, WeatherRecord](props)

  def pushData(record: WeatherRecord): Unit = {
    val data = new ProducerRecord[String, WeatherRecord](topic, record)
    producer.send(data)
  }
}

object Config {
  val KafkaBrokers = "KAFKA_BROKERS"
}