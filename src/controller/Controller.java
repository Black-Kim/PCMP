package controller;

import java.util.Map;

import service.BoardService;
import service.UserService;
import service.WorkerService;
import util.ScanUtil;
import util.View;

public class Controller {

	public static void main(String[] args) {

		new Controller().start();

	}

	public static Map<String, Object> loginUser;
	public static Map<String, Object> loginAdmin;

	private UserService userService = UserService.getInstance();
	private BoardService boardService = BoardService.getInstance();
	private WorkerService workerService = WorkerService.getInstance();

	private void start() {
		int view = View.HOME;
		while (true) {
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
