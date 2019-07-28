# Homework

This homework project attempts to show how a reddit like forum structure can be implemented. Various design patterns, code style and techniques have been applied in this project to demo a simple yet fairly advanced baseline for similar projects.
The technologies/lingo used here assumes basic knowledge of Java, Spring, AngularJS, Docker and some understanding of AWS.

## Getting Started

Below you will find instructions on how to setup, test and deploy the project.


### Prerequisites - AWS Config and Credentials

The project even though can be run locally without any direct connection to AWS. Initial setup of AWS keys for DynamoDB usage is required.
Go to your AWS Console > IAM > Users. Create a new user or choose a user with Admin/appropriate access for DynamoDB.
Go to Security Credentials and create a new Access Key. Create 2 files in ~/.aws folder called credentials and config.
Follow instructions [here](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-files.html).
The project right now is hardcoded to run in US_EAST_1 i.e. North Virginia.

### Installing

Clone the GitHub repository

```
git clone https://github.com/ashishg1/homework.git
```

Since this project is assuming that you have development environment ready with npm and maven installed, run below commands to install and run.

```
mvn clean install
```

Inside the target folder will be homework-1.0-SNAPSHOT.jar which can be run directly and the project will connect to live DynamoDB instance.
If you do no have tables created as would be the case on your first run then the app will create them for you. The app will not delete the tables on next Restart. There is a switch in SpringBootApp.main. Call to createTables can have true as argument which will delete and recreate the tables on restart.
After the application runs you can access it on

```
http://localhost:8080
```

To access the Swagger UI go to

```
http://localhost:8080/swagger-ui.html
```


## Tests

There are quite a few tests that have been added to the project. Unit/Integration tests can simply be run via IDE or Maven.
For Integration tests a test harness for local DynamoDB has been added that runs an in-memory DB. The same can also be setup for development purposes.
For Load/Stress testing a Gatling framework has been added under /src/test/scala/com/loyalty/homework/gatling. See ApiSimulation.scala for example of how load can be generated directy to a locally run server or the by changing the URL load can also be generated to a live server.
A sample Scenario builder has been added however any direct API calls can be made instead.
UI tests have been created by using the Jasmine/Karma framework. All tests are in the .spec.ts files.
To run UI Jasmine tests. Please run below command follow the instructions as provided by your local testbed.

```
ng test
```

## Deployment

The auto deployment provided with the project leverages AWS Code deploy and Beanstalk. All files relating to AWS deployment have been placed under /cloudformation. As such CloudFormation is being used to deploy the entire project automatically.
Please follow below steps to create the env on AWS. Note: Charges may apply. Commands assume aws CLI has been installed and credentials and config files have been configured.
Both Windows and Linux version of files have been provided. If Windows please run .bat files otherwise please use .sh files.

Create an application on AWS Beanstalk. This command will create Homework application on the server. Name ca be changed however it must match CloudFormation templates.

```
./createApplication.sh
```

Then run deploy script to deploy your version of environment. The Name provided is mandatory and will be used as prefix for stacks and buckets.

```
./deployEnv.sh <projectName>
```

Once executed the scripts will create stack for CodeBuild. Pull the branch from GitHub and then compile/build and produce artifacts that will then be placed under an s3 bucket.
The script will then move on to create an Elastic Beanstalk environment under the application you created earlier. All settings are configurable in the ebs cloudformation template.
The deployment takes place as load balanced Docker containers in EC2 using t1.micro instances that are not in any specific VPC.
Once deployed the script will print out the url for the application that can be used to directly access the application.
To delete the stacks please run similar deleteEnv and deleteApplication scripts with same projectName. And the stacks will be fully and automatically removed.

## HTTPS

The code on master branch runs on HTTP however see Secure branch for an example of how a local HTTPS version can be setup. Note: this version should not be deployed on AWS as that configuration does not exist yet. There is further setup required to get HTTPS working on AWS.

## UI Usage

The following behavior is expected from UI
* Anyone can post under any username and there is no restriction or authentication
* If a City is provided then it must be in the format of Toronto,CA or Montreal,CA if you wish to see longitude, latitude and current temperature otherwise on city name will appear as provided.
* If a replier's username is not provided the reply will be posted as anonymous
* All Posts and Replies use the same text box for simplicity purposes
* All Original Posts are latest first. All replies within a post are latest last.
* There is no auto load of posts and a local cache is maintained. Hit the reload button and the posts will reload for provided username
* The current behaviour loads all posts at once however it is not recommended for a Reddit forum type structure. Load them one by one if needed. The application will allow for that however currently unsupported.

Happy Replying!

## Authors

* **Ashish Gupta** - From start to end!


## License

This project is licensed under the Apache License.

