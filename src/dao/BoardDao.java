package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;
import util.ScanUtil;

public class BoardDao {

	private BoardDao() {
	}

	private static BoardDao instance;

	public static BoardDao getInstance() {
		if (instance == null) {
			instance = new BoardDao();
		}
		return instance;
	}

	private JDBCUtil jdbc = JDBCUtil.getInstance();

	public List<Map<String, Object>> selectBoardList() {

		List<Object> param = new ArrayList<>();
		param.add(Controller.LoginUser.get("CUS_ID"));
		String sql = "SELECT BOR_NO, BOR_DATE, BOR_COMM, TITLE , BOR_CHECK, CONTENT, B.NAME,  A.CUS_ID"
				+ " FROM PM_BOARD A"
				+ " , PM_CUSTOMER B"
				+ " WHERE A.CUS_ID = B.CUS_ID AND A.CUS_ID = ? "
				+ " ORDER BY BOR_NO DESC";

		return jdbc.selectList(sql, param);

	}

	public int insertBoardList() {
		String sql = "SELECT NVL(MAX(BOR_NO),0)+1 MAXNUM FROM PM_BOARD";

		Map<String, Object> num = jdbc.selectOne(sql);

		List<Object> param = new ArrayList<>();

		String title = " ";
		while (title.equals(" ")) {
			System.out.print("등록 메뉴 : 1.음식주문\t2.정액제 충전\t3.애로사항등록\t0.취소\n입력");
			int input = ScanUtil.nextInt();
			switch (input) {
			case 1:
				title = "음식주문";
				break;

			case 2:
				title = "요금충전";
				break;

			case 3:
				title = "애로사항";
				break;

			case 0:
				return 0;

			default:
				System.out.println("1,2,3번만 선택하세요");
				break;
			}
		}

		String content = "";
		
		if (title.equals("음식주문")) {
		    while (content.equals("")){
		    	
		    	String sql1 = "SELECT food_no, name, food_pr FROM pm_food ORDER BY food_no";
		    	
		    	List<Map<String, Object>> food = jdbc.selectList(sql1);
		    	
		    	for(int i = 0; i < food.size(); i++){
		    		if(i%6==0){
						System.out.println();
					}
					System.out.print(food.get(i).get("FOOD_NO") +"."+ food.get(i).get("NAME")+" "+ food.get(i).get("FOOD_PR") + "원  \t");
				}
		    	
		    	System.out.println();
				System.out.println("주문하실  음식을 선택해주세요");
				System.out.println("입력 > ");
				
				int input = ScanUtil.nextInt();
				
				boolean flag = false;
				
				for(int i = 0; i < food.size(); i++){
					if(Integer.parseInt(String.valueOf(food.get(i).get("FOOD_NO"))) == input){
						flag = true;
						break;
					}
				}
				
					if(flag == true){
						System.out.println("몇개 주문하시겠습니까?");
						int inputcount1 = ScanUtil.nextInt();
					String sql2 = "SELECT name, food_pr FROM pm_food WHERE food_no = ?";
					List<Object> param1 = new ArrayList<>();
					param1.add(input);
					Map<String, Object> charge = jdbc.selectOne(sql2, param1);
					content = charge.get("NAME") + " "+ charge.get("FOOD_PR") + "원 " + inputcount1 + "개";	
				}
				else{
					System.out.println("존재하지 않는 음식입니다.");
				}
					
				boolean check = false;
				
				while(check == false){
					System.out.println("추가 주문하시겠습니까?");
					System.out.println("1.예\t2.아니오");
					int inputanswer = ScanUtil.nextInt();
					
					if(inputanswer == 1){
						for(int i = 0; i < food.size(); i++){
							if(i%6==0){
								System.out.println();
							}
							System.out.print(food.get(i).get("FOOD_NO") +"."+ food.get(i).get("NAME")+" "+ 
						     food.get(i).get("FOOD_PR") + "원   \t");
						}
						
						System.out.println();
						System.out.println("주문하실 음식을 선택해주세요");
						System.out.println("입력 > ");
						int inputfood = ScanUtil.nextInt();
						
						boolean check2 = false;
						
						for(int i = 0; i < food.size(); i++){
							if(Integer.parseInt(String.valueOf(food.get(i).get("FOOD_NO"))) == inputfood){
								check2 = true;
								break;
							}
						}
						
						if(check2 == true){
							System.out.println("몇개 주문하시겠습니까?");
							int foodcount = ScanUtil.nextInt();
							
							String sql2 = "SELECT name, food_pr FROM pm_food WHERE food_no = ?";
							List<Object> param1 = new ArrayList<>();
							param1.add(inputfood);
							Map<String, Object> charge = jdbc.selectOne(sql2, param1);
							
							content += ", " + charge.get("NAME") + " "+ charge.get("FOOD_PR") + "원 " + foodcount + "개";
							
						}else{
							System.out.println("존재하지 않는 음식입니다.");
						}
							
					}else if(inputanswer == 2){
						check = true;
					}else{
					System.out.println("다른 번호를 입력하셨습니다.");
					}
						
				}
				
					
				}
				
				
		    } 
		
		
		if (title.equals("요금충전")) {
			while (content.equals("")) {

				String sql1 = "SELECT FLAT_NO, FLAT_NAME FROM PM_FLAT_RATE ORDER BY FLAT_NO";

				List<Map<String, Object>> flat = jdbc.selectList(sql1);

				for (int i = 0; i < flat.size(); i++) {
					if(i%6==0){
						System.out.println();
					}
					System.out.print(flat.get(i).get("FLAT_NO") + "."
							+ flat.get(i).get("FLAT_NAME") + " ");
				}

				System.out.println();
				System.out.println("충전할 정액제 상품을 선택해주세요");
				System.out.println("입력 > ");

				int inputnum = ScanUtil.nextInt();

				boolean flag = false;

				for (int i = 0; i < flat.size(); i++) {
					if (Integer.parseInt(String.valueOf(flat.get(i).get(
							"FLAT_NO"))) == inputnum) {
						flag = true;
						break;
					}
				}

				if (flag == true) {
					String sql2 = "SELECT FLAT_PRICE FROM PM_FLAT_RATE WHERE FLAT_NO = ?";
					List<Object> param1 = new ArrayList<>();
					param1.add(inputnum);
					Map<String, Object> charge = jdbc.selectOne(sql2, param1);
					content = "요금충전 " + charge.get("FLAT_PRICE") + "원";
				} else {
					System.out.println("존재하지 않는 상품입니다.");
				}

			}

		} 
		if(title.equals("애로사항")){

			System.out.println("내용을 입력해주세요");
			content = ScanUtil.nextLine();
		}
		sql = "INSERT INTO PM_BOARD VALUES(?,SYSDATE,?,?,?,?,?)";
		param.add(num.get("MAXNUM"));
		param.add(" ");
		param.add(" ");
		param.add(title);
		param.add(content);
		param.add(Controller.LoginUser.get("CUS_ID"));

		return jdbc.update(sql, param);

	}

