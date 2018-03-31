*UserDataService*
	
	User Data REST API
	Swagger available on - <http://ec2-13-126-92-66.ap-south-1.compute.amazonaws.com:8099/swagger-ui.html>

*Steps to get going*
	
	1) Install Java 8 - <https://java.com/download>, Set JAVA_HOME environment variable
	
	2) Install Maven - <https://maven.apache.org>, set MAVEN_HOME environment varible
	
	3) Install Mongodb - <https://www.mongodb.com>
		
	4) Clone the project and navigate to the project directory	
		
	5) Build using Maven,
	
		mvn clean install or mvn clean install -DskipTests [To skip running of test cases]
		This should create userdata-api-0.0.1-SNAPSHOT.jar under target directory.
		
	6) Start Mongodb by running 
		mongod --dbpath <Directory where you want db files>
	
	7) Navigate to Mongo shell, 
		mongo
		use below commands,
		> use user-data-service  
		> db.counter.insert({"name":"client","sequence":1})
		> exit
		
	8) Run the Rest api by 
		java -jar target/userdata-api-0.0.1-SNAPSHOT.jar
		
	9) Navigate to http://localhost:8099/swagger-ui.html to see Swagger Interface.
	
	10) Use Swagger Dropdown at top Right corner to switch between Client[default(/v2/api-docs)] and User[secured(/v2/api-docs?group=secured)] APIs.
	

*API Usage*

	1. Client Registration : POST call on /client/register with following request
	Request - Both name and password are mandatory.
	{"name": String,"password": String}
	Response gives the clientId, which is to be used for Login and getting AuthToken. 
	{"msg":String}

	2. Generate Auth Token(Login) : POST call on /client/auth-token
	Request - Both clientId and password are mandatory.
	{"clientId": String,"password": String}

	Response gives the authToken which is to be passed as Authorization header for all calls for User Data

	{"generatedOn": String,"token": String}
	
	All APIs starting with /secure need an Authorization header as obtained as token from point 2.
	3. Invalidate Auth Token(Logout): POST on /secure/client/logout
	
	Response 
	{"msg":String}

	4. User CRUD operations - 

		a. Create User - POST on /secure/user
			Request - firstName is a mandatory attribute, all emails should be valid.
			{"addresses": List<String>,
			"emails": List<String>,
			"firstName": String,
			"lastName": String}
		
			Response 
			{"id": String,
			"firstName": String,
			"lastName" : String,
			"emails" : List<String>,
			"addresses" : List<String>}
	
		b. Read User - GET on /secure/user/{id}
		
			Response 
			{"id": String,
			"firstName": String,
			"lastName" : String,
			"emails" : List<String>,
			"addresses" : List<String>}
	
		c. Update User - PUT on /secure/user/{id}
			Request - firstName is a mandatory attribute, all emails should be valid.
			{"addresses": List<String>,
			"emails": List<String>,
			"firstName": String,
			"lastName": String}
		
			Response 
			{"id": String,
			"firstName": String,
			"lastName" : String,
			"emails" : List<String>,
			"addresses" : List<String>}
	
		d. Delete User - DELETE on /secure/user/{id}
		
			Response 
			{"msg": String}
	
		e. Get All Users - GET on /secure/user
			Response
			[{"id": String,
			"firstName": String,
			"lastName" : String,
			"emails" : List<String>,
			"addresses" : List<String>}]
	
	

