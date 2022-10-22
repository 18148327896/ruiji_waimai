package com.rdq.ruiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rdq.ruiji.common.R;
import com.rdq.ruiji.entity.Employee;
import com.rdq.ruiji.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /*
    登录方法
     */
    @PostMapping("/login")
    //json形式接收使用requestbody
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        /*将密码使用MD5加密*/
        String password = employee.getPassword();
        log.info("传过来的的用户名是{}，密码密码是：{}",employee.getUsername()+password);
        log.info("开始md加密");
        //DigestUtils工具类可进行MD5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //根据用户名查询数据库
        log.info("开始查询数据库信息");
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //eq等值查询
        queryWrapper.eq(Employee ::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //查询输入结果
        if (emp == null) {
            return R.error("登录失败哦亲！，请检查用户名或密码");
        }
        //比对密码信息加密过的
        if (!emp.getPassword().equals(password)){
            return R.error("登录失败哦亲！，请检查用户名或密码");
        }

        /*
        * 查看员工的状态是否禁用
        * */
        if (emp.getStatus() == 0){
            return R.error("您的账号已禁用请联系管理员进行解封");
        }

        /*
        * 登录成功，将员工id存入session并返回结果，进行页面跳转
        * */
        request.getSession().setAttribute("employee",emp.getId());

        return R.success(emp);
    }

    /*
    * 推出登录
    * */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理存储在cooke中的session中登录的员工的id
        log.info("正在退出哦亲，稍等。。。");
        request.getSession().removeAttribute("employee");
        log.info("退出成功");
        return R.success("退出成功");
    }

    /*
    *增加员工
    *
    * */
    @PostMapping
    public R<String> save (HttpServletRequest request,@RequestBody Employee employee){
        log.info("开始新增员工，员工的信息是：",employee.toString());
        log.info("初始密码123456进行mf5加密");
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        /*//创建人通过session获取
        Long uid= (Long) request.getSession().getAttribute("employee");
        log.info("修改人是{}",uid);
        employee.setUpdateUser(uid);
        log.info("创建人是{}",uid);
        employee.setCreateUser(uid);
        log.info("创建时间");
        employee.setCreateTime(LocalDateTime.now());
        log.info("修改时间");
        employee.setUpdateTime(LocalDateTime.now());*/


        employeeService.save(employee);



        return R.success("亲，新增员工信息成功");
    }

    /*
    * 员工分页展示
    * */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("分页页数为page = {},页大小是pageSize={},name是={}",page,pageSize,name);

        /*
        * 构造分页构造器
        * 条件构造器
        * 执行查询
        * *
         */
        Page pageInfo = new Page(page, pageSize);
        //添加过滤条件 isNotEmpty 如果那么name不为空则执行like查询
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Employee :: getName,name);
        //排序
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo, lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    /*
    * 启用禁用用户状态
    *
    * */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        Long EmployeeId = (Long) request.getSession().getAttribute("employee");
       /* employee.setUpdateUser(EmployeeId);

        employee.setUpdateTime(LocalDateTime.now());*/

        employeeService.updateById(employee);


        return R.success("亲！状态修改成功 ");
    }

    //回显需要修改的参数
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable("id") Long id){
        log.info("根据id查询员工信息。。。");
        Employee emp = employeeService.getById(id);
        if (emp != null){
            return R.success(emp);
        }
        return R.error("暂无查询的员工信息");
    }






}
