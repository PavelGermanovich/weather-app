# Weather App


This is a weather analysis application developed for Senla.


## Details
The application runs on port 8091 and fetches weather data from an external API every 10000 ms (1 sec) by default. Java 11 used.
The fetch rate can be changed in the application.properties file. 
The city for each fetched data is specified as Minsk by default, but can also be changed in the application.properties file using the app.weather.city property.


The application uses an H2 embedded database to store fetched data. The database is created and saved locally in the target folder. If the database does not exist, the application creates it, otherwise it uses the existing one.

Docker image of the application created and available in docker hub (https://hub.docker.com/repository/docker/german2019docker/weather-app/general).
To run container locally on port 8091 using this image use command: 
#### docker run -p 8091:8091 german2019docker/weather-app

## Usage

The following endpoints are available in the application:
### GET - /weather/current
This endpoint returns the most up-to-date weather data for the specified city from the database.
```
{
    "temperature": 13.0,
    "windSpeedMtrHr": 15100.0,
    "pressureMb": 1004.0,
    "humidity": 67,
    "weatherCondition": "Clear",
    "location": "Minsk"
}
```

### POST - /weather/average

This endpoint calculates and returns the average weather data for the specified date range.
The date range should be provided in the request body as a JSON object with to and from properties in the format "dd-MM-yyyy". 
From and To date are included.


Request Json:
```
{
    "to": "22-11-2023",
    "from": "12-11-2023"
}
```
Example response if data for the specified range exists in the database:
```
{
    "average_temp": 12.962996389891696,
    "average_wind_speed_m_per_hour": 18334.476534296027,
    "average_humidity": 67.09386281588448,
    "average_pressure": 1004.0370036101083
}
```

Example response if no data exists for the specified date range:
```
{
    "message": "No weather data found for specified date range"
}
```
