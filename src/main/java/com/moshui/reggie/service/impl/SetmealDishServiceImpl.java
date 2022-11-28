package com.moshui.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moshui.reggie.entity.SetmealDish;
import com.moshui.reggie.mapper.SetmealDishMapper;
import com.moshui.reggie.service.SetmealDishService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish>
                                    implements SetmealDishService {
}
