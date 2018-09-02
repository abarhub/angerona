package org.abarhub.angerona.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Base64;

public class ByteArrayDeserializer implements JsonDeserializer<byte[]> {

	@Override
	public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		if (json == null) {
			return null;
		}
		String str = json.getAsJsonPrimitive().getAsString();
		if (str == null || str.trim().isEmpty()) {
			return null;
		}
		return Base64.getDecoder().decode(str);
	}
}
