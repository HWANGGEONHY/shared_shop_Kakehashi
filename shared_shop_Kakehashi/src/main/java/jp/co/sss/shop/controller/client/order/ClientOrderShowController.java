package jp.co.sss.shop.controller.client.order;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.repository.UserRepository;

/**
 * 注文詳細、一覧表示クラス
 * @author 田中一馬
 */
@Controller
public class ClientOrderShowController {
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderItemRepository orderItemRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	HttpSession session;

	@RequestMapping(path = "/client/order/list", method = RequestMethod.POST)
	public String ShowList(Model model) {
		Boolean isEmpty = true;
		UserBean userBean = (UserBean) session.getAttribute("user");
		//String URL = request.getRequestURI()
		Integer userId = userBean.getId();
		User user = new User();
		user = userRepository.getReferenceById(userId);
		List<Order> orderList = orderRepository.findByUserOrderByInsertDateDesc(user);
		if (orderList == null || (orderList.isEmpty())) {
			isEmpty = true;
			model.addAttribute("isEmpty", isEmpty);
		} else {
			isEmpty = false;
			List<String> payment = new ArrayList<String>();
			for (int i = 0; i < orderList.size(); i++) {
				switch (orderList.get(i).getPayMethod()) {
				case 1:
					payment.add("クレジットカード");
					break;
				case 2:
					payment.add("銀行振込");
					break;
				case 3:
					payment.add("着払い");
					break;
				case 4:
					payment.add("電子マネー");
					break;
				case 5:
					payment.add("コンビニ決済");
					break;
				}
			}
			model.addAttribute("payment", payment);
			model.addAttribute("orders", orderList);
			model.addAttribute("isEmpty", isEmpty);
			List<Integer> value = new ArrayList<>();
			for(Order order : orderList ) {
				List<OrderItem> itemList = orderItemRepository.findByOrder(order);
				Integer sum = 0;
				for(OrderItem orderItem : itemList) {
					Integer unitPrice = orderItem.getPrice();
					Integer quantiti = orderItem.getQuantity();
					sum += unitPrice * quantiti;
				}
				value.add(sum);
			}
			model.addAttribute("value", value);
		}
		return "client/order/list";
	}

	@RequestMapping(path = "/client/order/detail/{id}", method = RequestMethod.GET)
	public String ShowDetail(@PathVariable("id") Integer id, Model model) {
		Order order = new Order();
		order = orderRepository.getReferenceById(id);
		List<OrderItem> orderItem = orderItemRepository.findByOrder(order);
		model.addAttribute("orderItems", orderItem);
		model.addAttribute("orders", order);
		switch (order.getPayMethod()) {
		case 1:
			model.addAttribute("payment", "クレジットカード");
			break;
		case 2:
			model.addAttribute("payment", "銀行振込");
			break;
		case 3:
			model.addAttribute("payment", "着払い");
			break;
		case 4:
			model.addAttribute("payment", "電子マネー");
			break;
		case 5:
			model.addAttribute("payment", "コンビニ決済");
			break;
		}
		
		List<Integer> subTotal = new ArrayList<>();
		Integer total = 0;
		for(OrderItem item : orderItem) {
			Integer sum = 0;
			Integer unitPrice = item.getPrice();
			Integer quantiti = item.getQuantity();
			sum = unitPrice * quantiti;
			subTotal.add(sum);
			total+=sum;
		}
		model.addAttribute("total",total);
		model.addAttribute("subTotal", subTotal);

		return "client/order/detail";
	}

}
