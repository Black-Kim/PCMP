package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;
import util.ScanUtil;

public class PayDao {

	private PayDao() {
	}

	private static PayDao instance;

	public static PayDao getInstance() {
		if (instance == null) {
			instance = new PayDao();
		}
		return instance;
	}

	private static JDBCUtil jdbc = JDBCUtil.getInstance();

	public void insertPayFlat() {
		Boolean check = false;
		Boolean check2 = false;
		System.out.println("결제할 회원의 아이디를 입력해주세요");
		System.out.println("입력 > ");
		String input = ScanUtil.nextLine();

		String sql = "SELECT CUS_ID FROM PM_CUSTOMER";

		List<Map<String, Object>> temp = jdbc.selectList(sql);

		for (int i = 0; i < temp.size(); i++) {
			if (String.valueOf(temp.get(i).get("CUS_ID")).equals(input)) {
				check = true;
			}
		}

		if (check == true) {
			sql = "SELECT FLAT_NO, FLAT_NAME FROM PM_FLAT_RATE ORDER BY FLAT_NO";

			List<Map<String, Object>> flatList = jdbc.selectList(sql);
			System.out.println("번호\t상품명");
			for (int i = 0; i < flatList.size(); i++) {
				if(i%6==0){
					System.out.println();
				}
				System.out.print(flatList.get(i).get("FLAT_NO") + "."
						+ flatList.get(i).get("FLAT_NAME") + "\t");
			}

			System.out.println();
			System.out.println("결재한 상품을 선택해주세요");
			System.out.println("입력 > ");

			int numInput = ScanUtil.nextInt();

			for (int i = 0; i < flatList.size(); i++) {
				if (Integer.parseInt(String.valueOf(flatList.get(i).get(
						"FLAT_NO"))) == numInput) {
					check2 = true;
					break;
				}
			}
			if (check2 == true) {
				sql = "INSERT INTO PM_FLAT_PAY VALUES ((SELECT NVL(MAX(PAY_NO),0)+1 FROM PM_FLAT_PAY), SYSDATE,?,?)";

				List<Object> param = new ArrayList<>();

				param.add(numInput);
				param.add(input);

				jdbc.update(sql, param);

				System.out.println("결제가 완료되었습니다.");
			} else {
				System.out.println("존재하지 않는 상품입니다.");
			}

		} else {
			System.out.println("존재하지 않는 고객 아이디입니다.");
		}

	}

}
