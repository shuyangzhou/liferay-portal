package com.liferay.portal.util;

import java.util.HashMap;

public class IdExCatch {
	
	private HashMap<String, ExCatch> catches;
	private long highest = 0;
	
	
	public IdExCatch(ExCatch ex) {
		super();
		catches = new HashMap<String, ExCatch>();
		catches.put(ex.getStringMVCC(), ex);
	}
	
	public void put(ExCatch ex){

		if(ex.getMVCC() >= highest){
			catches.put(ex.getStringMVCC(), ex);
			highest = ex.getMVCC();
		}else{
			System.out.println("-------------------------->SOMEONE IS TRYING TO ADD MVCC STALE!!!!!");
			System.out.println("-INCOMING-");
			ex.print();
			ex.printEx();
			System.out.println("-VS-");
			catches.get(ex.getStringMVCC()).print();
			catches.get(ex.getStringMVCC()).printEx();
		}
	}
	
	public long getHighest(){
		return highest;
	}
		

}
