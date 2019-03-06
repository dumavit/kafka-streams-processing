package ua.ucu.edu.model

case object ReadMeasurement

case class RespondMeasurement(deviceId: String, sensorType: String, value: String)