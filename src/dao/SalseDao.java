package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;

public class SalseDao {

	private SalseDao() {
	}

	private static SalseDao instance;

	public static SalseDao getInstance() {
		if (instance == null) {
			instance = new SalseDao();
		}
		return instance;
	}

	private JDBCUtil jdbc = JDBCUtil.getInstance();
	
	//일매출
	public Map<String, Object> daySales(Map<String, Object> toda) {
		String sql ="SELECT  b.sum + c.sf - d.days one FROM "
				+ "(SELECT NVL(SUM(a.pay),0) sum FROM (SELECT b.name, (b.FOOD_PR * AMOUNT) PAY FROM PM_ORDER a, PM_FOOD b, pm_food_pay c WHERE a.FOOD_NO = b.FOOD_NO AND c.PAY_NO = a.PAY_NO AND TO_CHAR(c.PAY_DATE,'yyyymmdd') = TO_DATE(?,'yyyymmdd'))a)b, "
				+ "(SELECT NVL(SUM(a.FLAT_PRICE),0) sf FROM PM_FLAT_RATE a, pm_flat_pay b WHERE a.FLAT_NO = b.FLAT_NO AND TO_CHAR(b.PAY_DATE,'yyyymmdd') = TO_DATE(?,'yyyymmdd'))c, "
				+ "(SELECT NVL(SUM(a.wt * b.sal),0) days FROM (SELECT WORK_START,work_id,(TRUNC(work_end,'MI') - TRUNC(work_start,'MI'))*1440/60 wt FROM PM_SCHEDULE )a , (SELECT work_id,sal FROM PM_WORKER ) b WHERE TO_CHAR(a.work_start,'yyyymmdd') = TO_DATE(?,'yyyymmdd'))d";
		List<Object> param = new ArrayList<>();
		param.add(toda.get("DATE"));
		param.add(toda.get("DATE"));
		param.add(toda.get("DATE"));
		return jdbc.selectOne(sql, param);
	}
	
	//총매출
	public Map<String, Object> totalSales() {
		String sql ="SELECT  b.sum + c.sf - d.days sales FROM "
				+ "(SELECT NVL(SUM(a.pay),0) sum FROM (SELECT b.name, (b.FOOD_PR * AMOUNT) PAY FROM PM_ORDER a, PM_FOOD b, pm_food_pay c WHERE a.FOOD_NO = b.FOOD_NO AND c.PAY_NO = a.PAY_NO )a)b, "
				+ "(SELECT NVL(SUM(a.FLAT_PRICE),0) sf FROM PM_FLAT_RATE a, pm_flat_pay b WHERE a.FLAT_NO = b.FLAT_NO )c, "
				+ "(SELECT NVL(SUM(a.wt * b.sal),0) days FROM (SELECT WORK_START,work_id,(TRUNC(work_end,'MI') - TRUNC(work_start,'MI'))*1440/60 wt FROM PM_SCHEDULE )a , (SELECT work_id,sal FROM PM_WORKER ) b)d";
		
		return jdbc.selectOne(sql);
		
	}
	
	//월매출
	public Map<String, Object> monthSales(Map<String, Object> month) {
		String sql ="SELECT  b.sum + c.sf - d.days one FROM "
				+ "(SELECT NVL(SUM(a.pay),0) sum FROM (SELECT b.name, (b.FOOD_PR * AMOUNT) PAY FROM PM_ORDER a, PM_FOOD b, pm_food_pay c WHERE a.FOOD_NO = b.FOOD_NO AND c.PAY_NO = a.PAY_NO AND TO_CHAR(c.PAY_DATE,'yyyymm') = TO_CHAR(TO_DATE(?,'yyyymm'),'yyyymm'))a)b, "
				+ "(SELECT NVL(SUM(a.FLAT_PRICE),0) sf FROM PM_FLAT_RATE a, pm_flat_pay b WHERE a.FLAT_NO = b.FLAT_NO AND TO_CHAR(b.PAY_DATE,'yyyymm') = TO_CHAR(TO_DATE(?,'yyyymm'),'yyyymm'))c, "
				+ "(SELECT NVL(SUM(a.wt * b.sal),0) days FROM (SELECT WORK_START,work_id,(TRUNC(work_end,'MI') - TRUNC(work_start,'MI'))*1440/60 wt FROM PM_SCHEDULE )a , (SELECT work_id,sal FROM PM_WORKER ) b WHERE TO_CHAR(a.work_start,'yyyymm') = TO_CHAR(TO_DATE(?,'yyyymm'),'yyyymm'))d";
		List<Object> param = new ArrayList<>();
		param.add(month.get("DATE"));
		param.add(month.get("DATE"));
		param.add(month.get("DATE"));
		return jdbc.selectOne(sql, param);
		
	}
	
