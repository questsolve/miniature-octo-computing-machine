package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;
import com.model2.mvc.service.user.vo.UserVO;


public class AddPurchaseAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PurchaseVO purchaseVO = new PurchaseVO();
		ProductService productService = new ProductServiceImpl();
		UserService userService = new UserServiceImpl();
		PurchaseService purchaseService = new PurchaseServiceImpl();
		
		ProductVO productVO = productService.getProduct(Integer.parseInt(request.getParameter("prodNo") ) );
		System.out.println("#:"+productVO);
		UserVO userVO = userService.getUser(request.getParameter("buyerId"));
		System.out.println("#"+userVO);
		String payOption = request.getParameter("paymentOption");
		String receiverName = request.getParameter("receiverName");
		String receiverPhone = request.getParameter("receiverPhone");
		String receiverAddr = request.getParameter("receiverAddr");
		String receiverRequest= request.getParameter("receiverRequest");
		String receiverDate = request.getParameter("receiverDate");
		 		
		purchaseVO.setBuyer(userVO);
		purchaseVO.setDivyAddr(receiverAddr);
		purchaseVO.setDivyDate(receiverDate);
		purchaseVO.setDivyRequest(receiverRequest);
		purchaseVO.setPaymentOption(payOption);
		purchaseVO.setPurchaseProd(productVO);
		purchaseVO.setReceiverName(receiverName);
		purchaseVO.setReceiverPhone(receiverPhone);
		
		purchaseService.addPurchase(purchaseVO);	
		System.out.println("DB에 들어간 내용 : "+purchaseVO);
		
		request.setAttribute("purchaseVO", purchaseVO);
				
		return "forward:/purchase/addPurchase.jsp";
	}

}
