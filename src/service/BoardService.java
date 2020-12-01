package service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.ScanUtil;
import util.View;
import dao.BoardDao;
import dao.UserDao;

public class BoardService {

	private BoardService() {
	}

	private static BoardService instance;

	public static BoardService getInstance() {
		if (instance == null) {
			instance = new BoardService();
		}
		return instance;
	}

	private BoardDao boardDao = BoardDao.getInstance();

	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");

	public int boardList() {
		List<Map<String, Object>> boardList = boardDao.selectBoardList();
		System.out.println("======================================");
		System.out.println("번호\t제목\t작성자\t작성일\t답변여부");
		System.out.println("--------------------------------------");
		for (int i = 0; i < boardList.size(); i++) {
			System.out.println(boardList.get(i).get("BOR_NO") + "\t"
					+ boardList.get(i).get("TITLE") + "\t"
					+ boardList.get(i).get("NAME") + "\t"
					+ boardList.get(i).get("BOR_DATE") + "\t"
					+ boardList.get(i).get("BOR_CHECK"));
		}
		System.out.println("======================================");
		System.out.println("1.조회\t2.등록\t0.이전");
		System.out.println("입력 >");

		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:// 조회
			select();
			break;
		case 2:// 등록
			boardDao.insertBoardList();
			break;
		case 0:// 종료
			return View.SELECT_LIST;
		}

		return View.BOARD_LIST;

	}

	private void select() {

		System.out.print("조회할 번호를 입력해주세요 : ");

		int num = ScanUtil.nextInt();

		Map<String, Object> boardList = boardDao.selectList(num);

		System.out.println("================================");

		System.out.println("게시글 번호 : " + " " + boardList.get("BOR_NO"));
		System.out.println("제          목 : " + " " + boardList.get("TITLE"));
		System.out.println("내          용 : " + " " + boardList.get("CONTENT"));
		System.out.println("작    성   자 : " + " " + boardList.get("NAME"));
		System.out.println("작 성 자  ID: " + " " + boardList.get("CUS_ID"));
		System.out.println("답 변  내 용 : " + " " + boardList.get("BOR_COMM"));

		System.out.println("================================");
		System.out.println("1.수정\t2.삭제\t0.이전");
		System.out.print("입력>");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1:
			// 수정
			boardDao.updateBoardList(num);
			break;

		case 2:
			// 삭제
			boardDao.deleteBoardList(num);
			break;

		default:
			break;
		}
	}

	UserDao userdao = UserDao.getInstance();
	public int selectList() {

		System.out.println("=========================================");
		System.out.println("1.게시판 조회\t2.마이페이지\t0.로그아웃");
		System.out.println("=========================================");
		System.out.print("입력 >");
		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:
			return View.BOARD_LIST;
		case 2:
			return View.MY_PAGE;
		case 0:
			
		userdao.pcOff();
			
			Controller.LoginUser = null;
			return View.HOME;
		default:
			return View.SELECT_LIST;
		}

	}

}
