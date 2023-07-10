package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.util.JPQLConstant;

/**
 * categoriesテーブル用リポジトリ
 *
 * @author System Shared
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	/**
	 * カテゴリ名と削除フラグを条件に検索
	 * @param name カテゴリ名
	 * @param deleteFlag 削除フラグ
	 * @return カテゴリエンティティ
	 */
	Category findByNameAndDeleteFlag(String name, int deleteFlag);
	
	/**
	 * カテゴリIDと削除フラグを条件に検索
	 * @param id カテゴリID
	 * @param deleteFlag 削除フラグ
	 * @return カテゴリエンティティ
	 */
	Category findByIdAndDeleteFlag(Integer id, int deleteFlag);

	/**
	 * カテゴリ情報を登録日付降順に取得
	 * @param deleteFlag 削除フラグ
	 * @return カテゴリエンティティのリスト
	 */
	List<Category> findByDeleteFlagOrderByInsertDateDescIdDesc(int deleteFlag);
	//List<Category> findByDeleteFlagOrderByInsertDateDescIdDesc(int deleteFlag,Pageable pageable);

	
	/**
	 * カテゴリ情報を登録日付順に取得(ページング)
	 * @param deleteFlag 削除フラグ
	 * @param pageable ページング情報
	 * @return カテゴリエンティティのページオブジェクト
	 */
	@Query(JPQLConstant.FIND_ALL_CATEGORIES_ORDER_BY_INSERT_DATE)
	Page<Category> findByDeleteFlagOrderByInsertDateDescIdDescPage(
			@Param(value = "deleteFlag") int deleteFlag, Pageable pageable);

}
