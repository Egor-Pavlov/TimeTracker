# back
# устанавливаем самую лёгкую версию JVM
FROM openjdk:17-jdk-alpine

# указываем ярлык. Например, разработчика образа и проч. Необязательный пункт.
LABEL maintainer="egor-pavlov"

# указываем точку монтирования для внешних данных внутри контейнера (как мы помним, это Линукс)
VOLUME /etc/time-tracker

# внешний порт, по которому наше приложение будет доступно извне
EXPOSE 8080

# указываем, где в нашем приложении лежит джарник
ARG JAR_FILE=target/TimeTracker-0.0.1-SNAPSHOT.jar

# добавляем джарник в образ под именем rebounder-chain-backend.jar
ADD ${JAR_FILE} target/TimeTracker-0.0.1-SNAPSHOT.jar

# команда запуска джарника
ENTRYPOINT ["java","-jar","target/TimeTracker-0.0.1-SNAPSHOT.jar"]