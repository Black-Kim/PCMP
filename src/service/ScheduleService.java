package service;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.ScanUtil;
import util.View;
import dao.ScheduleDao;
import dao.WorkerDao;

public class ScheduleService {

	private ScheduleService() {
	}

	private static ScheduleService instance;

	public static ScheduleService getInstance() {
		if (instance == null) {
			instance = new ScheduleService();
		}
		return instance;
	}

	private ScheduleDao scheduleDao = ScheduleDao.getInstance();

	public int ScheduleList() {
		System.out.println("======================================");
		System.out.println("1.조회\t2.등록\t0.돌아가기");
		System.out.println("입력 >");

		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:
			List<Map<String, Object>> scheduleList = scheduleDao.selectScheduleList();
			System.out
					.println("================================================================");
			System.out.println("번호\t아이디\t출근시간\t\t\t 퇴근시간");
			System.out.println("--------------------------------------");
			for (int i = 0; i < scheduleList.size(); i++) {
				System.out.println(scheduleList.get(i).get("CODE")+ "\t" + scheduleList.get(i).get("WORK_ID")
						+ "\t" + scheduleList.get(i).get("WORK_START") + "\t"
						+ scheduleList.get(i).get("WORK_END"));
			}
			System.out
					.println("================================================================");
			System.out.println("1.수정\t2.삭제\t3.돌아가기");
			System.out.println("입력 >");

			input = ScanUtil.nextInt();

			switch (input) {
			case 1:
				if (Integer.parseInt(String.valueOf(Controller.loginAdmin
						.get("CODE"))) == 0) {
					updateSchedule();
				} else {
					System.out.println("수정 권한이 없습니다.");
				}
				break;

			case 2:
				if (Integer.parseInt(String.valueOf(Controller.loginAdmin
						.get("CODE"))) == 0) {
					deleteSchedule();
				} else {
					System.out.println("삭제 권한이 없습니다.");
				}
				break;
			case 3:
				return View.SCHEDULE;
			}

			break;

		case 2:
			if (Integer.parseInt(String.valueOf(Controller.loginAdmin
					.get("CODE"))) == 0) {
				insertSchedule();
			} else {
				System.out.println("등록 권한이 없습니다.");
			}
			break;
		case 0:
			break;

		}
		return View.ADMIN_FIRST_PAGE;

	}
	//알바 일급여


	public int insertSchedule() {
		Boolean check = false;
		System.out.println("근로자 아이디를 입력해주세요 >");
		String name = ScanUtil.nextLine(); //String -> Int로 수정 됨(10.19)
		
		List<Map<String,Object >> checknum = scheduleDao.selectScheduleName(name);
		
		for(int i = 0; i<checknum.size(); i++){
			if(String.valueOf(checknum.get(i).get("WORK_ID")).equals(name)){
				check=true;
				break;
			}
		}
		if(check==false){
			System.out.println("없는 아이디 입니다.");
			return View.SCHEDULE;
		}else{
		
		
		System.out.println("아르바이트생의 출근 시간 및 날짜 입력\n예시)202010161300> ");
		String startWork = ScanUtil.nextLine();
		System.out.println("아르바이트 생의 퇴근 시간 및 날짜를 입력해주세요\n예시)202010161300> ");
		String endWork = ScanUtil.nextLine();
		Map<String, Object> param = new HashMap<>();

		param.put("WORK_ID", name);
		param.put("WORK_START", startWork);
		param.put("WORK_END", endWork);

		int result = scheduleDao.insertschedule(param);

		System.out.println("등록 완료");

		}
		return View.SCHEDULE;
		

	}

	private void lookupSchedule() {
		List<Map<String, Object>> scheduleList = scheduleDao
				.selectScheduleList();

		System.out
				.println("=======================================================================");
		System.out.println("\t아이디\t출근시간\t\t\t 퇴근시간");
		System.out.println("--------------------------------------");
		for (int i = 0; i < scheduleList.size(); i++) {
			System.out.println("\t" + scheduleList.get(i).get("WORK_ID") + "\t"
					+ scheduleList.get(i).get("WORK_START") + "\t"
					+ scheduleList.get(i).get("WORK_END"));
		}
		System.out
				.println("========================================================================");
		System.out.println("1.수정\t2.삭제\t3.돌아가기");
		System.out.println("입력 >");

		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:

			int num1 = Integer.parseInt(String.valueOf(Controller.loginAdmin.get("CODE")));

			if (num1 == 0) {
				updateSchedule();
			} else {
				System.out.println("수정 권한이 없습니다.");
			}
			break;

		case 2:
			int num2 = Integer.parseInt(String.valueOf(Controller.loginAdmin
					.get("CODE")));

			if (num2 == 0) {
				deleteSchedule();
			} else {
				System.out.println("삭제 권한이 없습니다.");
			}
			break;
		case 3:
			// ScheduleList();
			break;
		}

	}
	
	//스케줄 수정
	public void updateSchedule() {

		Boolean check = false;
		while (check==false){ 
			
		
		System.out.println("수정 게시물 번호를 적어주세요");
		int code = ScanUtil.nextInt(); //String -> Int로 수정 됨(10.19)
		
		List<Map<String,Object >> checknum = scheduleDao.selectScheduleNum(code);
		
		for(int i = 0; i<checknum.size(); i++){
			if(Integer.parseInt(String.valueOf(checknum.get(i).get("CODE"))) == code){
				check=true;
				break;
			}
		}
		if(check==false){
			System.out.println("없는 번호 입니다.");
		}else{
		
		
		System.out.println("수정할 출근시간을 입력해주세요");
		String startwork = ScanUtil.nextLine();
		System.out.println("수정할 퇴근시간을 입력해주세요");
		String endwork = ScanUtil.nextLine();

		Map<String, Object> param = new HashMap<>();
		param.put("WORK_START", startwork);
		param.put("WORK_END", endwork);
		param.put("CODE", code);

			int result = scheduleDao.updateSchedule(param);

			if (0 < result) {
				System.out.println("스케줄 수정 성공");
			} else {
				System.out.println("스케줄 수정 실패");
			
			}
		}
		}
		} 

	public void deleteSchedule() {

		
		Boolean check = false;
		while (check==false){ 
			
		
		System.out.println("수정 게시물 번호를 적어주세요");
		int code = ScanUtil.nextInt(); //String -> Int로 수정 됨(10.19)
		
		List<Map<String,Object >> checknum = scheduleDao.selectScheduleNum(code);
		
		for(int i = 0; i<checknum.size(); i++){
			if(Integer.parseInt(String.valueOf(checknum.get(i).get("CODE"))) == code){
				check=true;
				break;
			}
		}
		if(check==false){
			System.out.println("없는 번호 입니다.");
		}else{

		Map<String, Object> name1 = new HashMap<>();
		name1.put("CODE", code);

		int result = scheduleDao.deleteScheudule(name1);

		if (0 < result) {
			System.out.println("게시글이 삭제 되었습니다.");
		} else {
			System.out.println("없는 번호 입니다");
		}
		}
		}
	}
	public int workScheduleList() {

		System.out.println("======================================");
		System.out.println("1.조회\t2.등록\t0.돌아가기\t3.test");
		System.out.println("입력 >");

		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1:
			List<Map<String, Object>> scheduleList = scheduleDao.selectWorkScheduleList();
			System.out.println(scheduleList);
			System.out
					.println("================================================================");
			System.out.println("\t아이디\t출근시간\t\t\t 퇴근시간");
			System.out.println("----------------------------------------------------------------");
			for (int i = 0; i < scheduleList.size(); i++) {
				System.out.println("\t" + scheduleList.get(i).get("WORK_ID")
						+ "\t" + scheduleList.get(i).get("WORK_START") + "\t"
						+ scheduleList.get(i).get("WORK_END"));
			}
			System.out
			.println("================================================================");
			System.out.println("아무 키나 누르면 돌아갑니다");
			String a = ScanUtil.nextLine();
			break;
		case 2:
				workerinsertSchedule();
			break;
		case 0:
			break;
		}
		

		return View.WORKER_FIRST_PAGE;
	}
	private int workerinsertSchedule() {

	
		System.out.println("출근 시간 및 날짜 입력\n예시)202010161300> ");
		String startWork = ScanUtil.nextLine();
		System.out.println("퇴근 시간 및 날짜를 입력해주세요\n예시)202010161300> ");
		String endWork = ScanUtil.nextLine();
		Map<String, Object> param = new HashMap<>();

		param.put("WORK_ID", Controller.loginAdmin.get("WORK_ID"));
		param.put("WORK_START", startWork);
		param.put("WORK_END", endWork);
		param.put("CODE", Controller.loginAdmin.get("CODE"));

		int result = scheduleDao.insertschedule(param);

		System.out.println("등록 완료");

		return View.WORK_SCHEDULE;
		
	}

}