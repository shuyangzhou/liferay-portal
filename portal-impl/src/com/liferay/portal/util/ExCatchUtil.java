package com.liferay.portal.util;

import java.util.HashMap;

public class ExCatchUtil {
	
	private static ExCatchUtil instance;
	private static HashMap<String, IdExCatch> catches;
	
	private ExCatchUtil(){
		
		catches = new HashMap<String, IdExCatch>();
		
	}
	
	public static ExCatchUtil getInstance(){
		
		if(instance == null){
			instance = new ExCatchUtil();
		}
		
		return instance;
	}
	
	public static void add(ExCatch exCatch){
		
		if(contains(exCatch)){
			IdExCatch existing = get(exCatch.getKey());
			existing.put(exCatch);
		}else{
			IdExCatch newIdEx = new IdExCatch(exCatch);
			getInstance().catches.put(exCatch.getKey(), newIdEx);
			//System.out.println("SAVING:");
		}
		
		//exCatch.print();
	}
	
	public static boolean contains(ExCatch exCatch){
		return getInstance().catches.containsKey(exCatch.getKey());
	}
	
	public static IdExCatch get(String key){
		return getInstance().catches.get(key);
	}
	
}
