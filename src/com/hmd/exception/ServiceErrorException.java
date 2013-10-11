package com.hmd.exception;

public class ServiceErrorException extends Exception {

	private static final long serialVersionUID = -7359568581865095930L;
	
	public ServiceErrorException(String message){
		super(message);
	}

}
