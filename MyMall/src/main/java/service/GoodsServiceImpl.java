package service;

import dao.GoodsDao;
import model.po.Goods;
import model.po.GoodsSpec;
import model.po.Msg;
import model.bo.goods.*;
import model.vo.goods.*;
import org.apache.ibatis.session.SqlSession;
import utils.MybatisUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yzw
 * @since 2022/06/09
 */

public class GoodsServiceImpl implements GoodsService{
    @Override
    public List<GoodsTypeVO> getType() {
        // 获得SqlSession和GoodsDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);

        // 在GoodsDao中执行查找，返回包装类的List
        List<GoodsTypeVO> goodsTypeVOList = goodsDao.queryAllGoodsType();
        sqlSession.commit();
        sqlSession.close();
        return goodsTypeVOList;
    }

    @Override
    public List<GoodsDetailVO> getGoodsByType(String typeId) {
        // 获得SqlSession和GoodsDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);

        // 在GoodsDao中执行查找，返回包装类的List
        List<GoodsDetailVO> goodsDetailVOList = goodsDao.queryGoodsDetailByType(Integer.parseInt(typeId));
        // 将包装类内img的前缀加上”http://localhost:8084/“
        for (GoodsDetailVO goodsDetailVO : goodsDetailVOList) {
            String path = goodsDetailVO.getImg();
            if (path != null){
                goodsDetailVO.setImg("http://192.168.13.40:8084/" + path);
            }
        }
        sqlSession.commit();
        sqlSession.close();
        return goodsDetailVOList;
    }

    @Override
    public Integer addGoodsType(GoodsTypeBO goodsTypeBO) {
        // 获得SqlSession和GoodsDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);

        // 将GoodsTypeBO映射在goods中
        Goods goods = new Goods();
        goods.setTypeName(goodsTypeBO.getName());

        // 判断该品目是否存在，若存在则返回状态码0
        Integer isGoodsTypeExist = goodsDao.queryGoodsType(goods);
        if (isGoodsTypeExist != null){
            sqlSession.commit();
            sqlSession.close();
            return 0;
        }

        // 在GoodsDao中执行添加条目，返回添加的行数
        Integer addResult = goodsDao.insertGoodsType(goods);
        sqlSession.commit();
        sqlSession.close();
        return addResult;
    }

    @Override
    public Integer addGoods(GoodsDetailBO goodsDetailBO) {
        // 获得SqlSession和GoodsDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);

        // 将GoodsDetailBO映射为Goods
        Goods goods = new Goods();
        goods.setName(goodsDetailBO.getName());
        goods.setImage(goodsDetailBO.getImg());
        goods.setDescription(goodsDetailBO.getDesc());
        goods.setTypeId(Integer.parseInt(goodsDetailBO.getTypeId()));

        List<GoodsSpec> goodsSpecList = new ArrayList<>();
        List<GoodsSpecBO> goodsSpecBOList = goodsDetailBO.getSpecList();
        for (GoodsSpecBO goodsSpecBO : goodsSpecBOList) {
            GoodsSpec goodsSpec = new GoodsSpec();
            goodsSpec.setPrice(Double.parseDouble(goodsSpecBO.getUnitPrice()));
            goodsSpec.setStockNum(Integer.parseInt(goodsSpecBO.getStockNum()));
            goodsSpec.setSpecName(goodsSpecBO.getSpecName());
            goodsSpecList.add(goodsSpec);
        }
        goods.setGoodsSpecList(goodsSpecList);

        // 计算商品的最小价格和总库存
        double minPrice = Double.MAX_VALUE;
        int stockSum = 0;
        for (GoodsSpec goodsSpec : goodsSpecList) {
            if (goodsSpec.getPrice() < minPrice){
                minPrice = goodsSpec.getPrice();
            }
            stockSum += goodsSpec.getStockNum();
        }

        // 返回状态码0，表示存在重复商品，添加失败
        Integer queryResult = goodsDao.queryIsGoodsExist(goods.getName(), goods.getTypeId());
        if (queryResult != null){
            sqlSession.commit();
            sqlSession.close();
            return 0;
        }

        // 在goodDetail中插入商品的信息、最小价格和总库存
        goodsDao.insertGoods(goods,minPrice,stockSum);

        // 遍历插入商品规格进goodsSpec表
        for (GoodsSpec goodsSpec : goodsSpecList) {
            Integer queryId = goodsDao.queryIsGoodsExist(goods.getName(), goods.getTypeId());
            goodsDao.insertGoodsSpec(queryId,goodsSpec.getSpecName(),goodsSpec.getPrice(),goodsSpec.getStockNum());
        }

        // 返回状态码1，表示添加成功
        sqlSession.commit();
        sqlSession.close();
        return 1;
    }

