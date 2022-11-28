package com.moshui.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moshui.reggie.common.R;
import com.moshui.reggie.entity.Employee;
import com.moshui.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;
    
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //将用户密码通过md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        
        //根据用户名查询是否有此用户
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee username_Employee = employeeService.getOne(lambdaQueryWrapper);
        if (username_Employee == null){
            return R.error("用户不存在");
        }
            
        //根据加密后的密码查询是否有此用户
        if (!username_Employee.getPassword().equals(password)){
           return R.error("密码错误");
        }
        
        //根据用户状态判断用户是否被禁用，0为被禁用
        if (username_Employee.getStatus() == 0){
            return R.error("用户已禁用");
        }
        
        //登录成功，将用户id放到session中
        request.getSession().setAttribute("employee",username_Employee.getId());
        
        return R.success(username_Employee);
    }
    
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        
        return R.success("退出成功");
    }
    
    @PostMapping()
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        //设置初始密码123456
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        
        //设置创建和更新时间
        // employee.setCreateTime(LocalDateTime.now());
        // employee.setUpdateTime(LocalDateTime.now());
        
        //设置创建新用户的用户id
        // Long id = (Long) request.getSession().getAttribute("employee");
        // employee.setCreateUser(id);
        // employee.setUpdateUser(id);
        
        employeeService.save(employee);

        return R.success("员工创建成功");
    }
    
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);
        
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        if (name != null) {
            queryWrapper.like(Employee::getName,name);
        }
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        
        //执行查询
        employeeService.page(pageInfo,queryWrapper);
        
        return R.success(pageInfo);
    }
    
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        // employee.setUpdateTime(LocalDateTime.now());
        // Long empId = (Long) request.getSession().getAttribute("employee");
        // employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        
        return R.success("状态修改成功");
    }
    
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        return R.success(employeeService.getById(id));
    }
    
}
