package io.brandy.realworld.domain.article.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedParams {
    protected Integer offset;
    protected Integer limit;

    @AssertTrue
    protected boolean getVaildPage() {
        return (offset != null && limit != null) || (offset == null && limit == null);
    }
}
