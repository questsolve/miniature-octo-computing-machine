package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;

public class GetPurchaseAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			
		PurchaseService purchaseService = new PurchaseServiceImpl();
		PurchaseVO	purchaseVO = new PurchaseVO();
		
		//거래번호로 구매 내역 추출하기(from DB)
		int tranNo = Integer.parseInt(request.getParameter("tranNo"));
		System.out.println(tranNo);
		purchaseVO = purchaseService.getPurchase(tranNo);
		
		
		request.setAttribute("purchaseVO", purchaseVO);
		
		
		return "forward:/purchase/getPurchase.jsp";
	}

}
