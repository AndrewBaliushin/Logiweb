DEPLOYMENT GUIDE
=================

By default Logiweb uses MySQL with next settings:
```
server adress: localhost:3306
db name: logiweb
user: javaschool 
pass: 12345
```
If you wish to change that you need to edit persistence.xml in 'persistence' module and liquibase settings in 'pom' file in root directory.

1. `git clone https://github.com/AndrewBaliushin/Logiweb.git`
2. Create user 'javaschool' with pass '12345' on your MySQL server for db with name 'logiweb'.
3. `mvn liquibase:update` 
4. `mvn clean install`

'war' file with project is located in 'presenation/target' folder.

####Tomcat deployment guide:
add to your maven setting.xml
```xml
<server>
    <id>TomcatServer</id>
    <username>admin</username>
    <password>admin</password>
</server>
```

add to your tomcat /conf/tomcat-users.xml:
    
```xml
<role rolename="manager-gui"/>
<role rolename="manager-script"/>
<role rolename="manager"/>
<role rolename="admin"/>
<user username="admin" password="admin" roles="admin,manager,manager-script,manager-gui"/>
```

and run `mvn tomcat7:deploy`

####Wildfly deployment guide:
Start your Wildfly server. 
Run `mvn wildfly:deploy`

Visit http://localhost:8080/logiweb/ 

Manger accaount: `manager@logiweb.com` pass:`12345`
Default Driver accaunt template: `driver-{employee id}@logiweb.com` pass:`12345` 
Can be changed in logiweb.properties.
