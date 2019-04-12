CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS articles;
DROP TABLE IF EXISTS students;

CREATE TABLE IF NOT EXISTS articles (
  id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
  title VARCHAR (256),
  keywords VARCHAR (256),
  content TEXT,
  url VARCHAR (256),
  student_id INT
);

CREATE TABLE IF NOT EXISTS students (
  id INT PRIMARY KEY,
  name VARCHAR (32),
  surname VARCHAR (32),
  mygroup VARCHAR (6)
);

INSERT INTO students VALUES (104, 'Airat', 'Ganeev', '11-502');

-- pg_dump -h localhost -U postgres isearch > dump.sql