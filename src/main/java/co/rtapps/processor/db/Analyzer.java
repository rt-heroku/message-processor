package co.rtapps.processor.db;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import co.rtapps.processor.repositories.GenericTableRepository;
import co.rtapps.processor.util.MetadataExtractor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Analyzer {

	@Autowired
	GenericTableRepository genericTableRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void printTableInfo(String db, String table) {
		try {
			MetadataExtractor metadataExtractor = new MetadataExtractor(jdbcTemplate);
			
			log.info("Analyzing Table: " + db + "."+ table);
			
			metadataExtractor.extractDatabaseInfo();
			metadataExtractor.extractSupportedFeatures();
			metadataExtractor.extractUserName();
			
			metadataExtractor.extractSystemTables();
			
			metadataExtractor.extractColumnInfo(db, table);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void analyzeTable() {
		
	}
	
}
