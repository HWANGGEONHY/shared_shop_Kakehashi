package jp.co.sss.shop.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * お気に入りテーブルのエンティティクラス
 *
 * @author 田中一馬
 */
@Entity
@Table(name = "favorite_items")
public class Favorite {
	/**
	 * お気に入り商品ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_fav_gen")
	@SequenceGenerator(name = "seq_fav_gen",sequenceName = "seq_fav",allocationSize = 1)
	private Integer fNo;
	
	/**
	 * アイテムID
	 */
	@ManyToOne
	@JoinColumn(name = "item_id",referencedColumnName = "id",nullable = false)
	private Item item;
	/**
	 * 削除フラグ
	 */
	@Column(insertable = false,nullable = false)
	private Integer deleteFlag;
	/**
	 * ユーザーID
	 */
	@ManyToOne
	@JoinColumn(name = "user_id",referencedColumnName = "id",nullable = false)
	private User user;
	/**
	 * 登録日時
	 */
	@Column(insertable = false,nullable = false)
	private Date fDate;
	
	/**
	 * コンストラクタ(引数なし)
	 */
	public Favorite() {
	}
	
	/**
	 * コンストラクタ
	 * @param f_no お気に入り商品ID
	 * @param item アイテムID
	 * @param user ユーザーID
	 */
	public Favorite(Integer fNo,Item item, User user) {
		this.fNo = fNo;
		this.item = item;
		this.user = user;
	}
	
	/**
	 * @return f_no
	 */
	public Integer getF_no() {
		return fNo;
	}
	/**
	 * @param f_no セットする f_no
	 */
	public void setF_no(Integer f_no) {
		this.fNo = f_no;
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
		return deleteFlag;
	}
	/**
	 * @param delete_flag セットする delete_flag
	 */
	public void setDelete_flag(Integer delete_flag) {
		this.deleteFlag = delete_flag;
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
		return fDate;
	}
	/**
	 * @param f_date セットする f_date
	 */
	public void setF_date(Date f_date) {
		this.fDate = f_date;
	}

}
