package com.taylorswiftcn.megumi.bedwarshop.database.mysql;

import java.sql.Connection;

public abstract class MegumiSQL {
    public abstract void openConnection();

    public abstract boolean checkConnection();

    public abstract Connection getConnection();

    public abstract void closeConnection();
}
