# Описание
Backend сервиса Многопользовательский тайм-трекер, доступ к которому можно
получить по REST.
Тайм-трекер (англ. Time-tracker или Time-tracking software) - это категория компьютерного
программного обеспечения, которое позволяет сотрудникам, работающим за компьютерами,
записывать время, потраченное на выполнение задач или проектов, а работодателям их
контролировать.

## Сборка
1. mvn clean package
2. docker build -t time-tracker-1.0 .
3. sudo docker run -p 8080:8080 time-tracker-1.0

https://habr.com/ru/articles/448094/

## Запуск

### Пример конфигурации по умолчанию:
```
spring.application.name=TimeTracker
spring.datasource.url=jdbc:mysql://localhost:3306/time_tracker?useLegacyDatetimeCode=false&serverTimezone-UTC
spring.datasource.username=javauser
spring.datasource.password=javapassword
spring.sql.init.mode=always

scheduled.task.cron=59 59 23 * * *

data.retention.period.days=2
```
### Описание параметров:  

* `spring.datasource.url` - Адрес подключения к БД. БД должна существовать
* `spring.datasource.url` - Пользователь mysql. Должен существовать
* `spring.datasource.password` - Пароль от УЗ mysql
* `scheduled.task.cron` - Время суток, в которое автоматически завершается трекинг
* `data.retention.period.days` - период очистки старых данных трекинга (в днях)

### Запуск докер-контейнера:
Для запуска нужно создать файл docker-compose.yml и указать параметры, которые нужно переопределить в блоке environment и смонтировать директорию для логов   
```
version: "3.8"
services:
  time-tracker:
    image: time-tracker-1.0
    container_name: time-tracker
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_JDBCURL=jdbc:mysql://100.110.2.118:3306/time_tracker?useLegacyDatetimeCode=false&serverTimezone-UTC
      - SPRING_DATASOURCE_USERNAME=javauser
      - SPRING_DATASOURCE_PASSWORD=javapassword
      - SCHEDULED_TASK_CRON=59 59 23 * * *
      - DATA_RETENTION_PERIOD_DAYS=1
    volumes:
      - /var/log/time-tracker:/var/log/time-tracker
    restart: always
```
После нужно выполнить команду   
`docker compose up -d`

## 1.1 Типы запросов
* создать пользователя трекинга
  `curl -i -w -X POST http://localhost:8080/api/users/new -H "content-type:application/json" -d '{"username":"tester", "email":"a@a.ru"}'`
* Получить список пользователей трекинга
  `curl -v -X GET "http://localhost:8080/api/users/list"`  
Ответ:

```
[
    {
        "userID": 1,
        "userName": "Alice",
        "userEmailAddress": "alice@example.com"
    },
    {
        "userID": 2,
        "userName": "Bob",
        "userEmailAddress": "bob@example.com"
    },
    {
        "userID": 3,
        "userName": "Charlie",
        "userEmailAddress": "charlie@example.com"
    }
]
```
* изменить данные пользователя
  `curl -i -w -X POST http://localhost:8080/api/user/update -H "content-type:application/json" -d '{"userID":1, "username":"tester123", "email":"a123@a.ru"}'`
* Добавить задачу
  `curl -i -w -X POST http://localhost:8080/api/tasks/new -H "content-type:application/json" -d '{"theme":"bug", "description":"bherjhjksmvsv1234"}'`
* Получить список задач
  `curl -v -X GET "http://localhost:8080/api/tasks/list"`  
Ответ:  

```
[
    {
        "taskID": 1,
        "creationDate": "2024-04-15T02:00:00.000+00:00",
        "taskTheme": "Bug Не создаются привязки",
        "taskDescription": "При инициализации ТД не создаются привязки"
    },
    {
        "taskID": 2,
        "creationDate": "2024-03-10T02:00:00.000+00:00",
        "taskTheme": "Актуализировать скрипт инсталятор",
        "taskDescription": "Убрать логику удаления томката"
    }
]
```
* начать отсчет времени по задаче Х
  `curl -v -X POST http://localhost:8080/api/user/tracking/start -H "content-type:application/json" -d '{"userId":1, "taskId":1}'`
* прекратить отсчет времени по задаче Х
  `curl -v -X POST http://localhost:8080/api/user/tracking/stop -H "content-type:application/json" -d '{"userId":1, "taskId":1}'`  
