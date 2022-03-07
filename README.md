# TourGuide


## Name
TourGuide : TourGuide est une application Spring Boot et une pièce maîtresse du portfolio d'applications TripMaster

## Getting started

Comment exécuter l'application :
###Build les projet
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
###Docker
Docker pour production:
```
docker-compose -f .\docker-compose.yml up -d
```


##les tests de performance
Apres Build les 4 projets 

###lancer le teste du performance
```
cd TourGuide
```
####TestCaculateRewardsPerformance
```
gradle test --tests tourGuide.testPerformance.withOutSleep.TestCaculateRewardsPerformance  -DnumberOfUsers=100000
gradle test --tests tourGuide.testPerformance.withOutSleep.TestTrackUsersPerformance -DnumberOfUsers=100000

```
changer le variable -DnumberOfUsers=[''number of user'']
#####Ou avec une sortie riche 
```
gradle test --tests tourGuide.testPerformance.TestCaculateRewardsPerformance  -DnumberOfUsers=1000
gradle test --tests tourGuide.testPerformance.TestTrackUsersPerformance -DnumberOfUsers=100000

```

####for windows il faut ajouter dans le HOST
127.0.0.1 testusers.localhost
127.0.0.1 user.localhost
127.0.0.1 gps.localhost
127.0.0.1 reward.localhost


# Home Page

[http://tourguide.localhost:81/](http://tourguide.localhost:81) 

#Swagger:
##TourGuide
[TourGuide](http://tourguide.localhost:81/swagger-ui/index.html)
##microservice
[user microservice](http://user.localhost:81/swagger-ui/index.html#/)

[reward microservice](http://reward.localhost:81/swagger-ui/index.html#/)

[gps microservice](http://gps.localhost:81/swagger-ui/index.html#/)


![](C:\Users\Admin\Mon Drive\Scan\emploi\RTE\Soutenance\8eme\TourGuide+Architectural+Overview+without+discription.jpg "TourGuide Architectural Overview without discription")