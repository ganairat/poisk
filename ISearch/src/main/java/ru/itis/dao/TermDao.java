package ru.itis.dao;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.itis.config.DataConfig;

import java.util.HashMap;
import java.util.Map;

public class TermDao {
    private static final String GET_TERMS_WITH_ARTICLE_IDS = "SELECT t.*, p.article_id " +
            "FROM words_porter p INNER JOIN terms_list t ON t.term_text = p.term;";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TermDao() {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DataConfig.getInstance().getDataSource());
    }

    public Map<String, Map<String, Integer>> getTermIdsWithArticleIds() {
        return getTermsWithArticleIds("term_id");
    }

    public Map<String, Map<String, Integer>> getTermTextsWithArticleIds() {
        return getTermsWithArticleIds("term_text");
    }

    private Map<String, Map<String, Integer>> getTermsWithArticleIds(String termField) {
        return namedParameterJdbcTemplate.getJdbcTemplate().query(GET_TERMS_WITH_ARTICLE_IDS, rs -> {
            Map<String, Map<String, Integer>> result = new HashMap<>();
            while (rs.next()) {
                String term = rs.getString(termField);
                String articleId = rs.getString("article_id");
                if (result.containsKey(term)) {
                    Map<String, Integer> article = result.get(term);
                    if (article.containsKey(articleId)) {
                        article.put(articleId, article.get(articleId) + 1);
                    } else {
                        article.put(articleId, 1);
                    }
                } else {
                    result.put(term, new HashMap<String, Integer>() {{
                        put(articleId, 1);
                    }});
                }
            }
            return result;
        });
    }
    /*public void insertTerm(Term term) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(term);
        namedParameterJdbcTemplate.update(SQL, parameters);
    }*/

    /*public Term getTermByText() {
    }*/
}
