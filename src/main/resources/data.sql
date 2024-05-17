# -- Вставка данных в таблицу пользователей
# INSERT INTO user (username, email) VALUES
#                    ('Alice', 'alice@example.com'),
#                    ('Bob', 'bob@example.com'),
#                    ('Charlie', 'charlie@example.com');
#
# -- Вставка данных в таблицу задач
# INSERT INTO task (theme, description, creation_Date) VALUES
#               ('Project A', 'Work on project A', '24-04-15 09:00:00'),
#               ('Project B', 'Work on project B', '24-03-10 09:00:00' ),
#               ('Project C', 'Work on project C', '24-01-29 09:00:00'),
#               ('Project D', 'Work on project D', '23-12-15 09:00:00'),
#               ('Project E', 'Work on project E', '23-01-30 09:00:00');
#
# -- Вставка данных в таблицу записей времени
# INSERT INTO time_entry (user_Id, task_Id, start_Time, end_Time, duration) VALUES
#                         (1, 1, '2024-05-15 09:00:00', '2024-05-15 11:00:00', 2.0),
#                         (1, 1, '2024-05-14 09:00:00', '2024-05-14 11:00:00', 2.0),
#                         (1, 2, '2024-05-16 10:00:00', '2024-05-16 12:00:00', 2.0),
#                         (2, 2, '2024-05-15 13:00:00', '2024-05-15 17:00:00', 4.0),
#                         (2, 3, '2024-05-16 08:00:00', '2024-05-16 09:30:00', 1.5),
#                         (3, 1, '2024-05-14 09:00:00', '2024-05-14 12:00:00', 3.0),
#                         (3, 4, '2024-05-14 13:00:00', '2024-05-14 15:00:00', 2.0),
#                         (3, 5, '2024-05-16 14:00:00', '2024-05-16 16:00:00', 2.0);