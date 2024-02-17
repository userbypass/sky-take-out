package com.sky.mapper;

import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @Insert("insert into employee (name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user) " +
            "values (#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser}) ")
    void insert(Employee employee);
    /**
     * @Description 根据员工姓名分页查询员工信息
     * @Date 2024/2/17 17:43
     * @Param [name]
     * @return java.util.List<com.sky.entity.Employee>
     */
    List<Employee> searchEmpList (String name);
    void update(Employee employee);
}