    @Override
    public Integer deleteGoodsById(String id) {
        // 获得SqlSession和GoodsDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);

        // 返回状态码0，表示商品不存在，删除失败
        Integer queryResult = goodsDao.queryGoodsIdById(Integer.parseInt(id));
        if (queryResult == null){
            sqlSession.commit();
            sqlSession.close();
            return 0;
        }

        // 根据id删除商品信息、商品规格、商品留言与回复已经相关商品的订单
        goodsDao.deleteGoodsById(Integer.parseInt(id));
        goodsDao.deleteGoodsSpecById(Integer.parseInt(id));
        goodsDao.deleteGoodsMsgByGoodsId(Integer.parseInt(id));
        goodsDao.deleteOrderByGoodsId(Integer.parseInt(id));

        // 返回状态码1，表示删除成功
        sqlSession.commit();
        sqlSession.close();
        return 1;
    }

    @Override
    public GoodsInfoVO getGoodsInfo(String id) {
        // 获得SqlSession和GoodsDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);

        // 两次独立查询的结果包装进GoodsInfoVO
        GoodsInfoDetailVO goodsInfoDetailVO = goodsDao.queryGoodsById(Integer.parseInt(id));
        List<GoodsInfoSpecVO> goodsInfoSpecVOS = goodsDao.queryGoodsSpecById(Integer.parseInt(id));
        GoodsInfoVO goodsInfoVO = new GoodsInfoVO();
        goodsInfoVO.setGoods(goodsInfoDetailVO);
        goodsInfoVO.setSpecs(goodsInfoSpecVOS);

        sqlSession.commit();
        sqlSession.close();
        return goodsInfoVO;
    }

    @Override
    public Integer updateGoods(GoodsInfoBO goodsInfoBO) {
        // 获得SqlSession和GoodsDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);

        Goods goods = new Goods();
        goods.setId(Integer.parseInt(goodsInfoBO.getId()));
        goods.setTypeId(goodsInfoBO.getTypeId());
        goods.setName(goodsInfoBO.getName());
        goods.setDescription(goodsInfoBO.getDesc());
        goods.setImage(goodsInfoBO.getImg());
        List<GoodsSpec> goodsSpecList = new ArrayList<>();
        Double minPrice = Double.MAX_VALUE;
        Integer stockSum = 0;
        for (GoodsInfoSpecBO goodsInfoSpecBO : goodsInfoBO.getSpecList()) {
            minPrice = Math.min(minPrice,goodsInfoSpecBO.getUnitPrice());
            stockSum += goodsInfoSpecBO.getStockNum();
            GoodsSpec goodsSpec = new GoodsSpec();
            goodsSpec.setId(goodsInfoSpecBO.getId());
            goodsSpec.setSpecName(goodsInfoSpecBO.getSpecName());
            goodsSpec.setPrice(Double.valueOf(goodsInfoSpecBO.getUnitPrice()));
            goodsSpec.setStockNum(goodsInfoSpecBO.getStockNum());
            goodsSpecList.add(goodsSpec);
        }

        // 进行更新
        goodsDao.updateGoodsDetail(goods,minPrice,stockSum);
        for (GoodsSpec goodsSpec : goodsSpecList) {
            goodsDao.updateGoodsSpec(goodsSpec);
        }

        // 更新完成，返回状态码1
        sqlSession.commit();
        sqlSession.close();
        return 1;
    }

