FROM payara/server-full

ARG WAR_NAME=simple-java-rest-backend

# Add PostgreSQL JDBC driver JAR file to payara lib directory
ENV PAYARA_LIB_PATH="/opt/payara/appserver/glassfish/domains/domain1/lib"
ENV JDBC_DRIVER_NAME="postgresql-42.3.1.jar"
ADD --chown=payara:payara https://jdbc.postgresql.org/download/$JDBC_DRIVER_NAME $PAYARA_LIB_PATH/
RUN chmod 751 $PAYARA_LIB_PATH/$JDBC_DRIVER_NAME

# Setup init hook for configurable container start (e.g. setup ALIASE)
COPY --chown=payara:payara init_0_simple_java_rest_backend.sh ${SCRIPT_DIR}/
RUN chmod 751 ${SCRIPT_DIR}/init_0_simple_java_rest_backend.sh

# Install generated WAR file on Payara
COPY target/${WAR_NAME}.war ${DEPLOY_DIR}/simple-java-rest-backend.war
