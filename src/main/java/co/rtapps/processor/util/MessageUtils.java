package co.rtapps.processor.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.rtapps.processor.message.MessageData;

public class MessageUtils {

	
	public static String printMap (Map<String,Object> map) {
		
		String t = "";
		String name = "";
		for (String k : map.keySet()) {
			Object value = map.get(k);
			if (value != null)
				name = value.getClass().getSimpleName();
			else
				name = "NULL";
			t = t + k + "[" + name  + "]"+ ":" + value + ",";
		}
		
		return t;
	}

	public static List<MessageData> mapToListOfMessageData (Map<String,Object> map) {
		return map.entrySet()
		  .stream()
		  .map(m->new MessageData(m.getKey(), m.getValue()))
		  .collect(Collectors.toCollection(ArrayList::new));
	}
	
}
