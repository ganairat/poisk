DROP TABLE IF EXISTS terms_list;
DROP TABLE IF EXISTS article_term;

CREATE TABLE terms_list (
  term_id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
  term_text VARCHAR (64)
);

CREATE TABLE article_term (
  article_id uuid,
  term_id uuid
);

INSERT INTO terms_list (term_text)
  SELECT DISTINCT ON (p.term) p.term FROM words_porter p ORDER BY (p.term) ASC;

INSERT INTO article_term (article_id, term_id)
  SELECT DISTINCT ON (p.article_id, t.term_id) p.article_id, t.term_id FROM words_porter p
  INNER JOIN terms_list t ON t.term_text = p.term;