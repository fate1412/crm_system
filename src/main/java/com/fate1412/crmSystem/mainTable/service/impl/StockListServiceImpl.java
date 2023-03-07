package com.fate1412.crmSystem.mainTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.mainTable.constant.TableNames;
import com.fate1412.crmSystem.mainTable.dto.select.StockListSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.update.StockListUpdateDTO;
import com.fate1412.crmSystem.mainTable.pojo.StockList;
import com.fate1412.crmSystem.mainTable.mapper.StockListMapper;
import com.fate1412.crmSystem.mainTable.service.IStockListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.security.service.ISysUserService;
import com.fate1412.crmSystem.utils.IdToName;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.MyCollections;
import com.fate1412.crmSystem.utils.TableResultData;
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
    private ISysUserService sysUserService;
    @Autowired
    private ITableOptionService tableOptionService;
    
    
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
    
        List<SysUser> sysUserList = sysUserService.listByIds(userIdList);
        Map<Long, String> userMap = MyCollections.list2MapL(sysUserList, SysUser::getUserId, SysUser::getRealName);
        
    
        List<StockListSelectDTO> stockListSelectDTOList = MyCollections.copyListProperties(stockLists, StockListSelectDTO::new);
        stockListSelectDTOList.forEach(dto -> {
            Long createId = dto.getCreater();
            Long updater = dto.getUpdater();
            Long owner = dto.getOwner();
            dto.setCreaterR(new IdToName(createId,userMap.get(createId), TableNames.sysUser));
            dto.setUpdaterR(new IdToName(updater,userMap.get(updater),TableNames.sysUser));
            dto.setOwnerR(new IdToName(owner,userMap.get(owner),TableNames.sysUser));
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
                SysUser sysUser = sysUserService.thisUser();
                stockList
                        .setUpdateTime(new Date())
                        .setUpdater(sysUser.getUserId());
                return stockList;
            }
        });
    }
    
    @Override
    public JsonResult<?> add(StockListUpdateDTO stockListUpdateDTO) {
        return add(stockListUpdateDTO, new MyEntity<StockList>(new StockList()) {
            @Override
            public StockList set(StockList stockList) {
                SysUser sysUser = sysUserService.thisUser();
                stockList
                        .setCreateTime(new Date())
                        .setUpdateTime(new Date())
                        .setCreater(sysUser.getUserId())
                        .setUpdater(sysUser.getUserId());
                return stockList;
            }
        });
    }
    
    @Override
    public TableResultData getColumns() {
        return getColumns(TableNames.stockList, new StockListSelectDTO(),tableOptionService);
    }
    
    @Override
    public List<IdToName> getOptions(String nameLike, Integer page) {
        QueryWrapper<StockList> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(StockList::getId)
                .like(StockList::getId,nameLike);
        IPage<StockList> iPage = new Page<>(page,10);
        stockListMapper.selectPage(iPage,queryWrapper);
        return IdToName.createList2(iPage.getRecords(), StockList::getId, StockList::getId);
    }
}
