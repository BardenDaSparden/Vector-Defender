package org.javatroid.error;

public class UnloadedResourceException extends RuntimeException{

	private static final long serialVersionUID = 1L;	
	
	public UnloadedResourceException(String message){
		super(message);
	}
	
	public UnloadedResourceException(String message, Throwable throwable){
		super(message, throwable);
	}
	
}
