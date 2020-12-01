package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUtil {

	//싱글톤 패턴 : 인스턴스 생성을 제한하여 하나의 인스턴스만 사용하는 디자인 패턴 (객체랑 인스턴스랑 거이같다 생각하면 됨)
	//다른클래스에서 객체생성을 못하게하고 자신의 클래스에서 생성한걸 빌려주는 느낌
	
	private JDBCUtil(){
		
	}
	
	//인스턴스(객체)를 보관할 변수
	private static JDBCUtil instance;
	
	//인스턴스를 빌려주는 메서드
	public static JDBCUtil getInstance(){
		if(instance == null){
			instance = new JDBCUtil();
		}
		return instance;
	}
	
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String user = "PC01";
	private String password = "java";
	
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	/*
	 * Map<String, Object> selectOne(String sql) // 물음표가 없는경우 selectOne:값이 하나의 행만 있을경우
	 * Map<String, Object> selectOne(String sql, List<Object> param) //물음표가 있는경우 param(물음표값)  
	 * List<Map<String, Object>> selectList(String, sql) selectList : 값이 여러 행인 경우
	 * List<Map<String, Object>> selectList(String, sql, List<Object> param)
	 * int update(String sql) // 업데이트 인서트 딜리트 용 -> 몇개의 행이 영향을 받았는지?
	 * int update(String sql, List<Object> param)
	 */
	
	public Map<String, Object> selectOne(String sql){
		Map<String, Object> row = null;
		try {
			con = DriverManager.getConnection(url,user,password);
			
			ps = con.prepareStatement(sql);
			
			rs = ps.executeQuery();
			
			ResultSetMetaData md = rs.getMetaData();
			md.getColumnCount();
			int columnCount = md.getColumnCount();
			
			while(rs.next()){
				row = new HashMap<>();
				for(int i = 1; i<=columnCount; i++){
					String key = md.getColumnName(i);
					Object value = rs.getObject(i);
					row.put(key, value);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs != null) try {rs.close();}catch (Exception e) {}
			if(ps != null) try {ps.close();}catch (Exception e) {}
			if(con != null) try {con.close();}catch (Exception e) {}
		}
		
		return row;

	}
	
	
	public Map<String, Object> selectOne(String sql, List<Object> param){
		Map<String, Object> row = null;
		try {
			con = DriverManager.getConnection(url,user,password);
			
			ps = con.prepareStatement(sql);
			
			for(int i = 0 ; i<param.size(); i++){
				ps.setObject(i+1, param.get(i));
			}
			
			rs = ps.executeQuery();
			
			ResultSetMetaData md = rs.getMetaData();
			md.getColumnCount();
			int columnCount = md.getColumnCount();
			
			while(rs.next()){
				row = new HashMap<>();
				for(int i = 1; i<=columnCount; i++){
					String key = md.getColumnName(i);
					Object value = rs.getObject(i);
					row.put(key, value);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs != null) try {rs.close();}catch (Exception e) {}
			if(ps != null) try {ps.close();}catch (Exception e) {}
			if(con != null) try {con.close();}catch (Exception e) {}
		}
		
		return row;
	}
	
	
	public List<Map<String, Object>> selectList(String sql){
		List<Map<String, Object>> list = new ArrayList<>(); //트라이안에서 쓰면 리턴불가
		try {
			con = DriverManager.getConnection(url,user,password);
			
			ps = con.prepareStatement(sql);
			
			rs = ps.executeQuery();
			
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			
			while(rs.next()){
				Map<String, Object> row = new HashMap<>();
				for(int i = 1; i<=columnCount; i++){
					String key = md.getColumnName(i);
					Object value = rs.getObject(i);
					row.put(key, value);
				}

				list.add(row);
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try {rs.close();}catch (Exception e) {}
			if(ps != null) try {ps.close();}catch (Exception e) {}
			if(con != null) try {con.close();}catch (Exception e) {}
		}
		
		return list;
		
		
	}
	
	
	public List<Map<String, Object>> selectList(String sql, List<Object> param){
		List<Map<String, Object>> list = new ArrayList<>(); //트라이안에서 쓰면 리턴불가
		try {
			con = DriverManager.getConnection(url,user,password);
			
			ps = con.prepareStatement(sql);
			
			for(int i = 0 ; i<param.size(); i++){
				ps.setObject(i+1, param.get(i));
			}
			
			rs = ps.executeQuery();
			
			ResultSetMetaData md = rs.getMetaData();
			md.getColumnCount();
			int columnCount = md.getColumnCount();
			
			while(rs.next()){
				Map<String, Object> row = new HashMap<>();
				for(int i = 1; i<=columnCount; i++){
					String key = md.getColumnName(i);
					Object value = rs.getObject(i);
					row.put(key, value);
				}
				list.add(row);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try {rs.close();}catch (Exception e) {}
			if(ps != null) try {ps.close();}catch (Exception e) {}
			if(con != null) try {con.close();}catch (Exception e) {}
		}
		
		return list;
	}
	
	public int update(String sql){
		int result = 0;
		try {
			con = DriverManager.getConnection(url,user,password);
			
			ps = con.prepareStatement(sql);
			
			result = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try {rs.close();}catch (Exception e) {}
			if(ps != null) try {ps.close();}catch (Exception e) {}
			if(con != null) try {con.close();}catch (Exception e) {}
		}
		
		return result;
	}
	
	public int update(String sql, List<Object> param){
		int result = 0;
		try {
			con = DriverManager.getConnection(url,user,password);

			
			
			
			ps = con.prepareStatement(sql);
			
			for(int i = 0 ; i<param.size(); i++){
				ps.setObject(i+1, param.get(i));
			}
			
			result = ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try {rs.close();}catch (Exception e) {}
			if(ps != null) try {ps.close();}catch (Exception e) {}
			if(con != null) try {con.close();}catch (Exception e) {}
		}
		
		return result;
	}
	
}




















