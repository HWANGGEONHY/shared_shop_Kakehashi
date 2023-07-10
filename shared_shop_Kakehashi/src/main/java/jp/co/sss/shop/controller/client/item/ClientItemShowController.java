package jp.co.sss.shop.controller.client.item;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.repository.CategoryRepository;
import jp.co.sss.shop.repository.FavoriteRepository;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.service.BeanTools;

/**
 * 商品管理 一覧表示機能(一般会員用)のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class ClientItemShowController {
	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	HttpSession session;

	//6月19日追加田中-----------------------
	@Autowired
	FavoriteRepository favoriteRepository;

	@Autowired
	UserRepository userRepository;
	//---------------------------------------
	/**
	/**
	* Entity、Form、Bean間のデータコピーサービス
	*/
	@Autowired
	BeanTools beanTools;

	/**
	 * トップ画面 表示処理
	 *
	 * @param model    Viewとの値受渡し
	 * @return "index" トップ画面
	 */
	@RequestMapping(path = "/", method = { RequestMethod.GET, RequestMethod.POST })
	public String index(Model model) {
		model.addAttribute("categories", categoryRepository.findAll());
		model.addAttribute("items", itemRepository.getListByCount());
		model.addAttribute("select", 0);
		return "index";
	}

	//辛による編集06/09
		@RequestMapping(path = "/client/item/list", method = { RequestMethod.GET, RequestMethod.POST })
		public String itemList(String page,Integer categoryId,String itemName,String t_select, Model model, Pageable pageable,HttpServletRequest request, HttpServletResponse response) {
			//System.out.println("@@@@@t_select : "+t_select);
			//System.out.println("@@@@@itemName : "+itemName);
			//System.out.println("@@@@@categoryId : "+categoryId);
			//System.out.println("@@@@@page : "+page);
			//System.out.println(request.getParameter("t_itemName")+"@@@@@@@@@@@@@@"+request.getParameter("t_categoryId"));
			
	
			if(t_select==null && itemName==null && categoryId==null) {
				
				Page<Item> pageList=itemRepository.findByOrderByInsertDateDescIdAsc(pageable);
				List<Item> itemList=pageList.getContent();
				
				//田中お気に入り情報を入力する処理
				List<Boolean> isFavorite = setFavorirte(itemList, model);
				model.addAttribute("isFavorite", isFavorite);
				Boolean isExist = userIsExist();
				model.addAttribute("user_isExist", isExist);
				
				
				
				model.addAttribute("pages",pageList);
				model.addAttribute("items",itemList);
				model.addAttribute("categories", categoryRepository.findAll());
				model.addAttribute("All","all");
				model.addAttribute("total", itemRepository.findAll().size());
				model.addAttribute("t_select",t_select);
				
			}else if(t_select==null && categoryId==0 && itemName.equals("")) {
				model.addAttribute("page", page);
				return "redirect:/client/item/list";
				
			}else if(t_select=="" && categoryId==null && itemName.equals("")) {
				
				
				Page<Item> pageList=itemRepository.findByOrderByInsertDateDescIdAsc(pageable);
				List<Item> itemList=pageList.getContent();
				
				//田中お気に入り情報を入力する処理
				List<Boolean> isFavorite = setFavorirte(itemList, model);
				model.addAttribute("isFavorite", isFavorite);
				Boolean isExist = userIsExist();
				model.addAttribute("user_isExist", isExist);
				
				
				
				model.addAttribute("pages",pageList);
				model.addAttribute("items",itemList);
				model.addAttribute("categories", categoryRepository.findAll());
				model.addAttribute("All","all");
				model.addAttribute("total", itemRepository.findAll().size());
				model.addAttribute("t_select",t_select);
				
			}
			else if(t_select!=null && t_select.equals("新着") && categoryId==null) {

					
					Page<Item> pageList=itemRepository.findByNameContainingOrderByInsertDateDescIdAsc(itemName,pageable);
					List<Item> itemList=pageList.getContent();
					
					//田中お気に入り情報を入力する処理
					List<Boolean> isFavorite = setFavorirte(itemList, model);
					model.addAttribute("isFavorite", isFavorite);
					Boolean isExist = userIsExist();
					model.addAttribute("user_isExist", isExist);
					
					model.addAttribute("pages",pageList);
					model.addAttribute("items",itemList);
					model.addAttribute("categories", categoryRepository.findAll());
					model.addAttribute("select", categoryId);
					model.addAttribute("t_select",t_select);
					model.addAttribute("search", itemName);
					model.addAttribute("total", itemRepository.findByNameContainingOrderByInsertDateDescIdAsc(itemName).size());
				
			}
			
			else if(t_select!=null && t_select.equals("売れ筋") && categoryId==null) {
				
				Page<Item> pageList=itemRepository.getListByCount(pageable);
				List<Item> itemList=pageList.getContent();
				
				//田中お気に入り情報を入力する処理
				List<Boolean> isFavorite = setFavorirte(itemList, model);
				model.addAttribute("isFavorite", isFavorite);
				Boolean isExist = userIsExist();
				model.addAttribute("user_isExist", isExist);
			
				model.addAttribute("pages",pageList);
				model.addAttribute("items",itemList);
				model.addAttribute("categories", categoryRepository.findAll());
				model.addAttribute("select", categoryId);
				model.addAttribute("t_select",t_select);
				model.addAttribute("search", itemName);
				model.addAttribute("total", itemRepository.getListByCount().size());
				
			}
			else if (!itemName.equals("") && categoryId==0) {
				
				Page<Item> pageList=itemRepository.findByNameContainingOrderByInsertDateDesc(itemName, pageable);
				List<Item> itemList=pageList.getContent();
				
				//田中お気に入り情報を入力する処理
				List<Boolean> isFavorite = setFavorirte(itemList, model);
				model.addAttribute("isFavorite", isFavorite);
				Boolean isExist = userIsExist();
				model.addAttribute("user_isExist", isExist);
				
				model.addAttribute("search", itemName);
				model.addAttribute("pages",pageList);
				model.addAttribute("items",itemList);
				model.addAttribute("categories", categoryRepository.findAll());
				model.addAttribute("select", categoryId);
				model.addAttribute("t_select",t_select);
				model.addAttribute("total", itemRepository.findByNameContainingOrderByInsertDateDesc(itemName).size());
				if(t_select!=null) {
					
					if(t_select.equals("新着")) {
					
						pageList=itemRepository.findByNameContainingOrderByInsertDateDescIdAsc(itemName,pageable);
						itemList=pageList.getContent();
						
						//田中お気に入り情報を入力する処理
						isFavorite = setFavorirte(itemList, model);
						model.addAttribute("isFavorite", isFavorite);
						isExist = userIsExist();
						model.addAttribute("user_isExist", isExist);
					
						model.addAttribute("pages",pageList);
						model.addAttribute("items",itemList);
						model.addAttribute("categories", categoryRepository.findAll());
						model.addAttribute("select", categoryId);
						model.addAttribute("search", itemName);
						model.addAttribute("t_select",t_select);
						model.addAttribute("total", itemRepository.findByNameContainingOrderByInsertDateDescIdAsc(itemName).size());
						}
					else if(t_select.equals("売れ筋")) {
						
						
						pageList=itemRepository.getListByCountByNameContaining(itemName, pageable);
						itemList=pageList.getContent();
						
						//田中お気に入り情報を入力する処理
						isFavorite = setFavorirte(itemList, model);
						model.addAttribute("isFavorite", isFavorite);
						isExist = userIsExist();
						model.addAttribute("user_isExist", isExist);
						
						model.addAttribute("pages",pageList);
						model.addAttribute("items",itemList);
						model.addAttribute("select", categoryId);
						model.addAttribute("search", itemName);
						model.addAttribute("t_select",t_select);
						model.addAttribute("total", itemRepository.getListByCountByNameContaining(itemName).size());
						}

				}
				
				
			}else if (categoryId != null && itemName.equals("")) {
				
				Page<Item> pageList=itemRepository.findByCategoryIdOrderByInsertDateDescIdAsc(categoryId,pageable);
				List<Item> itemList=pageList.getContent();
				
				//田中お気に入り情報を入力する処理
				List<Boolean> isFavorite = setFavorirte(itemList, model);
				model.addAttribute("isFavorite", isFavorite);
				Boolean isExist = userIsExist();
				model.addAttribute("user_isExist", isExist);
			
				model.addAttribute("pages",pageList);
				model.addAttribute("items",itemList);
				model.addAttribute("categories", categoryRepository.findAll());
				model.addAttribute("select", categoryId);
				model.addAttribute("t_select",t_select);
				model.addAttribute("total", itemRepository.findByCategoryIdOrderByInsertDateDesc(categoryId).size());
				if(t_select!=null) {
					
					if(t_select.equals("新着")) {
						
						pageList=itemRepository.findByCategoryIdOrderByInsertDateDescIdAsc(categoryId, pageable);
						itemList=pageList.getContent();
						
						//田中お気に入り情報を入力する処理
						isFavorite = setFavorirte(itemList, model);
						model.addAttribute("isFavorite", isFavorite);
						isExist = userIsExist();
						model.addAttribute("user_isExist", isExist);
					
						model.addAttribute("pages",pageList);
						model.addAttribute("items",itemList);
						model.addAttribute("categories", categoryRepository.findAll());
						model.addAttribute("select", categoryId);
						model.addAttribute("search", itemName);
						model.addAttribute("t_select",t_select);
						model.addAttribute("total", itemRepository.findByCategoryIdOrderByInsertDateDescIdAsc(categoryId).size());
						}
					
					else if(t_select.equals("売れ筋")) {
					
						pageList=itemRepository.getListByCountByCategoryId(categoryId, pageable);
						itemList=pageList.getContent();
						
						//田中お気に入り情報を入力する処理
						isFavorite = setFavorirte(itemList, model);
						model.addAttribute("isFavorite", isFavorite);
						isExist = userIsExist();
						model.addAttribute("user_isExist", isExist);
					
						model.addAttribute("pages",pageList);
						model.addAttribute("items",itemList);
						model.addAttribute("categories", categoryRepository.findAll());
						model.addAttribute("select", categoryId);
						model.addAttribute("search", itemName);
						model.addAttribute("t_select",t_select);
						model.addAttribute("total", itemRepository.getListByCountByCategoryId(categoryId).size());
						}


					
				}
				
				
			}
			else if (categoryId != null && !itemName.equals("")) {
				
					
				Page<Item> pageList=itemRepository.findByNameContainingAndCategoryIdOrderByInsertDateDescIdAsc(itemName,categoryId,pageable);
				List<Item> itemList=pageList.getContent();
				
				//田中お気に入り情報を入力する処理
				List<Boolean> isFavorite = setFavorirte(itemList, model);
				model.addAttribute("isFavorite", isFavorite);
				Boolean isExist = userIsExist();
				model.addAttribute("user_isExist", isExist);
			
				model.addAttribute("pages",pageList);
				model.addAttribute("items",itemList);
				model.addAttribute("categories", categoryRepository.findAll());
				model.addAttribute("select", categoryId);
				model.addAttribute("search", itemName);
				model.addAttribute("t_select",t_select);
				model.addAttribute("total", itemRepository.findByNameContainingAndCategoryIdOrderById(itemName,categoryId).size());
				
				if(t_select!=null) {
					
					if(t_select.equals("新着")) {
					
						pageList=itemRepository.findByNameContainingAndCategoryIdOrderByInsertDateDescIdAsc(itemName,categoryId,pageable);
						itemList=pageList.getContent();
						
						//田中お気に入り情報を入力する処理
						isFavorite = setFavorirte(itemList, model);
						model.addAttribute("isFavorite", isFavorite);
						isExist = userIsExist();
						model.addAttribute("user_isExist", isExist);
					
						model.addAttribute("pages",pageList);
						model.addAttribute("items",itemList);
						model.addAttribute("categories", categoryRepository.findAll());
						model.addAttribute("select", categoryId);
						model.addAttribute("search", itemName);
						model.addAttribute("t_select",t_select);
						model.addAttribute("total", itemRepository.findByNameContainingAndCategoryIdOrderByInsertDateDescIdAsc(itemName,categoryId).size());
						}
					else if(t_select.equals("売れ筋")) {
					
						pageList=itemRepository.getListByCountByNameContainingAndInsertDate(itemName, categoryId, pageable);
						itemList=pageList.getContent();
						
						//田中お気に入り情報を入力する処理
						isFavorite = setFavorirte(itemList, model);
						model.addAttribute("isFavorite", isFavorite);
						isExist = userIsExist();
						model.addAttribute("user_isExist", isExist);
					
						model.addAttribute("pages",pageList);
						model.addAttribute("items",itemList);
						model.addAttribute("categories", categoryRepository.findAll());
						model.addAttribute("select", categoryId);
						model.addAttribute("search", itemName);
						model.addAttribute("t_select",t_select);
						model.addAttribute("total", itemRepository.getListByCountByNameContainingAndInsertDate(itemName,categoryId).size());
						}
					
					//売れ筋領域
					
				}

				
			}
			if(page==null) {
				page="0";
			}
			model.addAttribute("page", page);
		
			return "client/item/list";
		}

