[![CircleCI](https://circleci.com/gh/KarolPulawski/sports-event-api.svg?style=svg&circle-token=c8e998d8f2efbb2d812be2a32693eb464ceeb616)](https://circleci.com/gh/KarolPulawski/sports-event-api)

Sports events and restults api generator.

#################################### run SEA
To run locally only that app: run docker-compose file from /src/main/scripts/sea/

Then sports event api app is running on: http://localhost:8080 or http://173.21.0.5:8080

only SEA (new version) available endpoints:

/api/events/games
/api/events/games/competition/{competitionId}
/api/events/games/{uniqueId}
/api/events/results
/api/events/results/competition/{competitionId}
/api/events/results/{uniqueId}

#################################### run SEA & BS

To run locally fully integrated sports event api (SEA) and betting site (BS) apps: run docker-compose file from /src/main/scripts/seabs/

Then sports event api app is running on: http://177.23.0.5:8080

Betting site app is running on: http://177.23.0.3:8085 

SEA available endpoints:
/api/events/{competitionId}}/games
/api/events/{competitionId}}/results
/api/events/results

BS credentials: karol@gmail.com/karol

#################################### overview

To generate more events and results, at first must be inserted new competition with teams. Then new set of methods to populate generating pre match events, odds and results must be provided.
All generating data are scheduled using CRON expressions.
Data can be sent using http post, jms or directly through endpoints, to server details passed as environment variables (BETTING_SERVERIP, BETTING_SERVERPORT).
