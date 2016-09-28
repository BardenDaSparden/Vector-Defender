package org.barden.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DebugEvent {

	public enum Type{
		INFO(),
		WARNING(), 
		BUG()
	}
	
	protected Type type;
	protected String name;
	protected String description;
	protected String timestamp;
	protected String threadName;
	
	public DebugEvent(Type type, String name, String description, String threadName){
		this.type = type;
		this.name = name;
		this.description = description;
		this.timestamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		this.threadName = threadName;
	}
	
}
