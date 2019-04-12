package ru.itis.task7;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import ru.itis.dao.ArticleDao;
import ru.itis.dao.TermDao;
import ru.itis.models.Article;
import ru.itis.util.StemProcessor;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LSI {
    private static final int K = 5;

    private String[] articleIds;
    private String[] terms;
    private double[][] matrix;

    public LSI() {
        ArticleDao articleDao = new ArticleDao();
        TermDao termDao = new TermDao();

        Map<String, Map<String, Integer>> termsWithArticleIds = termDao.getTermTextsWithArticleIds();

        articleIds = articleDao.getAllArticles().stream().map(Article::getId).toArray(String[]::new);
        terms = termsWithArticleIds.keySet().stream().toArray(String[]::new);

        matrix = new double[terms.length][articleIds.length];

        for (int i = 0; i < terms.length; i++) {
            Map<String, Integer> article = termsWithArticleIds.get(terms[i]);
            for (int j = 0; j < articleIds.length; j++) {
                matrix[i][j] = article.getOrDefault(articleIds[j], 0);
            }
        }
    }

    public static void main(String[] args) {
        LSI lsi = new LSI();
        String[] articleIds = lsi.getArticleIds();
        String[] terms = lsi.getTerms();
        double[][] sourceMatrix = lsi.getMatrix();

        int termsCount = terms.length;
        int documentsCount = articleIds.length;

        String text = "Обзор хорошей игры";
        Set<String> processedWords = Arrays.stream(text.split(" ")).map(e ->
                StemProcessor.getInstance().processPorterStem(e.toLowerCase())).collect(Collectors.toSet());

        double[][] query = new double[1][termsCount];
        for (int i = 0; i < termsCount; i++) {
            if (processedWords.contains(terms[i])) {
                query[0][i] = 1;
            }
        }

        Matrix matrix = new Matrix(sourceMatrix);
        Matrix q = new Matrix(query);

        SingularValueDecomposition svd = matrix.svd();

        Matrix u = svd.getU();
        Matrix s = svd.getS();
        Matrix vt = svd.getV().transpose();

        Matrix uk = u.getMatrix(0, termsCount - 1, 0, K - 1);
        Matrix sk = s.getMatrix(0, K - 1, 0, K - 1);
        Matrix vtk = vt.getMatrix(0, K - 1, 0, documentsCount - 1);

        Matrix result = q.times(uk).times(sk.inverse());

        for (int i = 0; i < documentsCount; i++) {
            Matrix d = vtk.getMatrix(0, K - 1, i, i).transpose();
            System.out.println(articleIds[i] + ": " + cosine(result.getArrayCopy()[0], d.getArrayCopy()[0]));
        }
    }

    public static double cosine(double[] q, double[] d) {
        double a2 = 0;
        double b2 = 0;
        double ab = 0;
        for (int i = 0; i < K; i++) {
            ab += (q[i] * d[i]);
            b2 += (q[i] * q[i]);
            a2 += (d[i] * d[i]);
        }
        return ab / Math.sqrt(a2) / Math.sqrt(b2);
    }

    public String[] getArticleIds() {
        return articleIds;
    }

    public String[] getTerms() {
        return terms;
    }

    public double[][] getMatrix() {
        return matrix;
    }
}
