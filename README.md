# PortalUI

## Development


# Building for production

To optimize the PortalUI application for production, run:

    ./mvnw -Pprod clean package

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

    java -jar target/*.war

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.





# FIESTA-IoT Production Platform


## Services installed

1. System requirement
  Java 8, Wildfly 10.0.0 or later
  
1. How to build
   System requirement
   - Java 8, Maven, bower
   Build on local
   - ./mvnw -DskipTests=true -Pdev clean package
   Build for test env
   - ./mvnw -DskipTests=true -Ptest clean package
   Build for prod env
   - ./mvnw -DskipTests=true -Pprod clean package
   Run on local
   - java -jar target/portalui.war
   
2. How to deploy
- System requirement
- Java 8, Widfly 10.0.0 or later
- upload portalui.war via Widfly
- Logs: portalui.log



3. Env

 Test 
./mvnw -DskipTests=true -Ptest clean package


 Prod
 
 ./mvnw -DskipTests=true -Pprod clean package
