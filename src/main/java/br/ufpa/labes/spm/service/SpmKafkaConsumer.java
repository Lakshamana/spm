package br.ufpa.labes.spm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SpmKafkaConsumer {

    private final Logger log = LoggerFactory.getLogger(SpmKafkaConsumer.class);
    private static final String TOPIC = "topic_spm";

    @KafkaListener(topics = "topic_spm", groupId = "group_id")
    public void consume(String message) throws IOException {
        log.info("Consumed message in {} : {}", TOPIC, message);
    }
}
