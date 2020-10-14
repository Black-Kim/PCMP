package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controller.Controller;
import dao.UserDao;
import util.ScanUtil;
import util.View;

public class UserService {
	
	private UserService(){}
	private static UserService instance;
	public static UserService getInstance(){
		if(instance == null){
			instance = new UserService();
		}
		return instance;
	}
	
	private UserDao userDao = UserDao.getInstance();
	
	public int join(){
		boolean power;
		boolean power1;
		System.out.println("=========== 회원가입 ===========");
		System.out.println("아이디 >"); //중복체크, 정규표현식으로 유효성 체크
		String cusId = ScanUtil.nextLine();
		Map<String, Object> user = new HashMap<>();
		user = userDao.finduser("CUS_ID",(Object)cusId);
		if(user == null){
			power = false;
		}else {
			power = user.get("CUS_ID").equals(cusId);
			System.out.println("동일한 아이디가 존재합니다.\n새로운 아이디를 적어주세요.");
		}
		while(power == true){
			cusId = ScanUtil.nextLine();
			user = new HashMap<>();
			user = userDao.finduser("CUS_ID",(Object)cusId);
			
			if(user == null){
				String id = "(\\w|-|_){5,20}+";
				power1 = cusId.matches(id);
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
			
		System.out.println("이름 >");
		String Name = ScanUtil.nextLine();
		System.out.println("연락처 >");
		String Hp = ScanUtil.nextLine();
		System.out.println("이메일 >");
		String Email = ScanUtil.nextLine();
		
		Map<String, Object> param = new HashMap<>();
		param.put("CUS_ID", cusId);
		param.put("NAME", Name);
		param.put("PASSWORD", password);
		param.put("HP", Hp);
		param.put("FIX_TIME", 0);
		param.put("EMAIL", Email);
		
		int result = userDao.insertUser(param);
		
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
		String userId = ScanUtil.nextLine();
		System.out.println("패스워드 >");
		String password = ScanUtil.nextLine();
		
		Map<String, Object> user = userDao.selectUser(userId, password);
		
		if(user == null){
			System.out.println("아이디 혹은 비밀번호를 잘못 입력하셨습니다.");
		}else{
			System.out.println("로그인 성공");
			
			Controller.loginUser = user;
			
			return View.BOARD_LIST;
		}
		return View.LOGIN;
	}
	
}
