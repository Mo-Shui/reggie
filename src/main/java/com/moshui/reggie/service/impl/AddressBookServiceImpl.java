package com.moshui.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moshui.reggie.entity.AddressBook;
import com.moshui.reggie.mapper.AddressBookMapper;
import com.moshui.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> 
                                    implements AddressBookService {

}
