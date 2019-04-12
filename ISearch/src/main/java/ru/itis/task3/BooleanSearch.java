package ru.itis.task3;

import ru.itis.dao.ArticleDao;
import ru.itis.util.StemProcessor;

import java.util.ArrayList;
import java.util.List;

public class BooleanSearch {
    public static void main(String[] args) {
        ArticleDao articleDao = new ArticleDao();

        String text = "Создатели сериала";

        List<String> processedWords = new ArrayList<>();
        for (String word : text.split(" ")) {
            processedWords.add(StemProcessor.getInstance().processPorterStem(word.toLowerCase()));
        }

        articleDao.getUrlsByWords(processedWords).forEach(System.out::println);
    }
}
