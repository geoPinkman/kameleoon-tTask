package org.tTask.services;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.tTask.models.JUser;

@Service
@AllArgsConstructor
public class UserService {

    private final DSLContext dslContext;

    public JUser createUser() {
        return null;
    }

    public JUser getUserById() {
        return null;

    }

    public JUser updateUser() {
        return null;

    }

    public void deleteUser() {


    }

}
