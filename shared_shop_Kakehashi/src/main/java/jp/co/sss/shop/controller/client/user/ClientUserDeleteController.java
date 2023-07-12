package jp.co.sss.shop.controller.client.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.FavoriteRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.repository.UserRepository;

@Controller
public class ClientUserDeleteController {

	/**
	 * 会員情報 リポジトリ
	 */
	@Autowired
	UserRepository userRepository;
	
	/**
	 * お気に入り　リポジトリ　田中
	 */
	@Autowired
	FavoriteRepository favoriteRetpository;

	@Autowired
	OrderItemRepository orderitemRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	/**
	 * セッション
	 */
	@Autowired
	HttpSession session;

	/**
	 * 削除ボタン押下時処理(処理1)
	 *
	 * @param id 
	 * @return "redirect:/client/user/delete/check/{id}" 削除確認画面 表示
	 */
	@RequestMapping(path = "/client/user/delete/input/{id}", method = RequestMethod.POST)
	public String deleteCheck(@PathVariable Integer id) {

		// 削除対象の会員情報をDBから取得し、id1に入れる
		Integer id1 = ((UserBean) session.getAttribute("user")).getId();
		User user = userRepository.getReferenceById(id1);
		
		if (user == null) {
				// 対象が無い場合、登録画面へ
				return "redirect:/client/user/regist_input";
			}


		// 取得情報からに削除確認画面表示用の入力フォーム情報を新規生成
		UserForm userForm = new UserForm();
		BeanUtils.copyProperties(user, userForm);

		//入力フォーム情報をセッションスコープに保存
		session.setAttribute("userForm", userForm);

		// 削除確認画面表示処理へリダイレクト
		return "redirect:/client/user/delete/check";
	}

	/**
	 * 削除確認画面表示処理(処理2)
	 *
	 * @param model Viewとの値受渡し
	 * @return "client/user/delete_check" 確認画面 表示
	 */
	@RequestMapping(path = "/client/user/delete/check", method = RequestMethod.GET)
	public String updateInput(Model model) {

		//セッションスコープから入力フォーム情報取得
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		if (userForm == null) {
			// セッション情報がない場合、エラー
			return "redirect:/syserror";
		}
		
		// 入力フォーム情報をリクエストスコープに設定
		model.addAttribute("userForm", userForm);

		// 削除確認画面表示
		return "client/user/delete_check";
	}

	/**
	 * 削除ボタン押下時処理(処理3)
	 *
	 * @return "redirect:/client/user/delete/complete" 会員情報 削除完了画面へ
	 */
	@RequestMapping(path = "/client/user/delete/complete", method = RequestMethod.POST)
	public String deleteComplete() {

		// セッションから削除対象フォーム情報を取得
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		
		
		if (userForm == null) {
			// セッション情報がない場合、エラー
			return "redirect:/syserror";
		}
		
		// 入力フォーム情報を元にDB登録用エンティティオブジェクトを生成
		User user = new User();
		
		// 削除対象の会員情報を取得
		Integer id1 = ((UserBean) session.getAttribute("user")).getId();
		user = userRepository.getReferenceById(id1);
		
		//6月26日お気に入り削除
		favoriteRetpository.deleteByUser(user);

		// 削除処理
		userRepository.deleteById(id1);
		
//		// 会員情報を保存
//		userRepository.save(user);

		// セッションの削除対象情報を削除
		session.invalidate();

		// 削除完了画面表示処理
		return "redirect:/client/user/delete/complete";
	}

	/**
	 * 会員情報削除完了処理(処理4)
	 *
	 * @return "client/user/delete_complete" 会員情報 削除完了画面へ
	 */
	@RequestMapping(path = "/client/user/delete/complete", method = RequestMethod.GET)
	public String deleteCompleteFinish() {

		return "client/user/delete_complete";
	}

}
