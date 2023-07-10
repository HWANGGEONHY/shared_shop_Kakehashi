package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.util.JPQLConstant;

/**
 * ordersテーブル用リポジトリ
 *
 * @author System Shared
 */
@Repository

public interface OrderRepository extends JpaRepository<Order, Integer> {

	/**
	 * 注文日付降順で注文情報すべてを検索(管理者機能で利用)
	 * @param pageable ページング情報
	 * @return 注文エンティティのページオブジェクト
	 */
	@Query(JPQLConstant.FIND_ALL_ORDERS_ORDER_BY_INSERT_DATE)
	Page<Order> findAllOrderByInsertdateDescIdDesc(Pageable pageable);
	//ユーザーで検索
	public List<Order> findByUser(User user);
	//ユーザーで検索
	public List<Order> findByUserOrderByInsertDateDesc(User user);

}
