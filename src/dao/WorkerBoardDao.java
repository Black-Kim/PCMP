package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;
import util.ScanUtil;

public class WorkerBoardDao {

	private WorkerBoardDao() {
	}

	private static WorkerBoardDao instance;

	public static WorkerBoardDao getInstance() {
		if (instance == null) {
			instance = new WorkerBoardDao();
		}
		return instance;
	}

	private JDBCUtil jdbc = JDBCUtil.getInstance();

	public List<Map<String, Object>> selectBoardList() {
		String sql = "SELECT BOR_NO, BOR_DATE, BOR_COMM, TITLE , BOR_CHECK, CONTENT, B.NAME"
				+ " FROM PM_BOARD A"
				+ " , PM_CUSTOMER B"
				+ " WHERE A.CUS_ID = B.CUS_ID" + " ORDER BY BOR_NO DESC";

		return jdbc.selectList(sql);

	}

	public Map<String, Object> selectList(int num) {

		String sql = "SELECT BOR_NO, BOR_DATE, BOR_COMM, TITLE , BOR_CHECK, CONTENT, B.NAME, A.CUS_ID "
				+ " FROM PM_BOARD A"
				+ " , PM_CUSTOMER B"
				+ " WHERE A.CUS_ID = B.CUS_ID AND BOR_NO = ? ";

		List<Object> param = new ArrayList<>();
		param.add(num);

		return jdbc.selectOne(sql, param);

	}

	
	// 전체 회원 조회
	public List<Map<String, Object>> userList(){
		String sql = "SELECT * FROM pm_customer ORDER BY cus_id";
		
		return jdbc.selectList(sql);
	}
	
	// 회원 한명 조회
		public Map<String, Object> userone(Object usercol) {
			String sql = "SELECT cus_ID, name, PASS, HP, EMAIL"
					+ " FROM PM_customer WHERE cus_ID = ?";
			List<Object> param = new ArrayList<>();
			param.add(usercol);
			return jdbc.selectOne(sql, param);
		}
		
