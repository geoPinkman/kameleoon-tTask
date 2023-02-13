package org.tTask.services;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.UpdateQuery;
import org.springframework.stereotype.Service;
import org.tTask.models.JQuote;
import static org.testTask.database.tables.Quotes.QUOTES;
import static org.testTask.database.tables.Users.USERS;
import static org.testTask.database.tables.Votes.VOTES;
import org.testTask.database.tables.records.QuotesRecord;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QuoteService {

    private final DSLContext dslContext;
    private final VoteService voteService;

    public JQuote create(int userId, String content) {
        int id = dslContext.insertInto(QUOTES)
                .set(QUOTES.CREATED_AT, Instant.now().atOffset(ZoneOffset.UTC))
                .set(QUOTES.UPDATED_AT, Instant.now().atOffset(ZoneOffset.UTC))
                .set(QUOTES.USER_ID, userId)
                .set(QUOTES.VOTE_ID, voteService.create())
                .set(QUOTES.CONTENT, content)
                .execute();

        return getQuoteById(id);
    }

    private JQuote parseQuote(Record r) {
        return JQuote.builder()
                .createdAt(r.get(QUOTES.CREATED_AT).toString())
                .userName(r.get(USERS.NAME))
                .content(r.get(QUOTES.CONTENT))
                .like(r.get(VOTES.LIKE))
                .dislike(r.get(VOTES.DISLIKE))
                .build();

    }

    private int getVoteIdByQuoteId(int id) {
        return dslContext.select(VOTES.ID)
                .from(VOTES)
                .where(VOTES.ID.eq(id))
                .fetchOptional()
                .map(Record1::value1)
                .orElseThrow(() -> new RuntimeException("Some wrong"));
    }

    public JQuote getQuoteById(int id) {
        return dslContext.select(QUOTES.CREATED_AT, QUOTES.CONTENT, USERS.NAME, VOTES.LIKE, VOTES.DISLIKE)
                .from(QUOTES)
                .join(USERS).on(QUOTES.USER_ID.eq(USERS.ID))
                .join(VOTES).on(QUOTES.VOTE_ID.eq(VOTES.ID))
                .where(QUOTES.ID.eq(id))
                .fetchOptional()
                .map(this::parseQuote)
                .orElseThrow(() -> new RuntimeException("Some wrong"));
    }

    public JQuote.JQuotes getBestQuotesByUserId(int userId) {
        return new JQuote.JQuotes(dslContext.select(QUOTES.CREATED_AT, QUOTES.CONTENT, USERS.NAME, VOTES.LIKE, VOTES.DISLIKE)
                .from(QUOTES)
                .join(USERS).on(QUOTES.USER_ID.eq(USERS.ID))
                .join(VOTES).on(QUOTES.VOTE_ID.eq(VOTES.ID))
                .where(USERS.ID.eq(userId))
                .orderBy(VOTES.LIKE.desc())
                .fetchStream()
                .map(this::parseQuote)
                .collect(Collectors.toUnmodifiableList()));
    }

    public JQuote.JQuotes getWorstQuotesByUserId(int userId) {
        return new JQuote.JQuotes(dslContext.select(QUOTES.CREATED_AT, QUOTES.CONTENT, USERS.NAME, VOTES.LIKE, VOTES.DISLIKE)
                .from(QUOTES)
                .join(USERS).on(QUOTES.USER_ID.eq(USERS.ID))
                .join(VOTES).on(QUOTES.VOTE_ID.eq(VOTES.ID))
                .where(USERS.ID.eq(userId))
                .orderBy(VOTES.DISLIKE.desc())
                .fetchStream()
                .map(this::parseQuote)
                .collect(Collectors.toUnmodifiableList()));
    }

    public JQuote updateQuote(int userId, int quoteId, String content) {
        UpdateQuery<QuotesRecord> q = dslContext.updateQuery(QUOTES);
        q.addValue(QUOTES.CONTENT, content);
        q.addValue(QUOTES.UPDATED_AT, Instant.now().atOffset(ZoneOffset.UTC));
        q.addConditions(QUOTES.ID.eq(quoteId)
                .and(QUOTES.USER_ID.eq(userId)));
        q.execute();
        return getQuoteById(quoteId);
    }

    public JQuote makeVote(int quoteId, boolean like) {
        voteService.updateVoteById(getVoteIdByQuoteId(quoteId), like);
        return getQuoteById(quoteId);
    }

    public void deleteQuote(int id) {
        dslContext.delete(QUOTES)
                .where(QUOTES.ID.eq(id));
    }

}
