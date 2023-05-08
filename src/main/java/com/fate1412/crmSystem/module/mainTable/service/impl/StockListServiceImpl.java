package com.fate1412.crmSystem.module.mainTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.module.flow.service.ISysFlowSessionService;
import com.fate1412.crmSystem.module.mainTable.constant.TableNames;
import com.fate1412.crmSystem.module.mainTable.dto.child.StockListChild;
import com.fate1412.crmSystem.module.mainTable.dto.insert.StockListInsertDTO;
import com.fate1412.crmSystem.module.mainTable.dto.select.StockListSelectDTO;
import com.fate1412.crmSystem.module.mainTable.dto.update.StockListUpdateDTO;
import com.fate1412.crmSystem.module.mainTable.pojo.Invoice;
import com.fate1412.crmSystem.module.mainTable.pojo.StockList;
import com.fate1412.crmSystem.module.mainTable.mapper.StockListMapper;
import com.fate1412.crmSystem.module.mainTable.pojo.StockListProduct;
import com.fate1412.crmSystem.module.mainTable.service.IStockListProductService;
import com.fate1412.crmSystem.module.mainTable.service.IStockListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.module.security.pojo.SysUser;
import com.fate1412.crmSystem.module.security.service.ISysUserService;
import com.fate1412.crmSystem.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private IStockListProductService stockListProductService;
    @Autowired
    private ISysFlowSessionService flowSessionService;
    
    
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
        
        //用户
        List<SysUser> sysUserList = sysUserService.listByIds(userIdList);
        Map<Long, String> userMap = MyCollections.list2MapL(sysUserList, SysUser::getUserId, SysUser::getRealName);
        
        //审批
        List<Long> stockListIds = MyCollections.objects2List(stockLists, StockList::getId);
        Map<Long, Integer> passMap = flowSessionService.getPass(TableNames.stockList, stockListIds);
        
        
        List<StockListSelectDTO> stockListSelectDTOList = MyCollections.copyListProperties(stockLists, StockListSelectDTO::new);
        stockListSelectDTOList.forEach(dto -> {
            Long createId = dto.getCreater();
            Long updater = dto.getUpdater();
            Long owner = dto.getOwner();
            dto.setCreaterR(new IdToName(createId, userMap.get(createId), TableNames.sysUser));
            dto.setUpdaterR(new IdToName(updater, userMap.get(updater), TableNames.sysUser));
            dto.setOwnerR(new IdToName(owner, userMap.get(owner), TableNames.sysUser));
            Integer pass = passMap.get(dto.getId());
            switch (pass) {
                case 0: dto.setPass("未审批"); break;
                case 1: dto.setPass("已通过"); break;
                default: dto.setPass("已拒绝");
            }
        });
        return stockListSelectDTOList;
    }
    
    @Override
    public BaseMapper<StockList> mapper() {
        return stockListMapper;
    }
    
    @Override
    @Transactional
    public JsonResult<?> updateByDTO(StockListUpdateDTO stockListUpdateDTO) {
        StockList stockList = new StockList();
        BeanUtils.copyProperties(stockListUpdateDTO, stockList);
        List<StockListChild> childList = stockListUpdateDTO.getChildList();
        return update(new MyEntity<StockList>(stockList) {
            @Override
            public StockList set(StockList stockList) {
                SysUser sysUser = sysUserService.thisUser();
                stockList
                        .setUpdateTime(new Date())
                        .setUpdater(sysUser.getUserId());
                return stockList;
            }
            
            @Override
            public ResultCode verification(StockList stockList) {
                return isRight(stockList);
            }
            
            @Override
            public boolean after(StockList stockList) {
                return afterUpdateChild(stockList, childList);
            }
        });
    }
    
    @Override
    @Transactional
    public JsonResult<?> addDTO(StockListInsertDTO stockListInsertDTO) {
        StockList stockList = new StockList();
        BeanUtils.copyProperties(stockListInsertDTO, stockList);
        List<StockListChild> childList = stockListInsertDTO.getChildList();
        return add(new MyEntity<StockList>(stockList) {
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
            
            @Override
            public ResultCode verification(StockList stockList) {
                return isRight(stockList);
            }
            
            @Override
            public boolean after(StockList stockList) {
                flowSessionService.addFlowSession(TableNames.stockList, stockList.getId());
                return afterUpdateChild(stockList, childList);
            }
        });
    }
    
    @Override
    @Transactional
    public boolean delById(Long id) {
        if (stockListProductService.delByStockListId(id)) {
            return removeById(id);
        }
        return false;
    }
    
    @Override
    public MyPage listByPage(SelectPage<StockListSelectDTO> selectPage) {
        StockListSelectDTO like = selectPage.getLike();
        QueryWrapper<StockList> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(like.getId() != null, StockList::getId, like.getId());
        return listByPage(selectPage.getPage(), selectPage.getPageSize(), queryWrapper);
    }
    
    @Override
    public TableResultData getColumns() {
        return getColumns(TableNames.stockList, new StockListSelectDTO(), tableOptionService);
    }
    
    @Override
    public List<IdToName> getOptions(String nameLike, Integer page) {
        QueryWrapper<StockList> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(StockList::getId)
                .like(StockList::getId, nameLike.trim());
        IPage<StockList> iPage = new Page<>(page, 10);
        stockListMapper.selectPage(iPage, queryWrapper);
        return IdToName.createList2(iPage.getRecords(), StockList::getId, StockList::getId);
    }
    
    private ResultCode isRight(StockList stockList) {
        //负责人
        if (stockList.getOwner() == null) {
            return ResultCode.PARAM_IS_BLANK;
        } else {
            SysUser user = sysUserService.getById(stockList.getOwner());
            if (user == null) {
                return ResultCode.PARAM_NOT_VALID;
            }
        }
        return ResultCode.SUCCESS;
    }
    
    private boolean afterUpdateChild(StockList stockList, List<StockListChild> childList) {
        if (MyCollections.isEmpty(childList)) {
            //删除所有备货单产品
            stockList.setPrices(0d);
            if (updateById(stockList)) {
                return stockListProductService.delByStockListId(stockList.getId());
            }
            return false;
        }
        
        childList = childList.stream().peek(t -> t.setStockListId(stockList.getId())).collect(Collectors.toList());
        
        QueryWrapper<StockListProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StockListProduct::getStockListId, stockList.getId());
        List<StockListProduct> list = stockListProductService.list(queryWrapper);
        
        List<StockListProduct> children = MyCollections.copyListProperties(childList, StockListProduct::new);
        
        //删除
        List<StockListProduct> delDifference = MyCollections.difference(list, children, StockListProduct::getId);
        //更新
        List<StockListProduct> intersection = MyCollections.intersection(children, list, StockListProduct::getId);
        //新增
        List<StockListProduct> addDifference = MyCollections.removeAll(children, intersection);
        
        //删除备货单产品
        List<Long> ids = MyCollections.objects2List(delDifference, StockListProduct::getId);
        stockListProductService.delByIds(ids);
        
        //更新备货单产品
        for (StockListProduct slp : intersection) {
            stockListProductService.updateByEntity(slp);
        }
        //新增备货单产品
        for (StockListProduct slp : addDifference) {
            stockListProductService.addEntity(slp);
        }
        return true;
    }
}
