package com.moshui.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moshui.reggie.common.CustomException;
import com.moshui.reggie.entity.Category;
import com.moshui.reggie.entity.Dish;
import com.moshui.reggie.entity.Setmeal;
import com.moshui.reggie.mapper.CategoryMapper;
import com.moshui.reggie.service.CategoryService;
import com.moshui.reggie.service.DishService;
import com.moshui.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
                                implements CategoryService {
    
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void remove(Long id) {
        //查询是否关联了菜品
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(Dish::getCategoryId,id);
        int dishCount = dishService.count(dishQueryWrapper);
        if (dishCount > 0){
            throw new CustomException("该分类已关联菜品，不能删除");
        }

        //查询是否关联了套餐
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.eq(Setmeal::getCategoryId,id);
        int setmealCount = setmealService.count(setmealQueryWrapper);
        if (setmealCount > 0){
            throw new CustomException("该分类已关联套餐，不能删除");
        }
        
        super.removeById(id);
    }
}
