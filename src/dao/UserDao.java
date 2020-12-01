package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;
import util.ScanUtil;

public class UserDao {

	private UserDao() {
	}

	private static UserDao instance;

	public static UserDao getInstance() {
		if (instance == null) {
			instance = new UserDao();
		}
		return instance;
	}

	private JDBCUtil jdbc = JDBCUtil.getInstance();

	public int insertUser(Map<String, Object> p) {
		String sql = "INSERT INTO PM_CUSTOMER VALUES (?, ?, ?, ?, ?, null)";

		List<Object> param = new ArrayList<>();

		param.add(p.get("CUS_ID"));
		param.add(p.get("NAME"));
		param.add(p.get("PASSWORD"));
		param.add(p.get("HP"));
		param.add(p.get("EMAIL"));

		return jdbc.update(sql, param);

	}

	public Map<String, Object> selectUser(String userId, String password) {
		String sql = "SELECT CUS_ID, PASS" + " FROM PM_CUSTOMER"
				+ " WHERE CUS_ID = ?" + " AND PASS = ?";
		List<Object> param = new ArrayList<>();
		param.add(userId);
		param.add(password);
		return jdbc.selectOne(sql, param);
	}

	public Map<String, Object> finduser(String string, Object userId) {
		String sql = "SELECT * FROM PM_CUSTOMER WHERE CUS_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(userId);
		return jdbc.selectOne(sql, param);
	}

	// 마이페이지
	public Map<String, Object> selectList(Object userId) {
		String sql = "SELECT CUS_ID, NAME, PASS, HP,  EMAIL"
				+ " FROM PM_CUSTOMER" + " WHERE CUS_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(userId);

		return jdbc.selectOne(sql, param);
	}

	// 마이페이지 - 수정
	public int updatemypage(Object userId) {
		boolean power, power1;
		System.out.println("수정할 이름 입력");
		String Name = ScanUtil.nextLine();
		System.out.println("수정할 비밀번호 입력");
		String Pass = ScanUtil.nextLine();
		power = Pass.equals(Controller.LoginUser.get("PASS"));
		while (power == true) {
			System.out.println("기존 비밀번호와 같습니다.\n새로운 비밀번호를 입력해 주세요.");
			Pass = ScanUtil.nextLine();
			power = Pass.equals(Controller.LoginUser.get("PASS"));
		}
		System.out.println("수정할 연락처 입력");
		String Hp = ScanUtil.nextLine();
		System.out.println("수정할 이메일 입력");
		String Email = ScanUtil.nextLine();
		power = true;
		while(power == true){
			String id = "\\w+@\\w+\\.\\w+(\\.\\w+)?";
			power1 = Email.matches(id);
			if(power1 == true){
				power = false;
			}else if(power1 == false){
				System.out.println("잘못된 양식의 이메일 주소입니다.\n다시 작성하여 주세요.\n예)example@naver.com");
				System.out.println("이메일 >");
				Email = ScanUtil.nextLine();
			}
		}
		String sql = "UPDATE PM_CUSTOMER SET NAME = ?, PASS = ? , HP = ? , EMAIL = ?"
				+ " WHERE CUS_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(Name);
		param.add(Pass);
		param.add(Hp);
		param.add(Email);
		param.add(userId);
		return jdbc.update(sql, param);
	}

	// 마이페이지 - 삭제(EXERD에서 관계값 변경 필요(CASCADE))
	public int deletemypage(Object userId) {
		String sql = "DELETE PM_CUSTOMER WHERE CUS_ID = ?";
		// sql = "DELETE PM_CUS_T WHERE CUS_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(userId);
		return jdbc.update(sql, param);
	}

