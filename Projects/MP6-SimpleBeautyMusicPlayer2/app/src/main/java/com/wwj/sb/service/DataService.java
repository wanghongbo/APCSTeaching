package com.wwj.sb.service;

import java.util.ArrayList;
import java.util.List;

public class DataService {
	public static List<String> getData(int offset, int maxResult) {
		List<String> data = new ArrayList<String>();
		for(int i = 0; i < 20; i++) {
			data.add("���Ǹ���" + i);
		}
		return data;
	}
}
