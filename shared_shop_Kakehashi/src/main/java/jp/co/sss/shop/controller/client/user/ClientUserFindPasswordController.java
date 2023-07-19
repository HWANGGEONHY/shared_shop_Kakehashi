package jp.co.sss.shop.controller.client.user;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;

@Controller
public class ClientUserFindPasswordController {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	HttpSession session;
	
	@RequestMapping(path = "/client/user/findPassword/input", method = RequestMethod.GET)
	public String userFindPasswordInput() {

		return "/client/user/find_password_input";
	}
	
	@RequestMapping(path = "/client/user/findPassword/check", method = RequestMethod.POST)
	public String userFindPasswordCheck(String email, String phoneNumber, Model model, RedirectAttributes redirectAttributes) {
		User user = new User();
		user = userRepository.findByEmailAndPhoneNumber(email, phoneNumber);
		
		String message = "";
		if(user == null) {
			message = "一致する情報がありません。";
			redirectAttributes.addFlashAttribute("message", message);
			
			return "redirect:/client/user/findPassword/input";
		}
		
		model.addAttribute("user", user);
		
		return "/client/user/find_password_check";
	}
	
	@RequestMapping(path = "/client/user/findPassword/check2", method = RequestMethod.POST)
	public String userFindPasswordCheck2(String email, String phoneNumber, String password, String password2, Model model) {
		User user = new User();
		user = userRepository.findByEmailAndPhoneNumber(email, phoneNumber);
		
		// 入力値にエラーがあった場合、
		String message = "";
		if(!password.equals(password2)) {
			message = "パスワードが一致していません。";
			
			model.addAttribute("message", message);
			model.addAttribute("user", user);
				
			return "/client/user/find_password_check";
		}
		
		if(password.length() < 8 || password.length() > 16) {
			message = "パスワードは8文字以上16文字以内半角英数字です。";
			
			model.addAttribute("message", message);
			model.addAttribute("user", user);
			
			return "/client/user/find_password_check";
		}
		
		if(!password.matches("^[a-zA-Z0-9]+$")) {
			message = "パスワードは8文字以上16文字以内半角英数字です。";
			
			model.addAttribute("message", message);
			model.addAttribute("user", user);
			
			return "/client/user/find_password_check";
		}
		
		user.setPassword(password);
		userRepository.save(user);
		
		return "redirect:/client/user/findPassword/complete";
	}
	
	@RequestMapping(path = "/client/user/findPassword/complete", method = RequestMethod.GET)
	public String userFindPasswordComplete() {
		
		return "/client/user/find_password_complete";
	}
}
