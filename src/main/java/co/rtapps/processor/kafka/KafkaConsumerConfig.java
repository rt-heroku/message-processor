package co.rtapps.processor.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import co.rtapps.processor.message.MessageKey;
import co.rtapps.processor.message.MessagePayload;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, String> consumerStringFactory() {
        return new DefaultKafkaConsumerFactory<>(
        		new KafkaConfig().buildConsumerDefaultsForGroup("listener2")
        		);
    }

    @Bean
    public ConsumerFactory<MessageKey, MessagePayload> consumerFactory() {
    	return new DefaultKafkaConsumerFactory<>(
    			new KafkaConfig().buildConsumerDefaultsForGroup("test"), 
    			new JsonDeserializer<>(MessageKey.class), 
    			new JsonDeserializer<>(MessagePayload.class)
    			);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<MessageKey, MessagePayload> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<MessageKey, MessagePayload> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerStringContainerFactory() {
    	ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    	factory.setConsumerFactory(consumerStringFactory());
    	
    	return factory;
    }

}
