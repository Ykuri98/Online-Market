package service;

import model.bo.admin.AdminInfoBO;
import model.bo.admin.AdminLoginBO;
import model.bo.admin.AdminPwdBO;
import model.vo.admin.AdminInfoVO;

import java.util.List;

/**
 * @author yzw
 */
public interface AdminService {
    /**
     * 登录方法
     * @param adminLoginBO 前端json包装类
     * @return nickname admin昵称
     */
    String login(AdminLoginBO adminLoginBO);

    /**
     * 获得所有admin方法
     * @return 数据库admin数据包装类的List
     */
    List<AdminInfoVO> allAdmins();

    /**
     * 根据传入的adminInfoBO类添加admin
     * @param adminInfoBO 前端json包装类
     * @return 1表示添加成功，0表示添加失败
     */
    Integer addAdmin(AdminInfoBO adminInfoBO);

    /**
     * 根据传入的id删除admin
     * @param id admin的id
     * @return 1表示添加成功，0表示添加失败
     */
    Integer deleteAdmin(String id);

    /**
     * 根据传入的adminInfoBO类获取符合条件的Admin集合
     * @param adminInfoBO 前端json包装类
     * @return 数据库数据包装类AdminInfoVO的集合
     */
    List<AdminInfoVO> getAdminInfo(AdminInfoBO adminInfoBO);

    /**
     * 根据传入的adminInfoBO类更新符合条件的Admin
     * @param adminInfoBO 前端json包装类
     * @return 1表示更新成功，0表示更新失败
     */
    Integer updateAdmin(AdminInfoBO adminInfoBO);

    /**
     * 根据传入的adminPwdBO类更新符合条件的Admin的密码
     * @param adminPwdBO 前端json包装类
     * @return 1表示更新成功，0表示更新失败
     */
    Integer updateAdminPwd(AdminPwdBO adminPwdBO);
}
