package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;

public class ScheduleDao {

	private ScheduleDao() {
	}

	private static ScheduleDao instance;

	public static ScheduleDao getInstance() {
		if (instance == null) {
			instance = new ScheduleDao();
		}
		return instance;
	}

	private JDBCUtil jdbc = JDBCUtil.getInstance();

	public int insertschedule(Map<String, Object> p) { // 스케줄표 추가
		String sql = "SELECT NVL(MAX(CODE),0)+1 MAXNUM FROM PM_SCHEDULE";
		Map<String, Object> num = jdbc.selectOne(sql);
		sql = "INSERT INTO PM_SCHEDULE(work_id,work_start,work_end,code) VALUES (?,TO_DATE( ?, 'YYYY-MM-DD HH24:MI'),TO_DATE(?, 'YYYY-MM-DD HH24:MI'),?)";

		List<Object> param = new ArrayList<>();

		param.add(p.get("WORK_ID"));
		param.add(p.get("WORK_START"));
		param.add(p.get("WORK_END"));
		param.add(num.get("MAXNUM"));

		return jdbc.update(sql, param);

	}

	// 스케줄 전체 조회
	public List<Map<String, Object>> selectScheduleList() { // 리스트 출력
		String sql = "SELECT * FROM PM_SCHEDULE";

		return jdbc.selectList(sql);
	}

	public List<Map<String, Object>> selectWorkScheduleList() {
		String sql = "SELECT * FROM PM_SCHEDULE WHERE WORK_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(Controller.loginAdmin.get("WORK_ID"));

		return jdbc.selectList(sql, param);
	}

	public Map<String, Object> findAdmin(String string, Object workid) {
		String sql = "SELECT * FROM PM_WORKER WHERE WORK_ID = ?";

		List<Object> param = new ArrayList<>();
		param.add(workid);

		return jdbc.selectOne(sql, param);
	}

	public int updateSchedule(Map<String, Object> p1) { // 업데이트 쿼리
		String sql = "UPDATE PM_SCHEDULE SET work_start = TO_DATE(?,'YYYY-MM-DD HH24:MI:SS'), work_end = TO_DATE(?,'YYYY-MM-DD HH24:MI:SS') WHERE CODE = ?";
		List<Object> param = new ArrayList<>();
		param.add(p1.get("WORK_START"));
		param.add(p1.get("WORK_END"));
		param.add(p1.get("CODE"));
		
		return jdbc.update(sql, param);
	}

	public int deleteScheudule(Map<String, Object> p2) { // 삭제쿼리
		String sql = "delete PM_SCHEDULE where CODE = ?";

		List<Object> param = new ArrayList<>();
		param.add(p2.get("CODE"));
		return jdbc.update(sql, param);
	}

	// 알바 일급여
	public Map<String, Object> worktime(Map<String, Object> workid) {
		String sql = "SELECT a.wt * b.sal sal FROM"
				+ " (SELECT work_id,(TRUNC(work_end,'MI') - TRUNC(work_start,'MI'))*1440/60 wt"
				+ " FROM PM_SCHEDULE )a, (SELECT work_id,sal FROM PM_WORKER ) b"
				+ " WHERE a.WORK_ID = ? AND b.work_id = ?";
		List<Object> param = new ArrayList<>();
		param.add(workid.get("WORK_ID"));
		param.add(workid.get("WORK_ID"));
		return jdbc.selectOne(sql, param);
	}

	public List<Map<String, Object>> selectScheduleNum(int code) {
		String sql = "SELECT CODE FROM PM_SCHEDULE";
		
		return jdbc.selectList(sql);
	}

	public List<Map<String, Object>> selectScheduleName(String name) {
		String sql = "SELECT WORK_ID FROM PM_WORKER";
	
		return jdbc.selectList(sql);
	}

}