* Получить весь трекинг времени всех пользователей
  `curl -v -X GET "http://localhost:8080/api/tracking/list"`  

Ответ:  
```
[
    {
        "id": 9,
        "userId": 1,
        "taskId": 1,
        "startTime": "2024-05-15T02:00:00.000+00:00",
        "endTime": "2024-05-15T04:00:00.000+00:00",
        "duration": 2
    },
    {
        "id": 10,
        "userId": 1,
        "taskId": 1,
        "startTime": "2024-05-14T02:00:00.000+00:00",
        "endTime": "2024-05-14T04:00:00.000+00:00",
        "duration": 2
    },
    {
        "id": 11,
        "userId": 1,
        "taskId": 2,
        "startTime": "2024-05-16T03:00:00.000+00:00",
        "endTime": "2024-05-16T05:00:00.000+00:00",
        "duration": 2
    },
    {
        "id": 12,
        "userId": 2,
        "taskId": 2,
        "startTime": "2024-05-15T06:00:00.000+00:00",
        "endTime": "2024-05-15T10:00:00.000+00:00",
        "duration": 4
    }
]
```
* показать все трудозатраты пользователя Y за период N..M в виде связного списка Задача - Сумма затраченного времени в виде (чч:мм), сортировка по времени поступления в трекер (для ответа на вопрос, На какие задачи я потратил больше времени)
  `curl -X GET "http://localhost:8080/api/user/tracking/durations/period?userID=1&startTime=2024-05-13T00:00:00&endTime=2024-05-17T23:59:59"`

Ответ 
 
```
  [  
    {  
      "taskId":1,
      "totalDuration":1.0  
    }  
  ]  
```
* показать все временные интервалы занятые работой за период N..M в виде связного списка Временной интервал (число чч:мм) - Задача (для ответа на вопросы, На что ушла моя неделя или Где за прошедшую неделю были ‘дыры’, когда я ничего не делал)
  ` curl -X GET "http://localhost:8080/api/user/tracking/intervals/period?userID=2&startTime=2024-05-31T00:00:00&endTime=2024-05-31T23:59:59"`  

Ответ:

```
  [  
    {  
      "taskID": 1,  
      "theme": "Project A",  
      "description": "Work on project A",  
      "startTime": "2024-05-30T04:00:00.000+00:00",  
      "endTime": "2024-05-30T06:00:00.000+00:00"  
    },  
    {  
      "taskID": 1,  
      "theme": "Project A",  
      "description": "Work on project A",  
      "startTime": "2024-05-30T01:21:05.000+00:00",  
      "endTime": "2024-05-30T03:21:05.000+00:00"  
    }  
  ]
```  
* показать сумму трудозатрат по всем задачам пользователя Y за период N..M
  ` curl -X GET "http://localhost:8080/api/user/tracking/sum/period?userID=2&startTime=2024-05-31T00:00:00&endTime=2024-05-31T23:59:59"`  

Ответ:
   
```
3
```
* удалить всю информацию о пользователе Z
  `curl -v -X DELETE http://localhost:8080/api/user/delete -H "content-type:application/json" -d '{"userId":1}'`  
  Ответ:
```
Deleted 1 rows
```
* очистить данные трекинга пользователя Z
  `curl -v -X DELETE http://localhost:8080/api/user/tracking/delete -H "content-type:application/json" -d '{"userId":1}'`  

Ответ:
```
Deleted 2 rows
```

## 1.2 Дополнительные условия
Если пользователь не завершил отсчет своего времени, то он завершается автоматически в 23:59 текущего дня.
Данные должны хранятся в системе не более периода, указанного в конфигурации сервиса. 
Очистка данных производится задачей по расписанию, работающей в составе java приложения.

## 2.3 Журналирование
Для журналирования работы используется log4j2, конфигурация в файле log4j2.xml. Реализована ротация логов.
Логи работы сервиса пишутся в файл time-tracker.log, системные логи спринга пишутся в time-tracker-spring.log. Разные файлы используются в основном в целях изучения работы логеров с разными файлами 

Несколько логеров создано для повышения читаемости логов.

## 2.4 Документация
Проект должен содержать список всех REST команд которые можно выполнить с кратким описанием входных и выходных данных (с примерами выполнения запросов curl).

Спецификация OpenAPI приветствуется.

Язык для документации русский или английский на усмотрение разработчика.

(Использовал в основном английский в качестве упражнения, сделал еще и на русском для сравненния читаемости)


## Тесты