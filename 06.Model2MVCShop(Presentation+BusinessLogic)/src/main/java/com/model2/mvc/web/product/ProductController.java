package com.model2.mvc.web.product;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;


@Controller
public class ProductController {

	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	public ProductController() {
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml 참조 할것
	//==> 아래의 두개를 주석을 풀어 의미를 확인 할것
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	@RequestMapping("/addProductView.do")
	public String addProductView() throws Exception {

		System.out.println("/addProductView.do");
		
		return "redirect:/product/addProductView.jsp";
	}
	
	@RequestMapping("/addProduct.do")
	public String AddProduct(@ModelAttribute("product")Product product) throws Exception {
		System.out.println("/addProduct.do");
		//Business Logic
		productService.addProduct(product);
		
		
		return "redirect:/product/addProduct.jsp";
		}
	
	@RequestMapping("/getProduct.do")
	public String GetProduct( @RequestParam("prodNo") String prodNo , Model model ) throws Exception {
		
		System.out.println("/getUser.do");
		//Business Logic
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		
		// Model 과 View 연결
		model.addAttribute("product", product);
		
		return "forward:/product/getProduct.jsp";
	}
		
	
	@RequestMapping("/updateProduct.do")
	 public void UpdateProduct(@ModelAttribute("product")Product product , Model model , HttpSession session) throws Exception{
		System.out.println("/updateUser.do");
		//Business Logic
		productService.updateProduct(product);
		
		int sessionId = ((Product)session.getAttribute("product")).getProdNo();
		if(sessionId == (product.getProdNo())){
			session.setAttribute("product", product);
		}
		
	 }
	
	
	@RequestMapping("/listProduct.do")
	public String listProduct( @ModelAttribute("search") Search search , Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("/listProduct.do");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		System.out.println("어디까지실행되는지1");
		
		// Business logic 수행
		Map<String , Object> map = productService.getProductList(search);
		System.out.println("어디까지실행되는지2");
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		System.out.println("어디까지실행되는지3");
		
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		System.out.println("어디까지실행되는지4");
		model.addAttribute("resultPage", resultPage);
		System.out.println("어디까지실행되는지5");
		model.addAttribute("search", search);
		System.out.println("어디까지실행되는지6");
		
		return "forward:/product/listProduct.jsp";
	 	
	 }
	 
	
}
