package ec.com.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ec.com.model.entity.Admin;

/**
 * 管理者(Admin)エンティティに対するデータアクセスを提供するDAOインターフェース
 * JpaRepositoryを継承し、標準的なCRUD操作に加えて独自クエリを定義できます。
 */
@Repository
public interface AdminDao extends JpaRepository<Admin, Long> {

    /**
     * 新規または更新したAdminエンティティを保存します。
     * @param admin 保存するAdminエンティティ
     * @return 保存されたAdminエンティティ
     */
    @Override
    Admin save(Admin admin);

    /**
     * 管理者IDでAdminを検索します。
     * @param adminId 検索対象の管理者ID
     * @return 該当するAdminエンティティ、存在しない場合null
     */
    Admin findByAdminId(Long adminId);

    /**
     * メールアドレスでAdminを検索します。
     * @param adminEmail 検索対象のメールアドレス
     * @return 該当するAdminエンティティ、存在しない場合null
     */
    Admin findByAdminEmail(String adminEmail);

    /**
     * メールアドレスとパスワードでAdminを検索し、認証チェックに使用します。
     * @param adminEmail    入力されたメールアドレス
     * @param adminPassword 入力されたパスワード
     * @return 認証成功時はAdminエンティティ、失敗時はnull
     */
    Admin findByAdminEmailAndAdminPassword(String adminEmail, String adminPassword);

}
