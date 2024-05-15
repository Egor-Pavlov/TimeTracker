-- Таблица пользователей
CREATE TABLE IF NOT EXISTS user (
                                     user_Id INT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(255) NOT NULL,
                                     email VARCHAR(255) NOT NULL UNIQUE
);

-- Таблица задач
CREATE TABLE IF NOT EXISTS task (
                                     task_Id INT AUTO_INCREMENT PRIMARY KEY,
                                     theme TEXT NOT NULL,
                                     description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS time_entry (
                                         Id INT AUTO_INCREMENT PRIMARY KEY,
                                         user_Id INT,
                                         task_Id INT,
                                         start_Time DATETIME NOT NULL,
                                         end_Time DATETIME,
                                         duration DOUBLE,
                                         FOREIGN KEY (user_Id) REFERENCES user(user_Id),
                                         FOREIGN KEY (task_Id) REFERENCES task(task_Id)
)