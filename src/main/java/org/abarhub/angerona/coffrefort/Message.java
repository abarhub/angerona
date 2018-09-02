package org.abarhub.angerona.coffrefort;

public class Message {

	private String message;
	private byte[] messageCrypte;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public byte[] getMessageCrypte() {
		return messageCrypte;
	}

	public void setMessageCrypte(byte[] messageCrypte) {
		this.messageCrypte = messageCrypte;
	}
}
