package org.tTask.models;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class JVote {

    public final int id;

    public final int like;

    public final int dislike;

}


