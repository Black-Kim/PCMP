package service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
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
		boolean power = true;
		boolean power1;
		System.out.println("=========== 회원가입 ===========");
		System.out.println("아이디 >"); 
		String cusId = ScanUtil.nextLine();
		Map<String, Object> user = new HashMap<>();
		user = userDao.finduser("CUS_ID",(Object)cusId);
		
		//아이디 유효성 검사
		while (power == true) {
			String id = "(\\w|-|_){5,20}+"; // 정규표현식
			user = userDao.finduser("CUS_ID", (Object) cusId);// 중복체크
			power1 = cusId.matches(id);
			if (power1 == false) {
				System.out.println("아이디는 5~20자의 영문 소문자, 숫자로만 작성해 주십시오.\n아이디>");
				cusId = ScanUtil.nextLine();
				user = new HashMap<>();
			} else if (user != null) {
				System.out.println("동일한 아이디가 존재합니다.\n새로운 아이디를 적어주세요.");
				cusId = ScanUtil.nextLine();
				user = new HashMap<>();
			} else if (user == null && power1 == true) {
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
		
		//이메일 유효성 검사
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
		System.out.println("================ 로그인 ================");
		System.out.print("아이디>");
		String userId = ScanUtil.nextLine();
		System.out.print("비밀번호>");
		String password = ScanUtil.nextLine();
		
		Map<String,Object> user = userDao.selectUser(userId, password);
		
		if(user == null){
			System.out.println("아이디 혹은 비밀번호를 잘못 입력하셨습니다.");
			System.out.println("첫 화면으로 돌아갑니다.");
		}else{
			System.out.println("로그인 성공");
			
			Controller.LoginUser = user;
			
			userDao.loginOn();
			
			
			
			return View.SELECT_LIST;
		}
		
		return View.HOME;
		
	}
	public int mypage() {
		boolean power;
		Map<String, Object> boardList = userDao.selectList(Controller.LoginUser.get("CUS_ID"));
//		System.out.println(boardList); //키 확인용
		Map<String, Object> lefthour = userDao.userlefthour();
		Map<String, Object> flattime = userDao.flattime();
		
		System.out.println("    회\t원\t정\t보");
		System.out.println("================================");
		System.out.println("회원 아이디 : "+" "+boardList.get("CUS_ID"));
		System.out.println("이          름 : "+" "+boardList.get("NAME"));
		System.out.println("연    락   처 : "+" "+boardList.get("HP"));
		System.out.println("정액제 시간 : "+" "+flattime.get("FT"));
		System.out.println("남은 시간    :  " +lefthour.get("TOTAL"));
		System.out.println("이    메   일 : "+" "+boardList.get("EMAIL"));
		System.out.println("총 결재금액 : "+" "+flattime.get("FP"));
		
		System.out.println("================================");
		System.out.println("1.수정\t2.삭제\t3.결재내역\t0.이전");
		System.out.print("입력>");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1:
			System.out.println("비밀번호>");
			String password = ScanUtil.nextLine();
			power = password.equals(Controller.LoginUser.get("PASS")); 
			while(power == false){
				System.out.println("비밀번호가 틀렸습니다.");
				System.out.println("비밀번호 >");
				password = ScanUtil.nextLine();
				power = password.equals(Controller.LoginUser.get("PASS"));
			}			
			int result = userDao.updatemypage(Controller.LoginUser.get("CUS_ID"));
			if(result > 0){
				System.out.println("회원 정보 수정이 완료되었습니다.\n다시 로그인 하여 주십시오.");
				Controller.LoginUser = null;
				return View.HOME;
			}else{
				System.out.println("▶수정오류 입니다◀\n다시 수정하여 주십시오.");
			}
			break;
			
		case 2:
			System.out.println("정말 삭제 하시겠습니까?");
			System.out.println("예 / 아니요");
			String ans = ScanUtil.nextLine();
			String ans1 = "예";
			if(ans.equals(ans1) == true){
				result = userDao.deletemypage(Controller.LoginUser.get("CUS_ID"));
				if(result > 0){
					System.out.println("회원 정보 삭제가 완료되었습니다.");
					System.out.println("그동안 사용해 주셔서 감사합니다. 개선사항이 있으시면 작성 해 주십시오.");
					ans = ScanUtil.nextLine();
				}else{
					System.out.println("▶떠나는것을 허락하지 않습니다.◀");
					}
				};
				
			return View.HOME;

		case 3:

			int input1 = 1;
			while (input1 != 0) {
			System.out.println("===========================================");
			System.out.println("           결제날짜"+"                결제금액            결제내용");
			System.out.println("===========================================");
			List<Map<String, Object>> pricedate = userDao.paymentmanagement();
			List<Map<String, Object>> pricedate1 = userDao.paymentmanagement1();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			if(pricedate != null){
				for(int i = 0; i<pricedate.size(); i++){
			System.out.println(sdf.format(pricedate.get(i).get("PAY_DATE")) + "    " +
					pricedate.get(i).get("FLAT_PRICE") + "      " + 
					pricedate.get(i).get("FLAT_NAME"));
				}
				
			}
			if(pricedate1 != null){
				for(int i = 0; i<pricedate1.size(); i++){
			System.out.println(sdf.format(pricedate1.get(i).get("PAY_DATE")) +  "    " + pricedate1.get(i).get("FOOD_PR") + "       " +pricedate1.get(i).get("NAME")
					);
				}
			}
			System.out.println("===========================================");
			System.out.println("0.돌아가기");
			System.out.println("입력 > ");
			input1 = ScanUtil.nextInt();
			
			if(input1 != 0){
				System.out.println("0번을 입력해주세요");
			}
			}
			
			break;

		default:
			break;
		}
		return View.SELECT_LIST;
	}
	
}
