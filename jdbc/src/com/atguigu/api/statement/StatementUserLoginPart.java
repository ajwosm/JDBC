package com.atguigu.api.statement;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author cheng
 * Description: 模拟用户登录
 * <p>
 * TODO：
 *     <ul>
 *     <li>1.明确jdbc的使用流程 和 详细讲解内部设计api方法</li>
 *     <li>2.发现问题，引出preparedStatement</li>
 *     <ul/>
 *
 * <p>
 * TODO：
 *  <ol>
 *  <li>输入账号和密码</li>
 *  <li>进行数据库信息查询（sys）</li>
 *  <li>反馈登录成功还是登录失败</li>
 *  </ol>
 * <p>
 * TODO：
 *  <ul>
 *  <li>1.键盘输入时间收集账号和密码信息</li>
 *  <li>2.注册驱动</li>
 *  <li>3.获取连接</li>
 *  <li>4.创建statement</li>
 *  <li>5.发送sql语句，并获取返回结果</li>
 *  <li>6.结果判断，显示登录成功或失败</li>
 *  <li>7.关闭资源</li>
 *  </ul>
 */
public class StatementUserLoginPart {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        //1.获取用户输入信息
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入账号");
        String account = scanner.nextLine();
        System.out.println("请输入密码");
        String password = scanner.nextLine();

        //2.注册驱动
        /*
         * 方案1:
         *      DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver())
         *      注意：8+ com.mysql.cj.jdbc.Driver
         *           5+ com.mysql.jdbc.Driver
         *      问题：注册两次驱动
         *          1.DriverManager.registerDriver()
         *          2.Driver.static{ DriverManager.registerDriver() } 静态代码块，也会注册一次
         *      解决: 只想注册一次驱动
         *          只触发静态代码快即可！ Driver
         *      触发静态代码块：
         *          类加载机制：类加载的时候，会触发静态代码块
         *          加载[class文件->jvm虚拟机的class对象]
         *          连接[验证（检查文件类型）-> 准备（静态变量默认值）-> 解析（触发静态代码块）]
         *          初始化（静态属性赋初始值）
         *      触发类加载：
         *          1.new 关键字
         *          2.调用静态方法
         *          3.调用静态属性
         *          4.接口 1.8 defaut默认实现
         *          5.反射
         *          6.子类触发父类
         *          7.程序的入口main
         *
         *
         */
        //方案1：
//        DriverManager.registerDriver(new Driver());
        //方案2：mysql新版本|换成oracle
//        new Driver();
        //字符串->提取到外部的配置文件->可以在不改变代码的情况下，完成数据库驱动的切换
        Class.forName("com.mysql.cj.jdbc.Driver");//触发类加载，触发静态代码块的调用

        //2.获取数据库连接
        /*
        * getConnection(1,2,3)方法，是一个重载方法
        * 允许开发者，用不同的形式传入数据库连接的核心参数
        *
        * 核心属性：
        *   1.数据库软件所在的主机ip地址： localhost | 127.0.0.1
        *   2.数据库软件所在的主机的端口号： 3306
        *   3.连接的具体库：sys
        *   4.连接的账号：root
        *   5.连接的密码：123456
        *   6.可选的信息：无
        *
        * 三个参数：
        *   String url        数据库软件所在的信息，连接的具体库，以及其他可选的信息
        *                     语法：  jdbc：数据库管理软件的名称[mysql，oracle]://ip地址|主机名:port端口号/数据库名?key=value
        *                     具体：jdbc:mysql://127.0.0.1:3306/sys
        *                          jdbc:mysql://localhost:3306/sys
        *                     本机的省略写法：如果你的数据库软件安装到本机，可以进行一些省略
        *                          jdbc:mysql://127.0.0.1:3306/sys = jdbc:mysql:///sys
        *                          省略了[本机地址]和[3306默认端口号]
        *                          强调：必须是本机并且端口号是3306，方可省略 使用///
        *   String user       数据库账号 root
        *   String password   数据库密码 123456
        * 二个参数：
        *   String url     : 此url和三个参数url的作用一样，数据库ip，端口号，具体的数据库和可选信息
        *   Properties info: 存储账号和密码
        *                    Properties 类似于Map 只不过key = value 都是字符串形式的
        *                    key user ：账号信息
        *                    key password ：密码信息
        * 一个参数：
        *   String url     : 数据库ip，端口号，具体的数据库   可选信息（账号密码）
        *                    jdbc:数据库软件名://ip:port/数据库?key=value&key=value
        *
        *                    jdbc:mysql://localhost:3306/sys?user=root&password=123456
        *                    携带固定的参数名 user password 传递账号和密码信息！
        *
        * */
        Connection connection = DriverManager.getConnection("jdbc:mysql:///sys","root","123456");

