package jp.co.sss.shop.controller.client.order;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.OrderForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.util.Constant;

/**
 * 注文内容入力クラス
 *
 * @author 和田
 */

@Controller
public class ClientOrderRegistController {
	
	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	OrderItemRepository orderItemRepository;
	
	/**
	 * セッション
	 */
	@Autowired
	HttpSession session;

	//処理1
	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.POST)
	public String addressForm(Model model) {
		//注文フォームの作成
		OrderForm form = new OrderForm();
		//セッションスコープからログイン会員情報を取得
		Integer id = ((UserBean) session.getAttribute("user")).getId();
		//取得したログイン会員情報のユーザIDを条件にDBからユーザ情報を取得
		User user = userRepository.findByIdAndDeleteFlag(id, Constant.NOT_DELETED);
		//取得したユーザ情報を注文入力フォーム情報に設定
		form.setId(user.getId());
		form.setAddress(user.getAddress());
		form.setName(user.getName());
		form.setPhoneNumber(user.getPhoneNumber());
		form.setPostalCode(user.getPostalCode());
		//注文入力フォーム情報の支払方法に初期値としてクレジットカードを設定
		form.setPayMethod(1);
		//注文入力フォーム情報をセッションスコープに保存
		session.setAttribute("order", form);
		return "redirect:/client/order/address/input";
	}

	//処理2
	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.GET)
	public String addressRegist(Model model) {
		
		BindingResult result = (BindingResult) session.getAttribute("result");
		BindingResult result1 = (BindingResult) session.getAttribute("resultError");
		if (result != null) {
			//セッションにエラー情報がある場合、エラー情報をスコープに設定
			model.addAttribute("org.springframework.validation.BindingResult.orderForm", result);
			System.out.println(result.getAllErrors());
			model.addAttribute("resultError", result1);
			// セッションにエラー情報を削除
			session.removeAttribute("result");
		} else {
			//セッションスコープから注文入力フォーム情報を取得
			OrderForm form = ((OrderForm) session.getAttribute("order"));
			//注文入力フォーム情報をリクエストスコープに設定
			model.addAttribute("orderForm",form);
		}
		
		return "client/order/address_input";
	}
	
	//処理3
	@RequestMapping(path = "/client/order/payment/input", method = RequestMethod.POST)
	public String paymentForm(@Valid @ModelAttribute OrderForm form, BindingResult bindingResult, RedirectAttributes ra) {
		
		//BindingResultオブジェクトに入力エラー情報がある場合
		if (bindingResult.hasErrors()) {
			
			// 入力値にエラーがあった場合、エラー情報をセッションに保持
			session.setAttribute("result", bindingResult);
			return "redirect:/client/order/address/input";
		} else {
			//リクエストスコープに画面から入力されたフォーム情報を注文入力フォーム情報として保存
			OrderForm orderForm = ((OrderForm) session.getAttribute("order"));
			orderForm.setAddress(form.getAddress());
			orderForm.setName(form.getName());
			orderForm.setPhoneNumber(form.getPhoneNumber());
			orderForm.setPostalCode(form.getPostalCode());
			//注文入力フォーム情報をセッションスコープに保存
			session.setAttribute("order",orderForm);
		
			return "redirect:/client/order/payment/input";
		}
	}
	
	//処理4
	@RequestMapping(path = "/client/order/payment/input", method = RequestMethod.GET)
	public String paymentRegist(OrderForm form, Model model) {
		//セッションスコープから注文入力フォーム情報を取得
		form = ((OrderForm) session.getAttribute("order"));
		//注文入力フォーム情報をリクエストスコープに設定
		model.addAttribute("payment",form);
		return "client/order/payment_input";
	}
	
	//処理5
	@RequestMapping(path = "/client/order/check", method = RequestMethod.POST)
	public String check(String payment, Model model) {
		//セッションスコープから注文入力フォーム情報を取得
		//form = ((OrderForm) session.getAttribute("order"));
		//画面から入力された支払方法を取得した注文入力フォーム情報に設定
		OrderForm orderForm = new OrderForm();
		orderForm = ((OrderForm) session.getAttribute("order"));
		Integer paymentMethod = 0;
		switch (payment) {
		case "1":
			paymentMethod = 1;
			break;
		case "2":
			paymentMethod = 2;
			break;
		case "3":
			paymentMethod = 3;
			break;
		case "4":
			paymentMethod = 4;
			break;
		case "5":
			paymentMethod = 5;
			break;
		}
		orderForm.setPayMethod(paymentMethod);
		//注文入力フォーム情報をセッションスコープに保存
		session.setAttribute("order",orderForm);
		return "redirect:/client/order/check";
	}
	
	//処理6
	@RequestMapping(path = "/client/order/check", method = RequestMethod.GET)
	public String checkFin(OrderForm form, Model model, RedirectAttributes ra) {
		//セッションスコープから注文情報を取得
		form = ((OrderForm) session.getAttribute("order"));
		//セッションスコープから買い物かご情報を取得
		List<BasketBean> basketBean = ((List<BasketBean>)session.getAttribute("baskets"));
		//注文商品の最新情報をDBから取得し、商品の在庫チェックをする
		
		String message = "";
		List<String> noAll = new ArrayList<String>();
		List<String> noPart = new ArrayList<String>();
		//注文商品の在庫チェック用
		List<Integer> flag = new ArrayList<Integer>();
		Integer cnt = basketBean.size();
		for (int i = 0; i < basketBean.size(); i++) {
			if (itemRepository.findByIdOrderById(basketBean.get(i).getId()).getStock() == 0) {
				//注文警告メッセージをリクエストスコープに保存
				message = basketBean.get(i).getName() +"は、在庫切れのため注文できません。";
				noAll.add(basketBean.get(i).getName());
				model.addAttribute("messages", message);
				
				flag.add(1);
				cnt--;
				
			}else if (basketBean.get(i).getOrderNum() > itemRepository.findByIdOrderById(basketBean.get(i).getId()).getStock()) {
				//注文警告メッセージをリクエストスコープに保存
				message = basketBean.get(i).getName() +"は、在庫不足のため、在庫数分のみ注文できます。";
				model.addAttribute("messages", message);
				//在庫数にあわせて、買い物かご情報を更新（注文数、在庫数）
				basketBean.get(i).setOrderNum(itemRepository.findByIdOrderById(basketBean.get(i).getId()).getStock());
				noPart.add(basketBean.get(i).getName());
				flag.add(0);
			} else {
				flag.add(0);
			}
		}
		
		if (cnt == 0) {
			model.addAttribute("msg", 1);
			model.addAttribute("noAll", noAll);
			//セッションスコープの注文入力フォーム情報と買い物かご情報を削除
			session.removeAttribute("order");
			session.removeAttribute("baskets");
			return "client/order/check";
		}
		
		for (int i = 0; i < flag.size(); i++) {
			if (flag.get(i) == 1) {
				//在庫切れの商品は、買い物かごか情報から削除
				basketBean.remove(i);
			}
		}
		
		model.addAttribute("noAll", noAll);
		model.addAttribute("noPart", noPart);
		
		//単価取得
		//List<Integer> price = new ArrayList<Integer>();
		for (int i = 0; i < basketBean.size(); i++) {
			basketBean.get(i).setPrice(itemRepository.findByIdOrderById(basketBean.get(i).getId()).getPrice());
		}
		//画像取得
		for (int i = 0; i < basketBean.size(); i++) {
			basketBean.get(i).setImage(itemRepository.findByIdOrderById(basketBean.get(i).getId()).getImage());
		}
		
		//在庫状況を反映した買い物かご情報をセッションに保存
		session.setAttribute("baskets", basketBean);
		model.addAttribute("baskets", session.getAttribute("baskets"));
		//買い物かご情報から、商品ごとの金額小計と全額を算出し、注文入力フォーム情報に設定
		//小計
		Integer sub = 0;
		List<Integer> subTotal = new ArrayList<Integer>();
		for (int i = 0; i < basketBean.size(); i++) {
			sub = basketBean.get(i).getOrderNum() * itemRepository.findByIdOrderById(basketBean.get(i).getId()).getPrice();
			subTotal.add(sub);
			basketBean.get(i).setSubTotal(sub);
		}
		model.addAttribute("subTotals", subTotal);
		//全額
		Integer fullAmount = 0;
		for (int i = 0; i< subTotal.size(); i++) {
			fullAmount += subTotal.get(i);
		}
		model.addAttribute("fullAmount", fullAmount);
		//注文入力フォーム情報をリクエストスコープに設定
		model.addAttribute("orders", form);
		
		switch (form.getPayMethod()) {
		case 1:
			model.addAttribute("paymentMethod", "クレジットカード");
			break;
		case 2:
			model.addAttribute("paymentMethod", "銀行振込");
			break;
		case 3:
			model.addAttribute("paymentMethod", "着払い");
			break;
		case 4:
			model.addAttribute("paymentMethod", "電子マネー");
			break;
		case 5:
			model.addAttribute("paymentMethod", "コンビニ決済");
			break;
		}
		model.addAttribute("msg", 0);
		model.addAttribute("noPart", noPart);
		return "client/order/check";
	}
	
	//処理7はhtmlのonclick="history.back()"で処理
	
	//処理8
	@RequestMapping(path = "/client/order/complete", method = RequestMethod.POST)
	public String complete(OrderForm form, Model model, RedirectAttributes ra) {
		//セッションスコープから注文入力フォーム情報を取得
		form = ((OrderForm) session.getAttribute("order"));
		//セッションスコープから買い物かご情報を取得
		List<BasketBean> basketBean = ((List<BasketBean>)session.getAttribute("baskets"));
		
		//注文商品の在庫チェックをする
		List<Integer> flag = new ArrayList<Integer>();
		for (int i = 0; i < basketBean.size(); i++) {
			if (itemRepository.findByIdOrderById(basketBean.get(i).getId()).getStock() == 0) {
				flag.add(1);
			} else {
				flag.add(0);
			}
		}
		
		String msg = "";		
		List<String> message = new ArrayList<String>();
		List<String> noAll = new ArrayList<String>();
		List<String> noPart = new ArrayList<String>();
		//全注文商品が在庫切れ
		if (flag.stream().allMatch(res -> res == 1)) {
			//注文を中止する
			
			//セッションスコープの注文入力フォーム情報と買い物かご情報を削除
			session.removeAttribute("order");
			//session.removeAttribute("baskets");
			msg = "ご注文を、中止します。";
			for (int i = 0; i < basketBean.size(); i++) {
				if (flag.get(i) == 0) {
					message.add(basketBean.get(i).getName() +"は、在庫切れのため注文できません。");
					noAll.add(basketBean.get(i).getName());
				}
			}
			model.addAttribute("msg", msg);
			model.addAttribute("messages", message);
			
			ra.addAttribute("msg", msg);
			ra.addAttribute("messages", message);
			ra.addAttribute("noAll", noAll);
			//注文確認画面表示処理へリダイレクト
			return "redirect:/client/order/check";
			
		} else if (flag.stream().allMatch(res -> res == 0)) {
			//全て在庫あり
			List<String> messageOrderNum = new ArrayList<String>();
			for (int i = 0; i < basketBean.size(); i++) {
				//DBの在庫数 < 注文数
				if (itemRepository.findByIdOrderById(basketBean.get(i).getId()).getStock() < basketBean.get(i).getOrderNum()) {
					
					messageOrderNum.add(basketBean.get(i).getName() +"は、在庫不足のため、在庫数分のみ注文できます。");
					noPart.add(basketBean.get(i).getName());
				}
				if (i == (basketBean.size()-1) && messageOrderNum.size() != 0) {
					model.addAttribute("messagesOrderNum", messageOrderNum);
					ra.addAttribute("messagesOrderNum", messageOrderNum);
					ra.addAttribute("noPart", noPart);
					//注文確認画面表示処理へリダイレクト
					return "redirect:/client/order/check";
				}
			}
			
			//注文情報を元にDB登録用エンティティオブジェクトを生成
			Order order =  new Order();
			
			Item item = new Item();
			
			//注文テーブルおよび注文商品テーブルのDB登録実施
			//日付をyyyyMMddの形で出力する
			Calendar cl = Calendar.getInstance();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        String str = sdf.format(cl.getTime());
	        Date sqlDate= Date.valueOf(str);
	        order.setInsertDate(sqlDate);
	        
	        //セッションスコープからログイン会員情報を取得
	        Integer id = ((UserBean) session.getAttribute("user")).getId();
	        //取得したログイン会員情報のユーザIDを条件にDBからユーザ情報を取得
	        User user = userRepository.findByIdAndDeleteFlag(id, Constant.NOT_DELETED);
	        
	        order.setUser(user);
	        order.setOrderItemsList((List<OrderItem>)session.getAttribute("baskets"));
			
			BeanUtils.copyProperties(form, order, "id");
			//orderテーブルに登録
			order = orderRepository.save(order);
			
			Integer orderNum = 0;
			for (int i = 0; i < basketBean.size(); i++) {
				OrderItem orderItem = new OrderItem(); 
				//注文個数
				orderNum += basketBean.get(i).getOrderNum();
				orderItem.setQuantity(orderNum);
				//注文情報
				orderItem.setOrder(order);
				//itemRepositoryでスコープ内のidを用いて検索
				orderItem.setItem(itemRepository.findByIdOrderById(basketBean.get(i).getId()));
				orderItem.setPrice(itemRepository.findByIdOrderById(basketBean.get(i).getId()).getPrice());
				BeanUtils.copyProperties(basketBean, orderItem, "id");
				//orderItemテーブルに登録
				orderItem = orderItemRepository.save(orderItem);
				orderNum = 0;
				
			}
			
			Integer basketOrder = 0;
			for (int i = 0; i < basketBean.size(); i++) {
				item = itemRepository.findByIdOrderById(basketBean.get(i).getId());
				basketOrder = itemRepository.findByIdOrderById(basketBean.get(i).getId()).getStock() - basketBean.get(i).getOrderNum();
				item.setStock(basketOrder);
				//itemテーブルに登録
				item = itemRepository.save(item);
			}
			
			//セッションスコープの注文入力フォーム情報と買い物かご情報を削除
			session.removeAttribute("order");
			session.removeAttribute("baskets");
			
			return "redirect:/client/order/complete";
			
		} else {
			//一部注文商品が在庫切れ
			for (int i = 0; i < basketBean.size(); i++) {
				if (flag.get(i) == 0) {
					message.add(basketBean.get(i).getName() +"は、在庫切れのため注文できません。");
					noPart.add(basketBean.get(i).getName());
				}
			}
			model.addAttribute("messages", message);
			ra.addAttribute("messages", message);
			ra.addAttribute("noPart", noPart);
			//注文確認画面表示処理へリダイレクト
			return "redirect:/client/order/check";
		}
	}
	
	//処理9
	@RequestMapping(path = "/client/order/complete", method = RequestMethod.GET)
	public String completeOK(OrderForm form, Model model) {
		
		return "client/order/complete";
	}
	
}
