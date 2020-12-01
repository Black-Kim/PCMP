package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;
import util.ScanUtil;

public class StaffDao {
	
	private StaffDao(){}
	private static StaffDao instance;
	public static StaffDao getInstance(){
		if(instance == null){
			instance = new StaffDao();
		}
		return instance;
	}
	JDBCUtil jdbc = JDBCUtil.getInstance();
	boolean power = true;
	boolean power1;
	//직원 리스트 전체
	public List<Map<String, Object>> staffList() {
		String sql = "SELECT WORK_ID, NAME, SAL FROM PM_WORKER";
			
		return jdbc.selectList(sql);
	}
	
	//직원 한명 조회
	public Map<String, Object> staffone(Object staffcol) {
		String sql = "SELECT WORK_ID, REG_NO, NAME, PASS, HP, EMAIL, ADDR, SAL, CODE"
				+ " FROM PM_WORKER WHERE WORK_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(staffcol);
		return jdbc.selectOne(sql, param);
	}
	
	//직원 정보 수정
	//주민등록번호
	public int updatereg_no(Object staffcol) {
		String sql = "UPDATE PM_WORKER SET REG_NO = ? WHERE WORK_ID = ?";
		System.out.println("수정할 주민등록번호를 적으세요>");
		String reg_no = ScanUtil.nextLine();
		power = true;
		while (power == true) {
			if(reg_no.length() == 13){
				power = false;
			}else if(reg_no.length() < 13){
				System.out.println("주민등록번호가 잘못되었습니다.(13자리 미만)\n다시 입력하세요.");
				System.out.println("주민등록번호 >");
				reg_no = ScanUtil.nextLine();
			}else if(reg_no.length() > 13){
				System.out.println("주민등록번호가 잘못되었습니다.(13자리 초과)\n다시 입력하세요.");
				System.out.println("주민등록번호 >");
				reg_no = ScanUtil.nextLine();
			}
		}
		List<Object> param = new ArrayList<>();
		param.add(reg_no);
		param.add(staffcol);
		return jdbc.update(sql, param);
	}
	//이름
	public int updatename(Object staffcol) {
		String sql = "UPDATE PM_WORKER SET NAME = ? WHERE WORK_ID = ?";
		System.out.println("수정할 이름을 적으세요>");
		String name = ScanUtil.nextLine();
		List<Object> param = new ArrayList<>();
		param.add(name);
		param.add(staffcol);
		return jdbc.update(sql, param);
	}
	//패스워드
	public int updatepass(Object staffcol) {
		String sql = "UPDATE PM_WORKER SET PASS = ? WHERE WORK_ID = ?";
		System.out.println("수정할 비밀번호를 적으세요>");
		String pass = ScanUtil.nextLine();
		List<Object> param = new ArrayList<>();
		param.add(pass);
		param.add(staffcol);
		return jdbc.update(sql, param);
	}
	//연락처
	public int updatehp(Object staffcol) {
		String sql = "UPDATE PM_WORKER SET HP = ? WHERE WORK_ID = ?";
		System.out.println("수정할 연락처를 적으세요>");
		String hp = ScanUtil.nextLine();
		List<Object> param = new ArrayList<>();
		param.add(hp);
		param.add(staffcol);
		return jdbc.update(sql, param);
	}
	//이메일
	public int updateemail(Object staffcol) {
		String sql = "UPDATE PM_WORKER SET EMAIL = ? WHERE WORK_ID = ?";
		System.out.println("수정할 이메일을 적으세요>");
		String email = ScanUtil.nextLine();
		power = true;
		while(power == true){
			String id = "\\w+@\\w+\\.\\w+(\\.\\w+)?";
			power1 = email.matches(id);
			if(power1 == true){
				power = false;
			}else if(power1 == false){
				System.out.println("잘못된 양식의 이메일 주소입니다.\n다시 작성하여 주세요.\n예)example@naver.com");
				System.out.println("이메일 >");
				email = ScanUtil.nextLine();
			}
		}
		List<Object> param = new ArrayList<>();
		param.add(email);
		param.add(staffcol);
		return jdbc.update(sql, param);
	}
	//주소
	public int updateaddr(Object staffcol) {
		String sql = "UPDATE PM_WORKER SET ADDR = ? WHERE WORK_ID = ?";
		System.out.println("수정할 주소를 적으세요>");
		String addr = ScanUtil.nextLine();
		List<Object> param = new ArrayList<>();
		param.add(addr);
		param.add(staffcol);
		return jdbc.update(sql, param);
	}
	//급여
	public int updatesal(Object staffcol) {
		String sql = "UPDATE PM_WORKER SET SAL = ? WHERE WORK_ID = ?";
		System.out.println("수정할 급여를 적으세요>");
		int sal = ScanUtil.nextInt();
		List<Object> param = new ArrayList<>();
		param.add(sal);
		param.add(staffcol);
		return jdbc.update(sql, param);
	}

	public int deletestaff(Object staffcol) {
		String sql = "DELETE PM_WORKER WHERE WORK_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(staffcol);
		return jdbc.update(sql, param);
	}


	
	

	
	
}