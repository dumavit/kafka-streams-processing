package ua.ucu.edu

import java.util.Properties
import java.util.concurrent.TimeUnit

import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.slf4j.LoggerFactory

// dummy app for testing purposes
object DummyStreamingApp extends App {

  val logger = LoggerFactory.getLogger(getClass)

  val props = new Properties()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streaming_app")
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv(Config.KafkaBrokers))
  props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, Long.box(5 * 1000))
  props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, Long.box(0))

  import Serdes._

  val builder = new StreamsBuilder

  /*val testStream = builder.stream[String, String]("weather_data")

  testStream.foreach { (k, v) =>
    logger.info(s"record processed $k->$v")
  }

  testStream.to("test_topic_out")

  val streams = new KafkaStreams(builder.build(), props)
  streams.cleanUp()
  streams.start()

  sys.addShutdownHook {
    streams.close(10, TimeUnit.SECONDS)
  }*/

  implicit val weatherSerde: Serde[Weather] = fromFn(View.serialize _, Weather.deserialize _)
  implicit val panelSerde: Serde[Panel] = fromFn(Panel.serialize _, Panel.deserialize _)
  implicit val panelResultSerde: Serde[PanelResult] = fromFn(PanelResult.serialize _, PanelResult.deserialize _)

  val builder = new StreamsBuilder
  val panels = builder.stream[String, Panel]("panel-data")
    .selectKey((_, panel) => panel.location)
  val weather: KTable[String, Weather] = builder.stream[String, Weather]("weather_data")
    .groupBy((_, weather) => weather.location)
    .reduce((prev, next) => {if (next.time > prev.time) next else prev})
  val result = panels.leftJoin(weather)((_, weather) => PanelResult(weather.data, weather.location))

  result.to("panel-result")
  val topology = builder.build()
  topology

  object Config {
    val KafkaBrokers = "KAFKA_BROKERS"
  }
}
