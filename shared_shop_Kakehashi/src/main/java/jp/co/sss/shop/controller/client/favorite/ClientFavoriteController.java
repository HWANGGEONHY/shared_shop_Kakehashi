package jp.co.sss.shop.controller.client.favorite;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Favorite;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.repository.FavoriteRepository;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.UserRepository;

/**
 * お気に入り管理　お気に入り登録、削除のコントローラクラス
 * @author 田中一馬
 *
 */
@Controller
public class ClientFavoriteController {
	/**
	 * お気に入り情報
	 */
	@Autowired
	FavoriteRepository favoriteRepository;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	HttpSession session;

	/**
	 * お気に入り追加処理
	 * @param itemId 商品ID
	 * @return 元の画面に戻す
	 */
	@RequestMapping(path = "/client/favorite/add", method = RequestMethod.POST)
	public String favoriteAdd(Integer itemId, HttpServletRequest request, Model model) {
		try {
			Item item = itemRepository.getReferenceById(itemId);
			UserBean userBean = (UserBean) session.getAttribute("user");
			//String URL = request.getRequestURI()
			Integer userId = userBean.getId();
			User user = new User();
			user = userRepository.getReferenceById(userId);
			Favorite add = favoriteRepository.findByItemAndUserAndDeleteFlag(item, user, 1);
			try {
				if (add != null) {
					final Date nowDate = new Date(System.currentTimeMillis());
					add.setDelete_flag(0);
					add.setF_date(nowDate);
					add = favoriteRepository.save(add);
					//return "redirect:"+returnUrl;
					model.addAttribute("isAdd", true);
					return "client/favorite/favorite_complete";
				} else {
					Favorite newFavorite = new Favorite();
					newFavorite.setItem(item);
					newFavorite.setUser(user);
					favoriteRepository.save(newFavorite);
					//return "redirect:"+returnUrl;
					model.addAttribute("isAdd", true);
					return "client/favorite/favorite_complete";
				}
			} catch (Exception e) {
				System.out.println("エラー");
				//return "redirect:"+returnUrl;
				return "client/favorite/favorite_failure";
			}
		} catch (Exception e) {
			return "client/favorite/favorite_failure";

		}
	}

	/**
	 * お気に入り削除機能
	 * @param itemId 商品ID
	 * @return 元の画面に戻す
	 */
	@RequestMapping(path = "/client/favorite/delete", method = RequestMethod.POST)
	public String favoriteDelete(Integer itemId, HttpServletRequest request, Model model) {
		Item item = itemRepository.getReferenceById(itemId);
		UserBean userBean = (UserBean) session.getAttribute("user");
		//String URL = request.getRequestURI()
		Integer userId = userBean.getId();
		User user = new User();
		user = userRepository.getReferenceById(userId);
		Favorite delete = favoriteRepository.findByItemAndUserAndDeleteFlag(item, user, 0);
		try {
			if (delete != null) {
				delete.setDelete_flag(1);
				delete = favoriteRepository.save(delete);
			}
		} catch (Exception e) {
			System.out.println("エラー");
			//return "redirect:"+returnUrl;
			return "client/favorite/favorite_failure";
		}
		//return "redirect:"+returnUrl;
		model.addAttribute("isAdd", false);
		return "client/favorite/favorite_complete";
	}

}