    @Override
    public GoodsAddSpecVO addSpec(GoodsAddSpecBO goodsAddSpecBO) {
        // 获得SqlSession和GoodsDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);

        GoodsSpec goodsSpec = new GoodsSpec();
        goodsSpec.setGoodsId(Integer.valueOf(goodsAddSpecBO.getGoodsId()));
        goodsSpec.setSpecName(goodsAddSpecBO.getSpecName());
        goodsSpec.setPrice(Double.valueOf(goodsAddSpecBO.getUnitPrice()));
        goodsSpec.setStockNum(Integer.valueOf(goodsAddSpecBO.getStockNum()));

        // 添加前判断是否已经存在,存在则返回null
        GoodsAddSpecVO goodsAddSpecVO = goodsDao.queryGoodsSpec(goodsSpec.getGoodsId(), goodsSpec.getSpecName(), goodsSpec.getPrice(), goodsSpec.getStockNum());
        if (goodsAddSpecVO != null){
            sqlSession.commit();
            sqlSession.close();
            return null;
        }

        goodsDao.insertGoodsSpec(goodsSpec.getGoodsId(),goodsSpec.getSpecName(),goodsSpec.getPrice(),goodsSpec.getStockNum());
        goodsAddSpecVO = goodsDao.queryGoodsSpec(goodsSpec.getGoodsId(), goodsSpec.getSpecName(), goodsSpec.getPrice(), goodsSpec.getStockNum());
        // 添加完成
        sqlSession.commit();
        sqlSession.close();
        return goodsAddSpecVO;
    }

    @Override
    public List<GoodsNoReplyMsgVO> getNoReplyMsg() {
        // 获得SqlSession和GoodsDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);

        List<GoodsNoReplyMsgVO> goodsNoReplyMsgVOList = goodsDao.queryGoodsNoReplyMsg();

        sqlSession.commit();
        sqlSession.close();
        return goodsNoReplyMsgVOList;
    }

    @Override
    public List<GoodsRepliedMsgVO> getRepliedMsg() {
        // 获得SqlSession和GoodsDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);

        List<GoodsRepliedMsgVO> goodsRepliedMsgVOList = goodsDao.queryGoodsRepliedMsg();

        sqlSession.commit();
        sqlSession.close();
        return goodsRepliedMsgVOList;
    }

    @Override
    public Integer replyMsg(GoodsReplyMsgBO goodsReplyMsgBO) {
        // 获得SqlSession和GoodsDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);

        Msg msg = new Msg();
        msg.setId(goodsReplyMsgBO.getId());
        msg.setReplyContent(goodsReplyMsgBO.getContent());
        Date date = new Date();
        msg.setReplyTime(date);

        Integer updateResult = goodsDao.updateGoodsMsg(msg);

        sqlSession.commit();
        sqlSession.close();
        return updateResult;
    }

    @Override
    public List<GoodsDetailVO> getGoodsByName(String name) {
        // 获得SqlSession和GoodsDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);

        List<GoodsDetailVO> goodsDetailVOList = goodsDao.queryGoodsByName(name);
        // 将包装类内img的前缀加上”http://localhost:8084/“
        for (GoodsDetailVO goodsDetailVO : goodsDetailVOList) {
            String path = goodsDetailVO.getImg();
            if (path != null){
                goodsDetailVO.setImg("http://192.168.13.40:8084/" + path);
            }
        }

        sqlSession.commit();
        sqlSession.close();
        return goodsDetailVOList;
    }