/*
	//辛による編集06/09
	@RequestMapping(path = "/client/item/list/searchByName", method = RequestMethod.POST)
	public String serachItem(String itemName, Model model, Pageable pageable) {

		if (itemName.equals("")) {
			Page<Item> pageList=itemRepository.findAll(pageable);
			List<Item> itemList=pageList.getContent();
			
			
			model.addAttribute("pages",pageList);
			model.addAttribute("items",itemList);
			model.addAttribute("categories", categoryRepository.findAll());

		} else {
			
			
			Page<Item> pageList=itemRepository.findByNameContainingOrderById(itemName, pageable);
			List<Item> itemList=pageList.getContent();
			model.addAttribute("search", itemName);
			model.addAttribute("pages",pageList);
			model.addAttribute("items",itemList);
			model.addAttribute("categories", categoryRepository.findAll());

		}
		
		
		return "client/item/list";
	}

	//辛による編集06/09
	@RequestMapping(path = "/client/item/list/searchByCategoryId/{categoryId}", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchCate(@PathVariable Integer categoryId, Model model,Pageable pageable) {

		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@"+categoryId);
		
		if (categoryId == 0) {
			
			Page<Item> pageList=itemRepository.findAll(pageable);
			List<Item> itemList=pageList.getContent();
		
			model.addAttribute("pages",pageList);
			model.addAttribute("items",itemList);
			model.addAttribute("categories", categoryRepository.findAll());
			model.addAttribute("select", categoryId);
			
		} else {

			Page<Item> pageList=itemRepository.findByCategoryIdOrderById(categoryId,pageable);
			List<Item> itemList=pageList.getContent();
		
			model.addAttribute("pages",pageList);
			model.addAttribute("items",itemList);
			model.addAttribute("categories", categoryRepository.findAll());
			model.addAttribute("select", categoryId);
		}
		

		return "client/item/list";
	}
*/


	//秋山による編集06/12、商品詳細
	@RequestMapping(path = "/client/item/detail/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	public String detail(@PathVariable Integer id,String page,Integer categoryId,String itemName,String t_select, Model model) {
		Integer stock = itemRepository.findByIdOrderById(id).getStock();
		String message = "";
		
		if (stock >= 6) {
			message = "在庫あり";
			model.addAttribute("messages", message);
		}

		else if (stock >= 1 && stock <= 5) {

			model.addAttribute("messages", stock);
		}

		else if (stock == 0) {
			message = "在庫なし";
			model.addAttribute("messages", message);
		}
			model.addAttribute("page", page);
			model.addAttribute("select", categoryId);
			model.addAttribute("search", itemName);
			model.addAttribute("t_select",t_select);
		model.addAttribute("items", itemRepository.findByIdOrderById(id));

		//お気に入り商品かどうかのフラグを与える(他のページへのベース)6月19日田中追加--------------------------------------------------
				//お気に入り検索に使うuser
				Boolean isFavorite = false;
				try {
				UserBean userBean = (UserBean) session.getAttribute("user");
				Integer userId = userBean.getId();
				User user = new User();
				user = userRepository.getReferenceById(userId);
				//
				//
					if ((favoriteRepository.findByItemAndUserAndDeleteFlag(itemRepository.getReferenceById(id),user,0)) != null) {
						//trueを追加
						isFavorite = true;
					} else {
						//そうでないならfalseを追加
						isFavorite = false;
					}
				}catch(Exception e) {
					isFavorite=false;
				}
				//FavoriteListをリクエストスコープに保存
				model.addAttribute("isFavorite", isFavorite);
				//----------------------------------------------------------------------------------------------------------------------------
		
		return "client/item/detail";
	}
	/**
	 * お気に入りを登録する機能　6月20日
	 */
	List<Boolean> setFavorirte(List<Item> itemList, Model model) {
		List<Boolean> isFavorite = new ArrayList<>();
		try{
			UserBean userBean = (UserBean) session.getAttribute("user");
			Integer userId = userBean.getId();
			User user = new User();
			user = userRepository.getReferenceById(userId);
			for (Item item : itemList) {
				if ((favoriteRepository.findByItemAndUserAndDeleteFlag(item, user, 0)) != null) {
					isFavorite.add(true);
				} else {
					isFavorite.add(false);
				}
			}
		}catch(Exception e) {
			return isFavorite;
		}
		//model.addAttribute("isFavorite", isFavorite);
		return isFavorite;
	}
	/**
	 * ユーザーが存在するかを判断する機能　6月21日
	 */
	Boolean userIsExist() {
		UserBean userBean = (UserBean) session.getAttribute("user");
		if(userBean == null) {
			return false;
		}
		else {
			return true;
		}
	}
}
