CREATE TABLE message_changes
(
  message_text VARCHAR(40) NOT NULL,
  id INT NOT NULL,
  message_id INT NOT NULL
);
CREATE TABLE message_deletions
(
  id INT NOT NULL,
  message_id INT NOT NULL
);
CREATE TABLE messages
(
  message_text VARCHAR(40) NOT NULL,
  username VARCHAR(20) NOT NULL,
  message_time VARCHAR(20) NOT NULL,
  message_id INT NOT NULL,
  is_deleted INT NOT NULL,
  user_id INT NOT NULL
);
CREATE TABLE username_changes
(
  id INT NOT NULL,
  username VARCHAR(20) NOT NULL
);
CREATE TABLE users
(
  username VARCHAR(20) NOT NULL,
  id INT NOT NULL
);

CREATE TABLE chatters
(
  id INT PRIMARY KEY NOT NULL,
  username VARCHAR(20) NOT NULL,
  session_id VARCHAR(45) NOT NULL
);
CREATE UNIQUE INDEX unique_id ON chatters (id);

