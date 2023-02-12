package org.tTask.configs;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Configuration;
import org.tTask.services.QuoteService;
import org.tTask.services.UserService;

@Configuration
public class ApplicationConfiguration {

    public QuoteService quoteService(DSLContext dslContext, UserService userService) {

        return new QuoteService(dslContext, userService);
    }

}
