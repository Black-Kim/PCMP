package service;

import java.util.HashMap;
import java.util.Map;

import util.ScanUtil;
import util.View;
import controller.Controller;
import dao.WorkerDao;

public class WorkerService {
	private WorkerService() {
	}

	private static WorkerService instance;

	public static WorkerService getInstance() {
		if (instance == null) {
			instance = new WorkerService();
		}
		return instance;
	}

	private WorkerDao workDao = WorkerDao.getInstance();

	public int join() {
		boolean power = true;
		boolean power1;
		System.out.println("=========== 회원가입 ===========");
		System.out.println("아이디 >"); 
		String workId = ScanUtil.nextLine();
		Map<String, Object> user = new HashMap<>();
		user = workDao.finduser("WORK_ID", (Object) workId);
		
		//아이디 유효성 검사
		while (power == true) {
			String id = "(\\w|-|_){5,20}+"; //정규표현식
			user = workDao.finduser("WORK_ID", (Object) workId);//중복체크
			power1 = workId.matches(id);
			if(power1 == false){
				System.out.println("아이디는 5~20자의 영문 소문자, 숫자로만 작성해 주십시오.\n아이디>");
				workId = ScanUtil.nextLine();
				user = new HashMap<>();
			}else if(user != null){
				System.out.println("동일한 아이디가 존재합니다.\n새로운 아이디를 적어주세요.");
				workId = ScanUtil.nextLine();
				user = new HashMap<>();
			}else if(user == null && power1 == true){
				power = false;
				break;
			}
		}
		
		//비밀번호 
		System.out.println("비밀번호 >"); //비밀번호는 2번 입력을 한다(입력, 확인)
		String password = ScanUtil.nextLine();
		System.out.println("다시 입력 비밀번호 >");
		String password1 = ScanUtil.nextLine();
		power = password.equals(password1); 
		while(power == false){
			System.out.println("입력하신 비밀번호가 서로 다릅니다.\n다시 입력하세요.");
			System.out.println("비밀번호 >");
			password = ScanUtil.nextLine();
			System.out.println("다시 입력 비밀번호 >");
			password1 = ScanUtil.nextLine();
			power = password.equals(password1);
		}
		
		//주민번호 유효성검사
		System.out.println("주민등록번호 >");
		String reg_No = ScanUtil.nextLine();
		power = true;
		while (power == true) {
			if(reg_No.length() == 13){
				power = false;
			}else if(reg_No.length() < 13){
				System.out.println("주민등록번호가 잘못되었습니다.(13자리 미만)\n다시 입력하세요.");
				System.out.println("주민등록번호 >");
				reg_No = ScanUtil.nextLine();
			}else if(reg_No.length() > 13){
				System.out.println("주민등록번호가 잘못되었습니다.(13자리 초과)\n다시 입력하세요.");
				System.out.println("주민등록번호 >");
				reg_No = ScanUtil.nextLine();
			}
		}
		
		System.out.println("이름 >");
		String Name = ScanUtil.nextLine();
		System.out.println("연락처 >");
		String Hp = ScanUtil.nextLine();
		
		//이메일 유효성검사
		System.out.println("이메일 >");
		String Email = ScanUtil.nextLine();
		power = true;
		while(power == true){
			String id = "\\w+@\\w+\\.\\w+(\\.\\w+)?";
			power1 = Email.matches(id);
			if(power1 == true){
				power = false;
			}else if(power1 == false){
				System.out.println("잘못된 양식의 이메일 주소입니다.\n다시 작성하여 주세요.\n예)example@naver.com");
				System.out.println("이메일 >");
				Email = ScanUtil.nextLine();
			}
		}
		
		System.out.println("주소 >");
		String Addr = ScanUtil.nextLine();
		System.out.println("급여 >");
		int Sal = ScanUtil.nextInt();
		System.out.println("식별코드 >");
		int Code = ScanUtil.nextInt();
		
		Map<String, Object> param = new HashMap<>();
		param.put("WORK_ID", workId);
		param.put("REG_NO", reg_No);
		param.put("Name", Name);
		param.put("PASSWORD", password);
		param.put("HP", Hp);
		param.put("EMAIL", Email);
		param.put("ADDR", Addr);
		param.put("SAL", Sal);
		param.put("CODE", Code);
		
		int result = workDao.insertAdmin(param);
		
		if(0 < result){
			System.out.println("회원가입 성공");
		}else{
			System.out.println("회원가입 실패");
		}
		return View.HOME;
	}

	public int login() {
		boolean power = true;
		System.out.println("============ 로그인 ===========");
		System.out.println("아이디 >");
		String workId = ScanUtil.nextLine();
		System.out.println("패스워드 >");
		String password = ScanUtil.nextLine();
		Map<String, Object> admin = new HashMap<>();
		admin = new HashMap<>();
		admin = workDao.selectAdmin(workId, password);
		
		if (admin == null) {
			System.out.println("아이디 혹은 비밀번호를 잘못 입력하셨습니다.");
			System.out.println("첫 화면으로 돌아갑니다.");
		} else if (admin != null) {
			if (admin.get("WORK_ID").equals(workId)
					&& admin.get("PASS").equals(password)
					&& Integer.parseInt(String.valueOf(admin.get("CODE"))) == 0) {
				System.out.println("총괄 관리자 로그인 성공");
				Controller.loginAdmin = admin;
				
				return View.ADMIN_FIRST_PAGE;
				
			} else if (admin.get("WORK_ID").equals(workId)
					&& admin.get("PASS").equals(password)
					&& Integer.parseInt(String.valueOf(admin.get("CODE"))) == 1) {
				System.out.println("직원 로그인 성공");
				Controller.loginAdmin = admin;

				return View.WORKER_FIRST_PAGE;
			}
		}
		return View.HOME;
	}

}
