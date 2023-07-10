package jp.co.sss.shop.form;

import java.sql.Date;

import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.User;

/**
 * お気に入り情報のフォーム
 * @author 田中一馬
 *
 */
public class FavoriteForm {
	/**
	 * お気に入り商品ID
	 */
	private Integer f_no;
	
	/**
	 * アイテムID
	 */
	private Item item;
	/**
	 * 削除フラグ
	 */
	private Integer delete_flag;
	/**
	 * ユーザーID
	 */
	private User user;
	/**
	 * 登録日時
	 */
	private Date f_date;
	/**
	 * @return f_no
	 */
	public Integer getF_no() {
		return f_no;
	}
	/**
	 * @param f_no セットする f_no
	 */
	public void setF_no(Integer f_no) {
		this.f_no = f_no;
	}
	/**
	 * @return item
	 */
	public Item getItem() {
		return item;
	}
	/**
	 * @param item セットする item
	 */
	public void setItem(Item item) {
		this.item = item;
	}
	
	/**
	 * @return delete_flag
	 */
	public Integer getDelete_flag() {
		return delete_flag;
	}
	/**
	 * @param delete_flag セットする delete_flag
	 */
	public void setDelete_flag(Integer delete_flag) {
		this.delete_flag = delete_flag;
	}
	/**
	 * @return user
	 */
	public User getUser() {
		return user;
	}
	/**
	 * @param user セットする user
	 */
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * @return f_date
	 */
	public Date getF_date() {
		return f_date;
	}
	/**
	 * @param f_date セットする f_date
	 */
	public void setF_date(Date f_date) {
		this.f_date = f_date;
	}
	
}
