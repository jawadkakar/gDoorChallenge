
# Looking Glass code challenge
#Assumption: Java 8 and Maven 3.+ is install. JAVA_HOME and M2_HOME are set.  
#1: Check out the code from github using the following command
git clone https://github.com/jawadkakar/gDoorChallenge.git
# 2: navigate to the folder where code was downloaded
cd gDoorChallenge
# 3: build the code using maven or if you just want to run it go to step 4. 
mvn clean install
# 4: When the project is successfully build 
 cd executabl
# 5: Start Elastic Search
# 6: Run gDoorChallenge by typing the following, make user path for the datafile is correct. 
 java -jar lookingGlass-jar-with-dependencies.jar -f /gDoorChallenge/SearchTask.yaml
 
 
 I have created 2 indices and 2 types
 lookingsearch/searchTask
 lookingresult/searchResult
 


