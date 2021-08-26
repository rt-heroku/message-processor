package co.rtapps.processor.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    @Value("${kafka.trustedCert:environment.KAFKA_TRUSTED_CERT}")
    private String trustedCert;

    @Value("${kafka.clientCert:environment.KAFKA_CLIENT_CERT}")
    private String clientCert;

    @Value("${kafka.clientKey:environment.KAFKA_CLIENT_CERT_KEY}")
    private String clientKey;

    @Value("${kafka.url:environment.KAFKA_URL}")
    private String kafkaUrl;

    @Value("${kafka.group}")
    private String kafkaGroup;

    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(new KafkaConfig(kafkaUrl, trustedCert, clientCert, clientKey, kafkaGroup).buildConsumerDefaults());
    }

    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }

}
