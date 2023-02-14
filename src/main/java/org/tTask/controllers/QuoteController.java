package org.tTask.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.tTask.models.JQuote;
import org.tTask.services.QuoteService;

@AllArgsConstructor
@RestController
@RequestMapping("api/quotes")
public class QuoteController {

    private final QuoteService quoteService;

    @PostMapping("/{userId}")
    public JQuote createQuote(@PathVariable int userId, @RequestParam String content) {
        return quoteService.create(userId, content);
    }

    @GetMapping("/{userId}")
    public JQuote.JQuotes readByUser(@PathVariable int userId) {
        return quoteService.getQuotesByUserId(userId);
    }

    @GetMapping("/bestOf/{userId}")
    public JQuote.JQuotes getBestQuotes(@PathVariable int userId, @RequestParam int numberOfRaws) {
        return quoteService.getBestQuotesByUserId(userId, numberOfRaws);
    }

    @GetMapping("/worstOf/{userId}")
    public JQuote.JQuotes getWorstQuotes(@PathVariable int userId, @RequestParam int numberOfRaws) {
        return quoteService.getWorstQuotesByUserId(userId, numberOfRaws);
    }

    @PatchMapping("/quote/{quoteId}")
    public JQuote update(@RequestParam int userId, @PathVariable int quoteId, @RequestParam String content) throws Exception {
        try {
            return quoteService.updateQuote(userId, quoteId, content);
        } catch (Exception e) {
            throw new Exception("Some wrong");
        }
    }

    @PatchMapping("/like/{quoteId}")
    public JQuote makeVote(@PathVariable int quoteId, @RequestParam boolean like) {
        return quoteService.makeVote(quoteId, like);
    }


    @DeleteMapping("/delete/{quoteId}")
    public void delete(@PathVariable int quoteId, @RequestParam int userId) throws Exception {
        try {
            quoteService.deleteQuote(quoteId, userId);
        } catch (Exception e) {
            throw new Exception("Oops");
        }
    }

}
