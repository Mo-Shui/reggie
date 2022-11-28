package com.moshui.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moshui.reggie.common.CustomException;
import com.moshui.reggie.dto.SetmealDto;
import com.moshui.reggie.entity.Setmeal;
import com.moshui.reggie.entity.SetmealDish;
import com.moshui.reggie.mapper.SetmealMapper;
import com.moshui.reggie.service.SetmealDishService;
import com.moshui.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
                                implements SetmealService {
    
    @Autowired
    private SetmealMapper setmealMapper;
    
    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息
        this.save(setmealDto);
        
        //保存关联关系
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //设置setmealId
        setmealDishes = setmealDishes.stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void removeWithDish(Long[] ids) {
        //只有停售的才可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        List<Long> idList = Arrays.asList(ids);
        queryWrapper.in(Setmeal::getId,idList);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        if (count > 0){
            throw new CustomException("套餐需停售，才能删除");
        }

        //删除套餐表中的数据
        this.removeByIds(idList);
        
        //删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(SetmealDish::getSetmealId,idList);
        setmealDishService.remove(dishLambdaQueryWrapper);
    }

}
