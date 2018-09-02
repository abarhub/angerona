package org.abarhub.angerona.core.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {


	@Override
	public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		if (json == null) {
			return null;
		}
		String str = json.getAsJsonPrimitive().getAsString();
		if (str == null || str.trim().isEmpty()) {
			return null;
		}
		return LocalDateTime.parse(str, DateTimeFormatter.ISO_DATE_TIME);
	}
}
