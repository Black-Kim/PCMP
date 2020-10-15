package controller;

import java.util.Map;

import service.BoardService;
import service.UserService;
import service.WorkerService;
import util.ScanUtil;
import util.View;


public class Controller {

	public static void main(String[] args) {
		/*
		 * 발표순서 : 조 소개 > 주제 소개 > 주제 선정 배경 > 메뉴 구조 > 시연
		 * 발표인원 : 발표자 1명, ppt 및 시연 도우미 1명
		 * 
		 * Controller : 화면 이동 -> 보기 ex)1,2,3 선택시 이동하게 구현
		 * Service : 화면 기능 -> 이동시 서비스구현
		 * Dao : 쿼리작성 ->데이터베이스 접속시 (select, insert update, delete)
		 * 
		 */
		new Controller().start();
	}
	
	
	public static Map<String, Object> LoginUser;//글쓴이의 네임 확인
	public static Map<String, Object> loginAdmin;
	
	private UserService userService = UserService.getInstance();
	private BoardService boardService = BoardService.getInstance();
	private WorkerService workerService = WorkerService.getInstance();

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

				//조회, 등록, 수정, 삭제를 구현해주세요
			}
		}
	}

	private int home() {
		System.out.println("----------------------------------");
		System.out.println("1.PC방 회원\t2.관리자");
		System.out.println("----------------------------------");
		int input = ScanUtil.nextInt();
		switch(input){
		case 1 : //회원
			System.out.println("----------------------------------");
			System.out.println("1.로그인\t2.회원가입\t0.프로그램 종료");
			System.out.println("----------------------------------");
			System.out.print("번호입력>");
			input = ScanUtil.nextInt();
			
			switch(input){
			case 1 : return View.LOGIN;
			case 2 : return View.JOIN;
			case 0 : 
				System.out.println("프로그램이 종료되었습니다.");
				System.exit(0);
			}
		case 2 : //관리자
			System.out.println("----------------------------------");
			System.out.println("1.로그인\t2.관리자등록\t0.프로그램 종료");
			System.out.println("----------------------------------");
			System.out.print("번호입력>");
			input = ScanUtil.nextInt();
			
			switch(input){
			case 1 : return View.WORKERLOGIN;
			case 2 : return View.WORKERJOIN;
			case 0 : 
				System.out.println("프로그램이 종료되었습니다.");
				System.exit(0);
			}
		}
		return View.HOME;
	}

}




















