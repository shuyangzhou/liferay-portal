package com.liferay.portal.util;

public class ExCatch {
	
	private long time;
	private Exception e;
	private String id;
	private long mvcc;
	private String key;
	private String className;
	private long threadId;
	
	
	public ExCatch(Exception e, String id, long mvcc, String className, long threadId) {
		super();
		this.e = e;
		this.id = id;
		this.mvcc = mvcc;
		this.className = className;
		this.threadId = threadId;
		this.time = System.currentTimeMillis();
		this.key = id;
	}
	
	public void print(){
		System.out.println("{id:" + id + " mvcc:" + mvcc + " class:" + className + " thread:" + threadId +
			" time:" + time + "}");
	}
	
	public String getKey(){
		return key;
	}
	
	public String getStringMVCC(){
		return mvcc + "";
	}
	
	public long getMVCC(){
		return mvcc;
	}
	
	public void printEx(){
		e.printStackTrace(System.out);
	}
	
	
	

}
