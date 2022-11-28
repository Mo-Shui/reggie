package com.moshui.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moshui.reggie.dto.DishDto;
import com.moshui.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    
    void saveWithFlavor(DishDto dishDto);

    DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);
    
}
