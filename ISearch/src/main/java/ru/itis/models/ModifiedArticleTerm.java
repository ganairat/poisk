package ru.itis.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifiedArticleTerm {
    private String articleUrl;
    private String term;
    private double tfIdf;
    private double idf;
}