        Properties info = new Properties();
        info.put("user","root");
        info.put("password","123456");
        Connection connection1 = DriverManager.getConnection("jdbc:mysql:///sys",info);

        Connection connection2 = DriverManager.getConnection("jdbc:mysql:///sys?user=root&password=123456");

        //3.创建发送sql语句的statement对象
        //statement 可以发送sql语句到数据库，并且获取返回结果
        Statement statement = connection2.createStatement();

        //4.发送sql语句（1.编写sql语句2.发送sql语句）
        String sql = "select * from t_user where account = '"+account+"' and password = '"+password+"';";

        statement.executeUpdate(sql);
        /*
        * SQL分类：DDL(容器创建，修改，删除) DML（插入，修改，删除） DCL（查询） DCL（权限控制） TPL（事务控制语言）
        *
        * 参数：sql 非DQL
        * 返回：int
        *           情况1：DML 返回影响的行数，例如：删除了三条数据 return 3；插入了俩条 return 2；修改了0条 return 0；
        *           情况2：非DML return 0
        * int row = executeUpdate(sql)
        *
        * 参数：sql DQL
        * 返回 resultSet 结果封装对象
        * ResultSet resultSet = executeQuery(sql);
        *
        * */
        ResultSet resultSet = statement.executeQuery(sql);

        //5.查询结果集解析 resultSet
        /*
        * Java是一种面向对象的思维，将查询结果封装成了resultSet对象，我们应该理解，内部一定也是有行和列的
        *
        * resultSet -> 逐行获取数据，行->行的列的数据
        *  A ResultSet object maintains a cursor pointing to its current row
        *  of data. Initially the cursor is positioned before the first row.
        * The next method moves the cursor to the next row, and because it
        * returns false when there are no more rows in the ResultSet object,
        * it can be used in a while loop to iterate through the result set.
        *
        * 想要进行数据解析，我们需要进行两件事情：1.移动游标指定获取数据行2.获取指定数据行的列数据即可
        *
        * 1.游标移动问题
        *   resultSet内部包含一个游标，指定当前行数据
        *   默认游标指定的是第一行数据之前
        *   我们可以调用next方法向后移动一行游标
        *   如果我们有很多行数据，我们可以使用while(next){获取每一行的数据}
        *
        *   boolean = next() true :有更多的数据，并且向下移动一行
        *                    false:没有更多的数据，不一定
        *
        * TODO: 移动光标的方法有很多，只需要记next即可，配合while循环获取全部数据
        *
        *
        * 2.获取列的数据问题（获取光标指定的行的数据）
        *
        *   resultSet.get类型(String columnLabel | int columnIndex);
        *       columnLabel: 列名 如果有别名 写别名   select * | (id,account.password,nickname)
        *
        *       columnIndex:列的下角标获取 从左向右 从1 开始
        *
        * */
        while(resultSet.next()){
            //指定当前行
            int id = resultSet.getInt(1);
            String account1 = resultSet.getString("account");
            String password1 = resultSet.getString(3);
            String nickname = resultSet.getString("nickname");
            System.out.println(id+"--"+account1+"--"+password1+"--"+nickname);
        }

    }
}
