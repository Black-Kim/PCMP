package service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.ScanUtil;
import util.View;
import dao.BoardDao;
import dao.UserDao;

public class BoardService {
	
	private BoardService(){}
	private static BoardService instance;
	public static BoardService getInstance(){
		if(instance == null){
			instance = new BoardService();
		}
		return instance;
	}
	
	private BoardDao boardDao = BoardDao.getInstance();
	
	public int boardList(){
		List<Map<String, Object>> boardList = boardDao.selectBoardList();
		System.out.println("======================================");
		System.out.println("번호\t제목\t작성자\t작성일");
		System.out.println("--------------------------------------");
		for(int i = 0; i < boardList.size(); i++){
			System.out.println(boardList.get(i).get("BOARD_NO") + "\t" +
							   boardList.get(i).get("TITLE") + "\t" +
							   boardList.get(i).get("USER_NAME") + "\t" +
							   boardList.get(i).get("REG_DATE"));
		}
		System.out.println("======================================");
		System.out.println("1.조회\t2.등록\t0.로그아웃");
		System.out.println("입력 >");
		
		int input = ScanUtil.nextInt();
		
		switch(input){
		case 1 : jo(); break;
		case 2 : create();
				 System.out.println("등록이 완료되었습니다.");
				 break;//등록
		case 0 ://로그아웃
			Controller.loginUser = null;
			return View.HOME;
		}
		return View.BOARD_LIST;
	}
	
	
	public void jo() {
		System.out.println("조회하실 게시글 번호를 적어주세요.");
		int board_no = ScanUtil.nextInt();
		Map<String, Object> param = new HashMap<>();
		param.put("BOARD_NO", board_no);
		Map<String, Object> selectOne = boardDao.jo(param);
		
		System.out.println("=========================================");
		for(int i = 0; i < 1; i++){
			System.out.println("게시글 번호 : "+"\t"+selectOne.get("BOARD_NO"));
			System.out.println("제          목 : "+"\t"+selectOne.get("TITLE"));
			System.out.println("내          용 : "+"\t"+selectOne.get("CONTENT"));
			System.out.println("작    성   자 : "+"\t"+selectOne.get("USER_ID"));
			System.out.println("작    성   일 : "+"\t"+selectOne.get("GEG_DATE"));
		}
		System.out.println("=========================================");
		System.out.println("1.수정\t2.삭제\t3.돌아가기");
		System.out.println("입력 >");
		int input = ScanUtil.nextInt();
		
		
		if(selectOne.get("USER_ID").equals(Controller.loginUser.get("USER_NAME"))){
			switch(input){
			case 1 :
				System.out.println("수정 할 제목을 입력하세요");
				String title = ScanUtil.nextLine();
				System.out.println("수정 할 내용을 입력하세요");
				String content = ScanUtil.nextLine();
				param.put("TITLE", title);
				param.put("CONTENT", content);
				
				int result = boardDao.update(param);
				if(0 < result){
					System.out.println("게시글이 수정 되었습니다.");
				}else{
					System.out.println("게시글 수정 실패");
				}
				break;
			case 2 :
				result = boardDao.delete(param);
				if(0 < result){
					System.out.println("게시글이 삭제 되었습니다.");
				}else{
					System.out.println("게시글 삭제 실패");
				}
				break;
			case 3 :
				break;
			}
			
		} else {
			System.out.println("작성자가 아닙니다.");
		}
	}


	private void create() {
		System.out.println("제목 >");
		String title = ScanUtil.nextLine();
		System.out.println("내용 >");
		String content = ScanUtil.nextLine();
		
		Map<String, Object> param = new HashMap<>();
		param.put("TITLE", title);
		param.put("CONTENT", content);
		param.put("USER_NAME", Controller.loginUser.get("USER_NAME"));
		
		int result = boardDao.create(param);
		
		if(0 < result){
			System.out.println("게시글이 등록되었습니다.");
		}else{
			System.out.println("게시글 등록 실패");
		}
	}
	
}
