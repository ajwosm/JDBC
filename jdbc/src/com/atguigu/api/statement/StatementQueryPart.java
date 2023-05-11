package com.atguigu.api.statement;

import com.mysql.cj.jdbc.Driver;

import java.sql.*;

/**
 * @author  cheng
 * <P>
 * Description: 使用statement查询t_user表下的全部数据
 */
public class StatementQueryPart {
    /**
     * TODO:
     *      DriverManager
     *      Connection
     *      Statement
     *      ResultSet
     */
    public static void main(String[] args) throws SQLException {


        //1.注册驱动
        /*

          TODO：
               注册驱动
               依赖:驱动版本 8+ com.mysql.cj.jdbc.Driver
                   驱动版本 5+ com.mysql.jdbc.Driver

         */
        DriverManager.registerDriver(new Driver());


        //2.获取连接
        /*
         * TODO:
         *      java程序要和数据库创建连接！
         *      java程序，连接数据库，肯定是调用某个方法，方法也需要填入连接数据库的基本信息
         *          数据库的ip地址 127.0.0.1
         *          数据库的端口号 3306
         *          账号 root
         *          密码 123456
         *          连接数据库的名称：sys
         */


        /*
        * 参数1：url
        *       jdbc:数据库厂商名://ip地址:port/数据库名
        *       jdbc:mysql://127.0.0.1:3306/sys
        * 参数2：数据库软件的账号 root
        * 参数3：数据库软件的密码 123456
        * */

        //java.sql 接口 = 实现类
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/sys","root","123456");


        //3.创建statement
        Statement statement = connection.createStatement();


        //4.发送sql语句，并且获取返回结果
        String sql = "select * from stu;";
        ResultSet resultSet = statement.executeQuery(sql);


        //5.进行结果解析
        //看看有没有下一行数据，有，你就可以获取
        while (resultSet.next()){
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            System.out.println(id+"--"+name);
        }


        //6.关闭资源
        resultSet.close();
        statement.close();
        connection.close();
    }
}
