FROM tomcat:10-jdk17
ARG WAR_FILE="../target/*.war"
COPY ${WAR_FILE} /usr/local/tomcat/webapps/ROOT.war
CMD ["catalina.sh", "run"]
