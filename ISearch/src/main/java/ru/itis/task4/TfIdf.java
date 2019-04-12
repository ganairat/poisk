package ru.itis.task4;

import ru.itis.dao.ArticleDao;
import ru.itis.dao.ArticleTermDao;
import ru.itis.dao.TermDao;
import ru.itis.models.ArticleTerm;
import ru.itis.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TfIdf {
    public static void main(String[] args) {
        ArticleDao articleDao = new ArticleDao();
        ArticleTermDao articleTermDao = new ArticleTermDao();
        TermDao termDao = new TermDao();

        Map<String, Integer> articleIdsWithWordCount = articleDao.getArticleIdsWithWordCount();

        List<ArticleTerm> articleTerms = new ArrayList<>();
        for (Map.Entry<String, Map<String, Integer>> termEntry : termDao.getTermIdsWithArticleIds().entrySet()) {
            double idf = Math.log((double) Constants.ARTICLES_QUANTITY / termEntry.getValue().size());
            for (Map.Entry<String, Integer> articleEntry : termEntry.getValue().entrySet()) {
                double tf = (double) articleEntry.getValue() / articleIdsWithWordCount.get(articleEntry.getKey());
                ArticleTerm articleTerm = ArticleTerm.builder()
                        .termId(termEntry.getKey())
                        .articleId(articleEntry.getKey())
                        .tfIdf(tf * idf)
                        .idf(idf)
                        .build();
                articleTerms.add(articleTerm);
            }
        }

        articleTermDao.updateArticleTerms(articleTerms);
    }
}
