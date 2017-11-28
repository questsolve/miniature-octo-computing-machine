package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;

public class UpdateTranCodeAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PurchaseService purchaseService = new PurchaseServiceImpl();
		int tranNo = Integer.parseInt(request.getParameter("tranNo"));
		PurchaseVO purchaseVO = new PurchaseVO();
		if(tranNo<2) {
			purchaseVO = purchaseService.getPurchase(tranNo+1);
		}else {
			purchaseVO = purchaseService.getPurchase(tranNo);
		}
		purchaseService.updateTranCode(purchaseVO);
		
		
		return "forward:/listPurchase.do?";
	}

}
