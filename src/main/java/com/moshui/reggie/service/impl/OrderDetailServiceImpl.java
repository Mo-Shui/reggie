package com.moshui.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moshui.reggie.entity.OrderDetail;
import com.moshui.reggie.mapper.OrderDetailMapper;
import com.moshui.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
                                    implements OrderDetailService {
}
