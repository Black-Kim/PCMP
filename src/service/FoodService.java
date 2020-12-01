package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;
import util.ScanUtil;
import util.View;
import dao.FoodDao;
import dao.WorkerBoardDao;

public class FoodService {

	private FoodService() {
	}

	private static FoodService instance;

	public static FoodService getInstance() {
		if (instance == null) {
			instance = new FoodService();
		}
		return instance;

	}

	// private JDBCUtil jdbc = JDBCUtil.getInstance();
	private FoodDao foodDao = FoodDao.getInstance();

	// 음식 전체 조회
	public int foodList() {
		List<Map<String, Object>> boardList = foodDao.foodList();
		System.out.println("======================================");
		System.out.println("번호\t단가\t음식명");
		System.out.println("--------------------------------------");
		for (int i = 0; i < boardList.size(); i++) {
			System.out.println(boardList.get(i).get("FOOD_NO") + "\t"
					+ boardList.get(i).get("FOOD_PR")+ " \t"
					+ boardList.get(i).get("NAME")
					);
		}
		System.out.println("======================================");
		System.out.println("1.음식 추가\t2.음식 수정\t3.음식 삭제\t0.뒤로가기");
		System.out.println("입력 >");

		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:// 추가
			boolean power = true;
			System.out.println("=========== 음식 등록 ===========");
			System.out.println("음식이름 >");
			String foodname = ScanUtil.nextLine();
			Map<String, Object> food = new HashMap<>();
			food = foodDao.finduser("NAME", (Object) foodname);
			while (power == true) {
				if (food == null) {
					power = false;
				} else {
					System.out.println("동일한 이름이 등록되어 있습니다. ");
					System.out.println("음식이름 >");
					foodname = ScanUtil.nextLine();
					power = food.get("NAME").equals(foodname);
				}
			}
			System.out.println("단가 >");
			String food_pr = ScanUtil.nextLine();
			Map<String, Object> param = new HashMap<>();
			param.put("NAME", foodname);
			param.put("FOOD_PR", food_pr);
			int result = foodDao.insertfood(param);
			if (0 < result) {
				System.out.println("음식 추가 성공");
			} else {
				System.out.println("음식 추가 실패");
			}
			break;

		case 2:// 수정System.out.println("비밀번호>");
			System.out.println("수정 할 음식 번호를 고르세요.");
			int no = ScanUtil.nextInt();
			Map<String, Object> foodno = new HashMap<>();
			foodno.put("FOOD_NO", no);
			result = foodDao.updatefood(foodno.get("FOOD_NO"));
			if (result > 0) {
				System.out.println("수정이 완료되었습니다.");
			} else {
				System.out.println("▶수정오류 입니다◀\n다시 수정하여 주십시오.");
			}
			return View.FOOD;
		case 3: // 삭제
			System.out.println("삭제 할 음식 번호를 고르세요.");
			no = ScanUtil.nextInt();
			foodno = new HashMap<>();
			foodno.put("FOOD_NO", no);
			result = foodDao.deletefood(foodno.get("FOOD_NO"));
			if (result > 0) {
				System.out.println("삭제가 완료되었습니다.");
			} else {
				System.out.println("▶삭제오류 입니다◀");
			}
			break;
		case 0:// 돌아가기
			if (Integer.parseInt(String.valueOf(Controller.loginAdmin
					.get("CODE"))) == 0) {
				return View.ADMIN_FIRST_PAGE;
			} else {
				return View.WORKER_FIRST_PAGE;
			}
		}
		return View.FOOD;
	}

	
	public int insertPayFood() {
		System.out.println("1.음식결재\t0.이전");
		System.out.print("입력 >");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1:
			foodDao.insertPayFood();
			return View.PAY_FOOD;
		case 0:
			if(Integer.parseInt(String.valueOf(Controller.loginAdmin.get("CODE")))==0){
				return View.ADMIN_FIRST_PAGE;
			}else{
				return View.WORKER_FIRST_PAGE;
			}
		default:
			break;
		}

		return View.PAY_FOOD;
	}
}
