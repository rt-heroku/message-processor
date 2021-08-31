package co.rtapps.processor.message;

import java.util.Optional;

import lombok.Data;

@Data
public class MessageData {

	private String key;
	private Object value;
	
	public MessageData(String key, Object value) {
		super();
		this.key = key;
		this.value = value;
	}
	
	public String getDataType() {
		String dataType;
		if (value != null)
			dataType = value.getClass().getSimpleName();
		else
			dataType = "NULL";
		
		return dataType;
	}
	
	public String getValueForInsert() {
		switch (getDataType()) {
			case "Integer":
			case "Double":
			case "Float":
			case "Boolean":
			case "Long":
				return Optional.ofNullable(value.toString()).orElse("").toString();
			case "NULL":
				return "null";
	
			default:
				return "'" + 
					Optional.ofNullable(value.toString()).orElse("").toString()
					+ "'";
		}
		
	}
	
    @Override
    public String toString() {
        return key + "[" + getDataType() + "]: " + value;
    }
	
}
