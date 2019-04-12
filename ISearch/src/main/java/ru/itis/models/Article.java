package ru.itis.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {
    private String id;
    private String title;
    private String keywords;
    private String content;
    private String url;
    private int studentId;
}
