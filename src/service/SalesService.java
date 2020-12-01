package service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.SalseDao;
import util.ScanUtil;
import util.View;

public class SalesService {
	private SalesService() {
	}

	private static SalesService instance;

	public static SalesService getInstance() {
		if (instance == null) {
			instance = new SalesService();
		}
		return instance;
	}

	private SalseDao salseDao = SalseDao.getInstance();
	public int selectSales() {
		System.out.println("1.일 매출\t2.월 매출\t3.년 매출\t4.총 매출\t0.뒤로가기");
		System.out.println("입력 > ");
		
		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1: //일매출
			System.out.println("조회하고자 하는 일자를 입력하세요.\n예시)20201016");
			System.out.println("일자입력 >");
			String today = ScanUtil.nextLine();
			Map<String, Object> toda = new HashMap<>();
			toda.put("DATE", today);
			Map<String, Object> topay = salseDao.daySales(toda);
			int input1 = 1;
			while(input1!=0){
			System.out.println("일 매출은 : " + topay.get("ONE"));
			
			System.out.println("1.상세내역 조회\t0.돌아가기");
			
		
			input1 = ScanUtil.nextInt();
			if(input1 == 1){
				List<Map<String, Object>> topayList = salseDao.listDaySales(toda);
				List<Map<String, Object>> topayList1 = salseDao.listDaySales1(toda);
				List<Map<String, Object>> topayList2 = salseDao.listDaySales2(toda);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				System.out.println("==========================================");
				System.out.println("날짜\t\t아이디\t이름\t금액\t내용");
				System.out.println("------------------------------------------");
				for(int i = 0; i<topayList.size(); i++){
					System.out.println(sdf.format(topayList.get(i).get("PAY_DATE"))+"\t"+
							topayList.get(i).get("CUS_ID")+"\t"+
							topayList.get(i).get("NAME")+"\t"+
							topayList.get(i).get("FLAT_PRICE")+"\t"+
							topayList.get(i).get("FLAT_NAME")+"\t");
				}
				for(int i = 0; i<topayList1.size(); i++){
					System.out.println(sdf.format(topayList1.get(i).get("PAY_DATE"))+"\t"+
							topayList1.get(i).get("CUS_ID")+"\t"+
							topayList1.get(i).get("USERNAME")+"\t"+
							topayList1.get(i).get("PRICE")+"\t"+
							topayList1.get(i).get("FOODNAME")+" "+topayList1.get(i).get("AMOUNT")+"개");
				}
				for(int i = 0; i<topayList2.size(); i++){
					System.out.println(sdf.format(topayList2.get(i).get("WORK_START"))+"\t"+
							topayList2.get(i).get("WORK_ID")+"\t"+
							topayList2.get(i).get("NAME")+"\t-"+
							topayList2.get(i).get("PAY")+"\t"+
							"급여");
				}
				
				System.out.println("==========================================");
				input1 = 0;
			}
			
				
			}
			return View.SALES;
		case 2: //월매출
			System.out.println("조회하고자 하는 월을 입력하세요.\n예시)202010");
			System.out.println("일자입력 >");
			String mon = ScanUtil.nextLine();
			Map<String, Object> month = new HashMap<>();
			month.put("DATE", mon);
			Map<String, Object> monpay = salseDao.monthSales(month);;
			int input2 = 1;
			while(input2!=0){
			System.out.println("월매출은 : " + monpay.get("ONE"));
			
			System.out.println("1.상세내역 조회\t0.돌아가기");
			
			
			input2 = ScanUtil.nextInt();
			if(input2 == 1){
				List<Map<String, Object>> monthList = salseDao.listMonthSales(month);
				List<Map<String, Object>> monthList1 = salseDao.listMonthSales1(month);
				List<Map<String, Object>> monthList2 = salseDao.listMonthSales2(month);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				System.out.println("==========================================");
				System.out.println("날짜\t\t아이디\t이름\t금액\t내용");
				System.out.println("------------------------------------------");
				for(int i = 0; i<monthList.size(); i++){
					System.out.println(sdf.format(monthList.get(i).get("PAY_DATE"))+"\t"+
							monthList.get(i).get("CUS_ID")+"\t"+
							monthList.get(i).get("NAME")+"\t"+
							monthList.get(i).get("FLAT_PRICE")+"\t"+
							monthList.get(i).get("FLAT_NAME")+"\t");
				}
				for(int i = 0; i<monthList1.size(); i++){
					System.out.println(sdf.format(monthList1.get(i).get("PAY_DATE"))+"\t"+
							monthList1.get(i).get("CUS_ID")+"\t"+
							monthList1.get(i).get("USERNAME")+"\t"+
							monthList1.get(i).get("PRICE")+"\t"+
							monthList1.get(i).get("FOODNAME")+" "+monthList1.get(i).get("AMOUNT")+"개");
				}
				for(int i = 0; i<monthList2.size(); i++){
					System.out.println(sdf.format(monthList2.get(i).get("WORK_START"))+"\t"+
							monthList2.get(i).get("WORK_ID")+"\t"+
							monthList2.get(i).get("NAME")+"\t-"+
							monthList2.get(i).get("PAY")+"\t"+
							"급여");
				}
				
				System.out.println("==========================================");
				input2 = 0;
			}
			
				
			}
			
			return View.SALES;
		case 3: //연매출
			System.out.println("조회하고자 하는 년도를 입력하세요.\n예시)2020");
			System.out.println("일자입력 >");
			String yea = ScanUtil.nextLine();
			Map<String, Object> year = new HashMap<>();
			year.put("DATE", yea);
			Map<String, Object> yearpay = salseDao.yearSales(year);
			int input3 = 1;
			while(input3!=0){
				System.out.println("연매출은 : " + yearpay.get("ONE"));
				System.out.println("1.상세내역 조회\t0.돌아가기");

				input3 = ScanUtil.nextInt();
				if (input3 == 1) {
				List<Map<String, Object>> yearList = salseDao.listYearSales(year);
				List<Map<String, Object>> yearList1 = salseDao.listYearSales1(year);
				List<Map<String, Object>> yearList2 = salseDao.listYearSales2(year);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				System.out.println("==========================================");
				System.out.println("날짜\t\t아이디\t이름\t금액\t내용");
				System.out.println("------------------------------------------");
				for(int i = 0; i<yearList.size(); i++){
					System.out.println(sdf.format(yearList.get(i).get("PAY_DATE"))+"\t"+
							yearList.get(i).get("CUS_ID")+"\t"+
							yearList.get(i).get("NAME")+"\t"+
							yearList.get(i).get("FLAT_PRICE")+"\t"+
							yearList.get(i).get("FLAT_NAME")+"\t");
				}
				for(int i = 0; i<yearList1.size(); i++){
					System.out.println(sdf.format(yearList1.get(i).get("PAY_DATE"))+"\t"+
							yearList1.get(i).get("CUS_ID")+"\t"+
							yearList1.get(i).get("USERNAME")+"\t"+
							yearList1.get(i).get("PRICE")+"\t"+
							yearList1.get(i).get("FOODNAME")+" "+yearList1.get(i).get("AMOUNT")+"개");
				}
				for(int i = 0; i<yearList2.size(); i++){
					System.out.println(sdf.format(yearList2.get(i).get("WORK_START"))+"\t"+
							yearList2.get(i).get("WORK_ID")+"\t"+
							yearList2.get(i).get("NAME")+"\t-"+
							yearList2.get(i).get("PAY")+"\t"+
							"급여");
				}
				
				System.out.println("==========================================");
				input3 = 0;
			}
			
				
			}
			
			return View.SALES;
		case 4: //총매출
			Map<String, Object> sum = salseDao.totalSales();
			int input4 = 1;
			while(input4!=0){
			System.out.println("총 매출 : "+sum.get("SALES"));
			System.out.println("1.상세내역 조회\t0.돌아가기");

			input4 = ScanUtil.nextInt();
			if (input4 == 1) {
			List<Map<String, Object>> sumList = salseDao.listSUMSales(sum);
			List<Map<String, Object>> sumList1 = salseDao.listSUMSales1(sum);
			List<Map<String, Object>> sumList2 = salseDao.listSUMSales2(sum);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			System.out.println("==========================================");
			System.out.println("날짜\t\t아이디\t이름\t금액\t내용");
			System.out.println("------------------------------------------");
			for(int i = 0; i<sumList.size(); i++){
				System.out.println(sdf.format(sumList.get(i).get("PAY_DATE"))+"\t"+
						sumList.get(i).get("CUS_ID")+"\t"+
						sumList.get(i).get("NAME")+"\t"+
						sumList.get(i).get("FLAT_PRICE")+"\t"+
						sumList.get(i).get("FLAT_NAME")+"\t");
			}
			for(int i = 0; i<sumList1.size(); i++){
				System.out.println(sdf.format(sumList1.get(i).get("PAY_DATE"))+"\t"+
						sumList1.get(i).get("CUS_ID")+"\t"+
						sumList1.get(i).get("USERNAME")+"\t"+
						sumList1.get(i).get("PRICE")+"\t"+
						sumList1.get(i).get("FOODNAME")+" "+sumList1.get(i).get("AMOUNT")+"개");
			}
			for(int i = 0; i<sumList2.size(); i++){
				System.out.println(sdf.format(sumList2.get(i).get("WORK_START"))+"\t"+
						sumList2.get(i).get("WORK_ID")+"\t"+
						sumList2.get(i).get("NAME")+"\t-"+
						sumList2.get(i).get("PAY")+"\t"+
						"급여");
			}
			
			System.out.println("==========================================");
			input4 = 0;
		}
		
			
		}
			return View.SALES;
		default:
			break;
		}
		
		return View.ADMIN_FIRST_PAGE;
	}
	
}
