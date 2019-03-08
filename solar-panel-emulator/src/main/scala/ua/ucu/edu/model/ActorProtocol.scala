package ua.ucu.edu.model

case object ReadMeasurement

object SensorTypes {
  val WIND_SPEED = "wind speed"
  val SOLAR_FACTOR = "solar factor"
  val EFFICIENCY = "efficiency"
}

case class RespondMeasurement(deviceId: String, value: String, sensorType: String)