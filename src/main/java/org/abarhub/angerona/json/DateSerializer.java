package org.abarhub.angerona.json;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Date;

public class DateSerializer implements JsonSerializer<Date> {

	public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
		if (src == null) {
			return JsonNull.INSTANCE;
		}
		return new JsonPrimitive(src.getTime());
	}
}
