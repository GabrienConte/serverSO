# Imagem base com o JDK e Maven
FROM maven:3.8.4-openjdk-11

# Diretório de trabalho
RUN mkdir -p /app/so-example
WORKDIR /app/so-example

# Instalação do Git
RUN apt-get update && apt-get install -y git

# Clone do projeto do GitHub
RUN git clone https://github.com/GabrienConte/serverSO.git /app/so-example

# Copiar o arquivo pom.xml para o diretório de trabalho
COPY pom.xml .

# Copiar todo o código-fonte para o diretório de trabalho
COPY src ./src

# Compilação e empacotamento do projeto
RUN mvn clean package

# Comando a ser executado quando o contêiner for iniciado
CMD ["java", "-jar", "/app/so-example/target/so-example-1.0-SNAPSHOT.jar"]

