package ua.ucu.edu.model

import org.joda.time.DateTime
import play.api.libs.json._
import ua.ucu.edu.Config
import ua.ucu.edu.serialization.{BaseJsonDeserializer, BaseJsonSerializer}

/**
  * To be used as a message in device topic
  */

case class MergedRecord(panelId: String, location: Location, measurements: Map[String, Double],
                        timestamp: DateTime, temperature: Double, humidity: Double)

class MergedRecordSerializer extends BaseJsonSerializer[MergedRecord] {
  implicit val TimestampWrites: Writes[DateTime] = JodaWrites.jodaDateWrites(Config.DateFormat)
  implicit val LocationWrites: OWrites[Location] = Json.writes[Location]
  override implicit val TWrites: OWrites[MergedRecord] = Json.writes[MergedRecord]
}

class MergedRecordDeserializer extends BaseJsonDeserializer[MergedRecord] {
  implicit val TimestampReads: Reads[DateTime] = JodaReads.jodaDateReads(Config.DateFormat)
  implicit val LocationReads: Reads[Location] = Json.reads[Location]
  override implicit val TReads: Reads[MergedRecord] = Json.reads[MergedRecord]
}