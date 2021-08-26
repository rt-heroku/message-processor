package co.rtapps.processor.kafka;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import co.rtapps.processor.MData;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
        		new KafkaConfig().buildConsumerDefaults()
        		);
    }
//    @Bean
//    public ConsumerFactory<String, MData> consumerFactory() {
//    	return new DefaultKafkaConsumerFactory<>(
//    			new KafkaConfig().buildConsumerDefaults(), 
//    			new StringDeserializer(), 
//    			new JsonDeserializer<>(MData.class)
//    			);
//    }

//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, MData> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, MData> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//
//        return factory;
//    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
    	ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    	factory.setConsumerFactory(consumerFactory());
    	
    	return factory;
    }

}
