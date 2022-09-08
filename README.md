# WebSocket Real-time Tracker

This is a twin project of [SockJS Real-time Tracker](https://github.com/sing-fung/sockjs-real-time-tracker).

It is a websocket-server template that fetches data from MongoDB at regular intervals.

![](images/1.png)

## Prerequisites
* Maven 3.6.0 or higher versions
* Java 11
* MongoDB 4.4.5 or higher versions

## Other frameworks
* WebSocket
* Spring Boot
* Spring
* Sping MVC

## How to run this project
1. Clone this project on IntelliJ IDEA.
2. Run `\src\main\java\com\singfung\tracker\WebSocketRealTimeTrackerApplication.java`.
3. Add some samples to MongoDB by running `\api-samples\saveGPS.http`. It adds the GPS information of two vehicles into MongoDB (`v100` and `v101`).
4. Open three tabs in a browser(`http://localhost:8080/v100.html`, `http://localhost:8080/v101.html` and `http://localhost:8080/v102.html`). We could see that tab `v100` and tab `v101` would receive the GPS information of its own vehicle every 2 seconds, while `v102` could not receive any data since its record is not found in MongoDB.