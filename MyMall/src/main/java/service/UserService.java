package service;

import model.bo.admin.AdminLoginBO;
import model.bo.user.UserSignupBO;
import model.bo.user.UserUpdateUserDataBO;
import model.vo.user.UserInfoVO;

import java.util.List;

public interface UserService {
    /**
     * 获得所有用户
     * @return 数据库包装类UserInfoVO的集合
     */
    List<UserInfoVO> allUser();

    /**
     * 根据id删除用户
     * @param id 用户id
     * @return 1表示删除成功，0表示删除失败
     */
    Integer deleteUser(String id);

    /**
     * 根据用户昵称查找用户
     * @param nickname 用户昵称
     * @return 数据库包装类UserInfoVO的集合
     */
    List<UserInfoVO> searchUser(String nickname);

    /**
     * 根据前端传入的数据查询是否有对应用户
     * @param adminLoginBO 前端json包装类
     * @return nickname用户昵称，返回null表示登录失败
     */
    String login(AdminLoginBO adminLoginBO);

    /**
     * 根据前端传入的数据添加用户
     * @param userSignupBO 前端json包装类
     * @return nickname用户昵称，返回null表示注册失败
     */
    String signup(UserSignupBO userSignupBO);

    Integer updateUserData(UserUpdateUserDataBO userUpdateUserDataBO);

    String searchUserPwdById(Integer id);
}
