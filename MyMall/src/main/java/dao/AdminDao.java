package dao;

import model.po.Admin;
import model.vo.admin.AdminInfoVO;

import java.util.List;

/**
 * @author yzw
 */
public interface AdminDao {
    /**
     * 查询admin的昵称
     * @param admin
     * @return nickname 昵称
     */
    String queryNickname(Admin admin);

    /**
     * 查询所有admin
     * @return 包含admin所有信息的list
     */
    List<AdminInfoVO> queryAllAdmin();

    /**
     * 根据admin信息添加admin
     * @param admin admin包装类
     * @return 影响行数
     */
    Integer addAdmin(Admin admin);

    Integer isAdminExist(Admin admin);

    Integer deleteAdmin(Integer id);

    List<AdminInfoVO> queryAdmin(Admin admin);

    Integer updateAdmin(Admin admin);

    Integer updateAdminPwd(Admin admin);

    Integer queryAdminByNickname(Admin oldAdmin);

}
