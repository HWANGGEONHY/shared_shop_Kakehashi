package jp.co.sss.shop.controller.client.user;

import java.sql.Date;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;

@Controller
public class ClientUserUpdateIController {
	
	/**
	 * 会員管理 変更機能(運用管理者、システム管理者)のコントローラクラス
	 *
	 * @author SystemShared
	 * 
	 */
	 

		@Autowired
		UserRepository userRepository;

		@Autowired
		HttpSession session;

		/**
		 * 変更ボタン押下時処理1(POST)
		 * 
		 * @param id 変更対象ID
		 * @return "redirect:/client/user/update/input" 
		 */
		@RequestMapping(path = "/client/user/update/input/{id}", method = RequestMethod.POST)
		public String updateInputInit(@PathVariable Integer id) {

			//セッションスコープから入力フォーム情報を取り出す
			UserForm userForm = (UserForm) session.getAttribute("userForm");
			if (userForm == null) {

				// セッション情報がない場合、詳細画面からの遷移と判断し初期値を準備
				// 変更対象の情報取得
				Integer id1 = ((UserBean) session.getAttribute("user")).getId();
				User user = userRepository.getReferenceById(id1);

				if (user == null) {
					// 対象が無い場合、エラー
					return "redirect:/syserror" ;
				}

				// 初期画面表示用入力フォーム情報の生成
				userForm = new UserForm();
				BeanUtils.copyProperties(user, userForm);

				//変更入力フォーム情報をセッションに保持
				session.setAttribute("userForm", userForm);

			}

			//変更入力画面表示処理
			return "redirect:/client/user/update/input";

		}

		/**
		 * 変更入力画面表示処理2(GET)
		 *
		 * @param model Viewとの値受渡し
		 * @return "client/user/update_input" 変更入力画面 表示
		 */
		@RequestMapping(path = "/client/user/update/input", method = RequestMethod.GET)
		public String updateInput(Model model) {

			//セッションからスコープから入力フォーム取得
			UserForm userForm = (UserForm) session.getAttribute("userForm");
			if (userForm == null) {
				// セッション情報がない場合、エラー
				return "redirect:/syserror";
			}
			// 入力フォーム情報をスコープに設定
			model.addAttribute("userForm", userForm);

			BindingResult result = (BindingResult) session.getAttribute("result");
			if (result != null) {
				//セッションにエラー情報がある場合、エラー情報をリクエストスコープに設定
				model.addAttribute("org.springframework.validation.BindingResult.userForm", result);
				//セッションスコープから、入力エラー情報を削除
				session.removeAttribute("result");
			}

			//変更入力画面表示
			return "client/user/update_input";

		}

		/**
		 * 変更確認ボタン押下時処理3(POST)
		 *
		 * @param form 入力フォーム
		 * @param result 入力チェック結果
		 * @return 
		 *   入力値エラーあり："redirect:/admin/user/update/input" 変更入力画面へ 
		 *   入力値エラーなし："redirect:/admin/user/update/check" 変更確認画面へ
		 */
		@RequestMapping(path = "/client/user/update/check", method = RequestMethod.POST)
		public String updateInputCheck(@Valid @ModelAttribute UserForm form, BindingResult result,Model model) {

			//セッションスコープから直前の情報を取得
			UserForm lastUserForm = (UserForm) session.getAttribute("userForm");
			
			if (lastUserForm == null) {
				// セッション情報が無い場合、エラー
				return "redirect:/syserror";
			}
			
			if(form.getAuthority()==null) {
				//権限情報がない場合、セッション情報から値をセット
				form.setAuthority(lastUserForm.getAuthority());
			}
			
			// 画面からの入力フォーム情報をセッションに保存
			session.setAttribute("userForm", form);

			// 入力値にエラーがあった場合、
			if (result.hasErrors()) {
		
				//入力エラー情報をセッションスコープに設定
				session.setAttribute("result", result);

				//変更入力画面にリダイレクト
				return "redirect:/client/user/update/input";

			}

			//変更確認画面表示処理
		
			
			
			
			return "redirect:/client/user/update/check";
		}

		/**
		 * 確認画面表示処理4(GET)
		 *
		 * @param model Viewとの値受渡し
		 * @return "client/user/update_check" 確認画面表示
		 */
		@RequestMapping(path = "/client/user/update/check", method = RequestMethod.GET)
		public String updateCheck(Model model) {
		
			//セッションスコープから入力フォーム情報取得
			UserForm userForm = (UserForm) session.getAttribute("userForm");
			
			if (userForm == null) {
				// セッション情報がない場合、エラー
				return "redirect:/syserror";
			}
			
			//入力フォーム情報をリクエストスコープへ設定
			model.addAttribute("userForm", userForm);

			// 変更確認画面表示
			int a = userForm.getPassword().length();
			String password ="";
			for(a=0; a<userForm.getPassword().length(); a++) {
				password=password+"*";
			}
			model.addAttribute("password", password);
			return "client/user/update_check";

		}


		/**
		 * 変更登録完了画面表示処理5(POST)
		 *
		 * @return "redirect:/client/user/update/complete" 
		 */
		@RequestMapping(path = "/client/user/update/complete", method = RequestMethod.POST)
		public String updateComplete(Model model) {

			//セッションスコープから入力フォーム情報再取得
			UserForm userForm = (UserForm) session.getAttribute("userForm");
			
			if (userForm == null) {
				// セッション情報がない場合、エラー
				return "redirect:/syserror";
			}

			// 変更対象情報を取得
			Integer id1 = ((UserBean) session.getAttribute("user")).getId();
			User user = userRepository.getReferenceById(id1);
			
			if (user == null) {
				// 対象が無い場合、エラー
				return "redirect:/syserror" ;
			}

			// 日付と削除フラグを用意
			Date insertDate = user.getInsertDate();
			Integer deleteFlag = user.getDeleteFlag();

			// 入力フォーム情報を変更用エンティティに上書き(更新)
			BeanUtils.copyProperties(userForm, user);

			// 入力値以外の項目を設定
			user.setDeleteFlag(deleteFlag);
			user.setInsertDate(insertDate);

			// 情報を保存
			userRepository.save(user);

			// ログインユーザ情報変更の場合、セッション保存ユーザ情報を更新
			UserBean loginUser = (UserBean)session.getAttribute("user") ; 
			if (loginUser.getId() == userForm.getId()) {
				loginUser.setName(userForm.getName()) ;
			}
			session.setAttribute("user", loginUser) ;

			
			//セッション情報の削除
			session.removeAttribute("userForm");
			model.addAttribute("id", userForm.getId());
			// 変更完了画面表示処理にリダイレクト
			return "redirect:/client/user/update/complete";
		}

		/**
		 * 変更完了画面表示(GET)
		 * 
		 * @return "client/user/update_complete"
		 */
		@RequestMapping(path = "/client/user/update/complete", method = RequestMethod.GET)
		public String updateCompleteFinish() {
			
			return "client/user/update_complete";
		}

	


}
