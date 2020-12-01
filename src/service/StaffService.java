package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.StaffDao;
import util.ScanUtil;
import util.View;

public class StaffService {

	private StaffService() {
	}

	private static StaffService instance;

	public static StaffService getInstance() {
		if (instance == null) {
			instance = new StaffService();
		}
		return instance;
	}

	private StaffDao staffDao = StaffDao.getInstance();

	public int staffList() {
		List<Map<String, Object>> boardList = staffDao.staffList();
		System.out.println("======================================");
		System.out.println("알바생 아이디       이름       급여");
		System.out.println("======================================");
		
		for (int i = 0; i < boardList.size(); i++) {
			System.out.println(boardList.get(i).get("WORK_ID") + "       "
					+ boardList.get(i).get("NAME") + "        "
					+ boardList.get(i).get("SAL"));
		}
		System.out.println("======================================");
		System.out.println("1.조회\t2.수정\t3.삭제\t0.뒤로가기");
		System.out.println("입력 >");

		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:
			System.out.println("조회 할 알바생 아이디를 입력하세요>");
			String name = ScanUtil.nextLine();
			Map<String, Object> param = new HashMap<>();
			param.put("WORK_ID", name);
			Map<String, Object> staffone = staffDao.staffone(param
					.get("WORK_ID"));
			System.out.println("======================================");

			System.out.println("아   이   디 : " + " " + staffone.get("WORK_ID"));
			System.out.println("주 민 번 호 : " + " " + staffone.get("REG_NO"));
			System.out.println("이          름: " + " " + staffone.get("NAME"));
			System.out.println("비 밀 번 호 : " + " " + staffone.get("PASS"));
			System.out.println("전 화 번 호 : " + " " + staffone.get("HP"));
			System.out.println("이   메   일 : " + " " + staffone.get("EMAIL"));
			System.out.println("주         소 : " + " " + staffone.get("ADDR"));
			System.out.println("급         여 : " + " " + staffone.get("SAL"));
			System.out.println("식 별 코 드 : " + " " + staffone.get("CODE"));

			System.out.println("======================================");
			return View.STAFF_MN;

		case 2: // 수정
			int result = 0;
			System.out.println("수정 할 알바생 아이디를 입력하세요>");
			name = ScanUtil.nextLine();
			param = new HashMap<>();
			param.put("WORK_ID", name);

			System.out.println("수정 할 항목을 고르세요>");
			System.out
					.println("1.주민등록번호     2.이름     3.비밀번호     4.전화번호     5.이메일      6.주소     7.급여     0.뒤로가기");
			input = ScanUtil.nextInt();
			switch (input) {
			case 1: // 주민등록번호
				result = staffDao.updatereg_no(param.get("WORK_ID"));
				break;
			case 2: // 이름
				result = staffDao.updatename(param.get("WORK_ID"));
				break;
			case 3: // 비밀번호
				result = staffDao.updatepass(param.get("WORK_ID"));
				break;
			case 4: // 연락처
				result = staffDao.updatehp(param.get("WORK_ID"));
				break;
			case 5: // 이메일
				result = staffDao.updateemail(param.get("WORK_ID"));
				break;
			case 6: // 주소
				result = staffDao.updateaddr(param.get("WORK_ID"));
				break;
			case 7: // 급여
				result = staffDao.updatesal(param.get("WORK_ID"));
				break;
			case 0: // 돌아가기
				return View.STAFF_MN;
			}
			if (0 < result) {
				System.out.println("수정되었습니다.");
			} else {
				System.out.println("▶수정오류 입니다◀");
			}
			return View.STAFF_MN;
		case 3: // 삭제
			System.out.println("삭제 하려고 하는 알바생 아이디를 적으세요.");
			name = ScanUtil.nextLine();
			param = new HashMap<>();
			param.put("WORK_ID", name);

			result = staffDao.deletestaff(param.get("WORK_ID"));
			if (0 < result) {
				System.out.println("수정되었습니다.");
			} else {
				System.out.println("▶수정오류 입니다◀");
			}
			return View.STAFF_MN;
		case 0: // 뒤로가기
			return View.ADMIN_FIRST_PAGE;
		}

		return 0;
	}

}
