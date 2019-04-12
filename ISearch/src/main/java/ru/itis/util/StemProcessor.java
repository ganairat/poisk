package ru.itis.util;

import opennlp.tools.stemmer.snowball.SnowballStemmer;
import ru.stachek66.nlp.mystem.holding.Factory;
import ru.stachek66.nlp.mystem.holding.MyStem;
import ru.stachek66.nlp.mystem.holding.MyStemApplicationException;
import ru.stachek66.nlp.mystem.holding.Request;
import ru.stachek66.nlp.mystem.model.Info;
import scala.Option;
import scala.collection.JavaConversions;

import java.util.Objects;

public class StemProcessor {
    private static volatile StemProcessor instance;

    private SnowballStemmer processorPorterStem;
    private MyStem processorMyStem;

    private StemProcessor() {
        processorPorterStem = new SnowballStemmer(SnowballStemmer.ALGORITHM.RUSSIAN);
        processorMyStem = new Factory("-igd --eng-gr --format json --weight")
                .newMyStem("3.0", Option.empty()).get();
    }

    public static StemProcessor getInstance() {
        StemProcessor localInstance = instance;
        if (localInstance == null) {
            synchronized (StemProcessor.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new StemProcessor();
                }
            }
        }
        return localInstance;
    }

    public String processPorterStem(String word) {
        return processorPorterStem.stem(word).toString();
    }

    public String processMyStem(String word) {
        String processedWord = "";
        try {
            Iterable<Info> infos = JavaConversions.asJavaIterable(processorMyStem
                    .analyze(Request.apply(word))
                    .info()
                    .toIterable());
            for (Info info : infos) {
                Option<String> lex = info.lex();
                if (Objects.nonNull(lex) && lex.isDefined()) {
                    processedWord = lex.get();
                }
            }
        } catch (MyStemApplicationException e) {
            e.printStackTrace();
        }
        return processedWord;
    }
}
