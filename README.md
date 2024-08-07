## Содержание
* [Описание](#description)  
* [Сборка](#build)  
* [Запуск](#run)  
* [API](#api)  
* [Задачи по расписанию](#tasks)  
* [Журналирование](#logs)  
* [Документация](#doc)  
* [Тесты](#tests)  

<a name="description"><h2>Описание</h2></a>
Backend сервиса Многопользовательский тайм-трекер, доступ к которому можно
получить по REST.  
**Тайм-трекер** (англ. _Time-tracker_ или _Time-tracking software_) - это категория компьютерного
программного обеспечения, которое позволяет сотрудникам, работающим за компьютерами,
записывать время, потраченное на выполнение задач или проектов, а работодателям их
контролировать.

<a name="build"><h2>Сборка</h2></a>
1. ```bash
   mvn clean package
   ```
2. ```bash
   docker build -t time-tracker-1.0 .
   ```

<a name="run"><h2>Запуск</h2></a>
### Пример конфигурации по умолчанию:
```properties
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
* `data.retention.period.days` - Период очистки старых данных трекинга (в днях)

### Запуск докер-контейнера:
Для запуска нужно создать файл docker-compose.yml и указать параметры, которые нужно переопределить в блоке environment и смонтировать директорию для логов   
```yaml
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
```bash
docker compose up -d
```

<a name="api"><h2>Доступные API запросы</h2></a>
### Cоздать пользователя трекинга
Возвращает id созданного пользователя
Параметры:
* id пользователя;
* Имя пользователя;
* Email (не должен использоваться у других пользователей)  

Запрос:  
```bash
curl -v -X POST http://localhost:8080/api/users/new -H "content-type:application/json" -d '{"username":"tester", "email":"a@a.ru"}'
```  
Ответ:   
```
< HTTP/1.1 200
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Sat, 01 Jun 2024 08:43:30 GMT
<
* Connection #0 to host localhost left intact
  1 
```

### Получить список пользователей трекинга  
Запрос:  
```bash
curl -v -X GET "http://localhost:8080/api/users/list"
```  
Ответ:
```json
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
###  Изменить данные пользователя
Параметры:
* id пользователя;
* Новое имя пользователя;
* Новый email (не должен использоваться у других пользователей)  

Запрос  
```bash
curl -i -w -X POST http://localhost:8080/api/user/update -H "content-type:application/json" -d '{"userID":1, "username":"tester123", "email":"a123@a.ru"}'
```  
Ответ:   
`Код 200`
### Добавить задачу
Создает задачу и возвращает статус ответа и id задачи
Параметры:
* Тема задачи;
* Описание задачи.  

Запрос:  
```bash
curl -v -X POST http://localhost:8080/api/tasks/new -H "content-type:application/json" -d '{"theme":"bug", "description":"bherjhjksmvsv1234"}'
```  
Ответ:   
```
< HTTP/1.1 200
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Sat, 01 Jun 2024 08:43:30 GMT
<
* Connection #0 to host localhost left intact
  1 
```

### Получить список задач
Запрос  
```bash
curl -v -X GET "http://localhost:8080/api/tasks/list"
```  
Ответ:
```json
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
### Начать отсчет времени по задаче Х
Параметры:
* id пользователя;
* id задачи.  

Запрос:   
```bash
curl -v -X POST http://localhost:8080/api/user/tracking/start -H "content-type:application/json" -d '{"userId":1, "taskId":1}'
```  
Ответ:   
`Код 200`
### Прекратить отсчет времени по задаче Х
Параметры:
* id пользователя;
* id задачи.  

Запрос:   
```bash
curl -v -X POST http://localhost:8080/api/user/tracking/stop -H "content-type:application/json" -d '{"userId":1, "taskId":1}'
```  
Ответ:   
`Код 200`  
### Получить весь трекинг времени всех пользователей
Запрос:   
```bash
curl -v -X GET "http://localhost:8080/api/tracking/list"
```  
Ответ:  
```json
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
### Показать все трудозатраты пользователя Y за период N..M.
В виде связного списка Задача - Сумма затраченного времени в виде (чч:мм), сортировка по времени поступления в трекер (для ответа на вопрос, На какие задачи я потратил больше времени)
Параметры:
* id пользователя;
* Начало периода;
* Окончание периода.  

Запрос:   
```bash
curl -X GET "http://localhost:8080/api/user/tracking/durations/period?userID=1&startTime=2024-05-13T00:00:00&endTime=2024-05-17T23:59:59"
```  
Ответ
```json
  [  
    {  
      "taskId":1,
      "totalDuration":1.0  
    }  
  ]  
```
### Показать все временные интервалы занятые работой за период N..M 
В виде связного списка Временной интервал (число чч:мм) - Задача (для ответа на вопросы, На что ушла моя неделя или Где за прошедшую неделю были ‘дыры’, когда я ничего не делал)
Параметры:   
* id пользователя;
* Начало периода;
* Окончание периода.  

Запрос:   
```bash
curl -X GET "http://localhost:8080/api/user/tracking/intervals/period?userID=2&startTime=2024-05-31T00:00:00&endTime=2024-05-31T23:59:59"
```  
Ответ:
```json
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
### Показать сумму трудозатрат по всем задачам пользователя Y за период N..M
Параметры:
* id пользователя;
* Начало периода;
* Окончание периода.  

Запрос:  
```bash
curl -X GET "http://localhost:8080/api/user/tracking/sum/period?userID=2&startTime=2024-05-31T00:00:00&endTime=2024-05-31T23:59:59"
```  
Ответ:
```
3
```
### Удалить всю информацию о пользователе Z
Параметры:
* id пользователя.  

Запрос:   
```bash
curl -v -X DELETE http://localhost:8080/api/user/delete -H "content-type:application/json" -d '{"userId":1}'
```  
Ответ:
```
Deleted 1 rows
```
### Очистить данные трекинга пользователя Z
Параметры:
* id пользователя.  

Запрос:   
```bash
curl -v -X DELETE http://localhost:8080/api/user/tracking/delete -H "content-type:application/json" -d '{"userId":1}'
```  
Ответ:
```
Deleted 2 rows
```

<a name="tasks"><h2>Задачи по расписанию</h2></a>
Если пользователь не завершил отсчет своего времени, то он завершается автоматически в 23:59 текущего дня (Время можно изменить в конфигурационном файле).  
Данные хранятся в системе не более периода, указанного в конфигурации сервиса.
Очистка данных производится задачей по расписанию, работающей в составе приложения.

<a name="logs"><h2>Журналирование</h2></a>
Для журналирования работы используется log4j2, конфигурация в файле log4j2.xml. Реализована ротация логов.  
Логи работы сервиса пишутся в файл time-tracker.log, системные логи спринга пишутся в time-tracker-spring.log. Разные файлы используются в основном в целях изучения работы логеров с разными файлами

Несколько логеров создано для повышения читаемости логов.

<a name="doc"><h2>Документация</h2></a>
Проект содержит список всех REST команд которые можно выполнить с кратким описанием входных и выходных данных (с примерами выполнения запросов curl). И документацию javadoc
Спецификация OpenAPI расположена в `/doc/openapi/openapi.yaml`.  
Язык для документации русский и английский.
(Использовал в основном английский в качестве упражнения, сделал еще и на русском для сравнения читаемости)

<a name="tests"><h2>Тесты</h2></a>
### Работа с БД
В проекте реализовал тест поиска записи в БД в качестве изучения структуры тестов репозитория  
`/src/test/java/org/example/timetracker/Repositories/UsersRepositoryTest.java`  
* `testExistsByEmail` - Создается пользователь, вызывается метод поиска по email, пользователь удаляется
Тест добавляет запись о пользователе в БД и проверяет работу метода поиска по email

### Unit-тесты


Так же реализовал несколько тестов методов сервиса, изучил работу с заглушками, проверку результата, проверку количества вызовов и передаваемых параметров   
`src/test/java/org/example/timetracker/Service/TimeTrackerServiceTest.java`  
* `testConvertDate_ValidDate` и `testConvertDate_ValidDate` проверяют метод конвертации данных в случае валидных и невалидных данных 
* `testFindAll` - Проверка метода получения всех записей трекинга
* `testCountByUserId` - Проверка метода получения количества записей трекинга с определенным id пользователя
* `testDeleteByUserId` - Проверка метода удаления записей по id пользователя
* `testStartTracking` - Проверка метода начала трекинга
* `testStopTracking` - Проверка метода окончания трекинга

### API
Реализовал тест API в рамках изучения  
`src/test/java/org/example/timetracker/Controller/TimeTrackerRestControllerTest.java`
* `testAddUser` - проверка обработчика запроса `/api/users/new`. Создается новый пользователь, проверятся что метод сохранения в базу вызван с нужными параметрами 

### Интеграционный тест
Реализован интеграционный тест услуги трекинга времени. 
Проверяется работоспособность функционала трекинга: начало трекинга, окончание и подсчет трудозатрат.
Шаги теста:
1. Создается пользователь и задача, начинается отсчет времени (с помощью API запроса).
2. Выполняется ожидание 30 секунд
3. Отсчет времени по задаче завершается (API запрос)
4. Запрашивается сумма трудозатрат пользователя за период настоящее время +- 5 минут. Тоже с помощью запроса 
5. трудозатраты должны быть равны 0,01, так как работа велась 30 секунд

Перед и после теста база очищается. Для запуска такого теста нужно работающее приложение. 