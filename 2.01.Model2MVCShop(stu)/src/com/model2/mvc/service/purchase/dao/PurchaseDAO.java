package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.RequestDispatcher;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;


public class PurchaseDAO {

	public PurchaseDAO() {
	}
	
	public void insertPurchase(PurchaseVO purchaseVO) throws Exception{
		//구매정보를 추가하는 메소드 구현
		Connection con = DBUtil.getConnection();
		
		
		String sql = "INSERT INTO transaction "
				+ "VALUES (seq_transaction_tran_no.nextval, ?, ?, ?, ?, ?, ?, ?, '0', SYSDATE, ?)";
		
		PreparedStatement preState = con.prepareStatement(sql);
		preState.setInt(1, purchaseVO.getPurchaseProd().getProdNo());
		preState.setString(2, purchaseVO.getBuyer().getUserId());
		preState.setString(3, purchaseVO.getPaymentOption());
		preState.setString(4, purchaseVO.getReceiverName());
		preState.setString(5, purchaseVO.getReceiverPhone());
		preState.setString(6, purchaseVO.getDivyAddr());
		preState.setString(7, purchaseVO.getDivyRequest());
		//배송희망일 쿼리 정리하기
		preState.setString(8, purchaseVO.getDivyDate());
		
		preState.executeUpdate();
		System.out.println(" INSERTPURCHASE에서 DB로 : " +purchaseVO);
		preState.close();
		con.close();
		
	}
	
	public PurchaseVO findPurchase(int tranNo) throws Exception {
		//구매정보를 검색하는 메소드 구현
		//필요한 데이터를 만드는데 사용되는 인스턴스들
		PurchaseVO purchaseVO = new PurchaseVO();
		ProductService productService = new ProductServiceImpl();
		UserService userService = new UserServiceImpl();
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT * FROM transaction WHERE tran_no = ?";
		PreparedStatement preState = con.prepareStatement(sql);
		preState.setInt(1, tranNo);
		ResultSet rs = preState.executeQuery();
		
		if(rs.next()) {
			//바이어 틀렸대
			purchaseVO.setBuyer(userService.getUser(rs.getString("buyer_id")));
			purchaseVO.setDivyAddr(rs.getString("dlvy_addr"));
			purchaseVO.setDivyDate(rs.getString("dlvy_date"));
			purchaseVO.setDivyRequest(rs.getString("dlvy_request"));
			purchaseVO.setOrderDate(rs.getDate("order_date"));
			purchaseVO.setPaymentOption(rs.getString("payment_option"));
			purchaseVO.setPurchaseProd(productService.getProduct(rs.getInt("prod_no")));
			purchaseVO.setReceiverName(rs.getString("receiver_name"));
			purchaseVO.setReceiverPhone(rs.getString("receiver_phone"));
			purchaseVO.setTranCode(rs.getString("tran_status_code"));
			purchaseVO.setTranNo(rs.getInt("tran_no"));
		}
		
		rs.close();
		preState.close();
		con.close();
		
		
		return purchaseVO;
	}
	
	public Map<String,Object> getPurchaseList(SearchVO searchVO, String buyer) throws Exception{
		//구매목록을 검색하는 메소드 구현
		Connection con = DBUtil.getConnection();
		UserService userService = new UserServiceImpl();
		ProductService productService = new ProductServiceImpl();
				
		String sql = "SELECT * FROM transaction WHERE buyer_id = ? ";
		sql += "ORDER BY tran_no";
		PreparedStatement preState = con.prepareStatement(sql, 
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		preState.setString(1, buyer);
		ResultSet rs = preState.executeQuery();
	
		//전체 개수 확인
		rs.last();
		int total = rs.getRow();
		System.out.println("총 데이터 수 : " + total);
		Map<String, Object> purchaseMap = new HashMap<String,Object>();
		purchaseMap.put("purchaseCount", new Integer(total));
		
		rs.absolute(searchVO.getPageUnit()*(searchVO.getPage() - 1)+1);
		//페이지 확인용
		System.out.println("searchVO.getPage() : " + searchVO.getPage());
		System.out.println("searchVO.getPageUnit() : " + searchVO.getPageUnit());
		
		List<PurchaseVO> purchaseList = new Vector<PurchaseVO>();
		if(total>0){
			for(int i =0; i< searchVO.getPageUnit();++i) {
				PurchaseVO purchaseVO = new PurchaseVO();
				purchaseVO.setBuyer(userService.getUser(rs.getString("buyer_id") ) );
				purchaseVO.setPurchaseProd(productService.getProduct(rs.getInt("prod_no") ) );
				purchaseVO.setDivyAddr(rs.getString("dlvy_addr"));
				purchaseVO.setDivyDate(rs.getString("dlvy_date"));
				purchaseVO.setDivyRequest(rs.getString("dlvy_request"));
				purchaseVO.setOrderDate(rs.getDate("order_date"));
				purchaseVO.setPaymentOption(rs.getString("payment_option"));
				purchaseVO.setReceiverName(rs.getString("receiver_name"));
				purchaseVO.setReceiverPhone(rs.getString("receiver_phone"));
				purchaseVO.setTranCode(rs.getString("tran_status_code"));
				purchaseVO.setTranNo(rs.getInt("tran_no"));
				purchaseList.add(purchaseVO);
				if(!rs.next()) {
					break;
				}
			}
		}
		purchaseMap.put("purchaseList", purchaseList);
		
		System.out.println("purchaseList 개수 : " + purchaseList.size());
		System.out.println("purchaseMap 개수" + purchaseMap.size());
		
		for(int i =0; i<purchaseList.size();++i) {
			System.out.println("%%" + purchaseList.get(i));
		}
					
		return purchaseMap;	
					
	}
	
	public HashMap<String,Object> getSaleList(SearchVO searchVO) throws Exception{
		//판매목록을 검색하는 메소드 구현
		return null;
	}
	
	public void updatePurchase(PurchaseVO purchaseVO) throws Exception{
		//구매자가 배송정보를 변경하는 메소드 구현
		
		//DB 접속
		Connection con = DBUtil.getConnection();
		
		//query문
		String sql = "UPDATE transaction SET payment_option = ?, receiver_name = ?, "
				+ "receiver_phone = ?, dlvy_addr = ?, dlvy_request = ?, dlvy_date = ? "
				+ "WHERE buyer_id = ? AND prod_no = ?";
		
		//query문 완성
		PreparedStatement preState = con.prepareStatement(sql);
		preState.setString(1, purchaseVO.getPaymentOption() );
		preState.setString(2, purchaseVO.getReceiverName());
		preState.setString(3, purchaseVO.getReceiverPhone());
		preState.setString(4, purchaseVO.getDivyAddr());
		preState.setString(5, purchaseVO.getDivyRequest());
		preState.setString(6, purchaseVO.getDivyDate());
		preState.setString(7, purchaseVO.getBuyer().getUserId());
		preState.setInt(8, purchaseVO.getPurchaseProd().getProdNo());
		
		preState.executeUpdate();
		
	}
	
	public void updateTranCode(PurchaseVO purchaseVO) {
		//판매자가 판매 상태를 변경하는 메소드를 구현
		Connection con = DBUtil.getConnection();
		String sql = "UPDATE transaction SET tran_status_code = 1";
		
	}

}