	//연매출
	public Map<String, Object> yearSales(Map<String, Object> year) {
		String sql ="SELECT  b.sum + c.sf - d.days one FROM "
				+ "(SELECT NVL(SUM(a.pay),0) sum FROM (SELECT b.name, (b.FOOD_PR * AMOUNT) PAY FROM PM_ORDER a, PM_FOOD b, pm_food_pay c WHERE a.FOOD_NO = b.FOOD_NO AND c.PAY_NO = a.PAY_NO AND TO_CHAR(c.PAY_DATE,'YYYY') = TO_CHAR(TO_DATE(?,'YYYY'),'yyyy'))a)b, "
				+ "(SELECT NVL(SUM(a.FLAT_PRICE),0) sf FROM PM_FLAT_RATE a, pm_flat_pay b WHERE a.FLAT_NO = b.FLAT_NO AND TO_CHAR(b.PAY_DATE,'YYYY') = TO_CHAR(TO_DATE(?,'YYYY'),'yyyy'))c, "
				+ "(SELECT NVL(SUM(a.wt * b.sal),0) days FROM (SELECT WORK_START,work_id,(work_end- work_start)*24 wt FROM PM_SCHEDULE )a , (SELECT work_id,sal FROM PM_WORKER ) b WHERE TO_CHAR(a.work_start,'YYYY') = TO_CHAR(TO_DATE(?,'YYYY'),'yyyy'))d";
		List<Object> param = new ArrayList<>();
		param.add(year.get("DATE"));
		param.add(year.get("DATE"));
		param.add(year.get("DATE"));
		return jdbc.selectOne(sql, param);
	}

	public List<Map<String, Object>> listDaySales(Map<String, Object> toda) {
		String sql = "SELECT flat_name, pay_date, flat_price, NVL(c.CUS_ID,'탈퇴회원') CUS_ID, NAME"
				+ " FROM pm_flat_pay p, pm_flat_rate r , PM_CUSTOMER c"
				+ " WHERE p.flat_no = r.flat_no AND p.CUS_ID = c.CUS_ID AND "
				+ " TO_CHAR(PAY_DATE,'YYYYMMDD') = ? "
				+ " ORDER BY p.pay_no";
		
		List<Object> param = new ArrayList<>();
		
		param.add(toda.get("DATE"));
		
		return jdbc.selectList(sql,param);
	}

	public List<Map<String, Object>> listDaySales1(Map<String, Object> toda) {

		String sql = "SELECT a.CUS_ID, PAY_DATE, a.NAME USERNAME, AMOUNT , FOOD_PR*AMOUNT PRICE , d.NAME FOODNAME"
				+ " FROM PM_CUSTOMER a, PM_FOOD_PAY b , PM_ORDER c, PM_FOOD d"
				+ " WHERE a.CUS_ID = b.CUS_ID AND b.PAY_NO = c.PAY_NO AND c.FOOD_NO = d.FOOD_NO AND "
				+ " TO_CHAR(PAY_DATE,'YYYYMMDD') = ? "
				+ " ORDER BY PAY_DATE";
		List<Object> param = new ArrayList<>();

		param.add(toda.get("DATE"));

		return jdbc.selectList(sql, param);
	}

	public List<Map<String, Object>> listDaySales2(Map<String, Object> toda) {
		String sql = "SELECT a.wt * b.sal PAY, a.WORK_ID, NAME, WORK_START FROM" +
				" (SELECT WORK_END,WORK_START, work_id,(TRUNC(work_end,'MI') - TRUNC(work_start,'MI'))*1440/60 wt"+
				" FROM PM_SCHEDULE )a, (SELECT work_id,sal,NAME FROM PM_WORKER ) b" +
				" WHERE a.WORK_ID = b.work_id AND TO_CHAR(WORK_END,'YYYYMMDD') = ? "
				+ " ORDER BY WORK_START ";
		List<Object> param = new ArrayList<>();

		param.add(toda.get("DATE"));

		return jdbc.selectList(sql, param);
	}

	public List<Map<String, Object>> listMonthSales(Map<String, Object> month) {
		String sql = "SELECT flat_name, pay_date, flat_price, c.CUS_ID, NAME"
				+ " FROM pm_flat_pay p, pm_flat_rate r , PM_CUSTOMER c"
				+ " WHERE p.flat_no = r.flat_no AND p.CUS_ID = c.CUS_ID AND "
				+ " TO_CHAR(PAY_DATE,'YYYYMM') = ? "
				+ " ORDER BY p.pay_no";
		
		List<Object> param = new ArrayList<>();
		
		param.add(month.get("DATE"));
		
		return jdbc.selectList(sql,param);
	}

	public List<Map<String, Object>> listMonthSales1(Map<String, Object> month) {
		String sql = "SELECT a.CUS_ID, PAY_DATE, a.NAME USERNAME, AMOUNT , FOOD_PR*AMOUNT PRICE , d.NAME FOODNAME"
				+ " FROM PM_CUSTOMER a, PM_FOOD_PAY b , PM_ORDER c, PM_FOOD d"
				+ " WHERE a.CUS_ID = b.CUS_ID AND b.PAY_NO = c.PAY_NO AND c.FOOD_NO = d.FOOD_NO AND "
				+ " TO_CHAR(PAY_DATE,'YYYYMM') = ? "
				+ " ORDER BY PAY_DATE";
		List<Object> param = new ArrayList<>();

		param.add(month.get("DATE"));

		return jdbc.selectList(sql, param);
	}

