package org.tTask.models;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class JUser {

    public final int id;

    public final String name;

    public final String email;

    public final String password;

    public final long createdAt;

}
