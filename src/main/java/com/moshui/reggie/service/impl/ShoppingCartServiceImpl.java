package com.moshui.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moshui.reggie.entity.ShoppingCart;
import com.moshui.reggie.mapper.ShoppingCartMapper;
import com.moshui.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
                                    implements ShoppingCartService {
}
