package org.tTask.services;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuoteService {

    private final DSLContext dslContext;
    private final UserService userService;

    


}
