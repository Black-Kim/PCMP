package util;

import java.util.Scanner;

public class ScanUtil {
	
	private static Scanner s= new Scanner(System.in);
	
	//유틸리티 성향의 메서드인 경우 static을 붙인다
	public static String nextLine(){
		return s.nextLine();
	}
	
	
	public static int nextInt(){
//		Math.random()
//		Math.round(a)
		return Integer.parseInt(s.nextLine());
	}
	

}
