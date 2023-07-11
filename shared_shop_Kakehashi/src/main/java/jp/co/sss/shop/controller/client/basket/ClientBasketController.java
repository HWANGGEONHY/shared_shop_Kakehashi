package jp.co.sss.shop.controller.client.basket;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.util.Constant;

@Controller
public class ClientBasketController {

	@Autowired
	ItemRepository itemrepository;

	@Autowired
	UserRepository userrepository;

	@RequestMapping(path = "/client/basket/list", method = RequestMethod.GET)
	String showBasketList(HttpSession session, Model model) {

		List<BasketBean> basketBean = (List<BasketBean>) session.getAttribute("baskets");
		
		if (basketBean != null) {
			
			for (int i = 0; i < basketBean.size(); i++) {
				BasketBean bean = new BasketBean();
				bean.setId(basketBean.get(i).getId());
				bean.setName(basketBean.get(i).getName());
				bean.setStock(itemrepository.findByIdAndDeleteFlag(basketBean.get(i).getId(), Constant.NOT_DELETED).getStock());
				bean.setImage(basketBean.get(i).getImage());
				bean.setOrderNum(basketBean.get(i).getOrderNum());
				basketBean.set(i, bean);
			}

			session.setAttribute("baskets", basketBean);
		}
		
		model.addAttribute("baskets", session.getAttribute("baskets"));

		return "client/basket/list";
	}

	@RequestMapping(path = "/client/basket/add", method = RequestMethod.POST)
	String addBasketList(String page,Integer categoryId,String itemName,String t_select,HttpSession session, String t_img, Integer t_id, String t_name, Integer t_stock,Integer t_order, Model model) {

		
		if (itemrepository.findByIdOrderById(t_id).getStock() == 0) {
			model.addAttribute("page", page);
			model.addAttribute("select", categoryId);
			model.addAttribute("search", itemName);
			model.addAttribute("t_select",t_select);
			model.addAttribute("items", itemrepository.findByIdOrderById(t_id));
			return "forward:/client/item/detail/" + t_id;
		}
		
		
		if ((List<BasketBean>) session.getAttribute("baskets") == null) {
			List<BasketBean> basketBean = new ArrayList<>();
			BasketBean bean = new BasketBean();

			bean.setId(t_id);
			bean.setName(t_name);
			bean.setStock(t_stock);
			bean.setOrderNum(t_order);
			bean.setImage(t_img);
			basketBean.add(bean);

			session.setAttribute("baskets", basketBean);

			model.addAttribute("baskets", session.getAttribute("baskets"));

		} else {

			List<BasketBean> basketBean = (List<BasketBean>) session.getAttribute("baskets");
			BasketBean bean = new BasketBean();

			boolean tf = true;

			for (BasketBean basketBean2 : basketBean) {
				if (basketBean2.getId() == t_id) {
					Integer a = basketBean2.getOrderNum();
					if(a + t_order > t_stock) {
						model.addAttribute("message", 2);
						model.addAttribute("page", page);
						model.addAttribute("select", categoryId);
						model.addAttribute("search", itemName);
						model.addAttribute("t_select",t_select);
						model.addAttribute("items", itemrepository.findByIdOrderById(t_id));
						return "forward:/client/item/detail/" + t_id;
						
					}else {
					basketBean2.setId(t_id);
					basketBean2.setName(t_name);
					basketBean2.setStock(t_stock);
					bean.setImage(t_img);
					basketBean2.setOrderNum(a + t_order);
					tf = false;
					}
				}
			}
			if (tf) {
				bean.setId(t_id);
				bean.setName(t_name);
				bean.setStock(t_stock);
				bean.setImage(t_img);
				bean.setOrderNum(t_order);
				basketBean.add(bean);
			}
			session.setAttribute("baskets", basketBean);
			model.addAttribute("baskets", session.getAttribute("baskets"));

		}
		model.addAttribute("message", 1);
		model.addAttribute("page", page);
		model.addAttribute("select", categoryId);
		model.addAttribute("search", itemName);
		model.addAttribute("t_select",t_select);
		model.addAttribute("items", itemrepository.findByIdOrderById(t_id));
		return "forward:/client/item/detail/" + t_id;
	}

	@RequestMapping(path = "/client/basket/delete/{id}", method = RequestMethod.POST)
	String deleteBasketList(@PathVariable Integer id, HttpSession session, Model model) {

		List<BasketBean> basketBean = (List<BasketBean>) session.getAttribute("baskets");

		int a = 0;
		for (int k = 0; k < basketBean.size(); k++) {
			if (basketBean.get(k).getId() == id) {
				a = k;
			}

		}
		basketBean.remove(a);

		session.setAttribute("baskets", basketBean);
		model.addAttribute("baskets", session.getAttribute("baskets"));

		return "redirect:/client/basket/list";
	}

	@RequestMapping(path = "/client/basket/deleteAll", method = RequestMethod.POST)
	String deleteAllBasketList(HttpSession session, Model model) {

		session.setAttribute("baskets", null);

		return "redirect:/client/basket/list";
	}
}
