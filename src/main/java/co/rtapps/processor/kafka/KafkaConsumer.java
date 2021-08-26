package co.rtapps.processor.kafka;

import java.util.ArrayList;
import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import co.rtapps.processor.MData;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaConsumer {

    public static final String TOPIC = "tombigbee-4880.101";

    public final List<MData> messages = new ArrayList<>();

//    @KafkaListener(topics = TOPIC)
//    public void receive(ConsumerRecord<String, String> consumerRecord) {
//        log.info("Received payload: '{}'", consumerRecord.toString());
//        messages.add(consumerRecord.value());
//    }
    @KafkaListener(topics = TOPIC)
    public void receive(MData consumerRecord) {
    	log.info("Received payload: '{}'", consumerRecord.toString());
    	messages.add(consumerRecord);
    }

}
