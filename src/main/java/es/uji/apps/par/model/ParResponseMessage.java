package es.uji.apps.par.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParResponseMessage {
	private boolean result;
	private String message;
	
	public ParResponseMessage(boolean result, String message) {
		this.result = result;
		this.message = message;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public ParResponseMessage() {
		
	}
}
