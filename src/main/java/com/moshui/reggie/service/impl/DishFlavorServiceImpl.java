package com.moshui.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moshui.reggie.entity.DishFlavor;
import com.moshui.reggie.mapper.DishFlavorMapper;
import com.moshui.reggie.service.DishFlavorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
                                    implements DishFlavorService {
    
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    
}
