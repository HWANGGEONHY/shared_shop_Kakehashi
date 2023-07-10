package jp.co.sss.shop.controller.client.user;

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

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;

@Controller
public class ClientUserRegistController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	HttpSession session;

	/**
	 * 入力画面表示処理(GET) 会員登録ボタン押下後の処理
	 * 
	 * @return "redirect:/client/user/regist/input" 入力画面表示処理
	 */
	@RequestMapping(path = "/client/user/regist/input/init", method = RequestMethod.GET)
	public String userRegistInput1() {

		UserForm userForm = new UserForm();
		userForm.setAuthority(2);
		session.setAttribute("userForm", userForm);

		//登録入力画面表示処理へ遷移
		return "redirect:/client/user/regist/input";
	}

	/**
	 * 入力画面表示処理(GET)
	 * 
	 * @return "client/user/regist_input" 入力画面表示
	 */
	
	@RequestMapping(path = "/client/user/regist/input", method = RequestMethod.GET)
	public String userRegistInput3(Model model) {

		//セッションスコープから入力フォーム情報を取得
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		
		if (userForm == null) {
			// セッション情報がない場合、エラー
			return "redirect:/syserror";
		}

		BindingResult result = (BindingResult) session.getAttribute("result");
		if (result != null) {
			//セッションにエラー情報がある場合、エラー情報をスコープに設定
			model.addAttribute("org.springframework.validation.BindingResult.userForm", result);
			// セッションにエラー情報を削除
			session.removeAttribute("result");
		}

		//入力フォーム情報をリクエストスコープに設定
		model.addAttribute("userForm", userForm);

		// 入力画面の表示
		return "client/user/regist_input";

	}

	/**
	 * 入力確認処理
	 *
	 * @param form 入力フォーム
	 * @param result 入力値チェックの結果
	 * @return 
	 * 	入力値エラーあり："redirect:/client/user/regist/input" 入力録画面表示処理
	 * 	入力値エラーなし："redirect:/client/user/regist/check" 登録確認画面表示処理
	 */
	@RequestMapping(path = "/client/user/regist/check", method = RequestMethod.POST)
	public String userRegistInputCheck4(@Valid @ModelAttribute UserForm form, BindingResult result) {

		//直前のセッション情報を取得
		UserForm lastUserForm = (UserForm) session.getAttribute("userForm");
		if (lastUserForm == null) {
			// セッション情報が無い場合、エラー
			return "redirect:/syserror";
		}

		// 入力フォーム情報をセッションに保持
		session.setAttribute("userForm", form);

		if (result.hasErrors()) {
			// 入力値にエラーがあった場合、エラー情報をセッションに保持
			session.setAttribute("result", result);
			// 登録入力画面表示処理
			return "redirect:/client/user/regist/input";
		}

		// 登録確認画面表示処理 
		return "redirect:/client/user/regist/check";
	}

	/**
	 * 確認画面表示処理
	 *
	 * @param model Viewとの値受渡し
	 * @return "admin/user/regist_check" 確認画面　表示
	 */
	@RequestMapping(path = "/client/user/regist/check", method = RequestMethod.GET)
	public String userRegistCheck(Model model) {
		//セッションから入力フォーム情報取得
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		userForm.setAuthority(2);
		//		if (userForm == null) {
		//			// セッション情報がない場合、エラー
		//			return "redirect:/syserror";
		//		}
		//入力フォーム情報をスコープへ設定
		model.addAttribute("userForm", userForm);

		//登録確認画面表示処理
		return "client/user/regist_check";

	}
	
	/**
	 * 登録ボタン 押下時処理(POST)
	 * @param model
	 * @return
	 */

	@RequestMapping(path = "/client/user/regist/complete", method = RequestMethod.POST)
	public String userRegistComplete(Model model) {

		//セッションスコープからフォーム入力情報取得
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		if (userForm == null) {
			// セッション情報がない場合、エラー
			return "redirect:/syserror";
		}

		// DB登録用エンティティオブジェクトを生成
		User user = new User();

		// 入力フォーム情報をエンティティに設定
		BeanUtils.copyProperties(userForm, user,"id");

		// DB登録
		user = userRepository.save(user);
		UserBean userBean = new UserBean();
		BeanUtils.copyProperties(user, userBean);
		model.addAttribute("userBean", userBean);
		
		session.getAttribute("email");
		session.getAttribute("password");

		session.setAttribute("user", userBean);
		
		//セッションから入力情報削除
		session.removeAttribute("userForm");

		//登録完了画面表示処理
		return "redirect:/client/user/regist/complete";
	}

	/**
	 * 登録完了画面表示処理
	 *
	 * @return "client/user/regist_complete" 登録完了画面表示
	 */
	@RequestMapping(path = "/client/user/regist/complete", method = RequestMethod.GET)
	public String userRegistCompleteFinish() {

		return "client/user/regist_complete";
	}
}
