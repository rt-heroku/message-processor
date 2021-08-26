package co.rtapps.processor.kafka;

import static java.lang.System.getenv;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import com.github.jkutner.EnvKeyStore;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KafkaConfig {
	private final String group;
	private final String prefix;

	public KafkaConfig() {
		this.prefix = checkNotNull(getenv("KAFKA_PREFIX"));
		
		if (checkNotNull(getenv("KAFKA_GROUP")).equals(""))
			this.group = "";
		else
			this.group = prefix + getenv("KAFKA_GROUP");
	}

	public Map<String, Object> buildConsumerDefaults() {
		Map<String, Object> properties = new HashMap<>();
		List<String> hostPorts = new ArrayList<>();

		buildDefaults(properties, hostPorts);

		System.out.println("buildConsumerDefaults -> Group: " + getGroup());
		System.out.println("buildConsumerDefaults -> Prefix: " + prefix);


		
		
		properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,
				hostPorts.stream().collect(Collectors.joining(",")));
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, getGroup());
		properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		return properties;
	}

	public Map<String, Object> buildProducerDefaults() {
		Map<String, Object> properties = new HashMap<>();
		List<String> hostPorts = new ArrayList<>();

		buildDefaults(properties, hostPorts);

		properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,
				hostPorts.stream().collect(Collectors.joining(",")));
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		return properties;
	}

	private void buildDefaults(Map<String, Object> properties, List<String> hostPorts) {
		String kafkaUrl = checkNotNull(getenv("KAFKA_URL"));
				
		for (String url : kafkaUrl.split(",")) {
			try {
				URI uri = new URI(url);
				hostPorts.add(String.format("%s:%d", uri.getHost(), uri.getPort()));

				switch (uri.getScheme()) {
				case "kafka":
					properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "PLAINTEXT");
					break;
				case "kafka+ssl":
					properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
					properties.put("ssl.endpoint.identification.algorithm", "");
					try {
						EnvKeyStore envTrustStore = EnvKeyStore.createWithRandomPassword("KAFKA_TRUSTED_CERT");
						EnvKeyStore envKeyStore = EnvKeyStore.createWithRandomPassword("KAFKA_CLIENT_CERT_KEY", "KAFKA_CLIENT_CERT");

						File trustStore = envTrustStore.storeTemp();
						File keyStore = envKeyStore.storeTemp();

						properties.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, envTrustStore.type());
						properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, trustStore.getAbsolutePath());
						properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, envTrustStore.password());
						properties.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, envKeyStore.type());
						properties.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, keyStore.getAbsolutePath());
						properties.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, envKeyStore.password());
					} catch (Exception e) {
						throw new RuntimeException("There was a problem creating the Kafka key stores", e);
					}
					break;
				default:
					throw new IllegalArgumentException(String.format("unknown scheme; %s", uri.getScheme()));
				}
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		}
		
		for (String h: hostPorts) 
			log.info("Server added: " + h);
		
		
	}

	private String checkNotNull(String val) {
		if (val == null)
			return "";
		
		return val;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getGroup() {
		return group;
	}

}
