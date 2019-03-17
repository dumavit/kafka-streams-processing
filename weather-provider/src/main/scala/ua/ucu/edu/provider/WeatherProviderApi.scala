package ua.ucu.edu.provider

import ua.ucu.edu.model._
import play.api.libs.json._
import ua.ucu.edu._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer

import scala.concurrent.Future


trait WeatherProviderApi {
  def weatherAtLocation(location: Location): Future[WeatherRecord]
}


object WeatherProvider extends WeatherProviderApi {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher


  def weatherAtLocation(loc: Location): Future[WeatherRecord] = {
    val query = s"lat=${loc.latitude}&lon=${loc.longitude}&appId=${WeatherConfig.WEATHER_PROVIDER_APP_ID}"


    val responseString = Http()
      .singleRequest(HttpRequest(uri = s"${WeatherConfig.WEATHER_PROVIDER_URL}?$query"))
      .flatMap(Unmarshal(_).to[String])

    responseString map {
      resp => {
        val jsvalue: JsValue = Json.parse(resp)
        val temperature: Double = (jsvalue \ "main" \ "temp").get.as[Double]
        val humidity: Double = (jsvalue \ "main" \ "humidity").get.as[Double]
        WeatherRecord(loc, temperature, humidity)
      }

    }
  }
}