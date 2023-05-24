package br.com.srgenex.utils.database;

import br.com.srgenex.utils.GXUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.ConfigurationSection;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.io.File;
import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SuppressWarnings("unused")
@Getter
public class SQL {

    private Connection connection;
    private final ExecutorService executor;
    @Getter
    public static SQL instance;

    @SneakyThrows
    public SQL() {
        instance = this;
        this.executor = Executors.newCachedThreadPool();
    }

    public SQL load(File data) throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:sqlite://" + data.getAbsolutePath() + "/database.db");
        } catch (Exception e) {
            e.printStackTrace();
            GXUtils.getInstance().getLogger().severe("No MySQL driver found.");
        }
        return this;
    }

    public SQL load(ConfigurationSection section) {
        String host = section.getString("host");
        String user = section.getString("user");
        String password = section.getString("password");
        String database = section.getString("database");
        int port = section.getInt("port");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", user, password);
        } catch (Exception e) {
            e.printStackTrace();
            GXUtils.getInstance().getLogger().severe("No MySQL driver found.");
        }
        return this;
    }

    @SneakyThrows
    public void update(String sql, Object... vars) {
        PreparedStatement ps = prepareStatement(sql, vars);
        ps.execute();
        ps.close();
    }

    public void execute(String sql, Object... vars) {
        executor.execute(() -> update(sql, vars));
    }

    @SneakyThrows
    public PreparedStatement prepareStatement(String query, Object... vars) {
        PreparedStatement ps = getConnection().prepareStatement(query);
        for (int i = 0; i < vars.length; i++)
            ps.setObject(i + 1, vars[i]);
        return ps;
    }

    @SneakyThrows
    public CachedRowSet query(String query, Object... vars) {
        CachedRowSet rowSet = null;
        Future<CachedRowSet> future = executor.submit(() -> {
            PreparedStatement ps = prepareStatement(query, vars);

            ResultSet rs = ps.executeQuery();
            CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(rs);
            rs.close();
            ps.close();

            if (crs.next())
                return crs;

            return null;
        });

        if (future.get() != null) rowSet = future.get();

        return rowSet;
    }

    public void createTable(String table, String columns) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            Statement stm = connection.createStatement();
            stm.execute("CREATE TABLE IF NOT EXISTS " + table + " (" + columns + ");");
        }
    }

}