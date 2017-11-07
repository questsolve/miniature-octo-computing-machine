package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.vo.UserVO;

public class AddPurchaseViewAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
				
		ProductVO productVO = (ProductVO)request.getAttribute("productVO");
		
		if(productVO==null) {
			int prodNo = Integer.parseInt(request.getParameter("prodNo"));
			System.out.println("0"+prodNo);
			ProductService proService = new ProductServiceImpl();
			productVO = proService.getProduct(prodNo);
						
		}
		System.out.print("1");
		System.out.println(productVO);
		
		//정상 작동
		UserVO userVO = (UserVO)session.getAttribute("user");
		System.out.print("2");
		System.out.println(userVO);
		
		
		PurchaseVO purchaseVO = new PurchaseVO();
		purchaseVO.setBuyer(userVO);
		purchaseVO.setPurchaseProd(productVO);
		
		request.setAttribute("purchaseVO", purchaseVO);
				
		return "forward:/purchase/addPurchaseView.jsp";
	}

}
