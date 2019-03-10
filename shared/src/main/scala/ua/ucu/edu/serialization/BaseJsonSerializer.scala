package ua.ucu.edu.serialization

import java.util

import org.apache.kafka.common.serialization. Serializer
import play.api.libs.json.{Json, OWrites}

//trait BaseJsonSerializer[T] extends Serializer[T] {
//  implicit val TWrites: OWrites[T]
//
//  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}
//
//  override def serialize(topic: String, data: T): Array[Byte] = {
//    Json.toBytes(Json.toJson[T](data))
//  }
//
//  override def close(): Unit = {}
//}