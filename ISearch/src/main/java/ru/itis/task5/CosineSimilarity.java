package ru.itis.task5;

import ru.itis.dao.ArticleTermDao;
import ru.itis.models.ModifiedArticleTerm;
import ru.itis.util.StemProcessor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CosineSimilarity {
    public static void main(String[] args) {
        ArticleTermDao articleTermDao = new ArticleTermDao();

        String text = "Создатели сериала";

        String[] words = text.split(" ");
        int queryWordCount = words.length;

        Map<String, Integer> processedWords = new HashMap<>();
        for (String word : words) {
            String processedWord = StemProcessor.getInstance().processPorterStem(word.toLowerCase());
            if (processedWords.containsKey(processedWord)) {
                processedWords.put(processedWord, processedWords.get(processedWord) + 1);
            } else {
                processedWords.put(processedWord, 1);
            }
        }

        Map<String, Map<String, Double>> articleUrlWithWordAndTfIdf = new HashMap<>();
        Map<String, Double> termFromQueryWithTfIdf = new HashMap<>();

        double b2 = 0;
        for (ModifiedArticleTerm articleTerm : articleTermDao.getAllModifiedArticleTerms()) {
            String articleUrl = articleTerm.getArticleUrl();
            if (articleUrlWithWordAndTfIdf.containsKey(articleUrl)) {
                articleUrlWithWordAndTfIdf.get(articleUrl).put(articleTerm.getTerm(), articleTerm.getTfIdf());
            } else {
                articleUrlWithWordAndTfIdf.put(articleUrl, new HashMap<String, Double>() {{
                    put(articleTerm.getTerm(), articleTerm.getTfIdf());
                }});
            }

            String term = articleTerm.getTerm();
            if (!termFromQueryWithTfIdf.containsKey(term) && processedWords.containsKey(term)) {
                double tf = (double) processedWords.get(term) / queryWordCount;
                double tfIdf = tf * articleTerm.getIdf();
                termFromQueryWithTfIdf.put(term, tfIdf);
                b2 += (tfIdf * tfIdf);
            }
        }
        b2 = Math.sqrt(b2);

        Map<String, Double> scores = new HashMap<>();
        for (Map.Entry<String, Map<String, Double>> entry : articleUrlWithWordAndTfIdf.entrySet()) {
            double ab = 0;
            double a2 = 0;
            for (Map.Entry<String, Double> termEntry : entry.getValue().entrySet()) {
                a2 += (termEntry.getValue() * termEntry.getValue());
                String term = termEntry.getKey();
                if (termFromQueryWithTfIdf.containsKey(term)) {
                    ab += (termFromQueryWithTfIdf.get(term) * termEntry.getValue());
                }
            }
            a2 = Math.sqrt(a2);
            scores.put(entry.getKey(), ab / a2 / b2);
        }

        scores.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).limit(10)
                .filter(e -> e.getValue() != 0.0).forEach(e -> System.out.println(e.getValue() + " " + e.getKey()));
    }
}
