package ua.ucu.edu.model

import java.util

import org.apache.kafka.common.serialization.{Deserializer, Serializer}
import play.api.libs.json.{Json, OWrites, Reads}

/**
  * To be used as a message in device topic
  */

case class SensorRecord(panelId: String, location: Location, measurements: Map[String, Double])

class SensorRecordSerializer extends BaseJsonSerializer[SensorRecord] {
  override implicit val TWrites: OWrites[SensorRecord] = Json.writes[SensorRecord]
}

class SensorRecordDeserializer extends BaseJsonDeserializer[SensorRecord] {
  override implicit val TReads: Reads[SensorRecord] = Json.reads[SensorRecord]
}

trait BaseJsonSerializer[T] extends Serializer[T] {
  implicit val TWrites: OWrites[T]

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def serialize(topic: String, data: T): Array[Byte] = {
    Json.toBytes(Json.toJson[T](data))
  }

  override def close(): Unit = {}
}

trait BaseJsonDeserializer[T] extends Deserializer[T] {
  implicit val TReads: Reads[T]

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def deserialize(topic: String, data: Array[Byte]): T = {
    Json.fromJson[T](Json.parse(data)).get
  }

  override def close(): Unit = {}
}