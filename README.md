# TourGuide


## Name
TourGuide : TourGuide est une application Spring Boot et une pièce maîtresse du portfolio d'applications TripMaster

## Getting started

Comment exécuter l'application :
### 1- Build les projet
Il y a 4 projets 
TourGuid , gps-api , reward-api , user-api
pour chaque projet :
```
cd TourGuide
./gradlew clean build -x test
cd ..
cd gps-api
./gradlew clean build
cd ..
cd reward-api
./gradlew clean build
cd ..
cd user-api
./gradlew clean build
cd ..

```
### 2- Docker
Docker pour production:
```
docker-compose -f .\docker-compose.yml up -d
```
#### Pour windows OS il faut ajouter dans le HOST
127.0.0.1 testusers.localhost
127.0.0.1 user.localhost
127.0.0.1 gps.localhost
127.0.0.1 reward.localhost

## Les tests de performance
Après "Build" les 4 projets 

### Pour lancer le teste du performance
#### 1- Entrez le dosier
```
cd TourGuide
```
#### 2- TestCaculateRewardsPerformance
```
gradle test --tests tourGuide.testPerformance.withOutSleep.TestCaculateRewardsPerformance  -DnumberOfUsers=100000
gradle test --tests tourGuide.testPerformance.withOutSleep.TestTrackUsersPerformance -DnumberOfUsers=100000

```
changer le variable -DnumberOfUsers=[''number of user'']
##### 3- Ou avec une sortie riche 
```
gradle test --tests tourGuide.testPerformance.TestCaculateRewardsPerformance  -DnumberOfUsers=1000
gradle test --tests tourGuide.testPerformance.TestTrackUsersPerformance -DnumberOfUsers=100000

```


# Home Page

[http://tourguide.localhost:81/](http://tourguide.localhost:81) 

# Swagger:
### TourGuide
[TourGuide Swagger ui](http://tourguide.localhost:81/swagger-ui/index.html)
### microservice
[user microservice Swagger ui](http://user.localhost:81/swagger-ui/index.html#/)

[reward microservice Swagger ui](http://reward.localhost:81/swagger-ui/index.html#/)

[gps microservice Swagger ui](http://gps.localhost:81/swagger-ui/index.html#/)

![TourGuide+Architectural+Overview+without+discription](https://user-images.githubusercontent.com/64974948/157071953-237efa6f-6182-427c-8045-ba2fb0094bcf.jpg)

#### Traefik dashboard
[Traefik dashboard](http://localhost:28081/dashboard/#/)