    @Override
    public GoodsInfoUserVO getGoodsInfoUser(String id) {
        // 获得SqlSession和GoodsDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);

        // 两次独立查询的结果包装进GoodsInfoVO
        GoodsInfoDetailVO goodsInfoDetailVO = goodsDao.queryGoodsById(Integer.parseInt(id));
        List<GoodsInfoSpecVO> goodsInfoSpecVOS = goodsDao.queryGoodsSpecById(Integer.parseInt(id));
        GoodsInfoUserVO goodsInfoUserVO = new GoodsInfoUserVO();
        goodsInfoUserVO.setImg("http://192.168.13.40:8084/" + goodsInfoDetailVO.getImg());
        goodsInfoUserVO.setId(goodsInfoDetailVO.getId());
        goodsInfoUserVO.setName(goodsInfoDetailVO.getName());
        goodsInfoUserVO.setTypeId(goodsInfoDetailVO.getTypeId());
        goodsInfoUserVO.setDesc(goodsInfoDetailVO.getDesc());
        goodsInfoUserVO.setUnitPrice(goodsInfoDetailVO.getUnitPrice());
        goodsInfoUserVO.setSpecs(goodsInfoSpecVOS);

        sqlSession.commit();
        sqlSession.close();
        return goodsInfoUserVO;
    }

    @Override
    public List<GoodsMsgGetByUserVO> getGoodsMsg(String id) {
        // 获得SqlSession和GoodsDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);

        List<GoodsMsgGetByUserVO> goodsMsgGetByUserVOList = goodsDao.queryGoodsMsgByGoodsId(Integer.parseInt(id));
        // 去除取出时间后面的.0
        for (GoodsMsgGetByUserVO goodsMsgGetByUserVO : goodsMsgGetByUserVOList) {
            String time = goodsMsgGetByUserVO.getTime();
            time = time.substring(0,19);
            goodsMsgGetByUserVO.setTime(time);

            String replyTime = goodsMsgGetByUserVO.getReply().getTime();
            if (replyTime != null){
                replyTime = replyTime.substring(0,19);
                goodsMsgGetByUserVO.setReplyTime(replyTime);
            }
        }

        sqlSession.commit();
        sqlSession.close();
        return goodsMsgGetByUserVOList;
    }

    @Override
    public GoodsCommentVO getGoodsComment(String goodsId) {
        // 获得SqlSession和GoodsDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);

        GoodsCommentVO goodsCommentVO = new GoodsCommentVO();
        List<GoodsCommentListVO> goodsCommentListVOList = goodsDao.queryOrderByGoodsId(Integer.parseInt(goodsId));

        // 计算平均分
        Double rate = 0.0;
        for (GoodsCommentListVO goodsCommentListVO : goodsCommentListVOList) {
            rate += goodsCommentListVO.getScore();
        }
        rate /= goodsCommentListVOList.size();
        goodsCommentVO.setRate(String.valueOf(rate));
        goodsCommentVO.setCommentList(goodsCommentListVOList);

        for (GoodsCommentListVO goodsCommentListVO : goodsCommentListVOList) {
            String time = goodsCommentListVO.getTime();
            time = time.substring(0,19);
            goodsCommentListVO.setTime(time);
        }

        sqlSession.commit();
        sqlSession.close();
        return goodsCommentVO;
    }

    @Override
    public Integer askGoodsMsg(GoodsAskGoodsMsgBO goodsAskGoodsMsgBO) {
        // 获得SqlSession和GoodsDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);

        // 获得昵称和商品id
        String token = goodsAskGoodsMsgBO.getToken();
        String goodsId = goodsAskGoodsMsgBO.getGoodsId();

        // 根据商品id找到商品名字
        GoodsInfoDetailVO goodsInfoDetailVO = goodsDao.queryGoodsById(Integer.parseInt(goodsId));
        String goodsName = goodsInfoDetailVO.getName();

        // 获得当前时间
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = sf.format(date);

        //根据昵称获取用户id
        Integer userId = goodsDao.queryUserIdByNickname(token);

        // 插入新的商品留言
        Integer insertResult = goodsDao.insertGoodsAskMsg(goodsAskGoodsMsgBO, userId, goodsName, createTime);

        sqlSession.commit();
        sqlSession.close();
        return insertResult;
    }

    @Override
    public Integer deleteGoodsType(String typeId) {
        // 获得SqlSession和GoodsDao
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        GoodsDao goodsDao = sqlSession.getMapper(GoodsDao.class);

        goodsDao.deleteGoodsTypeById(Integer.parseInt(typeId));

        sqlSession.commit();
        sqlSession.close();
        return 1;
    }
}
