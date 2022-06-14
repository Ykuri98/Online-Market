package service;

import dao.AdminDao;
import model.po.Admin;
import model.bo.admin.AdminInfoBO;
import model.bo.admin.AdminLoginBO;
import model.bo.admin.AdminPwdBO;
import model.vo.admin.AdminInfoVO;
import org.apache.ibatis.session.SqlSession;
import utils.MybatisUtil;

import java.util.List;

/**
 * @author yzw
 * @since 2022/06/08
 */

public class AdminServiceImpl implements AdminService{

    @Override
    public String login(AdminLoginBO adminLoginBO) {
        // 获得SqlSession和AdminDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        AdminDao adminDao = sqlSession.getMapper(AdminDao.class);

        // 从adminLoginBO中获取数据，包装在admin类中
        Admin admin = new Admin();
        admin.setUsername(adminLoginBO.getEmail());
        admin.setPassword(adminLoginBO.getPwd());

        // 根据admin的数据在AdminDao中查询并获得返回值，关闭SqlSession，返回查询得到的值
        String nickname = adminDao.queryNickname(admin);
        sqlSession.commit();
        sqlSession.close();
        return nickname;
    }

    @Override
    public List<AdminInfoVO> allAdmins() {
        // 获得SqlSession和AdminDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        AdminDao adminDao = sqlSession.getMapper(AdminDao.class);

        // 在AdminDao中查询并获得返回值，关闭SqlSession，返回查询得到的值
        List<AdminInfoVO> adminInfoVOList = adminDao.queryAllAdmin();
        sqlSession.commit();
        sqlSession.close();
        return adminInfoVOList;
    }

    @Override
    public Integer addAdmin(AdminInfoBO adminInfoBO) {
        // 获得SqlSession和AdminDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        AdminDao adminDao = sqlSession.getMapper(AdminDao.class);

        // 从adminLoginBO中获取数据，包装在admin类中
        Admin admin = new Admin();
        admin.setUsername(adminInfoBO.getEmail());
        admin.setPassword(adminInfoBO.getPwd());
        admin.setNickname(adminInfoBO.getNickname());

        // 查询是否已存在账户，存在则返回0状态码
        Integer adminExist = adminDao.isAdminExist(admin);
        if (adminExist != null){
            sqlSession.commit();
            sqlSession.close();
            return 0;
        }

        // 在AdminDao中执行添加，返回添加的行数
        Integer addResult = adminDao.addAdmin(admin);
        sqlSession.commit();
        sqlSession.close();
        return addResult;
    }

    // bug！
    @Override
    public Integer deleteAdmin(String id) {
        // 获得SqlSession和AdminDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        AdminDao adminDao = sqlSession.getMapper(AdminDao.class);

        // 在AdminDao中执行删除，返回删除的行数
        Integer isDeleted = adminDao.deleteAdmin(Integer.parseInt(id));
        sqlSession.commit();
        sqlSession.close();
        return isDeleted;
    }

    @Override
    public List<AdminInfoVO> getAdminInfo(AdminInfoBO adminInfoBO) {
        // 获得SqlSession和AdminDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        AdminDao adminDao = sqlSession.getMapper(AdminDao.class);

        // 从adminLoginBO中获取数据，包装在admin类中
        Admin admin = new Admin();
        admin.setId(adminInfoBO.getId());
        admin.setUsername(adminInfoBO.getEmail());
        admin.setNickname(adminInfoBO.getNickname());

        // 在AdminDao中查找Admin，返回包装类
        List<AdminInfoVO> adminInfoVOList = adminDao.queryAdmin(admin);
        sqlSession.commit();
        sqlSession.close();
        return adminInfoVOList;
    }

    @Override
    public Integer updateAdmin(AdminInfoBO adminInfoBO) {
        // 获得SqlSession和AdminDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        AdminDao adminDao = sqlSession.getMapper(AdminDao.class);

        // 从adminLoginBO中获取数据，包装在admin类中
        Admin admin = new Admin();
        admin.setId(adminInfoBO.getId());
        admin.setPassword(adminInfoBO.getPwd());
        admin.setNickname(adminInfoBO.getNickname());

        // 在AdminDao中查询并获得返回值，关闭SqlSession，返回查询得到的值
        Integer updateResult = adminDao.updateAdmin(admin);
        sqlSession.commit();
        sqlSession.close();
        return updateResult;
    }

    @Override
    public Integer updateAdminPwd(AdminPwdBO adminPwdBO) {
        // 获得SqlSession和AdminDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        AdminDao adminDao = sqlSession.getMapper(AdminDao.class);

        // 从adminPwdBO中获取数据，包装在oldAdmin类中
        Admin oldAdmin = new Admin();
        oldAdmin.setPassword(adminPwdBO.getOldPwd());
        oldAdmin.setNickname(adminPwdBO.getAdminToken());

        // 首先判断密码是否正确，如果不正确，更新失败
        Integer searchResult = adminDao.queryAdminByNickname(oldAdmin);
        if (searchResult == null){
            sqlSession.commit();
            sqlSession.close();
            return 0;
        }

        // 从adminPwdBO中获取数据，包装在newAdmin类中
        Admin newAdmin = new Admin();
        newAdmin.setPassword(adminPwdBO.getNewPwd());
        newAdmin.setNickname(adminPwdBO.getAdminToken());

        // 更新数据
        Integer updateResult = adminDao.updateAdminPwd(newAdmin);
        sqlSession.commit();
        sqlSession.close();
        return updateResult;
    }

}
