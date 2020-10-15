package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class UserDao {
	
	private UserDao(){}
	private static UserDao instance;
	public static UserDao getInstance(){
		if(instance == null){
			instance = new UserDao();
		}
		return instance;
	}
	
	private JDBCUtil jdbc = JDBCUtil.getInstance();
	
	public int insertUser(Map<String, Object> p){
		String sql = "INSERT INTO PM_CUS_T VALUES (?, ?, ?, ?, ?, ?)";

		List<Object> param = new ArrayList<>();

		param.add(p.get("CUS_ID"));
		param.add(p.get("NAME"));
		param.add(p.get("PASSWORD"));
		param.add(p.get("HP"));
		param.add(p.get("FIX_TIME"));
		param.add(p.get("EMAIL"));

		return jdbc.update(sql, param);

	}

	public Map<String, Object> selectUser(String userId, String password) {
		String sql = "SELECT CUS_ID, PASS" + " FROM PM_CUS_T"
				+ " WHERE CUS_ID = ?" + " AND PASS = ?";
		List<Object> param = new ArrayList<>();
		param.add(userId);
		param.add(password);
		return jdbc.selectOne(sql, param);
	}

	public Map<String, Object> finduser(String string, Object userId) {
		String sql = "SELECT * FROM PM_CUS_T WHERE CUS_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(userId);
		return jdbc.selectOne(sql, param);
	}
	
}
















