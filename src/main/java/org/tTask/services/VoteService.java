package org.tTask.services;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.UpdateQuery;
import org.tTask.models.JVote;

import static org.testTask.database.tables.Votes.VOTES;
import org.testTask.database.tables.records.VotesRecord;

@AllArgsConstructor
public class VoteService {

    private final DSLContext dslContext;

    private JVote parseVote(Record r) {
        return JVote.builder()
                .id(r.get(VOTES.ID))
                .like(r.get(VOTES.LIKE))
                .dislike(r.get(VOTES.DISLIKE))
                .build();
    }

    private int getLikes(int id) {
        return dslContext.select(VOTES.LIKE)
                .from(VOTES)
                .where(VOTES.ID.eq(id))
                .fetchOne()
                .value1();
    }

    private int getDislikes(int id) {
        return dslContext.select(VOTES.DISLIKE)
                .from(VOTES)
                .where(VOTES.ID.eq(id))
                .fetchOne()
                .value1();
    }

    public void create(){
        dslContext.insertInto(VOTES).defaultValues().execute();
    }

    public JVote getVoteById(int id) {
        return dslContext.selectFrom(VOTES)
                .where(VOTES.ID.eq(id))
                .fetchOptional()
                .map(this::parseVote)
                .orElseThrow(() -> new RuntimeException("Some wrong"));
    }

    public JVote updateVoteById(int id, boolean like) {
        UpdateQuery<VotesRecord> q = dslContext.updateQuery(VOTES);
        if (like) {
            q.addValue(VOTES.LIKE, getLikes(id) + 1);
        } else {
            q.addValue(VOTES.DISLIKE, getDislikes(id) + 1);
        }
        q.addConditions(VOTES.ID.eq(id));
        q.execute();
        return getVoteById(id);
    }

    public void deleteVotesById(int id) {
        UpdateQuery<VotesRecord> q = dslContext.updateQuery(VOTES);
        q.addValue(VOTES.LIKE, 0);
        q.addValue(VOTES.DISLIKE, 0);
        q.addConditions(VOTES.ID.eq(id));
        q.execute();
    }



}
