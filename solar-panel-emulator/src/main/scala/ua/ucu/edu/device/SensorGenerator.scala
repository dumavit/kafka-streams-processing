package ua.ucu.edu.device

class SensorGenerator extends SensorApi {
  override def readCurrentValue: Double = Math.random()
}
