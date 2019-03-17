package ua.ucu.edu.provider

import ua.ucu.edu.model._
import dispatch._
import Defaults._
import play.api.libs.json._
import ua.ucu.edu.WeatherConfig


trait WeatherProviderApi {
  def weatherAtLocation(location: Location): Future[WeatherRecord]
}


object WeatherProvider extends WeatherProviderApi {
  def weatherAtLocation(loc: Location): Future[WeatherRecord] = {
    val query = s"lat=${loc.latitude}&lon=${loc.longitude}&appId=${WeatherConfig.WEATHER_PROVIDER_APP_ID}"
    val svc = url(s"${WeatherConfig.WEATHER_PROVIDER_URL}?$query")
    val response: Future[String] = Http.default(svc OK as.String)

    response map {
      resp => {
        val jsvalue: JsValue = Json.parse(resp)
        val temperature: Double = (jsvalue \ "main" \ "temp").get.as[Double]
        val humidity: Double = (jsvalue \ "main" \ "humidity").get.as[Double]

        WeatherRecord(loc, temperature, humidity)
      }
    }
  }
}