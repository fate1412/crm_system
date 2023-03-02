package com.fate1412.crmSystem.mainTable.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate1412.crmSystem.mainTable.dto.ProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.SalesOrderSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.StockListSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.StockListUpdateDTO;
import com.fate1412.crmSystem.mainTable.pojo.Customer;
import com.fate1412.crmSystem.mainTable.pojo.SalesOrder;
import com.fate1412.crmSystem.mainTable.pojo.StockList;
import com.fate1412.crmSystem.mainTable.mapper.StockListMapper;
import com.fate1412.crmSystem.mainTable.service.IStockListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.security.mapper.SysUserMapper;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.utils.IdToName;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.MyCollections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 备货单 服务实现类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Service
public class StockListServiceImpl extends ServiceImpl<StockListMapper, StockList> implements IStockListService {
    @Autowired
    private StockListMapper stockListMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    
    
    @Override
    public List<?> getDTOList(List<StockList> stockLists) {
        if (MyCollections.isEmpty(stockLists)) {
            return new ArrayList<>();
        }
        //员工
        List<Long> createIds = MyCollections.objects2List(stockLists, StockList::getCreater);
        List<Long> updateMemberIds = MyCollections.objects2List(stockLists, StockList::getUpdater);
        List<Long> ownerList = MyCollections.objects2List(stockLists, StockList::getOwner);
        List<Long> userIdList = MyCollections.addList(true, createIds, updateMemberIds, ownerList);
    
        List<SysUser> sysUserList = sysUserMapper.selectBatchIds(userIdList);
        Map<Long, String> userMap = MyCollections.list2MapL(sysUserList, SysUser::getUserId, SysUser::getRealName);
        
    
        List<StockListSelectDTO> stockListSelectDTOList = MyCollections.copyListProperties(stockLists, StockListSelectDTO::new);
        stockListSelectDTOList.forEach(dto -> {
            Long createId = dto.getCreater();
            Long updater = dto.getUpdater();
            Long owner = dto.getOwner();
            dto.setCreaterR(new IdToName(createId,userMap.get(createId),"sysUser"));
            dto.setUpdaterR(new IdToName(updater,userMap.get(updater),"sysUser"));
            dto.setOwnerR(new IdToName(owner,userMap.get(owner),"sysUser"));
        });
        return stockListSelectDTOList;
    }
    
    @Override
    public BaseMapper<StockList> mapper() {
        return stockListMapper;
    }
    
    @Override
    public JsonResult<?> updateById(StockListUpdateDTO stockListUpdateDTO) {
        return updateByDTO(stockListUpdateDTO, new MyEntity<StockList>(new StockList()) {
            @Override
            public StockList set(StockList stockList) {
                stockList
                        .setUpdateTime(new Date());
                return stockList;
            }
        });
    }
    
    @Override
    public JsonResult<?> add(StockListUpdateDTO stockListUpdateDTO) {
        return add(stockListUpdateDTO, new MyEntity<StockList>(new StockList()) {
            @Override
            public StockList set(StockList stockList) {
                stockList
                        .setCreateTime(new Date());
                return stockList;
            }
        });
    }
}
