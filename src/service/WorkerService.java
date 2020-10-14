package service;

import java.util.HashMap;
import java.util.Map;

import util.ScanUtil;
import util.View;
import controller.Controller;
import dao.WorkerDao;

public class WorkerService {
	private WorkerService(){}
	private static WorkerService instance;
	public static WorkerService getInstance(){
		if(instance == null){
			instance = new WorkerService();
		}
		return instance;
	}
	
	
	private WorkerDao workDao = WorkerDao.getInstance();
	
	public int join(){
		boolean power;
		boolean power1;
		System.out.println("=========== 회원가입 ===========");
		System.out.println("아이디 >"); //중복체크, 정규표현식으로 유효성 체크
		String workId = ScanUtil.nextLine();
		Map<String, Object> user = new HashMap<>();
		user = workDao.finduser("WORK_ID",(Object)workId);
		if(user.get("WORK_ID") == null){
			power = false;
		}else{
			power = user.get("WORK_ID").equals(workId);
			System.out.println("동일한 아이디가 존재합니다.\n새로운 아이디를 적어주세요.");
		}
		while(power == true){
			workId = ScanUtil.nextLine();
			user = new HashMap<>();
			user = workDao.finduser("WORK_ID",(Object)workId);
			
			if(user == null){
				String id = "(\\w|-|_){5,20}+";
				power1 = workId.matches(id);
				if(power1 == true){
					power = false;
				}else if(power1 == false) {
					System.out.println("아이디는 5~20자의 영문 소문자, 숫자로만 작성해 주십시오.");
				}
			}else if(user != null){
				System.out.println("동일한 아이디가 존재합니다.\n새로운 아이디를 적어주세요.");
				
			}
		}
		System.out.println("비밀번호 >"); //비밀번호는 2번 입력을 한다(입력, 확인)
		String password = ScanUtil.nextLine();
		System.out.println("다시 입력 비밀번호 >");
		String password1 = ScanUtil.nextLine();
		power = password.equals(password1); 
		while(power == false){
			System.out.println("입력하신 비밀번호가 서로 다릅니다.");
			System.out.println("비밀번호 >");
			password = ScanUtil.nextLine();
			System.out.println("다시 입력 비밀번호 >");
			password1 = ScanUtil.nextLine();
			power = password.equals(password1);
		}
			
		System.out.println("주민등록번호 >");
		String reg_No = ScanUtil.nextLine();
		System.out.println("이름 >");
		String Name = ScanUtil.nextLine();
		System.out.println("연락처 >");
		String Hp = ScanUtil.nextLine();
		System.out.println("이메일 >");
		String Email = ScanUtil.nextLine();
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
		System.out.println("============ 로그인 ===========");
		System.out.println("아이디 >");
		String workId = ScanUtil.nextLine();
		System.out.println("패스워드 >");
		String password = ScanUtil.nextLine();
		
		Map<String, Object> admin = workDao.selectAdmin(workId, password);
		
		if(admin.get("WORK_ID").equals(workId) && admin.get("PASS").equals(password)){
			System.out.println("로그인 성공");
			System.out.println(admin);
			Controller.loginAdmin = admin;

			return View.BOARD_LIST;
		
		}else{
		
			System.out.println("아이디 혹은 비밀번호를 잘못 입력하셨습니다.");
			
		}
		return View.LOGIN;
	}
	
	
}
