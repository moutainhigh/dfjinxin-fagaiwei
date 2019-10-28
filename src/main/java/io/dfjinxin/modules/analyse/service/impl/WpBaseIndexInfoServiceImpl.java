package io.dfjinxin.modules.analyse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.Query;
import io.dfjinxin.modules.analyse.dao.WpBaseIndexInfoDao;
import io.dfjinxin.modules.analyse.entity.WpBaseIndexInfoEntity;
import io.dfjinxin.modules.analyse.service.WpBaseIndexInfoService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("wpBaseIndexInfoService")
public class WpBaseIndexInfoServiceImpl extends ServiceImpl<WpBaseIndexInfoDao, WpBaseIndexInfoEntity> implements WpBaseIndexInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WpBaseIndexInfoEntity> page = this.page(
                new Query<WpBaseIndexInfoEntity>().getPage(params),
                new QueryWrapper<WpBaseIndexInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<WpBaseIndexInfoEntity> getIndexNameByType(Integer commId) {

        if (commId == null) {
            return null;
        }

        QueryWrapper<WpBaseIndexInfoEntity> where = new QueryWrapper<>();
        where.eq("comm_id", commId);
        where.eq("index_flag", 0);
        where.eq("index_type", "价格");
        //114码表中用途是预警的
        where.eq("index_used", 114);
        return baseMapper.selectList(where);
    }

    @Override
    public List<WpBaseIndexInfoEntity> getIndexTreeByCommId(Integer commId) {
        if (commId == null) {
            return null;
        }
        QueryWrapper<WpBaseIndexInfoEntity> where = new QueryWrapper<>();
        where.eq("comm_id", commId);
        where.groupBy("index_type");
        List<WpBaseIndexInfoEntity> indexTypeList = baseMapper.selectList(where);
        for (WpBaseIndexInfoEntity entity : indexTypeList) {
            QueryWrapper<WpBaseIndexInfoEntity> where2 = new QueryWrapper<>();
            where2.eq("comm_id", entity.getCommId());
            where2.eq("index_type", entity.getIndexType());
            where2.groupBy("index_name");
            List<WpBaseIndexInfoEntity> indexNameList = baseMapper.selectList(where2);
            entity.setSubList(indexNameList);
        }
        return indexTypeList;
    }

}
