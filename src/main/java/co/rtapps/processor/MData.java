package co.rtapps.processor;

import lombok.Data;

import java.util.Map;

@Data
public class MData {
    /*
    {"database":"maxwelltest","table":"Persons","type":"insert","ts":1602904030,"xid":17358,"commit":true,"data":{"PersonId":1,"LastName":"Erichsen","FirstName":"Tom","City":"Stavanger"}}
     */
    private String database;
    private String table;
    private String type;
    private String serverId;
    private long ts;
    private int xid;
    private boolean commit;
    private Map<String, Object> data;

    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("database:").append(database)
    	.append(", table:").append(table)
    	.append(", type:").append(type)
    	.append(", serverId:").append(serverId)
    	.append(", ts:").append(ts)
    	.append(", xid:").append(xid)
    	.append(", commit:").append(commit);
    	
    	return sb.toString();
    }
    
}
