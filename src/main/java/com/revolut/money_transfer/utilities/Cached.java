package com.revolut.money_transfer.utilities;

import java.util.HashMap;

public class Cached {
	private static HashMap<Integer, String> currencyMap = new HashMap<Integer, String>();
	
	static{
		currencyMap.put(840, "USD");
		currencyMap.put(826, "GBP");
		currencyMap.put(978, "EUR");
		currencyMap.put(949, "TRY");
		currencyMap.put(756, "CHF");
	}

	public static String getCurrency(int iso){
		return currencyMap.get(iso);
	}
	
	public static boolean isCurrency(int iso){
		return currencyMap.containsKey(iso); 
	}
	
	public static String currencyList(){
		return currencyMap.keySet().toString();
	}
}
