package org.barden.util;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Debug {

	static ConcurrentLinkedQueue<DebugEvent> events = new ConcurrentLinkedQueue<DebugEvent>();
	
	public static void logInfo(String name, String desc){
		DebugEvent event = new DebugEvent(DebugEvent.Type.INFO, name, desc, Thread.currentThread().getName());
		log(event);
	}
	
	public static void logWarning(String name, String desc){
		DebugEvent event = new DebugEvent(DebugEvent.Type.WARNING, name, desc, Thread.currentThread().getName());
		log(event);
	}
	
	public static void logBug(String name, String desc){
		DebugEvent event = new DebugEvent(DebugEvent.Type.BUG, name, desc, Thread.currentThread().getName());
		log(event);
	}
	
	public static void log(DebugEvent event){
		events.add(event);
	}
	
	public static void print(){
		int n = events.size();
		for(int i = 0; i < n; i++){
			DebugEvent event = events.poll();
			System.out.println("[" + event.timestamp + "][THREAD=" + event.threadName + "][" + event.type.toString() + "] " + event.name + " -> " + event.description);
		}
	}
	
}
