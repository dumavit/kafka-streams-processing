package ua.ucu.edu

import java.util.Properties

import ua.ucu.edu.model._
import org.apache.kafka.streams.scala.kstream.KTable
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.{Serdes, StreamsBuilder}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig, Topology}
import Serdes._
import org.apache.kafka.common.serialization.{Serdes, Serde}


class StreamingApp {
  def joinWeatherAndPanelData(): Topology = {
    implicit val sensorRecordSerde: Serde[SensorRecord] =
      Serdes.serdeFrom(new SensorRecordSerializer, new SensorRecordDeserializer)
    implicit val weatherRecordSerde: Serde[WeatherRecord] =
      Serdes.serdeFrom(new WeatherRecordSerializer, new WeatherRecordDeserializer)
    implicit val mergedRecordSerde: Serde[MergedRecord] =
      Serdes.serdeFrom(new MergedRecordSerializer, new MergedRecordDeserializer)

    val builder = new StreamsBuilder
    val sensorData = builder
      .stream[String, SensorRecord]("sensor-data")

    val weatherData: KTable[String, WeatherRecord] = builder
      .table[String, WeatherRecord]("weather-data")

    sensorData.join(weatherData)((sensorRecord, weatherRecord) => {
      MergedRecord(sensorRecord.panelId, sensorRecord.location, sensorRecord.measurements,
        sensorRecord.timestamp, weatherRecord.temperature, weatherRecord.humidity)
    }).to("merged-data")

    val topology = builder.build()
    topology
  }
}

object StreamingApp extends App {
  val brokerList: String = System.getenv(Config.KafkaBrokers)//"localhost:9092"
  val topic = "sensor-data"
  val props = new Properties()
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList)
  props.put(StreamsConfig.CLIENT_ID_CONFIG, "streaming-app-1")
  props.put(StreamsConfig.RETRIES_CONFIG, "5")

  val app = new StreamingApp
  val topology = app.joinWeatherAndPanelData()

  val streams: KafkaStreams = new KafkaStreams(topology, props)
  streams.cleanUp()
  streams.start()

  sys.ShutdownHookThread {
    streams.close()
  }

  object Config {
    val KafkaBrokers = "KAFKA_BROKERS"
  }
}
