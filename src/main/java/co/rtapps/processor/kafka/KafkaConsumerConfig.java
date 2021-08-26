package co.rtapps.processor.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
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

    @Value("${kafka.group:group}")
    private String kafkaGroup;

    @Value("${kafka.prefix:environment.KAFKA_PREFIX}")
    private String kafkaPrefix;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(new KafkaConfig(kafkaUrl, trustedCert, clientCert, clientKey, kafkaGroup, kafkaPrefix).buildConsumerDefaults());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }

}
