package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.util.JPQLConstant;

/**
 * itemsテーブル用リポジトリ
 *
 * @author System Shared
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	/**
	 * 商品情報を登録日付順に取得 管理者機能で利用
	 * @param deleteFlag 削除フラグ
	 * @param pageable ページング情報
	 * @return 商品エンティティのページオブジェクト
	 */
	@Query(JPQLConstant.FIND_ALL_ITEMS_ORDER_BY_INSERT_DATE)
	Page<Item> findByDeleteFlagOrderByInsertDateDescPage(
	        @Param(value = "deleteFlag") int deleteFlag, Pageable pageable);

	/**
	 * 商品IDと削除フラグを条件に検索（管理者機能で利用）
	 * @param id 商品ID
	 * @param deleteFlag 削除フラグ
	 * @return 商品エンティティ
	 */
	public Item findByIdAndDeleteFlag(Integer id, int deleteFlag);
	//public Item findByIdAndDeleteFlag(Integer id, int deleteFla,Pageable pageable);

	/**
	 * 商品名と削除フラグを条件に検索 (ItemValidatorで利用)
	 * @param name 商品名
	 * @param notDeleted 削除フラグ
	 * @return 商品エンティティ
	 */
	public Item findByNameAndDeleteFlag(String name, int notDeleted);
	//public Item findByNameAndDeleteFlag(String name, int notDeleted,Pageable pageable);
	
	/**辛による編集 2023/06/09
	 */
	public List<Item> findByNameContainingOrderByInsertDateDesc(String name);
	public Page<Item> findByNameContainingOrderByInsertDateDesc(String name,Pageable pageable);
	
	public List<Item>findByOrderByInsertDateDescIdAsc();
	public Page<Item>findByOrderByInsertDateDescIdAsc(Pageable pageable);
	
	
	//辛による編集2023/06/09
	public List<Item> findByCategoryIdOrderByInsertDateDesc(Integer categoryId);
	public Page<Item> findByCategoryIdOrderByInsertDateDesc(Integer categoryId,Pageable pageable);
	
	public List<Item> findByCategoryIdOrderByInsertDateDescIdAsc(Integer categoryId);
	public Page<Item> findByCategoryIdOrderByInsertDateDescIdAsc(Integer categoryId,Pageable pageable);

	
	//秋山による編集2023/06/13
	public Item findByIdOrderById(Integer id);
	public Page<Item> findByIdOrderById(Integer id,Pageable pageable);
	
	//辛による編集2023/06/16
	public List<Item> findByNameContainingAndCategoryIdOrderByInsertDateDescIdAsc(String name,Integer categoryId);
	public Page<Item> findByNameContainingAndCategoryIdOrderByInsertDateDescIdAsc(String name,Integer categoryId,Pageable pageable);

	
	public List<Item> findByNameContainingOrderByInsertDateDescIdAsc(String name);
	public Page<Item> findByNameContainingOrderByInsertDateDescIdAsc(String name,Pageable pageable);
	
	@Query(value="select a.*"
			+ "from items a left join  order_items b\r\n"
			+ "on a.id = b.item_id\r\n"
			+ "GROUP BY a.id,a.name,a.price,a.description,a.stock,a.image,a.category_id,a.delete_flag,a.insert_date\r\n"
			+ "ORDER BY count(b.item_id) desc, a.id asc",nativeQuery = true) 
	public List<Item> getListByCount();
	
	
	@Query(value="select a.*"
			+ "from items a left join  order_items b\r\n"
			+ "on a.id = b.item_id\r\n"
			+ "GROUP BY a.id,a.name,a.price,a.description,a.stock,a.image,a.category_id,a.delete_flag,a.insert_date\r\n"
			+ "ORDER BY count(b.item_id) desc, a.id asc",
			countQuery ="select count(*) \r\n"
					+ "from items a left join  order_items b\r\n"
					+ "on a.id = b.item_id\r\n"
					+ "GROUP BY a.id,a.name,a.price,a.description,a.stock,a.image,a.category_id,a.delete_flag,a.insert_date\r\n"
					+ "ORDER BY count(b.item_id) desc, a.id asc"
			,nativeQuery = true) 
	public Page<Item> getListByCount(Pageable pageable);
	
	
	
	
	@Query(value="select a.*\r\n"
			+ "from items a left join  order_items b\r\n"
			+ "on a.id = b.item_id\r\n"
			+ "where a.category_id = :categoryId\r\n"
			+ "GROUP BY a.id,a.name,a.price,a.description,a.stock,a.image,a.category_id,a.delete_flag,a.insert_date\r\n"
			+ "ORDER BY count(b.item_id) desc, a.id asc",nativeQuery = true) 
	public List<Item> getListByCountByCategoryId(@Param(value = "categoryId") Integer categoryId);
	@Query(value="select a.*\r\n"
			+ "from items a left join  order_items b\r\n"
			+ "on a.id = b.item_id\r\n"
			+ "where a.category_id = :categoryId\r\n"
			+ "GROUP BY a.id,a.name,a.price,a.description,a.stock,a.image,a.category_id,a.delete_flag,a.insert_date\r\n"
			+ "ORDER BY count(b.item_id) desc, a.id asc",
			countQuery ="select count(*)\r\n"
					+ "from items a left join  order_items b\r\n"
					+ "on a.id = b.item_id\r\n"
					+ "where a.category_id = :categoryId\r\n"
					+ "GROUP BY a.id,a.name,a.price,a.description,a.stock,a.image,a.category_id,a.delete_flag,a.insert_date\r\n"
					+ "ORDER BY count(b.item_id) desc, a.id asc"
			,nativeQuery = true) 
	public Page<Item> getListByCountByCategoryId(@Param(value = "categoryId") Integer categoryId,Pageable pageable);
	
	
	
	@Query(value="select a.*\r\n"
			+ "from items a left join  order_items b\r\n"
			+ "on a.id = b.item_id\r\n"
			+ "where a.name like %:itemName%\r\n"
			+ "GROUP BY a.id,a.name,a.price,a.description,a.stock,a.image,a.category_id,a.delete_flag,a.insert_date\r\n"
			+ "ORDER BY count(b.item_id) desc, a.id asc",nativeQuery = true) 
	public List<Item> getListByCountByNameContaining(@Param(value = "itemName") String itemName);
	@Query(value="select a.*\r\n"
			+ "from items a left join  order_items b\r\n"
			+ "on a.id = b.item_id\r\n"
			+ "where a.name like %:itemName%\r\n"
			+ "GROUP BY a.id,a.name,a.price,a.description,a.stock,a.image,a.category_id,a.delete_flag,a.insert_date\r\n"
			+ "ORDER BY count(b.item_id) desc, a.id asc",
			countQuery ="select count(*)\r\n"
					+ "from items a left join  order_items b\r\n"
					+ "on a.id = b.item_id\r\n"
					+ "where a.name like %:itemName%"
					+ " GROUP BY a.id,a.name,a.price,a.description,a.stock,a.image,a.category_id,a.delete_flag,a.insert_date\r\n"
					+ "ORDER BY count(b.item_id) desc, a.id asc"
			,nativeQuery = true)
	public Page<Item> getListByCountByNameContaining(@Param(value = "itemName") String itemName, Pageable pageable);
	
	
	
	
	
	@Query(value="select a.*\r\n"
			+ "from items a left join  order_items b\r\n"
			+ "on a.id = b.item_id\r\n"
			+ "where a.name like %:itemName%\r\n"
			+ "and a.category_id = :categoryId\r\n"
			+ "GROUP BY a.id,a.name,a.price,a.description,a.stock,a.image,a.category_id,a.delete_flag,a.insert_date\r\n"
			+ "ORDER BY count(b.item_id) desc, a.id asc",nativeQuery = true) 
	public List<Item> getListByCountByNameContainingAndInsertDate(@Param(value = "itemName") String itemName,@Param(value = "categoryId")Integer categoryId);
	@Query(value="select a.*\r\n"
			+ "from items a left join  order_items b\r\n"
			+ "on a.id = b.item_id\r\n"
			+ "where a.name like %:itemName%\r\n"
			+ "and a.category_id = :categoryId\r\n"
			+ "GROUP BY a.id,a.name,a.price,a.description,a.stock,a.image,a.category_id,a.delete_flag,a.insert_date\r\n"
			+ "ORDER BY count(b.item_id) desc, a.id asc",
			countQuery ="select a.*\r\n"
					+ "from items a left join  order_items b\r\n"
					+ "on a.id = b.item_id\r\n"
					+ "where a.name like %:itemName%\r\n"
					+ "and a.category_id = :categoryId\r\n"
					+ "GROUP BY a.id,a.name,a.price,a.description,a.stock,a.image,a.category_id,a.delete_flag,a.insert_date\r\n"
					+ "ORDER BY count(b.item_id) desc, a.id asc"
			,nativeQuery = true)
	public Page<Item> getListByCountByNameContainingAndInsertDate(@Param(value = "itemName") String itemName,@Param(value = "categoryId")Integer categoryId  ,Pageable pageable);
	
	
	
	
	
	
	
	
	
	@Query(value="select * from(\r\n"
			+ " select tbl.*,rownum as rnum \r\n"
			+ " from (\r\n"
			+ " SELECT *\r\n"
			+ " from items\r\n"
			+ " where id  like '%%'\r\n"
			+ " ) tbl)\r\n"
			+ " where rnum >=(:num*10)-9 and rnum <=(:num*10)",nativeQuery = true) 
	public List<Item> getListByPaging( @Param(value = "num") int num);

	
	
	List<Item> findByNameContainingAndCategoryIdOrderById(String itemName, Integer categoryId);
	Page<Item> findByNameContainingAndCategoryIdOrderById(String itemName, Integer categoryId, Pageable pageable);
	
	
	
}
