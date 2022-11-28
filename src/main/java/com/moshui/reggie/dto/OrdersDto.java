package com.moshui.reggie.dto;

import com.moshui.reggie.entity.OrderDetail;
import com.moshui.reggie.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
