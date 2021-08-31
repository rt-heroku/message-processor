package co.rtapps.processor.message;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import co.rtapps.processor.util.MessageUtils;
import lombok.Data;

@Data
public class MessagePayload {
    /*
    {"database":"maxwelltest","table":"Persons","type":"insert","ts":1602904030,"xid":17358,"commit":true,"data":{"PersonId":1,"LastName":"Erichsen","FirstName":"Tom","City":"Stavanger"}}
key = {"database":"av3","table":"testsamples","pk.id":1790036}, value = {"database":"av3","table":"testsamples","type":"insert","ts":1630016099,"xid":24770,"xoffset":997,"server_id":1,"data":{"Id":1790036,"RunId":4,"LaneId":"*","SampNo":980,"SampTime":"2020-11-22 01:08:11","nDetected":0,"nPassed":0,"nMarginal":0,"nRejected":0,"WidthAverage":0.0,"WidthFirst":0.0,"WidthLast":0.0,"WidthMin":0.0,"WidthMax":0.0,"HeightAverage":0.0,"HeightFirst":0.0,"HeightLast":0.0,"HeightMin":0.0,"HeightMax":0.0,"DMajorAverage":0.0,"DMajorFirst":0.0,"DMajorLast":0.0,"DMajorMin":0.0,"DMajorMax":0.0,"DMinorAverage":0.0,"DMinorFirst":0.0,"DMinorLast":0.0,"DMinorMin":0.0,"DMinorMax":0.0,"DAvgAverage":0.0,"DAvgFirst":0.0,"DAvgLast":0.0,"DAvgMin":0.0,"DAvgMax":0.0,"EFAverage":0.0,"EFFirst":0,"EFLast":0,"EFMin":0,"EFMax":0,"EDAverage":0.0,"EDFirst":0,"EDLast":0,"EDMin":0,"EDMax":0,"HAAverage":0.0,"HAFirst":0.0,"HALast":0.0,"HAMin":0.0,"HAMax":0.0,"ShapeAverage":0.0,"ShapeFirst":0.0,"ShapeLast":0.0,"ShapeMin":0.0,"ShapeMax":0.0,"ToastAverage":0.0,"ToastFirst":0,"ToastLast":0,"ToastMin":0,"ToastMax":0,"RawAverage":0.0,"RawFirst":0,"RawLast":0,"RawMin":0,"RawMax":0,"TransAverage":0.0,"TransFirst":0,"TransLast":0,"TransMin":0,"TransMax":0,"LanePosAverage":0.0,"LanePosFirst":0.0,"LanePosLast":0.0,"LanePosMin":0.0,"LanePosMax":0.0,"nDMajorMin":0,"nDMajorMax":0,"nDMinorMin":0,"nDMinorMax":0,"nDAvgMin":0,"nDAvgMax":0,"nEFMax":0,"nEDMax":0,"nHAMax":0,"nShapeMax":0,"nRawMax":0,"nTransMax":0,"nLaneMin":0,"nLaneMax":0,"SyncUp":null}}
     *
     */
    private String database;
    private String table;
    private String type;
    
    @JsonProperty("server_id")
    private String serverId;
    
    private Long ts;
    private Integer xid;
    private Boolean commit;
    private Long xoffset;
    
    private Map<String, Object> data;

    @JsonIgnore
    private List<MessageData> mdata;
    
    public void setData(Map<String, Object> data) {
    	this.data = data;
    	mdata = MessageUtils.mapToListOfMessageData(data);
    }
    
    public List<MessageData> getMessageData(){
    	return MessageUtils.mapToListOfMessageData(data);
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("database:").append(database)
    	.append(", table:").append(table)
    	.append(", type:").append(type)
    	.append(", serverId:").append(serverId)
    	.append(", ts:").append(ts)
    	.append(", xid:").append(xid)
    	.append(", xoffset:").append(xoffset)
    	.append(", commit:").append(commit)
    	.append(", data: [")
    	.append(MessageUtils.printMap(data))
    	.append("]")
    	;
    	
    	
    	return sb.toString();
    }
    
}
