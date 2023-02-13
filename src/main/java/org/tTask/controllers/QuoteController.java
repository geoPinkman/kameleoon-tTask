package org.tTask.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tTask.services.QuoteService;

@AllArgsConstructor
@RestController
@RequestMapping("api/quotes")
public class QuoteController {

    private final QuoteService quoteService;

    public void createQuote() {
        quoteService.equals(Object.class);

    }

}
