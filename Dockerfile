# Imagem base com o JDK e Maven
FROM maven:3.8.4-openjdk-11

# Diretório de trabalho
WORKDIR /app/so-example

# Instalação do Git
RUN apt-get update && apt-get install -y git

# Clone do projeto do GitHub
RUN git clone https://github.com/GabrienConte/serverSO.git /app/so-example

RUN ["mvn", "package"]

# Comando a ser executado quando o contêiner for iniciado
# java -cp target/classes org.example.Main
ENTRYPOINT ["java", "-cp" , "target/classes", "org.example.Main"]