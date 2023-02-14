package org.tTask.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.UpdateQuery;
import org.springframework.stereotype.Service;
import org.tTask.models.JUser;
import org.tTask.utils.StringToBytes;
import static org.testTask.database.tables.Users.USERS;
import org.testTask.database.tables.records.UsersRecord;
import java.time.Instant;
import java.time.ZoneOffset;

@Service
@AllArgsConstructor
public class UserService {

    private final DSLContext dslContext;
    private final QuoteService quoteService;

    private JUser parseUser(Record r) {
        return JUser.builder()
                .id(r.get(USERS.ID))
                .name(r.get(USERS.NAME))
                .email(r.get(USERS.EMAIL))
                .createdAt(r.get(USERS.CREATED_AT).toString())
                .quotesList(quoteService.getQuotesByUserId(r.get(USERS.ID)))
                .build();
    }

    private Integer getUserIdByEmail(String email) {
        return dslContext.select(USERS.ID)
                .from(USERS)
                .where(USERS.EMAIL.eq(email))
                .fetchOne()
                .value1();
    }

    public JUser getUserByEmail(String email) {
        return dslContext.selectFrom(USERS)
                .where(USERS.EMAIL.eq(email))
                .fetchOptional()
                .map(this::parseUser)
                .orElseThrow(() -> new RuntimeException("Some wrong"));
    }

    public JUser createUser(QUser user) {
        String hPass = StringToBytes.getCodeString(user.password);

        int id = dslContext.insertInto(USERS)
                .set(USERS.NAME, user.name)
                .set(USERS.EMAIL, user.email)
                .set(USERS.PASSWORD, hPass)
                .set(USERS.CREATED_AT, Instant.now().atOffset(ZoneOffset.UTC))
                .execute();

        return getUserById(id);
    }

    public JUser getUserById(int id) {
        return dslContext.selectFrom(USERS)
                .where(USERS.ID.eq(id))
                .fetchOptional()
                .map(this::parseUser)
                .orElseThrow(() -> new RuntimeException("Some wrong"));
    }

    public JUser getUser(String email, String requestPassword) {
        String password = StringToBytes.getCodeString(requestPassword);
        return dslContext.selectFrom(USERS)
                .where(USERS.EMAIL.eq(email)
                        .and(USERS.PASSWORD.eq(password)))
                .fetchOptional()
                .map(this::parseUser)
                .orElseThrow(() -> new RuntimeException("User " + email + " not found"));
    }


    public JUser updateUser(QUser user) {
        if (user.email != null) {
            if (getUserIdByEmail(user.email) == null) {
                return createUser(user);
            } else {
                UpdateQuery<UsersRecord> q = dslContext.updateQuery(USERS);
                if (user.name != null) {
                    q.addValue(USERS.NAME, user.name);
                }
                if (user.password != null) {
                    q.addValue(USERS.PASSWORD, StringToBytes.getCodeString(user.password));
                }
                q.addConditions(USERS.EMAIL.eq(user.email));
                q.execute();
                return getUserByEmail(user.email);
            }
        } else {
            throw new RuntimeException("Field email must be not null");
        }
    }

    public void deleteUserById(int id) {
        dslContext.delete(USERS)
                .where(USERS.ID.eq(id))
                .execute();
    }

    @AllArgsConstructor
    @Builder
    public static class QUser {

        public final String name;

        public final String email;

        public final String password;
    }

}
