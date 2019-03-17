package ua.ucu.edu.model

import ua.ucu.edu.serialization.{BaseJsonDeserializer, BaseJsonSerializer}
import play.api.libs.json._

/**
  * To be used as a message in device topic
  */

case class WeatherRecord(location: Location, temperature: Double, humidity: Double)


class WeatherRecordSerializer extends BaseJsonSerializer[WeatherRecord] {
  implicit val LocationWrites: OWrites[Location] = Json.writes[Location]
  override implicit val TWrites: OWrites[WeatherRecord] = Json.writes[WeatherRecord]
}

class WeatherRecordDeserializer extends BaseJsonDeserializer[WeatherRecord] {
  implicit val LocationReads: Reads[Location] = Json.reads[Location]
  override implicit val TReads: Reads[WeatherRecord] = Json.reads[WeatherRecord]
}

