
# Looking Glass code challenge
#Assumption: Java 8 and Maven 3.+ is install. JAVA_HOME and M2_HOME are set.  
#1: Check out the code from github using the following command
git clone https://github.com/jawadkakar/gDoorChallenge.git
# 2: navigate to the folder where code was downloaded
cd gDoorChallenge
# 3: build the code using maven
mvn clean install
# 4: When the project is successfully build 
 cd executabl
# 5: Start Elastic Search
# 6: Run gDoorChallenge by typing the following
 java -jar gdoor.code.challenge.cli-1.0-SNAPSHOT-jar-with-dependencies.jar -f C:/gDoorChallenge/SearchTask.yaml
 



