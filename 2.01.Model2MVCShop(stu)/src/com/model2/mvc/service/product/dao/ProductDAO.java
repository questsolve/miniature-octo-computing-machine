package com.model2.mvc.service.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.product.vo.ProductVO;

public class ProductDAO {

	public ProductDAO() {
	}
	
	public void insertProduct(ProductVO productVO) throws SQLException {
		
		
		Connection con = DBUtil.getConnection();
		String sql = "INSERT INTO PRODUCT (prod_no, prod_name, "
				+ "prod_detail, manufacture_day, price, image_file, reg_date)"
					+ " VALUES (seq_product_prod_no.nextval, ?, ?, ?, ?, ?, SYSDATE)";
		PreparedStatement preState = con.prepareStatement(sql);
		preState.setString(1, productVO.getProdName());
		preState.setString(2, productVO.getProdDetail());
		preState.setString(3, productVO.getManuDate().replaceAll("-", "").trim());
		preState.setInt(4, productVO.getPrice());
		preState.setString(5, productVO.getFileName());
		preState.executeQuery();
			
		con.close();
			
	}
	
	public ProductVO findProduct(int prodNo) {
		//상품을 검색하는 메소드 구현
		Connection con = null;
		ProductVO productVO = new ProductVO();
		
		try {
			con = DBUtil.getConnection();
			String sql = "SELECT * "
					+ "FROM product "
					+ "WHERE prod_No = ?";
			PreparedStatement preState = con.prepareStatement(sql);
			preState.setInt(1, prodNo);
			
			ResultSet rs = preState.executeQuery();
			
			while(rs.next()) {
				productVO.setProdNo(rs.getInt("PROD_NO"));
				productVO.setProdName(rs.getString("PROD_NAME"));
				productVO.setProdDetail(rs.getString("PROD_DETAIL"));
				productVO.setManuDate(rs.getString("MANUFACTURE_DAY"));
				productVO.setPrice(rs.getInt("PRICE"));
				productVO.setFileName(rs.getString("IMAGE_FILE"));
				productVO.setRegDate(rs.getDate("REG_DATE"));
			}
					
		}catch(Exception e) {
			e.getMessage();
		
		}finally {
		
			try {
				if(con != null) {
					con.close();
				}
			}catch(Exception e) {
				e.getMessage();
			}
		}
		
		return productVO;
	}
	
	public Map<String,Object> getProductList(SearchVO searchVO) throws Exception{
		//상품목록을 검색하는 메소드 구현
		Connection con = DBUtil.getConnection(); 
							
		String sql = "SELECT * FROM PRODUCT ";
		if(searchVO.getSearchCondition()!= null) {
			if(searchVO.getSearchCondition().equals("0")) {
				sql +="WHERE PROD_NO = "+searchVO.getSearchKeyword();
			}else if(searchVO.getSearchCondition().equals("1")) {
				sql += "WHERE PROD_NAME LIKE '%"+ searchVO.getSearchKeyword()+"%'";
			}else if(searchVO.getSearchCondition().equals("2")) {
				sql += "WHERE PRICE = " + searchVO.getSearchKeyword();
			}
		}
		sql += " ORDER BY PROD_NO";
					
		PreparedStatement preState = con.prepareStatement(sql, 
						ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = preState.executeQuery();
			
		rs.last();
		int total = rs.getRow();
		System.out.println("총 데이터 수 : " + total);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("productCount", new Integer(total));
			
		rs.absolute(searchVO.getPageUnit()*(searchVO.getPage() - 1)+1);
		//페이지 확인용
		System.out.println("searchVO.getPage() : " + searchVO.getPage());
		System.out.println("searchVO.getPageUnit() : " + searchVO.getPageUnit());
		
		//DP개수만큼 리스트에 저장
		List<ProductVO> list = new Vector<ProductVO>();
		if(total>0) {
			for(int i=0 ; i< searchVO.getPageUnit();++i) {
				ProductVO productVO = new ProductVO();
				productVO.setProdNo(rs.getInt("prod_no"));
				productVO.setProdName(rs.getString("prod_name"));
				productVO.setProdDetail(rs.getString("prod_detail"));
				productVO.setManuDate(rs.getString("manufacture_day"));
				productVO.setPrice(rs.getInt("price"));
				productVO.setFileName(rs.getString("image_file"));
				productVO.setRegDate(rs.getDate("reg_date"));
				list.add(productVO);
				if(!rs.next()) {
					break;
				}
			}
		}
		map.put("productList", list);
		System.out.println("list 개수" + list.size());
		System.out.println("map 개수" + map.size());
			
		
		return map;	
	}
	
	public void updateProduct(ProductVO productVO) throws Exception {
		Connection con = DBUtil.getConnection();
		String sql = "UPDATE  product SET"
					+ " prod_name = ?, prod_detail = ?, manufacture_day = ?, "
					+ "price = ?, image_file = ? "
					+ "WHERE prod_no =?";				
		PreparedStatement preState = con.prepareStatement(sql);
		preState.setString(1, productVO.getProdName());
		preState.setString(2, productVO.getProdDetail());
		preState.setString(3, productVO.getManuDate().replaceAll("-", "").trim());
		preState.setInt(4, productVO.getPrice());
		preState.setString(5, productVO.getFileName());
		preState.setInt(6, productVO.getProdNo());
		preState.executeQuery();
		
		if(con != null) {
			con.close();
		}
		
	}

	
}
