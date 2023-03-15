package io.brandy.realworld.domain.article.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleQueryParam extends FeedParams {
    private String tag;
    private String author;
    private String favorite;
}
