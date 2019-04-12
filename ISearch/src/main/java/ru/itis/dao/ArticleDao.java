package ru.itis.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import ru.itis.config.DataConfig;
import ru.itis.models.Article;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleDao {
    private static final String INSERT_ARTICLE = "INSERT INTO articles (title, keywords, content, url, student_id) " +
            "VALUES (:title, :keywords, :content, :url, :studentId);";
    private static final String GET_ALL_ARTICLES = "SELECT * FROM articles;";
    private static final String GET_URLS = "SELECT a.url FROM articles a WHERE a.id IN " +
            "(SELECT at.article_id FROM article_term at INNER JOIN terms_list t ON at.term_id = t.term_id " +
            "WHERE t.term_text = '";
    private static final String GET_ARTICLE_IDS_WITH_WORD_COUNT = "SELECT article_id, COUNT(term) " +
            "AS cnt FROM words_porter GROUP BY (article_id);";
    private static final String GET_ARTICLE_BY_ID = "SELECT * FROM articles WHERE id = :id::UUID;";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ArticleDao() {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DataConfig.getInstance().getDataSource());
    }

    public void insertArticle(Article article) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(article);
        namedParameterJdbcTemplate.update(INSERT_ARTICLE, parameters);
    }

    public List<Article> getAllArticles() {
        return namedParameterJdbcTemplate.query(GET_ALL_ARTICLES, new BeanPropertyRowMapper<>(Article.class));
    }

    public List<String> getUrlsByWords(List<String> words) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT term FROM words_porter WHERE term IN (");
        for (String word : words) {
            query.append("'");
            query.append(word);
            query.append("',");
        }
        query.deleteCharAt(query.length() - 1);
        query.append(") GROUP BY (term) ORDER BY (COUNT(term));");
        List<String> sortedWords = namedParameterJdbcTemplate
                .getJdbcTemplate().queryForList(query.toString(), String.class);

        query = new StringBuilder();
        for (int i = 0; i < sortedWords.size() - 1; i++) {
            query.append(GET_URLS);
            query.append(sortedWords.get(i));
            query.append("') INTERSECT ");
        }
        query.append(GET_URLS);
        query.append(sortedWords.get(sortedWords.size() - 1));
        query.append("');");
        return namedParameterJdbcTemplate
                .getJdbcTemplate().queryForList(query.toString(), String.class);
    }

    public Map<String, Integer> getArticleIdsWithWordCount() {
        return namedParameterJdbcTemplate.getJdbcTemplate().query(GET_ARTICLE_IDS_WITH_WORD_COUNT, rs -> {
            Map<String, Integer> result = new HashMap<>();
            while (rs.next()) {
                result.put(rs.getString("article_id"), rs.getInt("cnt"));
            }
            return result;
        });
    }

    public Article getArticleById(String id) {
        return namedParameterJdbcTemplate.queryForObject(GET_ARTICLE_BY_ID, new MapSqlParameterSource()
                .addValue("id", id), new BeanPropertyRowMapper<>(Article.class));
    }
}
