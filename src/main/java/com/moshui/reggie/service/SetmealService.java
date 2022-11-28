package com.moshui.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moshui.reggie.dto.SetmealDto;
import com.moshui.reggie.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    
    void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(Long[] ids);
    
}
