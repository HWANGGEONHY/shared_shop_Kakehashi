package jp.co.sss.shop.controller.client.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.repository.UserRepository;

@Controller
public class ClientUserShowController {

	// 06/13 村山

		@Autowired
		UserRepository userRepository;

		@Autowired
		HttpSession session;
		
		
		
		
		// 会員詳細表示用メソッド
		// 会員情報がない場合は会員
		@RequestMapping(path = "/client/user/detail", method = {RequestMethod.GET , RequestMethod.POST})
		public String showUser(Integer id, Model model) {

			if(((UserBean) session.getAttribute("user")).getId()==null) {
				
				System.out.println("null!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
			}
			Integer id1 = ((UserBean) session.getAttribute("user")).getId();
			User user = userRepository.getReferenceById(id1);
			
			if (user == null) {
				// 対象が無い場合、登録画面へ
				return "redirect:/client/user/regist_input";
			}

			

			// Userエンティティの各フィールドの値をUserBeanにコピー
			UserBean userBean = new UserBean();
			BeanUtils.copyProperties(user, userBean);
			

			// 会員情報をViewに渡す
			model.addAttribute("userBean", userBean);

			//会員登録・変更・削除用のセッションスコープを初期化
			session.removeAttribute("userForm");

			// 詳細画面表示
			return "client/user/detail";
		}
	
	
		@RequestMapping(path="/client/user/check", method=RequestMethod.GET)
		public String deleteUser(){
			return "client/user/detail";
		}
		
		
	
}
