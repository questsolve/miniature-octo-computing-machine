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
		//���������� �߰��ϴ� �޼ҵ� ����
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
		//�������� ���� �����ϱ�
		preState.setString(8, purchaseVO.getDivyDate());
		
		preState.executeUpdate();
		System.out.println(" INSERTPURCHASE���� DB�� : " +purchaseVO);
		preState.close();
		con.close();
		
	}
	
	public PurchaseVO findPurchase(int tranNo) throws Exception {
		//���������� �˻��ϴ� �޼ҵ� ����
		//�ʿ��� �����͸� ����µ� ���Ǵ� �ν��Ͻ���
		PurchaseVO purchaseVO = new PurchaseVO();
		ProductService productService = new ProductServiceImpl();
		UserService userService = new UserServiceImpl();
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT * FROM transaction WHERE tran_no = ?";
		PreparedStatement preState = con.prepareStatement(sql);
		preState.setInt(1, tranNo);
		ResultSet rs = preState.executeQuery();
		
		if(rs.next()) {
			//���̾� Ʋ�ȴ�
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
		//���Ÿ���� �˻��ϴ� �޼ҵ� ����
		Connection con = DBUtil.getConnection();
		UserService userService = new UserServiceImpl();
		ProductService productService = new ProductServiceImpl();
				
		String sql = "SELECT * FROM transaction WHERE buyer_id = ? ";
		sql += "ORDER BY tran_no";
		PreparedStatement preState = con.prepareStatement(sql, 
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		preState.setString(1, buyer);
		ResultSet rs = preState.executeQuery();
	
		//��ü ���� Ȯ��
		rs.last();
		int total = rs.getRow();
		System.out.println("�� ������ �� : " + total);
		Map<String, Object> purchaseMap = new HashMap<String,Object>();
		purchaseMap.put("purchaseCount", new Integer(total));
		
		rs.absolute(searchVO.getPageUnit()*(searchVO.getPage() - 1)+1);
		//������ Ȯ�ο�
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
		
		System.out.println("purchaseList ���� : " + purchaseList.size());
		System.out.println("purchaseMap ����" + purchaseMap.size());
		
		for(int i =0; i<purchaseList.size();++i) {
			System.out.println("%%" + purchaseList.get(i));
		}
					
		return purchaseMap;	
					
	}
	
	public HashMap<String,Object> getSaleList(SearchVO searchVO) throws Exception{
		//�ǸŸ���� �˻��ϴ� �޼ҵ� ����
		return null;
	}
	
	public void updatePurchase(PurchaseVO purchaseVO) throws Exception{
		//�����ڰ� ��������� �����ϴ� �޼ҵ� ����
		
		//DB ����
		Connection con = DBUtil.getConnection();
		
		//query��
		String sql = "UPDATE transaction SET payment_option = ?, receiver_name = ?, "
				+ "receiver_phone = ?, dlvy_addr = ?, dlvy_request = ?, dlvy_date = ? "
				+ "WHERE buyer_id = ? AND prod_no = ?";
		
		//query�� �ϼ�
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
		//�Ǹ��ڰ� �Ǹ� ���¸� �����ϴ� �޼ҵ带 ����
		Connection con = DBUtil.getConnection();
		String sql = "UPDATE transaction SET tran_status_code = 1";
		
	}

}
