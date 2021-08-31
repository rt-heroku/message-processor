package co.rtapps.processor.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MessageKey {
    private String database;
    private String table;
    @JsonProperty("pk.id")
    private String pkid;
   
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("database:").append(database)
    	.append(", table:").append(table)
    	.append(", pkid:").append(pkid)
    	;
    	
    	return sb.toString();
    }
    

}
