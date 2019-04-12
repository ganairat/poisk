package ru.itis.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import ru.itis.config.DataConfig;
import ru.itis.models.ArticleTerm;
import ru.itis.models.ModifiedArticleTerm;

import java.util.List;

public class ArticleTermDao {
    private static final String UPDATE_ARTICLE_TERM = "UPDATE article_term SET tf_idf = :tfIdf, idf = :idf " +
            "WHERE article_id = :articleId::UUID AND term_id = :termId::UUID;";
    private static final String GET_ALL_MODIFIED_ARTICLE_TERMS = "SELECT at.tf_idf, at.idf, " +
            "a.url AS article_url, t.term_text AS term FROM article_term at " +
            "INNER JOIN articles a ON at.article_id = a.id INNER JOIN terms_list t ON t.term_id = at.term_id;";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ArticleTermDao() {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DataConfig.getInstance().getDataSource());
    }

    public void updateArticleTerms(List<ArticleTerm> articleTerms) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(articleTerms.toArray());
        namedParameterJdbcTemplate.batchUpdate(UPDATE_ARTICLE_TERM, batch);
    }

    public List<ModifiedArticleTerm> getAllModifiedArticleTerms() {
        return namedParameterJdbcTemplate
                .query(GET_ALL_MODIFIED_ARTICLE_TERMS, new BeanPropertyRowMapper<>(ModifiedArticleTerm.class));
    }
}