	public List<Map<String, Object>> listMonthSales2(Map<String, Object> month) {
		String sql = "SELECT a.wt * b.sal PAY, a.WORK_ID, NAME, WORK_START FROM" +
				" (SELECT WORK_END,WORK_START, work_id,(TRUNC(work_end,'MI') - TRUNC(work_start,'MI'))*1440/60 wt"+
				" FROM PM_SCHEDULE )a, (SELECT work_id,sal,NAME FROM PM_WORKER ) b" +
				" WHERE a.WORK_ID = b.work_id AND TO_CHAR(WORK_END,'YYYYMM') = ? "
				+ " ORDER BY WORK_START ";
		List<Object> param = new ArrayList<>();

		param.add(month.get("DATE"));

		return jdbc.selectList(sql, param);
	}

	public List<Map<String, Object>> listYearSales(Map<String, Object> year) {
		String sql = "SELECT flat_name, pay_date, flat_price, c.CUS_ID, NAME"
				+ " FROM pm_flat_pay p, pm_flat_rate r , PM_CUSTOMER c"
				+ " WHERE p.flat_no = r.flat_no AND p.CUS_ID = c.CUS_ID AND "
				+ " TO_CHAR(PAY_DATE,'YYYY') = ? "
				+ " ORDER BY p.pay_no";
		
		List<Object> param = new ArrayList<>();
		
		param.add(year.get("DATE"));
		
		return jdbc.selectList(sql,param);
	}

	public List<Map<String, Object>> listYearSales1(Map<String, Object> year) {
		String sql = "SELECT a.CUS_ID, PAY_DATE, a.NAME USERNAME, AMOUNT , FOOD_PR*AMOUNT PRICE , d.NAME FOODNAME"
				+ " FROM PM_CUSTOMER a, PM_FOOD_PAY b , PM_ORDER c, PM_FOOD d"
				+ " WHERE a.CUS_ID = b.CUS_ID AND b.PAY_NO = c.PAY_NO AND c.FOOD_NO = d.FOOD_NO AND "
				+ " TO_CHAR(PAY_DATE,'YYYY') = ? "
				+ " ORDER BY PAY_DATE";
		List<Object> param = new ArrayList<>();

		param.add(year.get("DATE"));

		return jdbc.selectList(sql, param);
	}

	public List<Map<String, Object>> listYearSales2(Map<String, Object> year) {
		String sql = "SELECT a.wt * b.sal PAY, a.WORK_ID, NAME, WORK_START FROM" +
				" (SELECT WORK_END,WORK_START, work_id,(TRUNC(work_end,'MI') - TRUNC(work_start,'MI'))*1440/60 wt"+
				" FROM PM_SCHEDULE )a, (SELECT work_id,sal,NAME FROM PM_WORKER ) b" +
				" WHERE a.WORK_ID = b.work_id AND TO_CHAR(WORK_END,'YYYY') = ? "
				+ " ORDER BY WORK_START ";
		List<Object> param = new ArrayList<>();

		param.add(year.get("DATE"));

		return jdbc.selectList(sql, param);
	}

	public List<Map<String, Object>> listSUMSales(Map<String, Object> sum) {
		String sql = "SELECT flat_name, pay_date, flat_price, c.CUS_ID, NAME"
				+ " FROM pm_flat_pay p, pm_flat_rate r , PM_CUSTOMER c"
				+ " WHERE p.flat_no = r.flat_no AND p.CUS_ID = c.CUS_ID "
				+ " ORDER BY p.pay_no";
		
		List<Object> param = new ArrayList<>();
		
		param.add(sum.get("DATE"));
		
		return jdbc.selectList(sql);
	}

	public List<Map<String, Object>> listSUMSales1(Map<String, Object> sum) {
		String sql = "SELECT a.CUS_ID, PAY_DATE, a.NAME USERNAME, AMOUNT , FOOD_PR*AMOUNT PRICE , d.NAME FOODNAME"
				+ " FROM PM_CUSTOMER a, PM_FOOD_PAY b , PM_ORDER c, PM_FOOD d"
				+ " WHERE a.CUS_ID = b.CUS_ID AND b.PAY_NO = c.PAY_NO AND c.FOOD_NO = d.FOOD_NO "
				+ " ORDER BY PAY_DATE";
		List<Object> param = new ArrayList<>();

		param.add(sum.get("DATE"));

		return jdbc.selectList(sql);
	}

	public List<Map<String, Object>> listSUMSales2(Map<String, Object> sum) {
		String sql = "SELECT a.wt * b.sal PAY, a.WORK_ID, NAME, WORK_START FROM" +
				" (SELECT WORK_END,WORK_START, work_id,(TRUNC(work_end,'MI') - TRUNC(work_start,'MI'))*1440/60 wt"+
				" FROM PM_SCHEDULE )a, (SELECT work_id,sal,NAME FROM PM_WORKER ) b" +
				" WHERE a.WORK_ID = b.work_id"
				+ " ORDER BY WORK_START ";
		List<Object> param = new ArrayList<>();

		param.add(sum.get("DATE"));

		return jdbc.selectList(sql);
	}
	
	
}
