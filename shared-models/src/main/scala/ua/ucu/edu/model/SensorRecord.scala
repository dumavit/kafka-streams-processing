package ua.ucu.edu.model

import org.joda.time.DateTime
import play.api.libs.json._
import ua.ucu.edu.Config
import ua.ucu.edu.serialization.{BaseJsonDeserializer, BaseJsonSerializer}

/**
  * To be used as a message in device topic
  */

case class SensorRecord(panelId: String, location: Location, measurements: Map[String, Double], timestamp: DateTime)

class SensorRecordSerializer extends BaseJsonSerializer[SensorRecord] {
  implicit val TimestampWrites: Writes[DateTime] = JodaWrites.jodaDateWrites(Config.DateFormat)
  implicit val LocationWrites: OWrites[Location] = Json.writes[Location]
  override implicit val TWrites: OWrites[SensorRecord] = Json.writes[SensorRecord]
}

class SensorRecordDeserializer extends BaseJsonDeserializer[SensorRecord] {
  implicit val TimestampReads: Reads[DateTime] = JodaReads.jodaDateReads(Config.DateFormat)
  implicit val LocationReads: Reads[Location] = Json.reads[Location]
  override implicit val TReads: Reads[SensorRecord] = Json.reads[SensorRecord]
}