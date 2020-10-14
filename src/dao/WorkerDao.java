package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import service.WorkerService;
import util.JDBCUtil;

public class WorkerDao {
	
	private WorkerDao(){}
	private static WorkerDao instance;
	public static WorkerDao getInstance(){
		if(instance == null){
			instance = new WorkerDao();
		}
		return instance;
	}
	
	private JDBCUtil jdbc = JDBCUtil.getInstance();
	
	public int insertUser(Map<String, Object> p){ //회원 가입
		String sql = "INSERT INTO TB_JDBC_USER VALUES (?, ?, ?)";
		
		List<Object> param = new ArrayList<>();
		param.add(p.get("USER_ID"));
		param.add(p.get("PASSWORD"));
		param.add(p.get("USER_NAME"));
		
		return jdbc.update(sql, param);
	}
	
	public int insertAdmin(Map<String, Object> p){ //관리자 가입
		String sql = "INSERT INTO TB_JDBC_USER VALUES (?, ?, ?)";
		
		List<Object> param = new ArrayList<>();
		param.add(p.get("USER_ID"));
		param.add(p.get("PASSWORD"));
		param.add(p.get("USER_NAME"));
		
		return jdbc.update(sql, param);
	}

	public Map<String, Object> selectUser(String userId, String password) {
		String sql = "SELECT USER_ID, USER_NAME" +
	                 " FROM TB_JDBC_USER" +
				     " WHERE USER_ID = ?" +
	                 " AND PASSWORD = ?";
		List<Object> param = new ArrayList<>();
		param.add(userId);
		param.add(password);
		return jdbc.selectOne(sql, param);
	}

	public Map<String, Object> finduser(String string, Object userId) {
		String sql = "SELECT * FROM TB_JDBC_USER WHERE USER_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(userId);
		return jdbc.selectOne(sql, param);
	}

}
