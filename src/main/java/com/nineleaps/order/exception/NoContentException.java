package com.nineleaps.order.exception;

import org.springframework.http.HttpStatus;

public class NoContentException extends Exception{
	public String errorMessage;
	public HttpStatus httpStatus;
	public int code;
	public String piCode;
	public String errorCode;
	
	 public NoContentException(HttpStatus httpStatus) {
	        super();
	        this.httpStatus = httpStatus;
	        
	        
	    }
	 
	 public NoContentException(HttpStatus httpStatus, String errorMessage, String piCode,String errorCode) {
	        super();
	        this.httpStatus = httpStatus;
	        this.errorMessage = errorMessage;
	        this.piCode = piCode;
	        this.errorCode = errorCode;
	    }

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getPiCode() {
		return piCode;
	}

	public void setPiCode(String piCode) {
		this.piCode = piCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	 
	 

}
