# Usa una imagen base de Java
FROM openjdk:11-jdk-slim

# Establecer variable de entorno para Maven
ENV MAVEN_VERSION=3.8.7

# Instala Maven
RUN apt-get update && \
    apt-get install -y curl tar && \
    curl -o /tmp/apache-maven.tar.gz https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    tar -xzf /tmp/apache-maven.tar.gz -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION}/bin/mvn /usr/bin/mvn && \
    rm -rf /tmp/apache-maven.tar.gz

# Establece el directorio de trabajo
WORKDIR /app

# Expone el puerto si es necesario
EXPOSE 8080

# Comando por defecto
CMD ["mvn", "--version"]