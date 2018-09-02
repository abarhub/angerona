package org.abarhub.angerona.json;

import com.google.gson.*;
import org.bouncycastle.util.encoders.Base64Encoder;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.Date;

public class ByteArraySerializer implements JsonSerializer<byte[]> {

	public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
		if (src == null) {
			return JsonNull.INSTANCE;
		}
		return new JsonPrimitive(Base64.getEncoder().encodeToString(src));
	}
}
