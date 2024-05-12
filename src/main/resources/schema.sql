-- Таблица пользователей
CREATE TABLE IF NOT EXISTS User (
                                     user_Id INT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(255) NOT NULL,
                                     email VARCHAR(255) NOT NULL UNIQUE
);

-- Таблица задач
CREATE TABLE IF NOT EXISTS Task (
                                     task_Id INT AUTO_INCREMENT PRIMARY KEY,
                                     theme TEXT NOT NULL,
                                     description TEXT NOT NULL
);

-- Таблица записей времени
CREATE TABLE IF NOT EXISTS TimeEntry (
                                           Id INT AUTO_INCREMENT PRIMARY KEY,
                                           userId INT,
                                           taskId INT,
                                           startTime DATETIME NOT NULL,
                                           endTime DATETIME NOT NULL,
                                           FOREIGN KEY (userId) REFERENCES User(user_Id),
                                           FOREIGN KEY (taskId) REFERENCES Task(task_Id)
);
