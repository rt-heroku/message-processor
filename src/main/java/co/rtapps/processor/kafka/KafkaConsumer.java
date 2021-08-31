package co.rtapps.processor.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import co.rtapps.processor.db.Analyzer;
import co.rtapps.processor.message.MessageData;
import co.rtapps.processor.message.MessageKey;
import co.rtapps.processor.message.MessagePayload;
import co.rtapps.processor.repositories.GenericTableRepository;
import co.rtapps.processor.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaConsumer {

	@Autowired
	GenericTableRepository genericTableRepository;
	
	@Autowired
	Analyzer analyzer;
	

    public final List<String> messages = new ArrayList<>();
    public final List<String> failedMessages = new ArrayList<>();

    @KafkaListener(topics = "tombigbee-4880.101", clientIdPrefix = "string",
            containerFactory = "kafkaListenerStringContainerFactory")
    public void receiveString(ConsumerRecord<MessageKey, MessagePayload> consumerRecord) {
        log.info("LISTENER1 - Action:" + consumerRecord.value().getType());
    	log.info("LISTENER1 - Received key: '{}' Payload:'{}'", consumerRecord.key().toString(), consumerRecord.value().toString());
    }

    @KafkaListener(topics = "tombigbee-4880.101", clientIdPrefix = "string",
            containerFactory = "kafkaListenerStringContainerFactory")
    public void receiveAnotherString(ConsumerRecord<MessageKey, MessagePayload> consumerRecord) {
    	log.info("LISTENER2 - Action:" + consumerRecord.value().getType());
		log.info("LISTENER2 - Received key: '{}' Payload:'{}'", consumerRecord.key().toString(), consumerRecord.value().toString());
    }

    @KafkaListener(topics = "tombigbee-4880.part-data", clientIdPrefix = "json",
            containerFactory = "kafkaListenerContainerFactory")
    public void receive(ConsumerRecord<MessageKey , MessagePayload> payload) {
    	
    	if (payload.value().getType().equals("insert")) {
	    	String sql = getInsertStatement(payload.value());
	
	    	if (log.isDebugEnabled())
	    		debugEntry(payload, sql);
	    	try {
	    		genericTableRepository.insert(sql);
	    		messages.add(sql);
	    	}catch (DataAccessException e) {
	    		log.error(e.getLocalizedMessage());
	    		failedMessages.add(sql);
			}
	    	
	    	if (((messages.size() + failedMessages.size()) % 100) == 0){
	    		log.info("Succesful Messages:" + messages.size() + " - Failed Messages:" + failedMessages.size());
	    	}
    	}else {
    		log.info("Action [" + payload.value().getType() + " NOT supported!");
    	}
    }

	private void debugEntry(ConsumerRecord<MessageKey, MessagePayload> payload, String sql) {
		List<MessageData> list =  MessageUtils.mapToListOfMessageData(payload.value().getData());
    	log.debug("Received key: '{}'", payload.key().toString());
    	log.debug("Received payload: '{}'", payload.value().toString());
    	log.debug("List:" + list.toString());
    	log.debug("SQL: " + sql);
	}

    
    public String getInsertStatement(MessagePayload record) {
    	String s = "INSERT INTO %s (%s) VALUES (%s)";
    	
    	String table = record.getDatabase() + "." + record.getTable();
    	String fields = "";
    	String values = "";
    	List<MessageData> data = record.getMessageData();

		fields = fields.concat("server_id").concat(",");
		values = values.concat(
				Optional.ofNullable(record.getServerId()).orElse("").toString()
				).concat(",");

		fields = fields.concat("ts").concat(",");
		values = values.concat(
				Optional.ofNullable(record.getTs()).orElse(0L).toString()
				).concat(",");

		fields = fields.concat("xid").concat(",");
		values = values.concat(
				Optional.ofNullable(record.getXid()).orElse(0).toString()
				).concat(",");

		fields = fields.concat("xoffset").concat(",");
		values = values.concat(
					Optional.ofNullable(record.getXoffset()).orElse(0L).toString()
				).concat(",");

    	for (MessageData m : data) {
    		String field = m.getKey().toLowerCase();
    		
    		if (field.equals("id")) field = "sourceid";
    		
    		fields = fields.concat(field).concat(",");
    		values = values.concat(m.getValueForInsert()).concat(",");
    	}
    	
    	fields = removeLastComma(fields);
    	values = removeLastComma(values);
    	
    	s = String.format(s, table,fields,values);
    	
    	return s;
    }

	private String removeLastComma(String s) {
		// TODO Auto-generated method stub
		return Optional.ofNullable(s)
		        .filter(str -> str.length() != 0)
		        .map(str -> str.substring(0, str.length() - 1))
		        .orElse(s);
	}
}
