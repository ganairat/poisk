package ru.itis.dao;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import ru.itis.config.DataConfig;
import ru.itis.models.StemWord;

import java.util.List;

public class StemDao {
    private static final String INSERT_STEM_WORD = "INSERT INTO %s (term, article_id) " +
            "VALUES (:term, :articleId::UUID);";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public StemDao() {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DataConfig.getInstance().getDataSource());
    }

    public void insertWordsPorterStem(List<StemWord> stemWords) {
        insertWordsStem(String.format(INSERT_STEM_WORD, "words_porter"), stemWords);
    }

    public void insertWordsMyStem(List<StemWord> stemWords) {
        insertWordsStem(String.format(INSERT_STEM_WORD, "words_mystem"), stemWords);
    }

    private void insertWordsStem(String query, List<StemWord> stemWords) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(stemWords.toArray());
        namedParameterJdbcTemplate.batchUpdate(query, batch);
    }
}
