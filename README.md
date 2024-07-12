# nowplay-copycat

My honest try to remake @nowplaybot with use of Java

## Installation

```bash
git clone https://github.com/r1pth3sl1t/nowplay-copycat.git
```

## Configuration
Make sure you have defined there properties in your application.properties file:
```
telegram-bot.token
telegram-bot.name
telegram.webhook-path
telegram.api-url

spring.datasource.driver-class-name
spring.datasource.url
spring.datasource.username
spring.datasource.password
spotify-api.client-id
spotify-api.client-secret
spotify-api.redirect-uri
spotify-api.auth-url
spotify-api.api-url
spotify-api.scopes-list=user-read-currently-playing user-top-read user-read-recently-played

```

## Usage
Default mode:
- /now - sends audio that is currently playing
- /top - sends 10 songs you listened to the most for  the last 4 weeks
- /recent - sends 5 songs you listened to last
- /logout - logs you out
##
Inline mode:
- empty query - returns 5 songs you listened to last
- track_name - returns a list of tracks that match with track_name
##
