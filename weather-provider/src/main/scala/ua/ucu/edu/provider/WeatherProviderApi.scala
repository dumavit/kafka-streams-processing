package ua.ucu.edu.provider

import ua.ucu.edu.model._

trait WeatherProviderApi {

  def weatherAtLocation(location: Location): WeatherData
}
