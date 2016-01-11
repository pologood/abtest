FROM jboss/wildfly:latest

USER jboss
EXPOSE 8080
RUN /opt/jboss/wildfly/bin/add-user.sh admin Admin#70365 --silent
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
ADD /resource/target/resource-0.0.1-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/
