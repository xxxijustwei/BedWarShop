package com.taylorswiftcn.megumi.bedwarshop.database.mysql;

import com.taylorswiftcn.megumi.bedwarshop.util.WeiUtil;

import java.sql.*;

public class MysqlHandler extends MegumiSQL {
    private final String hostname;
    private final String port;
    private final String database;
    private final String username;
    private final String password;
    private Connection connection;

    public MysqlHandler(String hostname, String port, String database, String username, String password) {
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.connection = null;
    }

    @Override
    public void openConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database + "?useSSL=false", this.username, this.password);
        } catch (SQLException e) {
            WeiUtil.log("连接数据库失败!");
            this.connection = null;
        } catch (ClassNotFoundException e) {
            WeiUtil.log("未找到JDBC驱动程序,连接数据库失败!");
            this.connection = null;
        }
    }

    @Override
    public boolean checkConnection() {
        return this.connection != null;
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void closeConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
                this.connection = null;
            } catch (SQLException e) {
                WeiUtil.log("关闭数据库连接失败");
                e.printStackTrace();
            }
        }
    }

    public ResultSet querySQL(String query) {
        if (!checkConnection()) return null;
        try {
            Statement stat = connection.createStatement();
            if (stat == null) return null;
            ResultSet set = stat.executeQuery(query);
            return set;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateSQL(String data) {
        if (!checkConnection()) return false;
        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate(data);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
