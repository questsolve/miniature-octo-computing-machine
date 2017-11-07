package com.model2.mvc.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.model2.mvc.common.pool.OracleConnectionPool;


public class DBUtil {
	
	private final static String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private final static String JDBC_URL = "jdbc:oracle:thin:scott/tiger@localhost:1521:xe";

	public static Connection getConnection() {
		Connection conn = null;
		try {
			//Class.forName(JDBC_DRIVER);
			//conn = DriverManager.getConnection(JDBC_URL);
			conn= OracleConnectionPool.getInstance().getConnection();
			System.out.println("ConnectionPool로 연결함");
		/*} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();*/
		}catch(Exception e) {
			e.getMessage();
		}finally {
			if(conn == null) {
				try {
					Class.forName(JDBC_DRIVER);
					conn = DriverManager.getConnection(JDBC_URL);
					System.out.println("DB에 직접 연결함");
				}catch(ClassNotFoundException e) {
					e.printStackTrace();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return conn;
	}
}