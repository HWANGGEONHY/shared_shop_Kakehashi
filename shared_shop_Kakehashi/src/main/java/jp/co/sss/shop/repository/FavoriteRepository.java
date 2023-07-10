package jp.co.sss.shop.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Favorite;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.User;

/**
 * Favoriteテーブル用リポジトリ
 * @author 田中一馬
 *
 */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
	/**
	 * ユーザーIDを条件に検索
	 * @param user ユーザーID
	 * @return お気に入りエンティティ
	 */
	public List<Favorite> findByUser(User user);
	/**
	 * ユーザーIDと削除フラグを条件に検索
	 * @param user ユーザーID
	 * @param flag
	 * @return お気に入りエンティティ
	 */
	public List<Favorite> findByUserAndDeleteFlag(User user,Integer flag);
	/**
	 * アイテムIDとユーザーIDを条件に検索
	 * @param item
	 * @param user
	 * @return お気に入りエンティティ
	 */
	public Favorite findByItemAndUser(Item item,User user);
	
	/**
	 * アイテムIDとユーザーIDと削除フラグを条件に検索
	 * @param item
	 * @param user
	 * @param delete_flag
	 * @return お気に入りエンティティ
	 */
	public Favorite findByItemAndUserAndDeleteFlag(Item item,User user,Integer flag);
	
	/**
	 * ユーザーを条件に削除
	 * @param user
	 */
	@Transactional
	public void deleteByUser(User user);
}
