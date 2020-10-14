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
	
	private JDBCUtil(){
		
	}
	
	//인스턴스를 보관할 변수
	private static JDBCUtil instance;
	
	//인스턴스를 빌려주는 메서드
	public static JDBCUtil getInstance(){
		if (instance == null){
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
	 * Map<String, Object> selectOne(String sql)
	 * Map<String, Object> selectOne(String sql, List<Object> param)
	 * List<Map<String,Object>> selectList(String sql)
	 * List<Map<String, Object>> selectList(String sql, List<Object> param)
	 * int update(String sql)
	 * int update(String sql, List<Object> param)
	 */
	
	public Map<String, Object> selectOne(String sql){
		Map<String, Object> map = null;
		try {
			con = DriverManager.getConnection(url,user,password);
			ps = con.prepareStatement(sql);
			ResultSetMetaData md = ps.getMetaData();
			int columnCount = md.getColumnCount();
			rs = ps.executeQuery();
			
			while(rs.next()){
				map = new HashMap<>();
				for(int i = 1; i <= columnCount; i++){
					String key = md.getColumnName(i);
					Object value = rs.getObject(i);
					map.put(key, value);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)try {rs.close();} catch (Exception e) {}
			if (ps != null)try {ps.close();} catch (Exception e) {}
			if (con != null)try {con.close();} catch (Exception e) {}
			}
		return map;
	}
	
	public Map<String, Object> selectOne(String sql, List<Object> param){
			Map<String, Object> map = null;
		try {
			con = DriverManager.getConnection(url,user,password);
			ps = con.prepareStatement(sql);
			
			for(int i = 0 ; i < param.size(); i++){
				ps.setObject(i+1, param.get(i));
			}
			rs = ps.executeQuery();
			ResultSetMetaData md = ps.getMetaData();
			int columnCount = md.getColumnCount();
			
			while(rs.next()){
				map = new HashMap<>();
				for(int i = 1; i <= columnCount; i++){
					String key = md.getColumnName(i);
					Object value = rs.getObject(i);
					map.put(key, value);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)try {rs.close();} catch (Exception e) {}
			if (ps != null)try {ps.close();} catch (Exception e) {}
			if (con != null)try {con.close();} catch (Exception e) {}
		}
		return map;
	}
	
	public List<Map<String,Object>> selectList(String sql){
		List<Map<String,Object>> list = new ArrayList<>();
		try {
			con = DriverManager.getConnection(url,user,password);
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			
			while(rs.next()){
				Map<String,Object> map = new HashMap<>();
				for(int i = columnCount; i >=1; i--){
					String key = md.getColumnName(i);
					Object value = rs.getObject(i);
					map.put(key, value);
				}
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)try {rs.close();} catch (Exception e) {}
			if (ps != null)try {ps.close();} catch (Exception e) {}
			if (con != null)try {con.close();} catch (Exception e) {}
		}
		return list;
		
	}
	
	public List<Map<String, Object>> selectList(String sql, List<Object> param){ //hashmap이 들어있는 리스트에 담아서 리턴
		List<Map<String, Object>> list = new ArrayList<>(); //메서드에 제일 위에다 리스트 변수 선언 -> 이 리스트는 리턴을 해줘야 하므로 try안에서 만들어 줄수 없음
		try {
			con = DriverManager.getConnection(url,user,password);
			ps = con.prepareStatement(sql);
			
			for(int i = 0; i < param.size(); i++){
				ps.setObject(i + 1, param.get(i));
			}
			rs = ps.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			
			while(rs.next()){
				Map<String, Object> row = new HashMap<>(); //리스트 내 hashmap 변수 선언
				for(int i = 1; i < columnCount; i++){
					String key = md.getColumnName(i); //컬럼이름 key에 넣어주고
					Object value = rs.getObject(i); // i번째 행 내용 value에 넣어줌
					row.put(key, value);
				}
				list.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		if (rs != null)try {rs.close();} catch (Exception e) {}
		if (ps != null)try {ps.close();} catch (Exception e) {}
		if (con != null)try {con.close();} catch (Exception e) {}
		}
		return list;
	}
	
	public int update(String sql){
		int update = 0;
		try {
			con = DriverManager.getConnection(url,user,password);
			ps = con.prepareStatement(sql);
			update = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)try {rs.close();} catch (Exception e) {}
			if (ps != null)try {ps.close();} catch (Exception e) {}
			if (con != null)try {con.close();} catch (Exception e) {}
		}
		return update;
		
		
	}
	
	public int update(String sql, List<Object> param){
		int update = 0;
		List<Object> list = new ArrayList<>();
		try {
			con = DriverManager.getConnection(url,user,password);
			ps = con.prepareStatement(sql);
			
			for(int i = 0; i < param.size(); i++){
				ps.setObject(i+1, param.get(i));
			}
			update = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)try {rs.close();} catch (Exception e) {}
			if (ps != null)try {ps.close();} catch (Exception e) {}
			if (con != null)try {con.close();} catch (Exception e) {}
		}
		return update;
		
	}
	
	
}
