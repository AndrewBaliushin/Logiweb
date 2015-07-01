DEPLOYMENT GUIDE
=================

1. git clone https://github.com/AndrewBaliushin/JavaSchool.git
2. create db, user and some mock data from /db-init.sql
3. mvn clean install
4. mvn tomcat:run       (or put /target/logiweb.war into your AppServer webapp directory)

If settings are default: http://localhost:8080/logiweb/ 

Edit ./persistence.xml to change db connection settings.
