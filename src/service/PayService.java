package service;

import controller.Controller;
import dao.PayDao;
import util.ScanUtil;
import util.View;

public class PayService {

	private PayService() {
	}

	private static PayService instance;

	public static PayService getInstance() {
		if (instance == null) {
			instance = new PayService();
		}
		return instance;
	}

	private PayDao payDao = PayDao.getInstance();

	public int insertPayFlatRate() {
		System.out.println("1.고객충전\t0.이전");
		System.out.print("입력 >");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1:
			payDao.insertPayFlat();
			return View.PAY_FLATRATE;
		case 0:
			if(Integer.parseInt(String.valueOf(Controller.loginAdmin.get("CODE")))==0){
				return View.ADMIN_FIRST_PAGE;
			}else{
				return View.WORKER_FIRST_PAGE;
			}
			

		default:
			break;
		}

		return View.PAY_FLATRATE;
	}

}
