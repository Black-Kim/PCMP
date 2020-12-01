package service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import controller.Controller;
import dao.UserDao;
import dao.WorkerBoardDao;
import dao.WorkerDao;
import util.ScanUtil;
import util.View;

public class WorkerBoardService {

	private WorkerBoardService() {
	}

	private static WorkerBoardService instance;

	public static WorkerBoardService getInstance() {
		if (instance == null) {
			instance = new WorkerBoardService();
		}
		return instance;
	}

	public int selectList() {

		System.out.println(Controller.loginAdmin);
		System.out.println("=========================================");
		System.out.println("1.게시판 조회\t2.결재\t3.마이페이지\t4.스케줄관리\t0.로그아웃");
		System.out.println("=========================================");
		System.out.print("입력 >");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1:
			return View.WORKER_BOARD_LIST;// 게시판

		case 2: //결재
			System.out.println("1.정액제 결제\t2.음식 결제\t0.뒤로가기");
			input = ScanUtil.nextInt();
			switch(input){
			case 1 :
				return View.PAY_FLATRATE;
			case 2 :
				return View.PAY_FOOD;//고객 음식 결제
			case 0 :
				return View.WORKER_FIRST_PAGE;
			}

			
		case 3:
			return View.WORKER_MYPAGE; // 알바 마이페이지
			
		case 4:
			return View.WORK_SCHEDULE; // 알바 스케줄 관리 페이지

		case 0:
			Controller.loginAdmin = null;// 로그아웃
			return View.HOME;
		default:
			return View.SELECT_LIST;
		}

	}

	private WorkerBoardDao WokerboardDao = WorkerBoardDao.getInstance();

	public int boardList() {
		List<Map<String, Object>> boardList = WokerboardDao.selectBoardList();
		System.out.println("==============================================");
		System.out.println("글 번호\t작성자\t제목\t답변여부\t      작성일");
		System.out.println("--------------------------------------");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		for (int i = 0; i < boardList.size(); i++) {
			System.out.println("  "+boardList.get(i).get("BOR_NO") + "\t " 
					 
					+ boardList.get(i).get("NAME") + "\t"
					+ boardList.get(i).get("TITLE") + "\t  "
					+ boardList.get(i).get("BOR_CHECK") + "\t"
			        + sdf.format(boardList.get(i).get("BOR_DATE")));
					 
		}
		System.out.println("===============================================");
		System.out.println("1.조회\t0.이전");
		System.out.println("입력 >");

		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:// 조회
			select();
			break;
		case 0:// 이전
			if (Integer.parseInt(String.valueOf(Controller.loginAdmin
					.get("CODE"))) == 1) {
				return View.WORKER_SELECT_LIST;
			} else {
				return View.ADMIN_FIRST_PAGE;
			}

		}

		return View.WORKER_BOARD_LIST;

	}

	private void select() {
		System.out.print("조회할 번호를 입력해주세요 : ");
		int num = ScanUtil.nextInt();
		
		Map<String, Object> boardList = WokerboardDao.selectList(num);
		if(boardList == null){
			System.out.println("없는 번호입니다.");
		}else{
			
			System.out.println("================================");
			
			System.out.println("게시글 번호 : " + " " + boardList.get("BOR_NO"));
			System.out.println("제          목 : " + " " + boardList.get("TITLE"));
			System.out.println("내          용 : " + " " + boardList.get("CONTENT"));
			System.out.println("작    성   자 : " + " " + boardList.get("NAME"));
			System.out.println("작 성 자  ID: " + " " + boardList.get("CUS_ID"));
			System.out.println("답 변  내 용 : " + " " + boardList.get("BOR_COMM"));
			
			System.out.println("================================");
			System.out.println("1.답변\t2.삭제\t0.이전");
			System.out.print("입력>");
			int input = ScanUtil.nextInt();
			switch (input) {
			case 1:
				WokerboardDao.updateBoardList(num);
				break;
				
			case 2:
				WokerboardDao.deleteBoardList(num);
				break;
				
			default:
				break;
			}
		}
	}

	public int flatrateList() {

		List<Map<String, Object>> flatList = WokerboardDao.selectFlatRateList();
		System.out.println("================================");
		System.out.println("번호\t이름\t단가\t시간");
		for (int i = 0; i < flatList.size(); i++)
			System.out.println(flatList.get(i).get("FLAT_NO") + "\t"
					+ flatList.get(i).get("FLAT_NAME") + "\t"
					+ flatList.get(i).get("FLAT_PRICE") + "\t"
					+ flatList.get(i).get("FLAT_TIME") + "\t");
		System.out.println("================================");

		System.out.println("1.상품추가\t2.상품변경\t3.상품삭제\t0.뒤로가기");
		System.out.print("입력 >");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1:
			WokerboardDao.insertFlatrateList();
			return View.WORKER_FlatRate_LIST;
		case 2:
			WokerboardDao.updateFlatrateList();
			return View.WORKER_FlatRate_LIST;
		case 3:
			WokerboardDao.deleteFlatrateList();
			return View.WORKER_FlatRate_LIST;
		case 0:
			return View.ADMIN_FIRST_PAGE;
		default:
			break;
		}
		return 0;
	}

	public int adminmypage() {
		boolean power;
		Map<String, Object> boardList = WokerboardDao
				.adminList(Controller.loginAdmin.get("WORK_ID"));

		System.out.println("  관\t리\t자\t정\t보");
		System.out.println("================================");
		System.out.println("관리자 아이디 : " + " " + boardList.get("WORK_ID"));
		System.out.println("주  민   번  호 : " + " " + boardList.get("REG_NO"));
		System.out.println("이             름 : " + " " + boardList.get("NAME"));
		System.out.println("전   화  번  호 : " + " " + boardList.get("HP"));
		System.out.println("이     메     일 : " + " " + boardList.get("EMAIL"));
		System.out.println("주             소 : " + " " + boardList.get("ADDR"));
		System.out.println("급             여 : " + " " + boardList.get("SAL"));
		System.out.println("식  별   코  드 : " + " " + boardList.get("CODE"));

		System.out.println("================================");
		System.out.println("1.수정\t2.삭제\t0.이전");
		System.out.print("입력>");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1:
			System.out.println("비밀번호>");
			String password = ScanUtil.nextLine();
			power = password.equals(Controller.loginAdmin.get("PASS"));
			while (power == false) {
				System.out.println("비밀번호가 틀렸습니다.");
				System.out.println("비밀번호 >");
				password = ScanUtil.nextLine();
				power = password.equals(Controller.loginAdmin.get("PASS"));
			}
			int result = WokerboardDao.adminupdatemypage(Controller.loginAdmin
					.get("WORK_ID"));
			if (result > 0) {
				System.out.println("회원 정보 수정이 완료되었습니다.\n다시 로그인 하여 주십시오.");
			} else {
				System.out.println("▶수정오류 입니다◀\n다시 수정하여 주십시오.");
			}
			break;

		case 2:
			System.out.println("정말 삭제 하시겠습니까?");
			System.out.println("예 / 아니요");
			String ans = ScanUtil.nextLine();
			String ans1 = "예";
			if (ans.equals(ans1) == true) {
				result = WokerboardDao.admindeletemypage(Controller.loginAdmin
						.get("WORK_ID"));
				if (result > 0) {
					System.out.println("회원 정보 삭제가 완료되었습니다.");
					System.out
							.println("그동안 사용해 주셔서 감사합니다. 개선사항이 있으시면 작성 해 주십시오.");
					ans = ScanUtil.nextLine();
				} else {
					System.out.println("▶떠나는것을 허락하지 않습니다.◀");
				}
			}
			;
			break;
		case 0:
			return View.ADMIN_FIRST_PAGE;
		default:
			return View.ADMIN_MYPAGE;
		}
		return 0;
	}

	public int mypage() {
		boolean power;
		Map<String, Object> boardList = WokerboardDao
				.workerList(Controller.loginAdmin.get("WORK_ID"));

		System.out.println("관\t리\t자\t정\t보");
		System.out.println("================================");
		System.out.println("관리자 아이디 : " + " " + boardList.get("WORK_ID"));
		System.out.println("주  민   번  호 : " + " " + boardList.get("REG_NO"));
		System.out.println("이             름 : " + " " + boardList.get("NAME"));
		System.out.println("전   화  번  호 : " + " " + boardList.get("HP"));
		System.out.println("이     메     일 : " + " " + boardList.get("EMAIL"));
		System.out.println("주             소 : " + " " + boardList.get("ADDR"));
		System.out.println("급             여 : " + " " + boardList.get("SAL"));
		System.out.println("식  별   코  드 : " + " " + boardList.get("CODE"));

		System.out.println("================================");
		System.out.println("1.수정\t2.삭제\t0.이전");
		System.out.print("입력>");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1:
			System.out.println("비밀번호>");
			String password = ScanUtil.nextLine();
			power = password.equals(Controller.loginAdmin.get("PASS"));
			while (power == false) {
				System.out.println("비밀번호가 틀렸습니다.");
				System.out.println("비밀번호 >");
				password = ScanUtil.nextLine();
				power = password.equals(Controller.loginAdmin.get("PASS"));
			}
			int result = WokerboardDao.workerupdatemypage(Controller.loginAdmin
					.get("WORK_ID"));
			if (result > 0) {
				System.out.println("회원 정보 수정이 완료되었습니다.\n다시 로그인 하여 주십시오.");
			} else {
				System.out.println("▶수정오류 입니다◀\n다시 수정하여 주십시오.");
			}
			break;

		case 2:
			System.out.println("정말 삭제 하시겠습니까?");
			System.out.println("예 / 아니요");
			String ans = ScanUtil.nextLine();
			String ans1 = "예";
			
			if (ans.equals(ans1) == true) {
				result = WokerboardDao.workerdeletemypage(Controller.loginAdmin
						.get("WORK_ID"));
				
				if (result > 0) {
					System.out.println("회원 정보 삭제가 완료되었습니다.");
					System.out
							.println("그동안 사용해 주셔서 감사합니다. 개선사항이 있으시면 작성 해 주십시오.");
					ans = ScanUtil.nextLine();
				} else {
					System.out.println("▶떠나는것을 허락하지 않습니다.◀");
				}
			}
			;
			break;
		case 0:
			return View.WORKER_FIRST_PAGE;

		default:
			return View.WORKER_MYPAGE;
		}
		return 0;
	}

	/*
	 * private void insertFlat() {
	 * 
	 * List<Map<String, Object>> flatrateList =
	 * WokerboardDao.insertFlatrateList();
	 * 
	 * 
	 * }
	 */

	public int admintList() {
		System.out.println("====================================================");
		System.out.println("1.게시판조회       2.결재       3.관리       4.마이페이지       0.로그아웃");
		System.out.println("====================================================");
		System.out.print("입력 >");
		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:
			return View.WORKER_BOARD_LIST;
		case 2:
			System.out.println("1.정액제 결제\t2.음식 결제\t0.뒤로가기");
			input = ScanUtil.nextInt();
			switch(input){
			case 1 :
				return View.PAY_FLATRATE;
			case 2 :
				return View.PAY_FOOD;//고객 음식 결제
			case 0 :
				return View.ADMIN_FIRST_PAGE;
			}
		case 3:
			System.out.println();
			System.out.println("===============================================================================================================");
			System.out.println("1.매출관리\t2.직원관리\t3.회원 관리\t4.정액제 관리\t5.음식 관리\t6.스케줄 관리\t0.첫 페이지로");
			System.out.println("===============================================================================================================");
			input = ScanUtil.nextInt();
			switch(input){
			case 1 :
				return View.SALES;//매출관리
			case 2 :
				return View.STAFF_MN;
			case 3 :
				return View.USER_MN;
			case 4 :
				return View.WORKER_FlatRate_LIST;
			case 5 :
				return View.FOOD;		
			case 6 :
				return View.SCHEDULE;
			case 0 :
				return View.ADMIN_FIRST_PAGE;
			}
		case 4:
			return View.ADMIN_MYPAGE;	
		case 0:
			Controller.loginAdmin = null;
			return View.HOME;
		default:
			return View.ADMIN_FIRST_PAGE;
		}
		

	}

	UserDao userDao = UserDao.getInstance();
	
	
	public int UserList() {
boolean check = false;
		
		List<Map<String, Object>> userList = WokerboardDao.userList();
		
		System.out.println();
		System.out.println("==================================================================================");
		System.out.println("이름         회원 아이디  \t비밀번호\t     전화번호\t이메일");
		System.out.println("----------------------------------------------------------------------------------");
		
		for(int i = 0; i < userList.size(); i++ ){
			System.out.println( userList.get(i).get("NAME") + "   \t" 
		                      + userList.get(i).get("CUS_ID") + "" + " \t"
		                      + userList.get(i).get("PASS") + "    \t"
		                      + userList.get(i).get("HP") + " \t"
		                      + userList.get(i).get("EMAIL"));
		}
		
		System.out.println("==================================================================================");
		System.out.println("1.조회\t2.수정\t3.삭제\t0.첫 페이지로");
		int input = ScanUtil.nextInt();
		
		List<Map<String, Object>> Alluser = WokerboardDao.userList();
		
		switch(input){
		case 2 :
			System.out.println("수정 할 회원의 아이디를 입력해주십시오>");
			String userId = ScanUtil.nextLine();
			
			for(int i = 0; i < Alluser.size(); i++){
			     if(check = userId.equals(Alluser.get(i).get("CUS_ID")) == true){
				break;
			    }
			}
			
			while(check == false){
				System.out.println("존재하지 않는 회원 아이디입니다. \n다시 입력해 주십시오.");
				System.out.println("아이디 >");
				userId = ScanUtil.nextLine();
			for(int i = 0; i < Alluser.size(); i++){
			     if(check = userId.equals(Alluser.get(i).get("CUS_ID")) == true){
						break;
					}
				}
			}
			
			for(int i = 0; i < Alluser.size(); i++){
				int result = WokerboardDao.userUpdate(userId);
			if (result > 0) {
				System.out.println("회원 정보 수정이 완료되었습니다.");
			} else {
				System.out.println("▶수정오류 입니다◀\n다시 수정하여 주십시오.");
			}
              break;
			}
			
			return View.USER_MN;
			
		case 3 :
			
			System.out.println("삭제할 회원 아이디를 입력해주십시오.");
			String inputuserid = ScanUtil.nextLine();
			
			for(int i = 0; i < Alluser.size(); i++){
			     if(check = inputuserid.equals(Alluser.get(i).get("CUS_ID")) == true){
				break;
			    }
			}
			
			while(check == false){
				System.out.println("존재하지 않는 회원 아이디입니다. \n다시 입력해 주십시오.");
				System.out.println("아이디 >");
				inputuserid = ScanUtil.nextLine();
			for(int i = 0; i < Alluser.size(); i++){
			     if(check = inputuserid.equals(Alluser.get(i).get("CUS_ID")) == true){
						break;
					}
				}
			}
			
			for(int i = 0; i < Alluser.size(); i++){
				
				System.out.println("정말 삭제 하시겠습니까?");
				System.out.println("예 / 아니요 중 하나를 입력해주세요");
				System.out.println("입력 > ");
				
				String ans = ScanUtil.nextLine();
				String ans1 = "예";
				
				
				if (ans.equals(ans1) == true) {
					int result = WokerboardDao.userdelete(inputuserid);
					if (result > 0) {
						System.out.println("회원 정보 삭제가 완료되었습니다.");
					} else {
						System.out.println("회원 정보 삭제가 실패하였습니다 \n 다시 입력해 주십시오.");
					}
					
				}else{
					return View.USER_MN;
				}
				break;
			}
				
			return View.USER_MN;	
			
		case 1 :
			System.out.println("조회할 회원의 아이디를 입력하세요");
			System.out.println("입력 >");
			String inputUser = ScanUtil.nextLine();
			
			for(int i = 0; i < Alluser.size(); i++){
			     if(check = inputUser.equals(Alluser.get(i).get("CUS_ID")) == true){
				break;
			    }
			}
			
			while(check == false){
				System.out.println("존재하지 않는 회원 아이디입니다. \n다시 입력해 주십시오.");
				System.out.println("아이디 >");
				inputUser = ScanUtil.nextLine();
			for(int i = 0; i < Alluser.size(); i++){
			     if(check = inputUser.equals(Alluser.get(i).get("CUS_ID")) == true){
						break;
					}
				}
			}
			
			
			
			Map<String, Object> boardList = userDao.selectList(inputUser);
//			System.out.println(boardList); //키 확인용
			Map<String, Object> lefthour = userDao.selectUserlefthour(inputUser);
			Map<String, Object> flattime = userDao.selectFlattime(inputUser);
			
			System.out.println("    회\t원\t정\t보");
			System.out.println("================================");
			System.out.println("회원 아이디 : "+" "+boardList.get("CUS_ID"));
			System.out.println("이          름 : "+" "+boardList.get("NAME"));
			System.out.println("연    락   처 : "+" "+boardList.get("HP"));
			System.out.println("정액제 시간 : "+" "+flattime.get("FT"));
			System.out.println("남은 시간    :  " +lefthour.get("TOTAL"));
			System.out.println("이    메   일 : "+" "+boardList.get("EMAIL"));
			System.out.println("결 재  금 액 : "+" "+flattime.get("FP"));
			
			System.out.println("================================");
			
			System.out.println("돌아갈려면 아무키나 누르세요");
			String a = ScanUtil.nextLine();
			
			return View.USER_MN;	
			
		}
			
		return View.ADMIN_FIRST_PAGE;
	}

}
