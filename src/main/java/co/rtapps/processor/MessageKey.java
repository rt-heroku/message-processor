package co.rtapps.processor;

import lombok.Data;

@Data
public class MessageKey {
    private String database;
    private String table;
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
