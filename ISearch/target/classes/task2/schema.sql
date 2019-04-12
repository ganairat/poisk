DROP TABLE IF EXISTS words_porter;
DROP TABLE IF EXISTS words_mystem;

CREATE TABLE words_porter (
  id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
  term VARCHAR (64),
  article_id uuid
);

CREATE TABLE words_mystem (
  id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
  term VARCHAR (64),
  article_id uuid
);