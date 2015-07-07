DEPLOYMENT GUIDE
=================

1. `git clone https://github.com/AndrewBaliushin/JavaSchool.git`
2. create db, user and some mock data from /db-init.sql
3. `mvn clean install`

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
    
Mock data includes two users: `manager@logiweb.com` & `driver@logiweb.com`;
Passwords: 12345

Visit http://localhost:8080/logiweb/ 

Edit `persistence.xml` to change db connection settings.
