package ua.ucu.edu.model

import play.api.libs.json.{Json, OWrites, Reads}
import ua.ucu.edu.serialization.{BaseJsonDeserializer, BaseJsonSerializer}

/**
  * To be used as a message in device topic
  */

case class SensorRecord(panelId: String, location: Location, measurements: Map[String, Double])

class SensorRecordSerializer extends BaseJsonSerializer[SensorRecord] {
  implicit val LocationWrites: OWrites[Location] = Json.writes[Location]
  override implicit val TWrites: OWrites[SensorRecord] = Json.writes[SensorRecord]
}

class SensorRecordDeserializer extends BaseJsonDeserializer[SensorRecord] {
  implicit val LocationReads: Reads[Location] = Json.reads[Location]
  override implicit val TReads: Reads[SensorRecord] = Json.reads[SensorRecord]
}