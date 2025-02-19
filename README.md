# ITI-JETS-ChatApplication
First collective project from iti JETS, department Java Enterprise.

To compile the application(s).
-----
First, the server:
1. You need MySQL installed where the server will be running, go in the resources folder, create a file with
the name db_info.properties, and append to it
URL=jdbc\:mysql\://localhost\:3306/jets_projects_chatzone_db
USER_NAME=user_name
PASSWORD=password

Be default it will look for the DBMS in the local machine, but you can change it if you want.
user_name and password should be changed to the username and password of your MySQL user
for example, root and root.

2. Run the db_script and db data script in the same exact order, teh db_script will create the database if it not exists, and will
drop the database if it already exists first before creating it, this is done to reduce human error.
THIS IS MANDATORY if you want to run the maven commnad install later since install command will run the unit tests defined
inside the server side test packages, it relies on the DB to be a specific statue to pass the hard coded tests.

4. You need Java JDK installed in your machine to be able to compile and run the application.
5. You need Maven installed, and configured to use the JDK version installed.
6. By default, the service will be published on your localhost, if you want to specify an IP address,
the ServiceManager class and specify the IP address you want (Refer to Java RMI docs).

7. Once the program is compiled, you can install it to extract ready to run Jar that can run the server directly, the pom.xml
file includes definition for the shade plugin that allows you to combine the dependencies of the project inside the generated jar automatically.

Once the application is installed, you are free to change the DB state, may resetting it again to the testing state using the MySQL scripts provided
or any other state you want, the install command will run the tests which will change the state of the database from the state defined in the scripts.

Second, the client side.
1. All the dependencies will be included automatically inside the installed jar.
2. The application is configured to connect to the server at localhost, you can change that inside the service manager class
inside the client side.

Third, the admin side.
The same exact idea as the client side. 
