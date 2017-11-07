package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;
import java.util.Map;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.dao.PurchaseDAO;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.dao.UserDAO;

public class PurchaseServiceImpl implements PurchaseService {

	PurchaseDAO purchaseDAO;
	
	public PurchaseServiceImpl() {
		purchaseDAO = new PurchaseDAO();
	}

	@Override
	public void addPurchase(PurchaseVO purchaseVO) throws Exception {
		purchaseDAO.insertPurchase(purchaseVO);
		System.out.println("DATA PurchaseServiceImpl µµÂø : "+purchaseVO);

	}

	@Override
	public PurchaseVO getPurchase(int tranNo) throws Exception {
		return purchaseDAO.findPurchase(tranNo);
	}

	@Override
	public PurchaseVO getPurchase2(int ProdNo) throws Exception {
		return this.getPurchase(ProdNo);
		
	}

	@Override
	public Map<String, Object> getPurchaseList(SearchVO searchVO, String buyerId) throws Exception {
		return purchaseDAO.getPurchaseList(searchVO,buyerId);
		 
	}

	@Override
	public HashMap<String, Object> getSaleList(SearchVO searchVO) throws Exception {
		return purchaseDAO.getSaleList(searchVO);
	}

	@Override
	public void updatePurchase(PurchaseVO purchaseVO) throws Exception {
		purchaseDAO.updatePurchase(purchaseVO);

	}

	@Override
	public void updateTranCode(PurchaseVO purchaseVO) throws Exception {
		purchaseDAO.updateTranCode(purchaseVO);
	}

}