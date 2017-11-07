package com.model2.mvc.view.product;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.dao.ProductDAO;
import com.model2.mvc.service.product.vo.ProductVO;

public class GetProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int prodNo =Integer.parseInt(request.getParameter("prodNo"));
		ProductDAO productDAO = new ProductDAO();
		ProductVO productVO = productDAO.findProduct(prodNo);
		System.out.println(productVO);
		request.setAttribute("productVO", productVO);
		
		String history = null;
		Cookie[] cookies = request.getCookies();
		if (cookies!=null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookie.getName().equals("history")) {
					history = cookie.getValue();
				}
			}
		}
		history += ","+prodNo;
		Cookie cookie = new Cookie("history",history);
		response.addCookie(cookie);		
		return "forward:/product/getProduct.jsp";
	}

}
