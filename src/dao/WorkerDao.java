package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import service.WorkerService;
import util.JDBCUtil;

public class WorkerDao {

	private WorkerDao() {
	}

	private static WorkerDao instance;

	public static WorkerDao getInstance() {
		if (instance == null) {
			instance = new WorkerDao();
		}
		return instance;
	}

	private JDBCUtil jdbc = JDBCUtil.getInstance();

	public int insertAdmin(Map<String, Object> p) { // 관리자 가입
		String sql = "INSERT INTO PM_WORKER VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		List<Object> param = new ArrayList<>();

		param.add(p.get("WORK_ID"));
		param.add(p.get("REG_NO"));
		param.add(p.get("Name"));
		param.add(p.get("PASSWORD"));
		param.add(p.get("HP"));
		param.add(p.get("EMAIL"));
		param.add(p.get("ADDR"));
		param.add(p.get("SAL"));
		param.add(p.get("CODE"));

		return jdbc.update(sql, param);
	}

	public Map<String, Object> selectAdmin(String workId, String password) {
		String sql = "SELECT WORK_ID, PASS, code" + " FROM PM_WORKER"
				+ " WHERE WORK_ID = ?" + " AND PASS = ?";
		List<Object> param = new ArrayList<>();
		param.add(workId);
		param.add(password);
		return jdbc.selectOne(sql, param);
	}

	public Map<String, Object> finduser(String string, Object userId) {
		String sql = "SELECT * FROM PM_WORKER WHERE WORK_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(userId);
		return jdbc.selectOne(sql, param);
	}

}
