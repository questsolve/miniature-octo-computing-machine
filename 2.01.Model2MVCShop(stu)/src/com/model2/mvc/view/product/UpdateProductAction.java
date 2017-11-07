package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.dao.ProductDAO;
import com.model2.mvc.service.product.impl.ProductServiceImpl;

public class UpdateProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		ProductDAO dao = new ProductDAO();
		ProductVO productVO= dao.findProduct(prodNo);
		productVO.setProdNo(prodNo);
		productVO.setProdName(request.getParameter("prodName"));
		productVO.setProdDetail(request.getParameter("prodDetail"));
		productVO.setManuDate(request.getParameter("manuDate"));
		productVO.setPrice(Integer.parseInt(request.getParameter("price") ) );
		productVO.setFileName(request.getParameter("fileName"));
		productVO.setRegDate(productVO.getRegDate());		
		
		System.out.println(productVO);
		
		ProductService service = new ProductServiceImpl();
		service.updateProduct(productVO);
		
		ProductVO vo = (ProductVO)request.getAttribute("productVO");
		
		
		
		
		return "redirect:/getProduct.do?prodNo="+prodNo;
	}
	
}
