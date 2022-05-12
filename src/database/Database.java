package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.GatePass;
import model.GatePassItem;
import model.Item;
import model.Vendor;
import utility.Callback;
import utility.DataItemCallback;
import utility.DataListCallback;

import java.sql.*;
import java.util.List;

public class Database {
    ///Table Names
    public String ITEM_TABLE = "item";
    public String VENDOR_TABLE = "vendor";
    public String GATE_PASS_TABLE = "gate_pass";
    public String GATE_PASS_ITEM_TABLE = "gate_pass_item";

    public static Database instance;

    ///Initializing Database
    public static Database getInstance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    public Database() {
        createTable();
    }

    public void createTable() {
        createItemTable();
        createVendorTable();
        createGatePassTable();
        createGatePassItemTable();
    }

    public Connection conn;
    String url = "jdbc:sqlite:C:/sqlite/db/NHDB.db";

    public Connection getConnection() {
        String databaseName = "";
        String databaseUser = "";
        String databasePassword = "";

        conn = null;
        try {
            // db parameters
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");
            return conn;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

        }

        return null;

    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    ///Items Related Code
    public void createItemTable() {
        conn = this.connect();
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS \"" + ITEM_TABLE + "\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL,\n" +
                "\t\"name\"\tTEXT NOT NULL,\n" +
                "\t\"assemble\"\tBLOB,\n" +
                "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                ");";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            conn.close();
        } catch (SQLException e) {
            System.out.println("ERROR " + e.getMessage());
        }
    }

    public void insertItem(Item item, DataItemCallback callback) {
        String sql = "INSERT INTO " + ITEM_TABLE + "(name,assemble) VALUES(?,?)";
        conn = this.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, item.getName());
            pstmt.setBoolean(2, item.isAssemble());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int generatedKey = 0;
            if (rs.next()) {
                generatedKey = rs.getInt(1);
                item.setId(generatedKey);
            }
            conn.close();
            callback.OnSuccess(item);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            callback.OnFailed(e.getMessage());

        }
    }

    public void getAllItem(DataListCallback<Item> callback) {
        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<Item> _list = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM " + ITEM_TABLE;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            System.out.println("ALL USERS\n");
            while (rs.next()) {
                Item item = new Item(rs.getInt("id"), rs.getString("name"), rs.getBoolean("assemble"));
                _list.add(item);
            }

            callback.OnSuccess(_list);
        } catch (SQLException e) {
            callback.OnFailed(e.getMessage());
            System.out.println(e.toString());
        } finally {
            try {
                rs.close();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }


    }

    public Item getItemById(long id) {
        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Item item = null;
        try {
            String sql = "SELECT * FROM " + ITEM_TABLE + " WHERE id = " + id;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                item = new Item(rs.getInt("id"),rs.getString("name"),rs.getBoolean("assemble"));
            }

        } catch (SQLException e) {

            System.out.println(e.toString());
        } finally {
            try {
                rs.close();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        return item;

    }
    public void updateItem(Item item, DataItemCallback<Item> callback) {
        String sql = "UPDATE " + ITEM_TABLE + " SET name=?,assemble=? WHERE id=?";
        conn = this.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, item.getName());
            pstmt.setBoolean(2, item.isAssemble());
            pstmt.setLong(3, (int) item.getId());
            pstmt.executeUpdate();
            conn.close();
            callback.OnSuccess(item);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            callback.OnFailed(e.getMessage());

        }
    }

    public void deleteItem(Item item, DataItemCallback callback) {
        String sql = "DELETE FROM " + ITEM_TABLE + " WHERE id = ?";
        conn = this.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, item.getId());

            pstmt.executeUpdate();
            conn.close();
            callback.OnSuccess(item);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            callback.OnFailed(e.getMessage());
        }
    }


    ///Vendor Related Code
    public void createVendorTable() {
        conn = this.connect();
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS \"" + VENDOR_TABLE + "\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL,\n" +
                "\t\"name\"\tTEXT NOT NULL,\n" +
                "\t\"phone\"\tTEXT NOT NULL,\n" +
                "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                ");";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            conn.close();
        } catch (SQLException e) {
            System.out.println("ERROR " + e.getMessage());
        }
    }

    public void insertVendor(Vendor vendor, DataItemCallback callback) {
        String sql = "INSERT INTO " + VENDOR_TABLE + "(name,phone) VALUES(?,?)";
        conn = this.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, vendor.getName());
            pstmt.setString(2, vendor.getPhone());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int generatedKey = 0;
            if (rs.next()) {
                generatedKey = rs.getInt(1);
                vendor.setId(generatedKey);
            }
            conn.close();
            callback.OnSuccess(vendor);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            callback.OnFailed(e.getMessage());

        }
    }

    public void getAllVendor(DataListCallback<Vendor> callback) {
        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<Vendor> _list = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM " + VENDOR_TABLE;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Vendor vendor = new Vendor
                        (rs.getInt("id"), rs.getString("name"), rs.getString("phone"));
                _list.add(vendor);
            }

            callback.OnSuccess(_list);
        } catch (SQLException e) {
            callback.OnFailed(e.getMessage());
            System.out.println(e.toString());
        } finally {
            try {
                rs.close();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }
    public void updateVendor(Vendor vendor, DataItemCallback<Vendor> callback) {
        String sql = "UPDATE " + VENDOR_TABLE + " SET name=? WHERE id=?";
        conn = this.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, vendor.getName());
            pstmt.setLong(2, (int) vendor.getId());
            pstmt.executeUpdate();
            conn.close();
            callback.OnSuccess(vendor);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            callback.OnFailed(e.getMessage());

        }
    }

    public void deleteVendor(Vendor vendor, DataItemCallback callback) {
        String sql = "DELETE FROM " + VENDOR_TABLE + " WHERE id = ?";
        conn = this.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, vendor.getId());

            pstmt.executeUpdate();
            conn.close();
            callback.OnSuccess(vendor);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            callback.OnFailed(e.getMessage());
        }
    }

    public Vendor getVendorById(long id) {
        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Vendor vendor = null;
        try {
            String sql = "SELECT * FROM " + VENDOR_TABLE + " WHERE id = " + id;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                vendor = new Vendor(rs.getInt("id"),rs.getString("name"),rs.getString("phone"));
            }

        } catch (SQLException e) {

            System.out.println(e.toString());
        } finally {
            try {
                rs.close();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        return vendor;

    }
    //GatePass Related Code
    public void createGatePassTable() {
        conn = this.connect();
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS \"" + GATE_PASS_TABLE + "\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL,\n" +
                "\t\"vendorId\"\tINTEGER NOT NULL,\n" +
                "\t\"date\"\tTEXT NOT NULL,\n" +
                "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                ");";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            conn.close();
        } catch (SQLException e) {
            System.out.println("ERROR " + e.getMessage());
        }
    }

    public void insertGatePass(GatePass gatePass, DataItemCallback<GatePass> callback) {
        String sql = "INSERT INTO " + GATE_PASS_TABLE + "(vendorId,date) VALUES(?,?)";
        conn = this.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, gatePass.getVendorId());
            pstmt.setString(2, gatePass.getDate());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int generatedKey = 0;
            if (rs.next()) {
                generatedKey = rs.getInt(1);
                gatePass.setId(generatedKey);
            }
            conn.close();

            //get next key
            callback.OnSuccess(gatePass);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            callback.OnFailed(e.getMessage());

        }
    }

    public void getAllGatePass(DataListCallback<GatePass> callback) {
        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<GatePass> _list = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM " + GATE_PASS_TABLE;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                GatePass gatePass = new GatePass
                        (rs.getInt("id"), rs.getInt("vendorId"), rs.getString("date"));
                _list.add(gatePass);
            }

            callback.OnSuccess(_list);
        } catch (SQLException e) {
            callback.OnFailed(e.getMessage());
            System.out.println(e.toString());
        } finally {
            try {
                rs.close();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }


    }

    public GatePass getGatePassById(long id) {
        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        GatePass gatePass = null;
        try {
            String sql = "SELECT * FROM " + GATE_PASS_TABLE + " WHERE id = " + id;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                gatePass = new GatePass
                        (rs.getInt("id"), rs.getInt("vendorId"), rs.getString("date"));
            }

        } catch (SQLException e) {

            System.out.println(e.toString());
        } finally {
            try {
                rs.close();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        return gatePass;

    }

    public void updateGatePass(GatePass gatePass,DataItemCallback<GatePass> callback){
        System.out.println("Update");
        String sql = "UPDATE " + GATE_PASS_TABLE + " SET vendorId=? , date=? WHERE id=?";
        conn = this.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, gatePass.getVendorId());
            pstmt.setString(2, gatePass.getDate());
            pstmt.setLong(3, gatePass.getId());
            pstmt.executeUpdate();

            conn.close();

            callback.OnSuccess(gatePass);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            callback.OnFailed(e.getMessage());

        }
    }

    public void searchGatePass(String date,String vendorId,DataListCallback<GatePass> callback){

        String sql = "";
        if(date!=""&& vendorId!="")
            sql = "SELECT * FROM "+GATE_PASS_TABLE+" WHERE date = '"+date +"' AND vendorId = "+vendorId;
        else if(date!=""&& vendorId=="")
            sql = "SELECT * FROM "+GATE_PASS_TABLE+" WHERE date = '"+date+"'";
        else if(date==""&& vendorId!="")
            sql = "SELECT * FROM "+GATE_PASS_TABLE+" WHERE vendorId = "+vendorId;

        System.out.println(sql);

        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<GatePass> _list = FXCollections.observableArrayList();
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                GatePass gatePass = new GatePass
                        (rs.getInt("id"), rs.getInt("vendorId"), rs.getString("date"));
                _list.add(gatePass);
            }

            callback.OnSuccess(_list);
        } catch (SQLException e) {
            callback.OnFailed(e.getMessage());
            System.out.println(e.toString());
        } finally {
            try {
                rs.close();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }

    }

    ///GatePassItem Related Code
    public void createGatePassItemTable() {
        conn = this.connect();
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS \"" + GATE_PASS_ITEM_TABLE + "\" (\n" +
                "\t\"id\"\tINTEGER,\n" +
                "\t\"gatePassId\"\tINTEGER,\n" +
                "\t\"itemId\"\tINTEGER,\n" +
                "\t\"weight\"\tNUMERIC,\n" +
                "\t\"bardana\"\tNUMERIC,\n" +
                "\t\"price\"\tNUMERIC,\n" +
                "\t\"isIn1KG\"\tBLOB,\n" +
                "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                ");";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);

            conn.close();
        } catch (SQLException e) {
            System.out.println("ERROR " + e.getMessage());
        }
    }

    public void insertGatePassItem(GatePassItem gatePassItem, DataItemCallback callback) {
        String sql = "INSERT INTO " + GATE_PASS_ITEM_TABLE + "(gatePassId,itemId,weight,bardana,price,isIn1KG) VALUES(?,?,?,?,?,?)";
        conn = this.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, gatePassItem.getGatePassId());
            pstmt.setLong(2, gatePassItem.getItemId());
            pstmt.setDouble(3, gatePassItem.getWeight());
            pstmt.setDouble(4, gatePassItem.getBardana());
            pstmt.setDouble(5, gatePassItem.getPrice());
            pstmt.setBoolean(6, gatePassItem.isIn1KG());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int generatedKey = 0;
            if (rs.next()) {
                generatedKey = rs.getInt(1);
                gatePassItem.setId(generatedKey);
            }
            conn.close();
            //get next key
            callback.OnSuccess(gatePassItem);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            callback.OnFailed(e.getMessage());

        }
    }

    public void deleteGatePassItems(long gatePassId) {
        String sql = "DELETE FROM " + GATE_PASS_ITEM_TABLE + " WHERE gatePassId = ?";
        conn = this.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, gatePassId);

            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void getGatePassItemList(long gatePassId,DataListCallback<GatePassItem> callback) {
        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<GatePassItem> _list = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM " + GATE_PASS_ITEM_TABLE+" WHERE gatePassId = "+gatePassId;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                GatePassItem item = new GatePassItem(
                        rs.getLong("itemId"),
                        rs.getDouble("weight"),
                        rs.getDouble("bardana"),
                        rs.getDouble("bardana"),
                        rs.getBoolean("isIn1KG")
                );
                _list.add(item);
            }

            callback.OnSuccess(_list);
        } catch (SQLException e) {
            callback.OnFailed(e.getMessage());
            System.out.println(e.toString());
        } finally {
            try {
                rs.close();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }


    }

}














