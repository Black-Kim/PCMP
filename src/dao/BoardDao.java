package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;
import util.ScanUtil;

public class BoardDao {
	
	private BoardDao(){}
	private static BoardDao instance;
	public static BoardDao getInstance(){
		if(instance == null){
			instance = new BoardDao();
		}
		return instance;
	}
	
	private JDBCUtil jdbc = JDBCUtil.getInstance();
	
	public List<Map<String, Object>> selectBoardList(){
		String sql = "SELECT BOR_NO, BOR_DATE, BOR_COMT, TITLE , BOR_CHECK, CONT, B.NAME" +
				 " FROM PM_BD_T A" +
				 " , PM_CUS_T B" +
				 " WHERE A.CUS_ID = B.CUS_ID" +
				 " ORDER BY BOR_NO DESC";
	
		return jdbc.selectList(sql);	
		
	}
	
	
	public int insertBoardList(){
		String sql = "SELECT NVL(MAX(BOR_NO),0)+1 MAXNUM FROM PM_BD_T";
		
		Map<String, Object> num = jdbc.selectOne(sql);
	
	
		List<Object> param = new ArrayList<>();
		
		
		
		System.out.print("등록 메뉴 : 1.음식주문\t2.애로사항등록\n입력");
		int input = ScanUtil.nextInt();
		String title = " ";
		while (title.equals(" ")) {
			switch (input) {
			case 1:
				title = "음식주문";
				break;

			case 2:
				title = "애로사항";
				break;

			default:
				System.out.println("1,2번만 선택하세요");
				break;
			}
		}
		System.out.println("내용을 입력해주세요");
		String content = ScanUtil.nextLine();
		
		sql = "INSERT INTO PM_BD_T VALUES(?,SYSDATE,?,?,?,?,?)";
		param.add(num.get("MAXNUM"));
		param.add(" ");
		param.add(" ");
		param.add(content);
		param.add(title);
		param.add(Controller.LoginUser.get("CUS_ID"));
		
		
		
		return jdbc.update(sql, param);
		
		
	}
	
	
	public Map<String, Object> selectList(int num){

		
		String sql = "SELECT BOR_NO, BOR_DATE, BOR_COMT, TITLE , BOR_CHECK, CONT, B.NAME" +
				 " FROM PM_BD_T A" +
				 " , PM_CUS_T B" +
				 " WHERE A.CUS_ID = B.CUS_ID AND BOR_NO = ? ";
		
		List<Object> param = new ArrayList<>();
		param.add(num);
		
	
		return jdbc.selectOne(sql,param);
		
	}


	public int deleteBoardList(int num) {
		List<Object> param = new ArrayList<>();
		
		String sql = "SELECT USER_ID FROM TB_JDBC_BOARD WHERE BOARD_NO = ?";
		param.add(num);
		
		Map<String, Object> name = jdbc.selectOne(sql,param);
		

		
		if(Controller.LoginUser.get("USER_NAME").equals(name.get("USER_ID")) ){
		
		sql = "DELETE TB_JDBC_BOARD WHERE BOARD_NO = ?";

			System.out.println("삭제되었습니다.");

			return jdbc.update(sql, param);
		} else {
			System.out.println("작성자 본인이 아닙니다.");
			return 0;
		}
	}


	public int updateBoardList(int num) {
		List<Object> param1 = new ArrayList<>();
		
		String sql = "SELECT USER_ID FROM TB_JDBC_BOARD WHERE BOARD_NO = ?";
		param1.add(num);
		
		
		Map<String, Object> name = jdbc.selectOne(sql,param1);

		if(Controller.LoginUser.get("USER_NAME").equals(name.get("USER_ID"))  ){
		
		System.out.println("수정할 제목 입력");
		String title = ScanUtil.nextLine();
		System.out.println("수정할 내용 입력");
		String content = ScanUtil.nextLine();
		
		sql = "UPDATE TB_JDBC_BOARD SET TITLE = ?, CONTENT = ? WHERE BOARD_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(title);
		param.add(content);
		param.add(num);
		
		System.out.println("수정되었습니다.");
		
		return jdbc.update(sql, param);
		}else{
			System.out.println("작성자 본인이 아닙니다");
			return 0;
			
		}
	}
	
}











