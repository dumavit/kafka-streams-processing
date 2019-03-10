package ua.ucu.edu.serialization

import java.util

import org.apache.kafka.common.serialization.Deserializer
import play.api.libs.json.{Json, Reads}

trait BaseJsonDeserializer[T] extends Deserializer[T] {
  implicit val TReads: Reads[T]

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def deserialize(topic: String, data: Array[Byte]): T = {
    Json.fromJson[T](Json.parse(data)).get
  }

  override def close(): Unit = {}
}