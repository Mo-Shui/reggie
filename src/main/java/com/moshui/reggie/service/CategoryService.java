package com.moshui.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moshui.reggie.entity.Category;

public interface CategoryService extends IService<Category> {
    
    void remove(Long id);
    
}
