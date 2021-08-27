package co.rtapps.processor.kafka;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import co.rtapps.processor.MessageData;
import co.rtapps.processor.MessageKey;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaConsumer {

    public static final String TOPIC = "tombigbee-4880.101";

    public final List<String> messages = new ArrayList<>();

    //@KafkaListener(topics = TOPIC)
    public void receiveString(ConsumerRecord<String, String> consumerRecord) {
        messages.add(consumerRecord.value());

        String env = System.getenv("DEBUG_ALL_RECORDS");
        
        if (env !=null && env.equals("TRUE"))
        	log.info("Received payload: '{}'", consumerRecord.toString());
        
        if (messages.size() % 100 == 0) {
        	log.info("Received payload: '{}'", consumerRecord.toString());
        	log.info("Number of messages processed: " + messages.size());
        }
    }

    @KafkaListener(topics = TOPIC)
    public void receive(MessageKey mkey, MessageData mdata) {
    	log.info("Received payload: '{}'", mdata.toString());
    	messages.add(mdata.toString());
    }

}
