package com.example.validatorservice.persistence.repository;

import com.example.validatorservice.persistence.model.OutboxEvent;
import com.example.validatorservice.persistence.model.OutboxEventStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OutboxRecordRepositoryCustomImpl implements OutboxRecordRepositoryCustom {

    private final MongoTemplate mongoTemplate;
    private static final String CREATED = "createdAt";
    private static final String EVENT_STATUS = "eventStatus";
    private static final String EVENT_ID = "_id";

    public List<OutboxEvent> selectForProcessing(Set<String> statuses, int batchSize) {
        List<OutboxEvent> result = new ArrayList<>();

        for (int i = 0; i < batchSize; i++) {
            Query query = new Query()
                    .addCriteria(Criteria.where(EVENT_STATUS).in(statuses))
                    .with(Sort.by(Sort.Direction.ASC, CREATED))
                    .limit(1);

            Update update = new Update().set(EVENT_STATUS, OutboxEventStatus.PROCESSING.getStatus());

            OutboxEvent updated = mongoTemplate.findAndModify(
                    query,
                    update,
                    FindAndModifyOptions.options().returnNew(true),
                    OutboxEvent.class
            );

            if (updated != null) {
                result.add(updated);
            } else {
                break;
            }
        }

        return result;
    }

    @Override
    public void updateStatus(String id, String status) {
        Query query = new Query(Criteria.where(EVENT_ID).is(id));
        Update update = new Update().set(EVENT_STATUS, status);
        mongoTemplate.updateFirst(query, update, OutboxEvent.class);
    }
}