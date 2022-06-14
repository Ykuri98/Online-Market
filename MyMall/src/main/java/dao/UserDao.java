package dao;

import model.po.User;
import model.vo.user.UserInfoVO;

import java.util.List;

public interface UserDao {
    List<UserInfoVO> queryAllUser();

    Integer deleteUser(Integer id);

    List<UserInfoVO> queryUserByNickname(String nickname);

    String queryNickname(User user);

    Integer insertUser(User user);

    List<Integer> queryUserByNicknameOrUsername(User user);

    Integer updateUserData(User user);

    String queryUserPwdById(Integer id);
}