	public Map<String, Object> selectList(int num) {

		String sql = "SELECT BOR_NO, BOR_DATE, BOR_COMM, TITLE , BOR_CHECK, CONTENT, B.NAME, A.CUS_ID"
				+ " FROM PM_BOARD A"
				+ " , PM_CUSTOMER B"
				+ " WHERE A.CUS_ID = B.CUS_ID AND BOR_NO = ? ";

		List<Object> param = new ArrayList<>();
		param.add(num);

		return jdbc.selectOne(sql, param);

	}

	public int deleteBoardList(int num) {
		List<Object> param = new ArrayList<>();

		String sql = "SELECT CUS_ID FROM PM_BOARD WHERE BOR_NO = ?";
		param.add(num);

		Map<String, Object> name = jdbc.selectOne(sql, param);

		// 답변이 왔는지 확인
		List<Object> param2 = new ArrayList<>();
		sql = "SELECT BOR_COMM FROM PM_BOARD WHERE BOR_NO = ?";
		param2.add(num);

		Map<String, Object> temp = jdbc.selectOne(sql, param2);

		if (Controller.LoginUser.get("CUS_ID").equals(name.get("CUS_ID"))) {
			String check = String.valueOf(temp.get("BOR_COMM"));
			if (check.equals(" ")) {
				sql = "DELETE PM_BOARD WHERE BOR_NO = ?";

				System.out.println("삭제되었습니다.");

				return jdbc.update(sql, param);
			} else {
				System.out.println("이미 관리자가 처리한 글이여서 삭제가 불가능 합니다.");
				return 0;
			}

		} else {
			System.out.println("없는 글 입니다.");
			return 0;
		}
	}
	
	
	

