package controller;

import java.util.Map;

import java.util.Calendar;
import service.BoardService;
import service.FoodService;
import service.PayService;
import service.SalesService;
import service.ScheduleService;
import service.StaffService;
import service.UserService;
import service.WorkerBoardService;
import service.WorkerService;
import util.ScanUtil;
import util.View;


public class Controller {

	public static void main(String[] args) {
		new Controller().start();
	}
	
	
	public static Map<String, Object> LoginUser;//글쓴이의 네임 확인
	public static Map<String, Object> loginAdmin;
	
	private UserService userService = UserService.getInstance();
	private BoardService boardService = BoardService.getInstance();
	private WorkerService workerService = WorkerService.getInstance();
	private WorkerBoardService workerBoardService = WorkerBoardService.getInstance();
	private PayService payService = PayService.getInstance();
	private StaffService staffService = StaffService.getInstance();
	private FoodService foodService = FoodService.getInstance();
	private SalesService salesService = SalesService.getInstance();
	private ScheduleService scheService = ScheduleService.getInstance();
	private void start() {
		int view = View.HOME;
		
		
		while(true){
			switch (view) {
			case View.HOME:
				view = home();
				break;
			case View.LOGIN:
				view = userService.login();
				break;
			case View.JOIN:
				view = userService.join();
				break;
			case View.WORKERLOGIN:
				view = workerService.login();
				break;
			case View.WORKERJOIN:
				view = workerService.join();
				break;
			case View.BOARD_LIST:
				view = boardService.boardList();
				break;
			case View.SELECT_LIST:
				view = boardService.selectList();
				break;
			case View.WORKER_SELECT_LIST:
				view = workerBoardService.selectList();
				break;
			case View.WORKER_BOARD_LIST:
				view = workerBoardService.boardList();
				break;
			case View.WORKER_FlatRate_LIST:
				view = workerBoardService.flatrateList();
				break;
			case View.PAY_FLATRATE:
				view = payService.insertPayFlatRate();
				break;
			case View.PAY_FOOD:
				view = foodService.insertPayFood();
				break;
			case View.WORKER_FIRST_PAGE:
				view = workerBoardService.selectList();//알바 첫페이지
				break;
			case View.WORKER_MYPAGE:
				view = workerBoardService.mypage(); //알바 마이페이지
				break;
			case View.ADMIN_FIRST_PAGE:
				view = workerBoardService.admintList(); //총관리자 첫 페이지
				break;
			case View.ADMIN_MYPAGE:
				view = workerBoardService.adminmypage(); //총관리자 마이페이지
				break;
			case View.SALES:
				view = salesService.selectSales(); //매출 관리
				break;
			case View.STAFF_MN:
				view = staffService.staffList(); //직원 관리
				break;
			case View.FOOD:
				view = foodService.foodList(); //음식 관리
				break;
			case View.MY_PAGE:
				view = userService.mypage();
				break;
			case View.SCHEDULE:
				view = scheService.ScheduleList(); //스케줄 관리
				break;
			case View.WORK_SCHEDULE:
				view = scheService.workScheduleList(); //스케줄 관리
				break;
			case View.USER_MN:
			    view = workerBoardService.UserList();//회원 관리
			    break;
			}
		}
	}

	private int home() {
		boolean power;
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("■――――――――――――――――――――――――――――――――――――■");
		System.out.println("■       수선호 PC방 에 오신걸 환영합니다           ■");
		System.out.println("■                                    ■");
		System.out.println("■  1.PC방 회원     2.근무자     0.프로그램 종료    ■");
		System.out.println("■――――――――――――――――――――――――――――――――――――■");
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		
		int input = ScanUtil.nextInt();
		power = true;
		while(power == true){
			if(input == 1 || input == 2 || input == 0){
				power = false;
			}else{
				System.out.println("잘못된 번호입니다.");
				input = ScanUtil.nextInt();
			}
		}
		switch(input){
		case 1 : //회원
			
			System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
			System.out.println("■――――――――――――――――――――――――――――――――――――■");
			System.out.println("■                                    ■");
			System.out.println("■     1.로그인    2.회원가입   0.처음화면          ■");
			System.out.println("■                                    ■");
			System.out.println("■――――――――――――――――――――――――――――――――――――■");
			System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		
			input = ScanUtil.nextInt();
			power = true;
			while(power == true){
				if(input == 1 || input == 2 || input == 0){
					power = false;
				}else{
					System.out.println("잘못된 번호입니다.");
					input = ScanUtil.nextInt();
					
				}
			}
			switch(input){
			case 1 : return View.LOGIN;
			case 2 : return View.JOIN;
			case 0 : return View.HOME;
			}
				
			
			
			
		case 2 : //관리자
			System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
			System.out.println("■――――――――――――――――――――――――――――――――――――■");
			System.out.println("■                                    ■");
			System.out.println("■    1.로그인    2.관리자등록    0.이전 화면       ■");
			System.out.println("■                                    ■");
			System.out.println("■――――――――――――――――――――――――――――――――――――■");
			System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
			
			input = ScanUtil.nextInt();
			power = true;
			while(power == true){
				if(input == 1 || input == 2 || input == 0){
					power = false;
				}else{
					System.out.println("잘못된 번호입니다.");
					input = ScanUtil.nextInt();
				}
			}
			
			switch(input){
			case 1 : return View.WORKERLOGIN;
			case 2 : return View.WORKERJOIN;
			case 0 : return 0;
			}
		
		case 0 : 
			System.out.println("프로그램이 종료되었습니다.");
			System.exit(0);
		}
		return View.HOME;
	}

}

















