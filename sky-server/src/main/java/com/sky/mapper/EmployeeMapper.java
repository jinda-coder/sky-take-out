package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
    @AutoFill
    @Insert("insert into employee (name,username," +
            "password,phone," +
            "sex,id_number," +
            "status,create_time," +
            "update_time,create_user," +
            "update_user)" +
            "values (#{name},#{username}," +
            "#{password},#{phone}," +
            "#{sex},#{idNumber}," +
            "#{status},#{createTime}," +
            "#{updateTime},#{createUser},#{updateUser})")
    void save(Employee employee);
    @Select("select * from employee")
    List<Employee> select(EmployeePageQueryDTO dto);

    List<Employee> selectByName(EmployeePageQueryDTO dto);
    @AutoFill(value = OperationType.UPDATE)
    void disable(Employee employee);

    Employee selectById(Integer id);

}
