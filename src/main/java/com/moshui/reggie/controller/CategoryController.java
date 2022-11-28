package com.moshui.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moshui.reggie.common.R;
import com.moshui.reggie.entity.Category;
import com.moshui.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @PostMapping()
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        
        return R.success("新增分类成功");
    }
    
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        //分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);
        
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        
        categoryService.page(pageInfo,queryWrapper);
        
        return R.success(pageInfo);
    } 
    
    @DeleteMapping
    public R<String> delete(Long ids){
        // categoryService.removeById(id);
        categoryService.remove(ids);
        
        return R.success("删除成功");
    }
    
    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        
        return R.success("修改成功");
    }
    
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        
        return R.success(list);
    }
    
    
}
