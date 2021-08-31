package co.rtapps.processor.repositories;

import java.sql.PreparedStatement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class GenericTableRepository {
	@Autowired
    JdbcTemplate jdbcTemplate;

    public void insert(final String insertSQL) throws DataAccessException {
        
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertSQL);
            return ps;
        }, keyHolder);

//        return (long) keyHolder.getKey();
    }
	
    public void execute(final String sql) {
    	
    	jdbcTemplate.execute(sql);
    	
    }
    
}
