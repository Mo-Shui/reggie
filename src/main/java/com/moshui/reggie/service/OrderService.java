package com.moshui.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moshui.reggie.entity.Orders;

public interface OrderService extends IService<Orders> {
    
    void submit(Orders orders);
    
}
