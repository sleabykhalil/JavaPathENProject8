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
###Docker
```
docker-compose -f .\docker-compose.yml down
docker-compose -f .\docker-compose-test-perf.yml up -d
```
###lancer le teste du performance
```
cd TourGuide
```
####TestCaculateRewardsPerformance
```
gradle test --tests tourGuide.testPerformance.withOutSleep.TestCaculateRewardsPerformance  -DnumberOfUsers=1000
```
changer le variable -DnumberOfUsers=[''number of user'']
#####Ou avec une sortie riche 
```
gradle test --tests tourGuide.testPerformance.TestCaculateRewardsPerformance  -DnumberOfUsers=1000
```

####for windows il faut ajouter dans le HOST
127.0.0.1 testusers.localhost
127.0.0.1 user.localhost
127.0.0.1 gps.localhost
127.0.0.1 reward.localhost

### To run the BACK server :
In the folder of back we will start with: 
Create your the database with Mysql which is named `gotogether_db`
Create the .env file that content the follwoing varaiable:
PORT
BASE_API_URL
DATABASE_URL
SALT_ROUND
JWT_PASSPHRASE
DURATION_EXPIRE_TOKEN
CORS_ORIGIN

After that we will run the commands:
npm install --> to install the packages mentioned in the package.json
npm run watch --> for typescript to update in real time the dist folder
npm run dev --> to start nodemon (a tool that helps develop node.js based applications by automatically restarting the node application when file changes in the directory are detected)
npm run build --> to build the dist folder

### To run the FRONT server :
In the project directory, you can run:

### `yarn start`
Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in your browser.

### `yarn test`

Launches the test runner in the interactive watch mode.\

### `yarn build`

Builds the app for production to the `build` folder.\
It correctly bundles React in production mode and optimizes the build for the best performance.

Your app is ready to be deployed!


## Tenchnologies
# BACK

**NodeJs** : an open-source, cross-platform, back-end JavaScript runtime environment
**Express** : a minimal and flexible Node.js web application framework that provides a robust set of features for web and mobile applications
**Mysql** : an open-source relational database management system (RDBMS)
**TypeScript** : an open-source language which builds on JavaScript, one of the world’s most used tools, by adding static type definitions
**Prisma Client**: Auto-generated and type-safe query builder for Node.js & TypeScript and it is one of part of Prisma which is an open source next-generation ORM,
**Nodemon** : a tool that helps develop node.js based applications by automatically restarting the node application when file changes in the directory are detected
**bycrypt** : a function to hash password
**Jsonwebtoken** : is a proposed Internet standard. I used a jwt to authenticate the user.
**swagger-ui-express** : HTTP request logger middleware for node.js
**Dotenv** : a module that loads environment variables from a .env file into process.env, we used it to store confidential variables
**Winston & Morgan**:  log system to check  the application behavior and  help to look into bugs
**Jest**: JavaScript testing framework
**supertest**: A library for testing Node.js HTTP servers and the endpount of our API
# FRONT
**React**:
```
cd existing_repo
git remote add origin https://gitlab.com/YomnaBATCH/go_together.git
git branch -M main
git push -uf origin main
```


## Badges
On some READMEs, you may see small images that convey metadata, such as whether or not all the tests are passing for the project. You can use Shields to add some to your README. Many services also have instructions for adding a badge.

## Visuals
Depending on what you are making, it can be a good idea to include screenshots or even a video (you'll frequently see GIFs rather than actual videos). Tools like ttygif can help, but check out Asciinema for a more sophisticated method.

## Installation
Within a particular ecosystem, there may be a common way of installing things, such as using Yarn, NuGet, or Homebrew. However, consider the possibility that whoever is reading your README is a novice and would like more guidance. Listing specific steps helps remove ambiguity and gets people to using your project as quickly as possible. If it only runs in a specific context like a particular programming language version or operating system or has dependencies that have to be installed manually, also add a Requirements subsection.

## Usage
Use examples liberally, and show the expected output if you can. It's helpful to have inline the smallest example of usage that you can demonstrate, while providing links to more sophisticated examples if they are too long to reasonably include in the README.

## Support
Tell people where they can go to for help. It can be any combination of an issue tracker, a chat room, an email address, etc.

## Roadmap
If you have ideas for releases in the future, it is a good idea to list them in the README.

## Contributing
State if you are open to contributions and what your requirements are for accepting them.

For people who want to make changes to your project, it's helpful to have some documentation on how to get started. Perhaps there is a script that they should run or some environment variables that they need to set. Make these steps explicit. These instructions could also be useful to your future self.

You can also document commands to lint the code or run tests. These steps help to ensure high code quality and reduce the likelihood that the changes inadvertently break something. Having instructions for running tests is especially helpful if it requires external setup, such as starting a Selenium server for testing in a browser.

## Authors and acknowledgment
Please don't hestate to contact me for more information or to do pull request

## License
For open source projects, say how it is licensed.

## Project status
If you have run out of energy or time for your project, put a note at the top of the README saying that development has slowed down or stopped completely. Someone may choose to fork your project or volunteer to step in as a maintainer or owner, allowing your project to keep going. You can also make an explicit request for maintainers.