	public int updateBoardList(int num) {
		List<Object> param1 = new ArrayList<>();

		String sql = "SELECT CUS_ID FROM PM_BOARD WHERE BOR_NO = ?";
		param1.add(num);

		Map<String, Object> name = jdbc.selectOne(sql, param1);

		// 답변이 왔는지 확인
		List<Object> param2 = new ArrayList<>();
		sql = "SELECT BOR_COMM FROM PM_BOARD WHERE BOR_NO = ?";
		param2.add(num);

		Map<String, Object> temp = jdbc.selectOne(sql, param2);
		//
		List<Object> param3 = new ArrayList<>();
		sql = "SELECT TITLE FROM PM_BOARD WHERE BOR_NO = ?";
		param3.add(num);

		Map<String, Object> cont = jdbc.selectOne(sql, param3);

		if (Controller.LoginUser.get("CUS_ID").equals(name.get("CUS_ID"))) {
			// 여기부터 작업
			String check = String.valueOf(temp.get("BOR_COMM"));
			if (check.equals(" ")) {
				String checkCont = String.valueOf(cont.get("TITLE"));
				if (checkCont.equals("음식주문")) {
					String content1 = "";
					while (content1.equals("")) {
					
						String sql1 = "SELECT food_no, name, food_pr FROM pm_food ORDER BY food_no";
				    	
				    	List<Map<String, Object>> food = jdbc.selectList(sql1);
				    	
				    	for(int i = 0; i < food.size(); i++){
				    		if(i%6==0){
								System.out.println();
							}
							System.out.print(food.get(i).get("FOOD_NO") +"."+ food.get(i).get("NAME")+" "+ food.get(i).get("FOOD_PR") + "원  \t");
						}
				    	
				    	System.out.println();
						System.out.println("주문하실  음식을 선택해주세요");
						System.out.println("입력 > ");
						
						int input = ScanUtil.nextInt();
						
						boolean flag = false;
					
						for(int i = 0; i < food.size(); i++){
							if(Integer.parseInt(String.valueOf(food.get(i).get("FOOD_NO"))) == input){
								flag = true;
								break;
							}
						}
						
							if(flag == true){
								System.out.println("몇개 주문하시겠습니까?");
								int inputcount1 = ScanUtil.nextInt();
							String sql2 = "SELECT name, food_pr FROM pm_food WHERE food_no = ?";
							List<Object> param5 = new ArrayList<>();
							param5.add(input);
							Map<String, Object> charge = jdbc.selectOne(sql2, param5);
							content1 = charge.get("NAME") + " "+ charge.get("FOOD_PR") + "원 " + inputcount1 + "개";	
						}
						else{
							System.out.println("존재하지 않는 음식입니다.");
						}
							
							
							while(true){
								System.out.println("추가 주문하시겠습니까?");
								System.out.println("1.예\t2.아니오");
								int inputanswer = ScanUtil.nextInt();
								
								if(inputanswer == 1){
									for(int i = 0; i < food.size(); i++){
										if(i%6==0){
											System.out.println();
										}
										System.out.print(food.get(i).get("FOOD_NO") +"."+ food.get(i).get("NAME")+" "+ 
									     food.get(i).get("FOOD_PR") + "원  \t");
									}
									
									System.out.println();
									System.out.println("주문하실 음식을 선택해주세요");
									System.out.println("입력 > ");
									int inputfood = ScanUtil.nextInt();
									
									boolean check2 = false;
									
									for(int i = 0; i < food.size(); i++){
										if(Integer.parseInt(String.valueOf(food.get(i).get("FOOD_NO"))) == inputfood){
											check2 = true;
											break;
										}
									}
									
									if(check2 == true){
										System.out.println("몇개 주문하시겠습니까?");
										int foodcount = ScanUtil.nextInt();
										
										String sql2 = "SELECT name, food_pr FROM pm_food WHERE food_no = ?";
										List<Object> param6 = new ArrayList<>();
										param6.add(inputfood);
										Map<String, Object> charge = jdbc.selectOne(sql2, param6);
										
										content1 += ", " + charge.get("NAME") + " "+ charge.get("FOOD_PR") + "원 " + foodcount + "개";
										
										
										
									}else{
										System.out.println("존재하지 않는 음식입니다.");
									}
										
								}else if(inputanswer == 2){
									
									sql = "UPDATE PM_BOARD SET CONTENT = ? WHERE BOR_NO = ?";
									List<Object> param = new ArrayList<>();

									param.add(content1);
									param.add(num);

									System.out.println("수정되었습니다.");

									return jdbc.update(sql, param);
									
								}else{
								System.out.println("다른 번호를 입력하셨습니다.");
								}
									
							}
						
						
					}
					
				}
				
				
				
				
				if (checkCont.equals("요금충전")) {
					String content = "";
					while (content.equals("")) {
						String sql1 = "SELECT flat_no, flat_name FROM PM_FLAT_RATE ORDER BY flat_no";

						List<Map<String, Object>> flat = jdbc.selectList(sql1);

						for (int i = 0; i < flat.size(); i++) {
							System.out.print(flat.get(i).get("FLAT_NO") + "."
									+ flat.get(i).get("FLAT_NAME") + " ");
						}

						System.out.println();
						System.out.println("충전할 정액제 상품을 선택해주세요");
						System.out.println("입력 > ");

						int inputnum = ScanUtil.nextInt();

						boolean flag = false;

						for (int i = 0; i < flat.size(); i++) {
							if (Integer.parseInt(String.valueOf(flat.get(i)
									.get("FLAT_NO"))) == inputnum) {
								flag = true;
								break;
							}
						}

						if (flag == true) {
							String sql2 = "SELECT flat_price FROM PM_FLAT_RATE WHERE flat_no = ?";
							List<Object> param4 = new ArrayList<>();
							param4.add(inputnum);
							Map<String, Object> charge = jdbc.selectOne(sql2,
									param4);
							content = "요금충전 " + charge.get("FLAT_PRICE") + "원";
						} else {
							System.out.println("존재하지 않는 상품입니다.");
						}
					}
					sql = "UPDATE PM_BOARD SET CONTENT = ? WHERE BOR_NO = ?";
					List<Object> param = new ArrayList<>();

					param.add(content);
					param.add(num);

					System.out.println("수정되었습니다.");

					return jdbc.update(sql, param);
				} else {
					System.out.println("수정할 내용 입력");
					String content = ScanUtil.nextLine();

					sql = "UPDATE PM_BOARD SET CONTENT = ? WHERE BOR_NO = ?";
					List<Object> param = new ArrayList<>();

					param.add(content);
					param.add(num);

					System.out.println("수정되었습니다.");

					return jdbc.update(sql, param);
				}
			} else {
				System.out.println("이미 관리자가 처리한 글이여서 수정이 불가능 합니다.");
				return 0;
			}
		} else {
			System.out.println("등록되지 않은 글 입니다.");
			return 0;

		}
	}

}
