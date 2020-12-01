package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;
import util.ScanUtil;

public class FoodDao {

	private FoodDao() {
	}

	private static FoodDao instance;

	public static FoodDao getInstance() {
		if (instance == null) {
			instance = new FoodDao();
		}
		return instance;
	}

	private JDBCUtil jdbc = JDBCUtil.getInstance();

	// 음식 중복 체크
	public Map<String, Object> finduser(String string, Object foodname) {
		String sql = "SELECT * FROM PM_FOOD WHERE NAME = ?";
		List<Object> param = new ArrayList<>();
		param.add(foodname);
		return jdbc.selectOne(sql, param);
	}

	// 음식 리스트
	public List<Map<String, Object>> foodList() {
		String sql = "SELECT FOOD_NO, NAME, FOOD_PR FROM PM_FOOD ORDER BY FOOD_NO";

		return jdbc.selectList(sql);
	}

	// 음식 추가
	public int insertfood(Map<String, Object> foodinfo) {
		String sql = "SELECT NVL(MAX(FOOD_NO),0)+1 FOOD_NO FROM PM_FOOD";
		Map<String, Object> info = jdbc.selectOne(sql);
		sql = "INSERT INTO PM_FOOD VALUES(?,?,?)";
		List<Object> param = new ArrayList<>();
		param.add(info.get("FOOD_NO"));
		param.add(foodinfo.get("NAME"));
		param.add(foodinfo.get("FOOD_PR"));

		return jdbc.update(sql, param);
	}

	// 음식 수정
	public int updatefood(Object foodno) {
		String sql = "UPDATE PM_FOOD SET NAME = ?, FOOD_PR = ? WHERE FOOD_NO = ?";
		System.out.println("수정할 이름>");
		String name = ScanUtil.nextLine();
		System.out.println("수정할 단가>");
		int price = ScanUtil.nextInt();
		List<Object> param = new ArrayList<>();
		param.add(name);
		param.add(price);
		param.add(foodno);

		return jdbc.update(sql, param);
	}

	// 음식 삭제
	public int deletefood(Object foodno) {
		String sql = "DELETE PM_FOOD WHERE FOOD_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(foodno);
		return jdbc.update(sql, param);
	}

	public void insertPayFood() {
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
			sql = "SELECT FOOD_NO, NAME, FOOD_PR FROM PM_FOOD ORDER BY FOOD_NO";

			List<Map<String, Object>> flatList = jdbc.selectList(sql);
			System.out.println("번호\t상품명");
			
			for (int i = 0; i < flatList.size(); i++) {
				if(i%6==0){
					System.out.println();
				}
				System.out.print(flatList.get(i).get("FOOD_NO") + "."
						+ flatList.get(i).get("NAME") + "("
						+ flatList.get(i).get("FOOD_PR") + "원) \t");
			}

			boolean numKey = false;

			System.out.println();
			System.out.println("결제한 상품을 선택해주세요");
			System.out.println("입력 > ");

			int numInput = ScanUtil.nextInt();

			for (int i = 0; i < flatList.size(); i++) {
				if (Integer.parseInt(String.valueOf(flatList.get(i).get(
						"FOOD_NO"))) == numInput) {
					check2 = true;
					break;
				}
			}
			if (check2 == true) {

				sql = "INSERT INTO PM_FOOD_PAY VALUES ((SELECT NVL(MAX(PAY_NO),0)+1 FROM PM_FOOD_PAY), SYSDATE,?)";

				List<Object> param = new ArrayList<>();

				param.add(input);

				jdbc.update(sql, param);

				System.out.println("결제할 수량을 입력해주세요");
				System.out.print("수량 >");
				int quantity = ScanUtil.nextInt();

				sql = "INSERT INTO PM_ORDER VALUES ( ? ,(SELECT NVL(MAX(PAY_NO),0) FROM PM_FOOD_PAY) ,?)";
				List<Object> order = new ArrayList<>();
				order.add(numInput);
				order.add(quantity);

				jdbc.update(sql, order);
				int addFood = quantity;

				boolean checkAdd = false;
				while (checkAdd == false) {
					System.out.println("추가주문 하시겠습니까?");
					System.out.println("1.예\t2.아니요");
					int checkPay = ScanUtil.nextInt();

					if (checkPay == 1) {

						for (int i = 0; i < flatList.size(); i++) {
							if(i%6==0){
								System.out.println();
							}
							System.out.print(flatList.get(i).get("FOOD_NO")
									+ "." + flatList.get(i).get("NAME") + "("
									+ flatList.get(i).get("FOOD_PR") + "원)\t");
						}

						boolean numKey2 = false;

						System.out.println();
						System.out.println("결제한 상품을 선택해주세요");
						System.out.println("입력 > ");

						int numInput2 = ScanUtil.nextInt();

						boolean check3 = false;
						for (int i = 0; i < flatList.size(); i++) {
							if (Integer.parseInt(String.valueOf(flatList.get(i)
									.get("FOOD_NO"))) == numInput2) {
								check3 = true;
								break;
							}
						}

						if (check3 == true) {
							sql = "SELECT FOOD_NO,PAY_NO FROM PM_ORDER WHERE PAY_NO = (SELECT NVL(MAX(PAY_NO),0) FROM PM_FOOD_PAY)";
							/*List<Object> numFoodPay = new ArrayList<>();
							numFoodPay.add(numInput2);*/
							Map<String, Object> foodPay = jdbc.selectOne(sql);
							

							System.out.println("결제할 수량을 입력해주세요");
							System.out.print("수량 >");
							int quantity1 = ScanUtil.nextInt();
							
							addFood += quantity1;

							if (Integer.parseInt(String.valueOf(foodPay.get("FOOD_NO"))) == numInput2) {
								sql = "UPDATE PM_ORDER SET AMOUNT = ? WHERE PAY_NO = (SELECT NVL(MAX(PAY_NO),0) FROM PM_FOOD_PAY)"
										+ " AND FOOD_NO = ?";
//										sql = "INSERT INTO PM_ORDER VALUES ( ? ,(SELECT NVL(MAX(PAY_NO),0) FROM PM_FOOD_PAY) , ?)" ;	
//								"SELECT AMOUNT+? FROM PM_ORDER WHERE PAY_NO = (SELECT NVL(MAX(PAY_NO),0) FROM PM_FOOD_PAY)"
//								+ " AND FOOD_NO = ?)
								
								List<Object> order3 = new ArrayList<>();
								order3.add(addFood);
								order3.add(numInput2);
					
								
								jdbc.update(sql, order3);
							}else{

						

							sql = "INSERT INTO PM_ORDER VALUES ( ? ,(SELECT NVL(MAX(PAY_NO),0) FROM PM_FOOD_PAY) ,?)";
							List<Object> order2 = new ArrayList<>();
							order2.add(numInput2);
							order2.add(quantity1);

							jdbc.update(sql, order2);
						}

						} else {
							System.out.println("존재하지 않는 상품번호 입니다.");
						}

					} else {
						checkAdd = true;
						System.out.println("결제가 완료되었습니다.");
					}

				}
			} else {
				System.out.println("존재하지 않는 상품번호 입니다.");
			}

		} else {
			System.out.println("존재하지 않는 고객 아이디입니다.");
		}

	}

}
