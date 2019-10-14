package io.dfjinxin.modules.price.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.dto.PssCommTotalDto;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.price.dao.PssCommConfDao;
import io.dfjinxin.modules.price.dao.PssCommTotalDao;
import io.dfjinxin.modules.price.entity.PssCommTotalEntity;
import io.dfjinxin.modules.price.service.PssCommTotalService;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("pssCommTotalService")
public class PssCommTotalServiceImpl extends ServiceImpl<PssCommTotalDao, PssCommTotalEntity> implements PssCommTotalService {

    @Autowired
    private PssCommTotalDao pssCommTotalDao;

    @Autowired
    private PssCommConfDao pssCommConfDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PssCommTotalEntity> page = this.page(
                new Query<PssCommTotalEntity>().getPage(params),
                new QueryWrapper<PssCommTotalEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Map<String, List<PssCommTotalEntity>> queryCommType() {

        QueryWrapper where1 = new QueryWrapper();
        where1.eq("level_code", "0");
        where1.eq("data_flag", "0");

        List<PssCommTotalEntity> commType1 = baseMapper.selectList(where1);
        List<PssCommTotalEntity> commType2 = new ArrayList<>();
        List<PssCommTotalEntity> commType3 = new ArrayList<>();
        List<PssCommTotalEntity> commType4 = new ArrayList<>();
        Map<String, List<PssCommTotalEntity>> resultMap = new HashMap<>();
        for (PssCommTotalEntity entity : commType1) {
            QueryWrapper where2 = new QueryWrapper();
            where2.in("parent_code", entity.getCommId());
            where2.eq("data_flag", "0");
            where2.eq("level_code", "1");
            List<PssCommTotalEntity> subType = baseMapper.selectList(where2);
            commType2.addAll(subType);
        }

        for (PssCommTotalEntity entity : commType2) {
            QueryWrapper where3 = new QueryWrapper();
            where3.in("parent_code", entity.getCommId());
            where3.eq("data_flag", "0");
            where3.eq("level_code", "2");
            List<PssCommTotalEntity> subType3 = baseMapper.selectList(where3);
            commType3.addAll(subType3);
        }

        for (PssCommTotalEntity entity : commType3) {
            QueryWrapper where4 = new QueryWrapper();
            where4.in("parent_code", entity.getCommId());
            where4.eq("data_flag", "0");
            where4.eq("level_code", "3");
            List<PssCommTotalEntity> subType4 = baseMapper.selectList(where4);
            commType4.addAll(subType4);
        }

        //商品类型-0类 大宗，民生
        resultMap.put("commLevelCode_0", commType1);
        //商品大类-1类
        resultMap.put("commLevelCode_1", commType2);
        //商品名称-2类
        resultMap.put("commLevelCode_2", commType3);
        resultMap.put("commLevelCode_3", commType4);
        return resultMap;
    }

    /**
     * @Desc: 查询商品预警配置, 只有四类商品会配置预警
     * @Param: [pssCommTotalDto]
     * @Return: io.dfjinxin.common.utils.PageUtils
     * @Author: z.h.c
     * @Date: 2019/10/12 13:05
     */
    @Override
    public PageUtils queryPageList(PssCommTotalDto pssCommTotalDto) {

        String levelCode_0 = pssCommTotalDto.getCommLevelCode_0();
        String levelCode_1 = pssCommTotalDto.getCommLevelCode_1();
        String levelCode_2 = pssCommTotalDto.getCommLevelCode_2();
        //商品类型-0类 为空 查询所有
        if (StringUtils.isEmpty(levelCode_0)) {
            QueryWrapper where1 = new QueryWrapper();
            where1.eq("level_code", "0");
            where1.eq("data_flag", "0");
            List<PssCommTotalEntity> commType0 = baseMapper.selectList(where1);

//            List<Map<String, PssCommTotalEntity>> resultList = new ArrayList();
            List<PssCommTotalEntity> resultList2 = new ArrayList();
            Map<String, PssCommTotalEntity> map = new HashMap<>();
//            Integer totalCount = 0;
            for (PssCommTotalEntity entity : commType0) {
                Map<String, Object> temp = getCommEwarnByType_1(entity, pssCommTotalDto);
                PssCommTotalEntity result = (PssCommTotalEntity) temp.get("result");
//                totalCount = (Integer) temp.get("totalCount");
//                if (1 == entity.getCommId()) {
//                    map.put("dazong", result);
//                    map.put("minsheng", result);
//                } else {
//                }
                resultList2.add(result);
            }
//            resultList.add(map);

//            查询所有商品预警配置表的总数
            QueryWrapper where3 = new QueryWrapper();
            where3.eq("del_flag", "0");
            int totalCount = pssCommConfDao.selectCount(where3);

            return new PageUtils(resultList2, totalCount, pssCommTotalDto.getPageSize(), pssCommTotalDto.getPageIndex());

        }
        //商品类型-0类 不为空，商品大类-1类 为空，查询指定0类
        if (StringUtils.isNotBlank(levelCode_0) && StringUtils.isBlank(levelCode_1)) {
//            PssCommTotalEntity commType0 = selectCommByLevelCode0(Integer.valueOf(levelCode_0));
//            int countLevelCode0 = pssCommTotalDao.queryPageListCountByLevelCode0(pssCommTotalDto);
//            List<PssCommTotalEntity> listLevelCode0 = pssCommTotalDao.queryPageLisByLevelCode0(pssCommTotalDto);
//            commType0.setSubCommList(listLevelCode0);
//            List<PssCommTotalEntity> returnList0 = new ArrayList<>();
//            returnList0.add(commType0);

            //1类商品
            PssCommTotalEntity type1Comm = selectCommByCommId(levelCode_0, 0);

            Map<String, Object> temp = getCommEwarnByType_1(type1Comm, pssCommTotalDto);
            PssCommTotalEntity result = (PssCommTotalEntity) temp.get("result");
            int totalCount = (Integer) temp.get("totalCount");
            List<PssCommTotalEntity> list = new ArrayList<>();
            list.add(result);

            return new PageUtils(list, totalCount, pssCommTotalDto.getPageSize(), pssCommTotalDto.getPageIndex());
        }

        //商品类型-0类 不为空，商品大类-1类 不为空，商品名称为空，查询指定1类
        if (StringUtils.isNotBlank(levelCode_0) && StringUtils.isNotBlank(levelCode_1)
                && StringUtils.isBlank(levelCode_2)) {
//            PssCommTotalEntity commType0 = selectCommByLevelCode0(Integer.valueOf(levelCode_0));
//            int countLevelCode1 = pssCommTotalDao.queryPageListCountByLevelCode1(pssCommTotalDto);
//            List<PssCommTotalEntity> listLevelCode1 = pssCommTotalDao.queryPageLisByLevelCode1(pssCommTotalDto);
//            commType0.setSubCommList(listLevelCode1);
//            List<PssCommTotalEntity> returnList1 = new ArrayList<>();
//            returnList1.add(commType0);
//            return new PageUtils(returnList1, countLevelCode1, pssCommTotalDto.getPageSize(), pssCommTotalDto.getPageIndex());
            //1类商品
            PssCommTotalEntity type1Comm = selectCommByCommId(levelCode_0, 0);
            //查询2类商品
            PssCommTotalEntity type2Comm = selectCommByCommId(levelCode_1, 1);
            //查询3类商品
            List<PssCommTotalEntity> type3CommList = selectCommListByCommId(type2Comm.getCommId(), 2);
            int count = 0;
            for (PssCommTotalEntity type3Comm : type3CommList) {
                //根据3类商品查询该商品下所有4类商品的预警信息
                List<PssCommTotalEntity> type4CommEwarnList = pssCommTotalDao.selectSubCommByLevelCode2(type3Comm.getCommId(), pssCommTotalDto);
                type3Comm.setSubCommList(type4CommEwarnList);
                count += pssCommTotalDao.selectSubCommCountByLevelCode2(type3Comm.getCommId(), pssCommTotalDto);
            }
            List<PssCommTotalEntity> type1CommList = new ArrayList<>();
            List<PssCommTotalEntity> type2CommList = new ArrayList<>();

            type2Comm.setSubCommList(type3CommList);

            type2CommList.add(type2Comm);
            type1Comm.setSubCommList(type2CommList);
            type1CommList.add(type1Comm);
            return new PageUtils(type1CommList, count, pssCommTotalDto.getPageSize(), pssCommTotalDto.getPageIndex());

        }

        //商品类型-0类 不为空，商品大类-1类 不为空，商品名称-2类 不为空，查询指定2类
        if (StringUtils.isNotBlank(levelCode_0) && StringUtils.isNotBlank(levelCode_1)
                && StringUtils.isNotBlank(levelCode_2)) {
//            PssCommTotalEntity commType0 = selectCommByLevelCode0(Integer.valueOf(levelCode_0));
//            int countLevelCode2 = pssCommTotalDao.queryPageListCountByLevelCode2(pssCommTotalDto);
//            List<PssCommTotalEntity> listLevelCode2 = pssCommTotalDao.queryPageLisByLevelCode2(pssCommTotalDto);
//            commType0.setSubCommList(listLevelCode2);
//            List<PssCommTotalEntity> returnList2 = new ArrayList<>();
//            returnList2.add(commType0);
            //1类商品
            PssCommTotalEntity type1Comm = selectCommByCommId(levelCode_0, 0);
            //查询2类商品
            PssCommTotalEntity type2Comm = selectCommByCommId(levelCode_1, 1);
            //查询3类商品
            PssCommTotalEntity type3Comm = selectCommByCommId(levelCode_2, 2);
            //根据3类商品查询该商品下所有4类商品的预警信息
            List<PssCommTotalEntity> type4CommEwarnList = pssCommTotalDao.selectSubCommByLevelCode2(type3Comm.getCommId(), pssCommTotalDto);
            int count = pssCommTotalDao.selectSubCommCountByLevelCode2(type3Comm.getCommId(), pssCommTotalDto);

            type3Comm.setSubCommList(type4CommEwarnList);
            List<PssCommTotalEntity> type1CommList = new ArrayList<>();
            List<PssCommTotalEntity> type2CommList = new ArrayList<>();
            List<PssCommTotalEntity> type3CommList = new ArrayList<>();

            type3CommList.add(type3Comm);
            type2Comm.setSubCommList(type3CommList);

            type2CommList.add(type2Comm);
            type1Comm.setSubCommList(type2CommList);
            type1CommList.add(type1Comm);
            return new PageUtils(type1CommList, count, pssCommTotalDto.getPageSize(), pssCommTotalDto.getPageIndex());
        }

        return null;
    }

    @Override
    public List<PssCommTotalEntity> getAll() {
        QueryWrapper where2 = new QueryWrapper();
        where2.eq("data_flag", "0");
        return baseMapper.selectList(where2);
    }

    /**
     * @Desc: 查询商品信息
     * @Param: [params]
     * @Return: io.dfjinxin.common.utils.PageUtils
     * @Author: z.h.c
     * @Date: 2019/10/12 13:06
     */
    @Override
    public PageUtils queryCommInfoPageList(PssCommTotalDto params) {
        String levelCode_0 = params.getCommLevelCode_0();
        String levelCode_1 = params.getCommLevelCode_1();
        String levelCode_2 = params.getCommLevelCode_2();
        //商品类型-0类 为空 查询所有
        if (StringUtils.isBlank(levelCode_0)) {
            QueryWrapper where1 = new QueryWrapper();
            where1.eq("level_code", "0");
            where1.eq("data_flag", "0");
            List<PssCommTotalEntity> commType0 = baseMapper.selectList(where1);

            List<Map<String, PssCommTotalEntity>> resultList = new ArrayList();
            Map<String, PssCommTotalEntity> map = new HashMap<>();
            Integer totalCount = 0;
            for (PssCommTotalEntity entity : commType0) {
                Map<String, Object> temp = getCommEwarnByType_1(entity, params);
                PssCommTotalEntity result = (PssCommTotalEntity) temp.get("result");
                totalCount = (Integer) temp.get("totalCount");
                if (1 == entity.getCommId()) {
                    map.put("dazong", result);
                } else {
                    map.put("minsheng", result);
                }
            }
            resultList.add(map);
            return new PageUtils(resultList, totalCount, params.getPageSize(), params.getPageIndex());

        }
        //商品类型-0类 不为空，商品大类-1类 为空，查询指定0类
        if (StringUtils.isNotBlank(levelCode_0) && StringUtils.isBlank(levelCode_1)) {
            PssCommTotalEntity commType0 = selectCommByLevelCode0(Integer.valueOf(levelCode_0));
            int countLevelCode0 = pssCommTotalDao.queryPageListCountByLevelCode0(params);
            List<PssCommTotalEntity> listLevelCode0 = pssCommTotalDao.queryPageLisByLevelCode0(params);
            commType0.setSubCommList(listLevelCode0);
            List<PssCommTotalEntity> returnList0 = new ArrayList<>();
            returnList0.add(commType0);
            return new PageUtils(returnList0, countLevelCode0, params.getPageSize(), params.getPageIndex());
        }

        //商品类型-0类 不为空，商品大类-1类 不为空，商品名称为空，查询指定1类
        if (StringUtils.isNotBlank(levelCode_0) && StringUtils.isNotBlank(levelCode_1)
                && StringUtils.isBlank(levelCode_2)) {
            PssCommTotalEntity commType0 = selectCommByLevelCode0(Integer.valueOf(levelCode_0));
            int countLevelCode1 = pssCommTotalDao.queryPageListCountByLevelCode1(params);
            List<PssCommTotalEntity> listLevelCode1 = pssCommTotalDao.queryPageLisByLevelCode1(params);
            commType0.setSubCommList(listLevelCode1);
            List<PssCommTotalEntity> returnList1 = new ArrayList<>();
            returnList1.add(commType0);
            return new PageUtils(returnList1, countLevelCode1, params.getPageSize(), params.getPageIndex());
        }

        //商品类型-0类 不为空，商品大类-1类 不为空，商品名称-2类 不为空，查询指定2类
        if (StringUtils.isNotBlank(levelCode_0) && StringUtils.isNotBlank(levelCode_1)
                && StringUtils.isNotBlank(levelCode_2)) {
            PssCommTotalEntity commType0 = selectCommByLevelCode0(Integer.valueOf(levelCode_0));
//            int countLevelCode2 = pssCommTotalDao.queryPageListCountByLevelCode2(params);
            int countLevelCode2 = pssCommTotalDao.queryCommInfoCountLevelCode2(params);
//            List<PssCommTotalEntity> listLevelCode2 = pssCommTotalDao.queryPageLisByLevelCode2(params);
            List<PssCommTotalEntity> listLevelCode2 = pssCommTotalDao.queryCommInfoLevelCode2(params);
            commType0.setSubCommList(listLevelCode2);
            List<PssCommTotalEntity> returnList2 = new ArrayList<>();
            returnList2.add(commType0);
            return new PageUtils(returnList2, countLevelCode2, params.getPageSize(), params.getPageIndex());
        }

        return null;
    }


    /**
     * @Desc: 根据1类商品id, 查询4类商品预警配置，
     * @Param: -类商品id,分页参数
     * @Return: java.util.Map<java.lang.String, java.lang.Object>
     * @Author: z.h.c
     * @Date: 2019/10/14 10:13
     */
    private Map<String, Object> getCommEwarnByType_1(PssCommTotalEntity levelCode0, PssCommTotalDto dto) {
        if (levelCode0 == null || levelCode0.getCommId() == null) {
            return null;
        }
        //根据1类查询2类
        QueryWrapper where2 = new QueryWrapper();
        where2.in("parent_code", levelCode0.getCommId());
        where2.eq("data_flag", "0");
        where2.eq("level_code", "1");
        // 获取2类商品
        List<PssCommTotalEntity> commLevelCode1 = baseMapper.selectList(where2);
        int type4CommEwarnCount = 0;
        for (PssCommTotalEntity entity1 : commLevelCode1) {
            //根据2类查询3类商品
            QueryWrapper where3 = new QueryWrapper();
            where3.in("parent_code", entity1.getCommId());
            where3.eq("data_flag", "0");
            where3.eq("level_code", "2");
            // 获取3类商品
            List<PssCommTotalEntity> type3CommList = baseMapper.selectList(where3);
            for (PssCommTotalEntity type3Comm : type3CommList) {
                //根据3类商品查询该商品下所有4类商品的预警信息
                List<PssCommTotalEntity> type4CommEwarnList = pssCommTotalDao.selectSubCommByLevelCode2(type3Comm.getCommId(), dto);
                type3Comm.setSubCommList(type4CommEwarnList);
                int count = pssCommTotalDao.selectSubCommCountByLevelCode2(type3Comm.getCommId(), dto);
                type4CommEwarnCount += count;
            }
            entity1.setSubCommList(type3CommList);
        }
        levelCode0.setSubCommList(commLevelCode1);

        //查询所有商品预警配置表的总数
//        QueryWrapper where3 = new QueryWrapper();
//        where3.eq("del_flag", "0");
//        int totalCount = pssCommConfDao.selectCount(where3);

        Map<String, Object> map = new HashMap<>();
        map.put("result", levelCode0);
        map.put("totalCount", type4CommEwarnCount);
        return map;
    }

    private PssCommTotalEntity selectCommByLevelCode0(Integer commId) {

        if (commId == null) {
            return null;
        }
        QueryWrapper where1 = new QueryWrapper();
        where1.eq("level_code", "0");
        where1.eq("data_flag", "0");
        where1.eq("comm_id", commId);
        return baseMapper.selectOne(where1);
    }

    /**
     * @Desc: 根据商品id和商品类型查询
     * @Param: [commId, levelCode]
     * @Return: io.dfjinxin.modules.price.entity.PssCommTotalEntity
     * @Author: z.h.c
     * @Date: 2019/10/14 13:35
     */
    private PssCommTotalEntity selectCommByCommId(String commId, Integer levelCode) {

        if (commId == null) {
            return null;
        }
        QueryWrapper where1 = new QueryWrapper();
        where1.eq("data_flag", "0");
        where1.eq("level_code", levelCode);
        where1.eq("comm_id", commId);
        return baseMapper.selectOne(where1);
    }

    private List<PssCommTotalEntity> selectCommListByCommId(Integer commId, Integer levelCode) {

        if (commId == null) {
            return null;
        }
        QueryWrapper where1 = new QueryWrapper();
        where1.eq("data_flag", "0");
        where1.eq("level_code", levelCode);
        where1.eq("parent_code", commId);
        return baseMapper.selectList(where1);
    }

}
