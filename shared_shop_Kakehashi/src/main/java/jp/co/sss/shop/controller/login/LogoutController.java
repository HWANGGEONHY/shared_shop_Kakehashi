package jp.co.sss.shop.controller.login;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ログアウト機能のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class LogoutController {
	/**
	 * ログアウト処理
	 *
	 * @param session セッション情報
	 * @return "redirect:/" トップ画面へ
	 */
	@RequestMapping(path = "/logout")
	public String logout(HttpSession session) {
		// セッション情報を無効にする

		if(session.getAttribute("user") == null) {
		
			return "redirect:/login";
			
		}else {
	
		session.invalidate();
		return "redirect:/";
		}
	}
}