package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface EmployeeMapper {
    /**
     * 根据用户名查询员工
     *
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * @return void
     * @Description 新增员工
     * @Date 2024/2/18 17:52
     * @Param [employee]
     */
    @Insert("insert into employee (name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user) " +
            "values (#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser}) ")
    @AutoFill(OperationType.INSERT)
    void insert(Employee employee);

    /**
     * @return java.util.List<com.sky.entity.Employee>
     * @Description 根据员工姓名分页查询员工信息
     * @Date 2024/2/17 17:43
     * @Param [name]
     */
    List<Employee> searchEmpList(String name);

    /**
     * @return void
     * @Description 更新员工信息
     * @Date 2024/2/18 17:52
     * @Param [employee]
     */
    @AutoFill(OperationType.UPDATE)
    void update(Employee employee);

    /**
     * @return com.sky.entity.Employee
     * @Description 根据ID查询员工信息
     * @Date 2024/2/18 17:53
     * @Param [id]
     */
    @Select("select  * from employee where id = #{id}")
    Employee search(String id);
}
