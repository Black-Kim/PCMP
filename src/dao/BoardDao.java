package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

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
		String sql = "SELECT A.BOARD_NO, A.TITLE, A.CONTENT, B.USER_NAME, A.REG_DATE" +
					 " FROM TB_JDBC_BOARD A" +
					 " LEFT OUTER JOIN TB_JDBC_USER B" +
					 " ON A.USER_ID = B.USER_NAME" +
					 " ORDER BY A.BOARD_NO DESC";
		
		return jdbc.selectList(sql);	
	}


	public int create(Map<String, Object> p) {
		int num = 0;
		String sql = "select NVL(MAX(board_no),0)+1 BB from tb_jdbc_board";
		Map<String, Object> num1 = jdbc.selectOne(sql);
		System.out.println(num1);
		sql = "insert into tb_jdbc_board VALUES (?,?,?,?,SYSDATE)";
		List<Object> param = new ArrayList<>();
		param.add(num1.get("BB"));
		param.add(p.get("TITLE"));
		param.add(p.get("CONTENT"));
		param.add(p.get("USER_NAME"));
		
		
		return jdbc.update(sql, param);
	}

	public Map<String, Object> jo(Map<String, Object> p) {
		String sql = "SELECT COUNT(*) FROM tb_jdbc_board WHERE BOARD_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(p.get("BOARD_NO"));
		return jdbc.selectOne(sql, param);
	}


	public int delete(Map<String, Object> p) {
		String sql = "delete tb_jdbc_board where board_no = ?";
		List<Object> param = new ArrayList<>();
		param.add(p.get("BOARD_NO"));
		return jdbc.update(sql, param);
	}


	public int update(Map<String, Object> p) {
		String sql = "update tb_jdbc_board set title = ?, content = ? where board_no = ?";
		List<Object> param = new ArrayList<>();
		param.add(p.get("TITLE"));
		param.add(p.get("CONTENT"));
		param.add(p.get("BOARD_NO"));
		
		return jdbc.update(sql, param);
	}


}
