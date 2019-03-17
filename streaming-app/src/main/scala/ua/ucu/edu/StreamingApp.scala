package ua.ucu.edu

import java.util.Properties

import org.apache.kafka.streams.kstream._
import ua.ucu.edu.model._
import org.apache.kafka.streams.scala.kstream.KTable
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig, Topology}
import org.apache.kafka.common.serialization.{Serde, Serdes}


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
      .stream[String, SensorRecord]("sensor-data")(Consumed.`with`(Serdes.serdeFrom(classOf[String]), sensorRecordSerde))
      .selectKey((_, sensorRecord) => s"${sensorRecord.location.latitude}:${sensorRecord.location.longitude}")

    val weatherData: KTable[String, WeatherRecord] = builder
      .stream[String, WeatherRecord]("weather-data")(Consumed.`with`(Serdes.serdeFrom(classOf[String]), weatherRecordSerde))
      .groupBy(
        (_, weatherRecord) =>
          s"${weatherRecord.location.latitude}:${weatherRecord.location.longitude}"
      )(Serialized.`with`(Serdes.serdeFrom(classOf[String]), weatherRecordSerde))
      .reduce(
        (prev, next) =>
          next
      )(Materialized.`with`(Serdes.serdeFrom(classOf[String]), weatherRecordSerde))

    sensorData.leftJoin(weatherData)((sensorRecord, weatherRecord) => {
      MergedRecord(sensorRecord.panelId, sensorRecord.location, sensorRecord.measurements,
        sensorRecord.timestamp, weatherRecord.temperature, weatherRecord.humidity)
    })(Joined.`with`(Serdes.serdeFrom(classOf[String]), sensorRecordSerde, weatherRecordSerde))
      .to("merged-data")(Produced.`with`(Serdes.serdeFrom(classOf[String]), mergedRecordSerde))

    val topology = builder.build()
    topology
  }
}

object StreamingApp extends App {
  val brokerList: String = System.getenv(Config.KafkaBrokers)//"localhost:9092"
  val topic = "sensor-data"
  val props = new Properties()
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList)
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streaming-app-1")
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
