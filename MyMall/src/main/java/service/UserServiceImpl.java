package service;

import dao.UserDao;
import model.bo.user.UserUpdateUserDataBO;
import model.po.User;
import model.bo.admin.AdminLoginBO;
import model.bo.user.UserSignupBO;
import model.vo.user.UserInfoVO;
import org.apache.ibatis.session.SqlSession;
import utils.MybatisUtil;

import java.util.List;

/**
 * @author yzw
 * @since 2022/06/09
 */

public class UserServiceImpl implements UserService{
    @Override
    public List<UserInfoVO> allUser() {
        // 获得SqlSession和userDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);

        // 在userDao中查询并获得返回值，关闭SqlSession，返回查询得到的值
        List<UserInfoVO> userInfoVOList = userDao.queryAllUser();
        sqlSession.commit();
        sqlSession.close();
        return userInfoVOList;
    }

    @Override
    public Integer deleteUser(String id) {
        // 获得SqlSession和AdminDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);

        // 在userDao中执行删除，返回删除的行数
        Integer isDeleted = userDao.deleteUser(Integer.parseInt(id));
        sqlSession.commit();
        sqlSession.close();
        return isDeleted;
    }

    @Override
    public List<UserInfoVO> searchUser(String nickname) {
        // 获得SqlSession和AdminDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);

        // 在UserDao中执行查找，返回查找的包装类
        List<UserInfoVO> searchResult = userDao.queryUserByNickname(nickname);
        return searchResult;
    }

    @Override
    public String login(AdminLoginBO adminLoginBO) {
        // 获得SqlSession和AdminDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);

        // 从adminLoginBO中获取数据，包装在admin类中
        User user = new User();
        user.setUsername(adminLoginBO.getEmail());
        user.setPassword(adminLoginBO.getPwd());

        // 根据admin的数据在AdminDao中查询并获得返回值，关闭SqlSession，返回查询得到的值
        String nickname = userDao.queryNickname(user);
        sqlSession.commit();
        sqlSession.close();

        return nickname;
    }

    @Override
    public String signup(UserSignupBO userSignupBO) {
        // 获得SqlSession和UserDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);

        User user = new User();
        user.setUsername(userSignupBO.getEmail());
        user.setPassword(userSignupBO.getPwd());
        user.setNickname(userSignupBO.getNickname());
        user.setAddress(userSignupBO.getAddress());
        user.setRecipient(userSignupBO.getRecipient());
        user.setPhone(userSignupBO.getPhone());

        // 查找是否存在重复的用户名或昵称，存在则注册失败
        List<Integer> queryResult = userDao.queryUserByNicknameOrUsername(user);
        if (queryResult.size() != 0){
            sqlSession.commit();
            sqlSession.close();
            return null;
        }

        Integer insertResult = userDao.insertUser(user);
        if (insertResult == 0){
            sqlSession.commit();
            sqlSession.close();
            return null;
        }

        sqlSession.commit();
        sqlSession.close();
        return user.getNickname();
    }

    @Override
    public Integer updateUserData(UserUpdateUserDataBO userUpdateUserDataBO) {
        // 获得SqlSession和UserDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);

        User user = new User();
        user.setAddress(userUpdateUserDataBO.getAddress());
        user.setId(Integer.parseInt(userUpdateUserDataBO.getId()));
        user.setPhone(userUpdateUserDataBO.getPhone());
        user.setRecipient(userUpdateUserDataBO.getRecipient());

        Integer updateResult = userDao.updateUserData(user);

        sqlSession.commit();
        sqlSession.close();
        return updateResult;
    }

    @Override
    public String searchUserPwdById(Integer id) {
        // 获得SqlSession和UserDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);

        String password = userDao.queryUserPwdById(id);

        sqlSession.commit();
        sqlSession.close();
        return password;
    }
}
