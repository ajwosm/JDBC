package com.atguigu.api.preparedstatement;

import java.sql.*;
import java.util.Scanner;

/**
 * @author cheng
 * Description:使用预编译statement完成用户登录
 * TODO: 防止注入攻击  |  演示PS的使用流程
 */
public class PSUserLoginPart {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //1.收集用户信息
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入账号");
        String account = scanner.nextLine();
        System.out.println("请输入密码");
        String password = scanner.nextLine();

        //2.ps数据库流程
        //1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        //2.获取连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///sys?user=root&password=123456");
        /*
        * statement
        *   1.创建statement
        *   2.拼接SQL语句
        *   3.发送SQL语句，并且获取返回结果
        *
        * prepared statement
        *   1.编写SQL语句  不包括动态值部分的语句，动态值部分使用占位符? 代替  ? 只能代替动态值
        *   2.创建prepared statement,并且传入动态值
        *   3.动态值 占位符 赋值 ? 单独赋值即可
        *   4.发送SQL语句，并且返回结果
        *
        * */
        //3.编写SQL语句结构
        String sql = "select * from t_user where account= ? and password= ? ;";

        //4.创建预编译statement并且设置SQL语句结果
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //5.单独占位值进行赋值
        /*
        * 参数1 index 占位符的位置 从左到右 从1 开始
        * 参数2 object 占位符的值 可以设置任何类型的数据，避免了我们拼接和类型更加丰富
        *
        * */

        preparedStatement.setString(1,account);
        preparedStatement.setString(2,password);

        //6.发送SQL语句，并且获取返回结果
        //statement.executeUpdate  |  executeQuery(String sql);
        //prepareStatement.executeUpdate   | executeQuery(); TODO:因为它已经知道语句，知道语句动态值！
        ResultSet resultSet = preparedStatement.executeQuery();


        //7.结果集解析
        if(resultSet.next()){
            System.out.println("登录成功");
        }else{
            System.out.println("登录失败");
        }

        //8.释放资源
        resultSet.close();
        preparedStatement.close();
        connection.close();

    }

}
