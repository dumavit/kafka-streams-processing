package ua.ucu.edu.device

class SensorGenerator extends SensorApi {
  override def readCurrentValue: String = Math.random().toString
}
