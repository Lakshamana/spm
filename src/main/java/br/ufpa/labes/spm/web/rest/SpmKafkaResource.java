package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.SpmKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/spm-kafka")
public class SpmKafkaResource {

    private final Logger log = LoggerFactory.getLogger(SpmKafkaResource.class);

    private SpmKafkaProducer kafkaProducer;

    public SpmKafkaResource(SpmKafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        log.debug("REST request to send to Kafka topic the message : {}", message);
        this.kafkaProducer.sendMessage(message);
    }
}
