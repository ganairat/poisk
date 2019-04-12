package ru.itis.task2;

import ru.itis.dao.ArticleDao;
import ru.itis.dao.StemDao;
import ru.itis.models.Article;
import ru.itis.models.StemWord;
import ru.itis.util.StemProcessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentParser {
    public static void main(String[] args) {
        ArticleDao articleDao = new ArticleDao();

        Set<String> stopWords = new HashSet<>();
        try {
            Scanner scanner = new Scanner(new File("src/main/resources/task2/stopwords-ru.txt"));
            while (scanner.hasNextLine()) {
                stopWords.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        List<StemWord> wordsPorterStem = new ArrayList<>();
        List<StemWord> wordsMyStem = new ArrayList<>();

        Pattern pattern = Pattern.compile("[А-Яа-яЁё]+(-[А-Яа-яЁё]+)*");

        for (Article article : articleDao.getAllArticles()) {
            Matcher matcher = pattern.matcher(article.getContent());
            while (matcher.find()) {
                String word = matcher.group().toLowerCase();
                if (!stopWords.contains(word)) {
                    String wordPorterStem = StemProcessor.getInstance().processPorterStem(word);
                    String wordMyStem = StemProcessor.getInstance().processMyStem(word);
                    if (!wordPorterStem.isEmpty()) {
                        wordsPorterStem.add(StemWord.builder()
                                .term(wordPorterStem)
                                .articleId(article.getId())
                                .build());
                    }
                    if (!wordMyStem.isEmpty()) {
                        wordsMyStem.add(StemWord.builder()
                                .term(wordMyStem)
                                .articleId(article.getId())
                                .build());
                    }
                }
            }
        }

        StemDao stemDao = new StemDao();

        stemDao.insertWordsPorterStem(wordsPorterStem);
        stemDao.insertWordsMyStem(wordsMyStem);
    }
}
