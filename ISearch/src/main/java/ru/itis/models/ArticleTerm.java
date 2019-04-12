package ru.itis.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleTerm {
    private String articleId;
    private String termId;
    private double tfIdf;
    private double idf;
}
