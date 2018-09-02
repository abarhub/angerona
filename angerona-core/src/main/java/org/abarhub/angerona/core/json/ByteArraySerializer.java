package org.abarhub.angerona.core.json;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Base64;

public class ByteArraySerializer implements JsonSerializer<byte[]> {

	public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
		if (src == null) {
			return JsonNull.INSTANCE;
		}
		return new JsonPrimitive(Base64.getEncoder().encodeToString(src));
	}
}
