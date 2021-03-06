package ua.ucu.edu.kafka

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import ua.ucu.edu.model.{SensorRecord, SensorRecordSerializer}

object SolarPanelKafkaProducer {
  val brokerList: String = System.getenv(Config.KafkaBrokers)//"localhost:9092"
  val topic = "sensor-data"
  val props = new Properties()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList)
  props.put(ProducerConfig.CLIENT_ID_CONFIG, "solar-panel-1")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getCanonicalName)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[SensorRecordSerializer].getCanonicalName)
  props.put(ProducerConfig.RETRIES_CONFIG, "5")
  val producer = new KafkaProducer[String, SensorRecord](props)

  def pushData(record: SensorRecord): Unit = {
    val data = new ProducerRecord[String, SensorRecord](topic, record)
    producer.send(data)
  }
}

object Config {
  val KafkaBrokers = "KAFKA_BROKERS"
}