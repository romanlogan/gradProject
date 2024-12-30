package com.gradproject.commentservice.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gradproject.commentservice.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class KafkaConsumer {

    CommentRepository commentRepository;

    @Autowired
    public KafkaConsumer(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


//    @KafkaListener(topics = "getCommentList-topic")
    public void getCommentList(String kafkaMessage) {
        log.info("Kafka Message: ->" + kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

//  get Comment list logic

//        CatalogEntity entity = repository.findByProductId((String)map.get("productId"));
//        if (entity != null) {
//            entity.setStock(entity.getStock() - (Integer)map.get("qty"));
//            repository.save(entity);
//        }
    }
}
