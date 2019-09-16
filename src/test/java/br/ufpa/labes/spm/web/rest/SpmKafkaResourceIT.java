package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.service.SpmKafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class SpmKafkaResourceIT {

  @Autowired private SpmKafkaProducer kafkaProducer;

  private MockMvc restMockMvc;

  @BeforeEach
  public void setup() {
    SpmKafkaResource kafkaResource = new SpmKafkaResource(kafkaProducer);

    this.restMockMvc = MockMvcBuilders.standaloneSetup(kafkaResource).build();
  }

  @Test
  public void sendMessageToKafkaTopic() throws Exception {
    restMockMvc.perform(post("/api/spm-kafka/publish?message=yolo")).andExpect(status().isOk());
  }
}
