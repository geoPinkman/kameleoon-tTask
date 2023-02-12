package org.tTask.models;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class JQuote {

    public final int id;

    public final String createdAt;

    public final String updatedAt;

    public final String content;

    public final JUser user;

    public final JVote vote;
}
