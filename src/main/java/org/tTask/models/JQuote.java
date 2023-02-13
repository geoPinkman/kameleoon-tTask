package org.tTask.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.List;

@AllArgsConstructor
@Builder
public class JQuote {

    public final String createdAt;

    public final String userName;

    public final String content;

    public final int like;

    public final int dislike;

    @AllArgsConstructor
    public static class JQuotes {

        public final List<JQuote> quoteList;
    }
}
