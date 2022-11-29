package com.moshui.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moshui.reggie.common.R;
import com.moshui.reggie.dto.DishDto;
import com.moshui.reggie.entity.Category;
import com.moshui.reggie.entity.Dish;
import com.moshui.reggie.entity.DishFlavor;
import com.moshui.reggie.service.CategoryService;
import com.moshui.reggie.service.DishFlavorService;
import com.moshui.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {
    
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private RedisTemplate redisTemplate;
    
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);

        //删除分类的缓存
        String key = "dish_" + dishDto.getCategoryId() + "_" + dishDto.getStatus();
        redisTemplate.delete(key);
        
        return R.success("保存成功");
    }
    
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        Page<Dish> pageInfo = new Page(page,pageSize);

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null,Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        
        dishService.page(pageInfo,queryWrapper);
        
        //由于前端需要分类名称，所以需要对pageInfo进行处理
        List<Dish> dishList = pageInfo.getRecords();
        List<DishDto> dishDtoList = dishList.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Category category = categoryService.getById(item.getCategoryId());
            dishDto.setCategoryName(category.getName());

            return dishDto;
        }).collect(Collectors.toList());
        
        Page<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(pageInfo,dishDtoPage);
        dishDtoPage.setRecords(dishDtoList);

        return R.success(dishDtoPage);
    }
    
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable("id") Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        
        return R.success(dishDto);
    }
    
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        
        //删除分类的缓存
        String key = "dish_" + dishDto.getCategoryId() + "_" + dishDto.getStatus();
        redisTemplate.delete(key);
        
        return R.success("修改成功");
    }
    
    // @GetMapping("/list")
    // public R<List<Dish>> list(Dish dish){
    //     LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
    //     queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
    //     //查询起售状态的菜品
    //     queryWrapper.eq(Dish::getStatus,1);
    //     queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
    //
    //     List<Dish> list = dishService.list(queryWrapper);
    //    
    //     return R.success(list);
    // }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        List<DishDto> dishDtoList = null;
        
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
        
        if (dishDtoList != null){
            return R.success(dishDtoList);
        }

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        //查询起售状态的菜品
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        dishDtoList = list.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Category category = categoryService.getById(item.getCategoryId());
            dishDto.setCategoryName(category.getName());

            LambdaQueryWrapper<DishFlavor> dishFlavorServiceLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorServiceLambdaQueryWrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorServiceLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);

            return dishDto;
        }).collect(Collectors.toList());
        
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);

        return R.success(dishDtoList);
    }
    
}
