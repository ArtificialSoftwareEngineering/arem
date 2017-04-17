package biorimp.storage.repositories;

import biorimp.storage.connection.ConnectionJDBC;
import biorimp.storage.connection.ConnectionSQLite;
import biorimp.storage.entities.Entity;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Created by developer on 12/29/15.
 */
public abstract class RepositorySqlite<T extends Entity> {
    protected Connection connection;

    protected void getConnection() {
        try {
            connection = ConnectionSQLite.getInstance().connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract T resultEntity(ResultSet resultSet);

}
