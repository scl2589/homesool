package com.ssafy.homesool.service;

import java.util.Random;

public class Util {
    final static char[] characterTable = {	'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 
            								'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 
            								'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
    final static int tablelength = characterTable.length;

	public static String getRandomCode() {
		Random random = new Random(System.currentTimeMillis());
		StringBuffer buf = new StringBuffer();
		
		for(int i = 0; i < 10; i++) {
			buf.append(characterTable[random.nextInt(tablelength)]);
	    }
        return buf.toString();
	}
}