	//정액제 결제 날짜,금액
		public List<Map<String, Object>> paymentmanagement(){
			String sql = "SELECT flat_name, pay_date, flat_price FROM pm_flat_pay p, pm_flat_rate r "
					+ "WHERE p.flat_no = r.flat_no AND CUS_ID = ?"
					+ " ORDER BY p.pay_no";
			
			List<Object> param = new ArrayList<>();
			
			param.add(Controller.LoginUser.get("CUS_ID"));
			
			return jdbc.selectList(sql,param);
		}
		
	
	public List<Map<String, Object>> paymentmanagement1(){
		String sql = "SELECT name, pay_date, food_pr FROM pm_food_pay p, pm_food f, pm_order o"
				+ " WHERE p.pay_no = o.pay_no AND CUS_ID = ? "
				+ " AND o.food_no = f.food_no";
		
		List<Object> param = new ArrayList<>();
		
		param.add(Controller.LoginUser.get("CUS_ID"));
		
		return jdbc.selectList(sql,param);

	}
	//남은 정액제 시간 조회
	public Map<String, Object> userlefthour(){
		
		String sql = "SELECT NVL(SUM(aa),0) total FROM"
				+ " (SELECT  CASE WHEN lefttime < 0 THEN 0 WHEN lefttime >= 0 THEN lefttime END aa FROM "
				+ "(SELECT FLAT_TIME - trunc((SYSDATE - PAY_DATE)*24,0) lefttime, "
				+ "FLAT_TIME FROM pm_flat_pay p, pm_flat_rate r, pm_customer c "
				+ "WHERE p.flat_no = r.flat_no AND c.cus_id = p.cus_id AND c.CUS_ID = ? AND c.PC_ON = ?))";
		List<Object> param = new ArrayList<>();
		param.add(Controller.LoginUser.get("CUS_ID"));
		return jdbc.selectOne(sql,param);
	}
	
	//남은 총 정액제 시간, 남은 총 결제금액
	public Map<String, Object> flattime(){
		String sql = "SELECT NVL(TO_CHAR(MAX(pay_date)),0) md, NVL(SUM(R.FLAT_time),0) ft, NVL(SUM(flat_price),0) fp"
				+ " FROM pm_customer c, pm_flat_pay p, pm_flat_rate r"
				+ " WHERE C.CUS_ID = P.CUS_ID"
				+ "  AND P.FLAT_NO = R.FLAT_NO AND PAY_DATE+ FLAT_TIME/24 > sysdate";
		
		return jdbc.selectOne(sql);
				 
	}

	public Map<String, Object> selectUserlefthour(String inputUser) {
		
		String sql = "SELECT NVL(SUM(aa),0) total FROM"
				+ " (SELECT  CASE WHEN lefttime < 0 THEN 0 WHEN lefttime >= 0 THEN lefttime END aa FROM "
				+ "(SELECT FLAT_TIME - trunc((SYSDATE - PAY_DATE)*24,0) lefttime, "
				+ "FLAT_TIME FROM pm_flat_pay p, pm_flat_rate r, pm_customer c "
				+ "WHERE p.flat_no = r.flat_no AND c.cus_id = p.cus_id AND c.CUS_ID = ?))";
		List<Object> param = new ArrayList<>();
		param.add(inputUser);
		return jdbc.selectOne(sql,param);
		
		
	}

	public Map<String, Object> selectFlattime(String inputUser) {
		String sql = "SELECT NVL(TO_CHAR(MAX(pay_date)),0) md, NVL(SUM(R.FLAT_time),0) ft, NVL(SUM(flat_price),0) fp"
				+ " FROM pm_customer c, pm_flat_pay p, pm_flat_rate r"
				+ " WHERE C.CUS_ID = P.CUS_ID"
				+ "  AND P.FLAT_NO = R.FLAT_NO AND PAY_DATE+ FLAT_TIME/24 > sysdate AND C.CUS_ID = ? ";
		
		List<Object> param = new ArrayList<>();
		param.add(inputUser);
		return jdbc.selectOne(sql,param);
	}

	public void loginOn() {
		String sql = "UPDATE PM_CUSTOMER SET PC_ON = SYSDATE, PC_ST = 1 WHERE CUS_ID = ?";
		List<Object> param = new ArrayList<>();
		
		param.add(Controller.LoginUser.get("CUS_ID"));
		
		jdbc.update(sql, param);
		
		
		
	}

	public void pcOff() {

		String sql = "UPDATE PM_CUSTOMER SET PC_ON = null, PC_ST = 0 WHERE CUS_ID = ?";
		List<Object> param = new ArrayList<>();
		
		param.add(Controller.LoginUser.get("CUS_ID"));
		
		jdbc.update(sql, param);
		
		
	}
	
}
