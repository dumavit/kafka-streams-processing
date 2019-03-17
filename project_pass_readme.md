# Description

- Main design and implementation decisions that have been made (nevertheless you used recommended approach and evolved it or did it from scratch) and complexities faced
- Scalability of your solution - limitations, performance, how it may scale (and which attributes might be determining in this regard), how much load it can handle (approximation), where it can be improved, any metrics (if were measured)
- How to test your solution - what to build and how to deploy for a simple test, so we will know how to assess and grade

# Main design and implementation

Used Yuri's template. Implemented Kafka's serialization/deserialization using `play-json`. 

`solar-panel-app` sends messages to `sensor-data` topic. 

`weather-provider` sends messages to `weather-data` topic.

`streaming-app` merges data from `sensor-topic` and `weather-data` by `location` property and sends messages to `merged-data` topic.

# Scalability of your solution

`ActorConfig` object in `solar-panel-app` has `SensorsCount = 3`, `PanelCount = 50`, `PlantCount = 20`.

# How to test your solution

Create `sensor-data`, `weather-data`, `merged-topic` topics. Use Yuri's `readme.md` instructions to run application in docker.