package net.mad.ads.base.api.service.adserver.services;

import java.util.Map;

public class Calculator {
	public int add(int i1, int i2) {
		return i1 + i2;
	}

	public int subtract(int i1, int i2) {
		return i1 - i2;
	}
	
	public int add (Map<String, Object> parameters) {
		return (Integer)parameters.get("x") + (Integer)parameters.get("y");
	}
}
