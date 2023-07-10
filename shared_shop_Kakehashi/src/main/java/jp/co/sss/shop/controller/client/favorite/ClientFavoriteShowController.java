package jp.co.sss.shop.controller.client.favorite;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Favorite;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.repository.FavoriteRepository;
import jp.co.sss.shop.repository.UserRepository;

/**
 * お気に入り一覧画面表示クラス
 * @author 田中一馬
 *
 */
@Controller
public class ClientFavoriteShowController {
	/**
	 * お気に入り表示
	 * @return 
	 */
	@Autowired
	FavoriteRepository favoriteRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	HttpSession session;
	@Autowired
	HttpServletRequest servletRequest;

	@RequestMapping(path = "/client/favorite/list", method = RequestMethod.GET)
	public String favoriteList(Model model) {
		//お気に入り検索に使うuser
		UserBean userBean = (UserBean) session.getAttribute("user");
		Integer userId = userBean.getId();
		User user = new User();
		user = userRepository.getReferenceById(userId);
		//
		List<Favorite> favList = favoriteRepository.findByUserAndDeleteFlag(user, 0);
		if (favList == null || (favList.isEmpty())) {
			model.addAttribute("favorite_isEmpty", true);
		} else {
			model.addAttribute("favorite", favList);
			model.addAttribute("favorite_isEmpty", false);
			//テスト用ケース
			/*
			List<Boolean> FavoriteList = new ArrayList<Boolean>();
			FavoriteList.add(false);
			FavoriteList.add(true);
			model.addAttribute("isFavorite", FavoriteList);
			//
			*/
			//お気に入り商品かどうかのフラグを与える(他のページへのベース)
			//FavoriteListを作り、表示する全てのデータを転送する
			List<Boolean> FavoriteList = new ArrayList<Boolean>();
			for (Favorite serchfav : favList) {//データが存在するなら
				if ((favoriteRepository.findByItemAndUserAndDeleteFlag(serchfav.getItem(), user, 0)) != null) {
					//Listにtrueを追加
					FavoriteList.add(true);
				} else {
					//そうでないならfalseを追加
					FavoriteList.add(false);
				}
			}
			//FavoriteListをリクエストスコープに保存
			model.addAttribute("isFavorite", FavoriteList);
			//
		}

		return "client/favorite/favorite_list";
	}

}
