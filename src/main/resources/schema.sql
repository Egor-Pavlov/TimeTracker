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

CREATE TABLE IF NOT EXISTS Time_Entry (
                                         Id INT AUTO_INCREMENT PRIMARY KEY,
                                         user_Id INT,
                                         task_Id INT,
                                         start_Time DATETIME NOT NULL,
                                         end_Time DATETIME,
                                         duration DOUBLE,
                                         FOREIGN KEY (user_Id) REFERENCES User(user_Id),
                                         FOREIGN KEY (task_Id) REFERENCES Task(task_Id)
)
