package com.model2.mvc.view.product;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;

public class ListProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SearchVO searchVO = new SearchVO();
		
		int page = 1;
		if(request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));			
		}
		searchVO.setPage(page);
		
		searchVO.setSearchCondition(request.getParameter("searchCondition"));
		searchVO.setSearchKeyword(request.getParameter("searchKeyword"));
				
		String pageUnit = getServletContext().getInitParameter("pageSize");
		searchVO.setPageUnit(Integer.parseInt(pageUnit));
		
		String menu = request.getParameter("menu");
		
		
		ProductService productService = new ProductServiceImpl();
		Map<String,Object> productMap = productService.getProductList(searchVO);
		
		request.setAttribute("productMap", productMap);
		request.setAttribute("productSearchVO", searchVO);
		request.setAttribute("menu", menu);
				
		return "forward:/product/listProduct.jsp";
	}

}