	// 회원 수정
	public int userUpdate(Object userId){
		
//		boolean check = false;
		
		System.out.println("수정할 이름");
		String Name = ScanUtil.nextLine();
		System.out.println("수정할 비밀번호 입력");
		String Pass = ScanUtil.nextLine();
		
//		check = Pass.equals(Controller.LoginUser.get("PASS"));
//		while (check == true) {
//			System.out.println("기존 비밀번호와 같습니다.\n새로운 비밀번호를 입력해 주세요.");
//			Pass = ScanUtil.nextLine();
//			check = Pass.equals(Controller.LoginUser.get("PASS"));
//		}
		
		System.out.println("수정할 연락처 입력");
		String Hp = ScanUtil.nextLine();
		System.out.println("수정할 이메일 입력");
		String Email = ScanUtil.nextLine();
		
		boolean power, power1;
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
	
	//회원 삭제
	public int userdelete(Object userId) {
		String sql = "DELETE PM_CUSTOMER WHERE CUS_ID = ?";
		
		List<Object> param = new ArrayList<>();
		param.add(userId);
		return jdbc.update(sql, param);
	}
	
	
	public void updateBoardList(int num) {

		System.out.println("답변 내용 입력");
		String content = ScanUtil.nextLine();

		String sql = "UPDATE PM_BOARD SET BOR_COMM = ?, BOR_CHECK = 'O' WHERE BOR_NO = ?";
		List<Object> param = new ArrayList<>();

		param.add(content);
		param.add(num);

		System.out.println("수정되었습니다.");

		jdbc.update(sql, param);

	}

	public void deleteBoardList(int num) {

		String sql = "DELETE PM_BOARD WHERE BOR_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(num);
		System.out.println("삭제되었습니다.");

		jdbc.update(sql, param);

	}

	public List<Map<String, Object>> selectFlatRateList() {
		String sql = "SELECT * FROM PM_FLAT_RATE ORDER BY FLAT_NO";

		return jdbc.selectList(sql);
	}

	public void insertFlatrateList() {

		System.out.println("추가할 상품의 단가을 입력해주세요");
		int price = ScanUtil.nextInt();
		System.out.println("추가할 상품의 시간을 입력해주세요");
		int time = ScanUtil.nextInt();
		String name = price + "원(" + time + "시간)";
		List<Object> param = new ArrayList<>();
		param.add(price);
		param.add(name);
		param.add(time);

		String sql = "INSERT INTO PM_FLAT_RATE VALUES((SELECT NVL(MAX(FLAT_NO),0)+1 MAXNUM FROM PM_FLAT_RATE) , ? , ? , ? )";

		System.out.println("상품 등록 성공");
		jdbc.update(sql, param);
	}

	public void updateFlatrateList() {
		Boolean check = false;
		System.out.println("변경할 상품의 번호을 입력해주세요");
		int num = ScanUtil.nextInt();
		String sql = "SELECT FLAT_NO FROM PM_FLAT_RATE";
		List<Map<String, Object>> temp = jdbc.selectList(sql);

		for (int i = 0; i < temp.size(); i++) {
			if (Integer.parseInt(String.valueOf(temp.get(i).get("FLAT_NO"))) == num) {
				check = true;
			}
		}

		if (check == true) {
			System.out.println("상품의 단가을 입력해주세요");
			int price = ScanUtil.nextInt();
			System.out.println("상품의 시간을 입력해주세요");
			int time = ScanUtil.nextInt();
			String name = price + "원(" + time + "시간)";
			List<Object> param1 = new ArrayList<>();
			param1.add(price);
			param1.add(name);
			param1.add(time);
			param1.add(num);

			sql = "UPDATE PM_FLAT_RATE SET FLAT_PRICE = ?, FLAT_NAME = ?, FLAT_TIME = ? WHERE FLAT_NO = ?";

			jdbc.update(sql, param1);
			System.out.println("수정되었습니다.");
		} else {
			System.out.println("해당 번호는 존재하지 않습니다.");
		}
	}

	public void deleteFlatrateList() {
		Boolean check = false;
		System.out.println("변경할 상품의 번호을 입력해주세요");
		int num = ScanUtil.nextInt();
		String sql = "SELECT FLAT_NO FROM PM_FLAT_RATE";
		List<Map<String, Object>> temp = jdbc.selectList(sql);

		for (int i = 0; i < temp.size(); i++) {
			if (Integer.parseInt(String.valueOf(temp.get(i).get("FLAT_NO"))) == num) {
				check = true;
			}
		}

		if (check == true) {
			List<Object> param1 = new ArrayList<>();
			param1.add(num);
			sql = "DELETE PM_FLAT_RATE WHERE FLAT_NO = ?";
			jdbc.update(sql, param1);
			System.out.println("삭제되었습니다.");
		} else {
			System.out.println("해당 번호는 존재하지 않습니다.");
		}

	}

	// 알바 - 마이페이지 조회
	public Map<String, Object> workerList(Object workId) {
		String sql = "SELECT WORK_ID, REG_NO, NAME, HP, EMAIL, ADDR, SAL, CODE"
				+ " FROM PM_WORKER" + " WHERE WORK_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(workId);

		return jdbc.selectOne(sql, param);
	}

	// 알바 - 마이페이지 - 수정
	public int workerupdatemypage(Object workId) {
		boolean power, power1;
		System.out.println("수정할 이름 입력");
		String Name = ScanUtil.nextLine();
		System.out.println("수정할 비밀번호 입력");
		String Pass = ScanUtil.nextLine();
		power = Pass.equals(Controller.loginAdmin.get("PASS"));
		while (power == true) {
			System.out.println("기존 비밀번호와 같습니다.\n새로운 비밀번호를 입력해 주세요.");
			Pass = ScanUtil.nextLine();
			power = Pass.equals(Controller.loginAdmin.get("PASS"));
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

		String sql = "UPDATE PM_WORKER SET NAME = ?, PASS = ? , HP = ? , EMAIL = ?"
				+ " WHERE WORK_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(Name);
		param.add(Pass);
		param.add(Hp);
		param.add(Email);
		param.add(workId);
		return jdbc.update(sql, param);
	}

	// 알바 - 마이페이지 - 삭제(EXERD에서 관계값 변경 필요(CASCADE))
	public int workerdeletemypage(Object workId) {
		String sql = "DELETE PM_WORKER WHERE WORK_ID = ?";
		// sql = "DELETE PM_CUS_T WHERE CUS_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(workId);
		return jdbc.update(sql, param);
	}

	// 총관리자 - 마이페이지
	public Map<String, Object> adminList(Object workId) {
		String sql = "SELECT WORK_ID, REG_NO, HP, EMAIL, ADDR, SAL, CODE, NAME"
				+ " FROM PM_WORKER" + " WHERE WORK_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(workId);

		return jdbc.selectOne(sql, param);
	}

	// 총관리자 - 마이페이지 - 수정
	public int adminupdatemypage(Object workId) {
		boolean power, power1;
		System.out.println("수정할 이름 입력");
		String Name = ScanUtil.nextLine();
		System.out.println("수정할 비밀번호 입력");
		String Pass = ScanUtil.nextLine();
		power = Pass.equals(Controller.loginAdmin.get("PASS"));
		while (power == true) {
			System.out.println("기존 비밀번호와 같습니다.\n새로운 비밀번호를 입력해 주세요.");
			Pass = ScanUtil.nextLine();
			power = Pass.equals(Controller.loginAdmin.get("PASS"));
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

		String sql = "UPDATE PM_WORKER SET NAME = ?, PASS = ? , HP = ? , EMAIL = ?"
				+ " WHERE WORK_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(Name);
		param.add(Pass);
		param.add(Hp);
		param.add(Email);
		param.add(workId);
		return jdbc.update(sql, param);
	}

	// 총관리자 - 마이페이지 - 삭제(EXERD에서 관계값 변경 필요(CASCADE))
	public int admindeletemypage(Object workId) {
		String sql = "DELETE PM_WORKER WHERE WORK_ID = ?";
		// sql = "DELETE PM_CUS_T WHERE CUS_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(workId);
		return jdbc.update(sql, param);
	}

	UserDao userDao = UserDao.getInstance();

}
