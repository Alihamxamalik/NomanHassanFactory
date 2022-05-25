package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import utility.ActionCallback;
import utility.Callback;
import utility.DataItemCallback;
import utility.DataListCallback;

import java.sql.*;

public class Database {
    ///Table Names
    public String ITEM_TABLE = "item";
    public String VENDOR_TABLE = "vendor";
    public String CUSTOMER_TABLE = "customer";
    public String GATE_PASS_TABLE = "gate_pass";
    public String GATE_PASS_ITEM_TABLE = "gate_pass_item";
    public String SALES_TABLE = "sales";
    public String SALES_ITEM_TABLE = "sales_item";
    public String PRODUCTION_TABLE = "production";
    public String PRODUCTION_ITEM_TABLE = "production_item";

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
        createCustomerTable();
        createGatePassTable();
        createGatePassItemTable();
        createSalesTable();
        createSalesItemTable();
        createProductionTable();
        createProductionItemTable();
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
                item = new Item(rs.getInt("id"), rs.getString("name"), rs.getBoolean("assemble"));
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
    public void createCustomerTable() {
        conn = this.connect();
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS \"" + CUSTOMER_TABLE + "\" (\n" +
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

    public void insertCustomer(Customer customer, DataItemCallback callback) {
        String sql = "INSERT INTO " + CUSTOMER_TABLE + "(name,phone) VALUES(?,?)";
        conn = this.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getPhone());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int generatedKey = 0;
            if (rs.next()) {
                generatedKey = rs.getInt(1);
                customer.setId(generatedKey);
            }
            conn.close();
            callback.OnSuccess(customer);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            callback.OnFailed(e.getMessage());

        }
    }

    public void getAllCustomer(DataListCallback<Customer> callback) {
        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<Customer> _list = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM " + CUSTOMER_TABLE;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer
                        (rs.getInt("id"), rs.getString("name"), rs.getString("phone"));
                _list.add(customer);
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

    public void updateCustomer(Customer customer, DataItemCallback<Customer> callback) {
        String sql = "UPDATE " + VENDOR_TABLE + " SET name=? WHERE id=?";
        conn = this.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, customer.getName());
            pstmt.setLong(2, (int) customer.getId());
            pstmt.executeUpdate();
            conn.close();
            callback.OnSuccess(customer);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            callback.OnFailed(e.getMessage());

        }
    }

    public void deleteCustomer(Customer customer, DataItemCallback<Customer> callback) {
        String sql = "DELETE FROM " + CUSTOMER_TABLE + " WHERE id = ?";
        conn = this.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, customer.getId());

            pstmt.executeUpdate();
            conn.close();
            callback.OnSuccess(customer);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            callback.OnFailed(e.getMessage());
        }
    }

    public Customer getCustomerById(long id) {
        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Customer customer = null;
        try {
            String sql = "SELECT * FROM " + CUSTOMER_TABLE + " WHERE id = " + id;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                customer = new Customer(rs.getInt("id"), rs.getString("name"), rs.getString("phone"));
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
        return customer;

    }

    ///Customer Related Code
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
                vendor = new Vendor(rs.getInt("id"), rs.getString("name"), rs.getString("phone"));
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

    public void updateGatePass(GatePass gatePass, DataItemCallback<GatePass> callback) {
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

    public void searchGatePass(String date, String vendorId, DataListCallback<GatePass> callback) {

        String sql = "";
        if (date != "" && vendorId != "")
            sql = "SELECT * FROM " + GATE_PASS_TABLE + " WHERE date = '" + date + "' AND vendorId = " + vendorId;
        else if (date != "" && vendorId == "")
            sql = "SELECT * FROM " + GATE_PASS_TABLE + " WHERE date = '" + date + "'";
        else if (date == "" && vendorId != "")
            sql = "SELECT * FROM " + GATE_PASS_TABLE + " WHERE vendorId = " + vendorId;

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

    public void insertGatePassItem(ObservableList<GatePassItem> list, Callback callback) {

        String sql = "INSERT INTO " + GATE_PASS_ITEM_TABLE + " (gatePassId,itemId,weight,bardana,price,isIn1KG) VALUES(?,?,?,?,?,?)";
        for (GatePassItem gatePassItem : list) {
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
                conn.close();
                //get next key
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                callback.OnFailed(e.getMessage());
                return;
            }
        }
        callback.OnSuccess();

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

    public void getGatePassItemList(long gatePassId, DataListCallback<GatePassItem> callback) {
        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<GatePassItem> _list = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM " + GATE_PASS_ITEM_TABLE + " WHERE gatePassId = " + gatePassId;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                GatePassItem item = new GatePassItem(
                        rs.getLong("gatePassId"),
                        rs.getLong("itemId"),
                        rs.getDouble("weight"),
                        rs.getDouble("bardana"),
                        rs.getDouble("price"),
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

    public void getGatePassItemListByItemId(long itemId, DataListCallback<GatePassItem> callback) {
        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<GatePassItem> _list = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM " + GATE_PASS_ITEM_TABLE + " WHERE itemId = " + itemId;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                GatePassItem item = new GatePassItem(
                        rs.getLong("itemId"),
                        rs.getDouble("weight"),
                        rs.getDouble("bardana"),
                        rs.getDouble("price"),
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


    //Sales Related Code
    public void createSalesTable() {
        conn = this.connect();
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS \"" + SALES_TABLE + "\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL,\n" +
                "\t\"customerId\"\tINTEGER NOT NULL,\n" +
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

    public void insertSales(Sales sales, DataItemCallback<Sales> callback) {
        String sql = "INSERT INTO " + SALES_TABLE + "(customerId,date) VALUES(?,?)";
        conn = this.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, sales.getCustomerId());
            pstmt.setString(2, sales.getDate());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int generatedKey = 0;
            if (rs.next()) {
                generatedKey = rs.getInt(1);
                sales.setId(generatedKey);
            }
            conn.close();

            //get next key
            callback.OnSuccess(sales);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            callback.OnFailed(e.getMessage());

        }
    }

    public void getAllSales(DataListCallback<Sales> callback) {
        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<Sales> _list = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM " + SALES_TABLE;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Sales sales = new Sales
                        (rs.getInt("id"), rs.getInt("customerId"), rs.getString("date"));
                _list.add(sales);
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

    public Sales getSalesById(long id) {
        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Sales sales = null;
        try {
            String sql = "SELECT * FROM " + SALES_TABLE + " WHERE id = " + id;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                sales = new Sales
                        (rs.getInt("id"), rs.getInt("customerId"), rs.getString("date"));
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
        return sales;

    }

    public void updateSales(Sales sales, DataItemCallback<Sales> callback) {
        System.out.println("Update");
        String sql = "UPDATE " + SALES_TABLE + " SET customerId=? , date=? WHERE id=?";
        conn = this.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, sales.getCustomerId());
            pstmt.setString(2, sales.getDate());
            pstmt.setLong(3, sales.getId());
            pstmt.executeUpdate();

            conn.close();

            callback.OnSuccess(sales);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            callback.OnFailed(e.getMessage());

        }
    }

    public void searchSales(String date, String customerId, DataListCallback<Sales> callback) {

        String sql = "";
        if (date != "" && customerId != "")
            sql = "SELECT * FROM " + SALES_TABLE + " WHERE date = '" + date + "' AND customerId = " + customerId;
        else if (date != "" && customerId == "")
            sql = "SELECT * FROM " + SALES_TABLE + " WHERE date = '" + date + "'";
        else if (date == "" && customerId != "")
            sql = "SELECT * FROM " + SALES_TABLE + " WHERE customerId = " + customerId;

        System.out.println(sql);

        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<Sales> _list = FXCollections.observableArrayList();
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Sales sales = new Sales
                        (rs.getInt("id"), rs.getInt("customerId"), rs.getString("date"));
                _list.add(sales);
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

    ///SalesItem Related Code
    public void createSalesItemTable() {
        conn = this.connect();
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS \"" + SALES_ITEM_TABLE + "\" (\n" +
                "\t\"id\"\tINTEGER,\n" +
                "\t\"salesId\"\tINTEGER,\n" +
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

    public void insertSalesItem(SalesItem salesItem, DataItemCallback<SalesItem> callback) {
        String sql = "INSERT INTO " + SALES_ITEM_TABLE + "(salesId,itemId,weight,bardana,price,isIn1KG) VALUES(?,?,?,?,?,?)";
        conn = this.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, salesItem.getSalesId());
            pstmt.setLong(2, salesItem.getItemId());
            pstmt.setDouble(3, salesItem.getWeight());
            pstmt.setDouble(4, salesItem.getBardana());
            pstmt.setDouble(5, salesItem.getPrice());
            pstmt.setBoolean(6, salesItem.isIn1KG());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int generatedKey = 0;
            if (rs.next()) {
                generatedKey = rs.getInt(1);
                salesItem.setId(generatedKey);
            }
            conn.close();
            //get next key
            callback.OnSuccess(salesItem);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            callback.OnFailed(e.getMessage());

        }
    }

    public void deleteSalesItems(long salesId) {
        String sql = "DELETE FROM " + SALES_ITEM_TABLE + " WHERE salesId = ?";
        conn = this.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, salesId);

            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getSalesItemList(long salesId, DataListCallback<SalesItem> callback) {
        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<SalesItem> _list = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM " + SALES_ITEM_TABLE + " WHERE salesId = " + salesId;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                SalesItem item = new SalesItem(
                        rs.getLong("salesId"),
                        rs.getLong("itemId"),
                        rs.getDouble("weight"),
                        rs.getDouble("bardana"),
                        rs.getDouble("price"),
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

    public void getSalesItemListByItemId(long itemId, DataListCallback<SalesItem> callback) {
        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<SalesItem> _list = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM " + SALES_ITEM_TABLE + " WHERE itemId = " + itemId;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                SalesItem item = new SalesItem(
                        rs.getLong("salesId"),
                        rs.getLong("itemId"),
                        rs.getDouble("weight"),
                        rs.getDouble("bardana"),
                        rs.getDouble("price"),
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


    //Production
    public void createProductionTable() {
        conn = this.connect();
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS \"" + PRODUCTION_TABLE + "\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL,\n" +
                "\t\"itemId\"\tINTEGER NOT NULL,\n" +
                "\t\"weight\"\tNUMERIC NOT NULL,\n" +
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

    public void insertProduction(Production production, DataItemCallback<Production> callback) {
        String sql = "INSERT INTO " + PRODUCTION_TABLE + "(itemId,date,weight) VALUES(?,?,?)";
        conn = this.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, production.getItemId());
            pstmt.setString(2, production.getDate());
            pstmt.setDouble(3, production.getWeight());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int generatedKey = 0;
            if (rs.next()) {
                generatedKey = rs.getInt(1);
                production.setId(generatedKey);
            }
            conn.close();

            //get next key
            callback.OnSuccess(production);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            callback.OnFailed(e.getMessage());

        }
    }

    public void updateProduction(Production production, DataItemCallback<Production> callback) {
        String sql = "UPDATE " + PRODUCTION_TABLE + " SET itemId=? , date=? , weight =? WHERE id =?";
        System.out.println(sql);

        conn = this.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, production.getItemId());
            pstmt.setString(2, production.getDate());
            pstmt.setDouble(3, production.getWeight());
            pstmt.setDouble(4, production.getId());
            pstmt.executeUpdate();

            conn.close();

            callback.OnSuccess(production);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            callback.OnFailed(e.getMessage());

        }
    }

    public void getAllProduction(DataListCallback<Production> callback) {
        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<Production> _list = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM " + PRODUCTION_TABLE;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Production production = new Production
                        (rs.getLong("id"), rs.getLong("itemId"), rs.getDouble("weight"), rs.getString("date"));
                _list.add(production);
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

    public void getAllProductionByItemId(long itemId, DataListCallback<Production> callback) {
        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<Production> _list = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM " + PRODUCTION_TABLE + " WHERE itemId = " + itemId;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Production production = new Production
                        (rs.getLong("id"), rs.getLong("itemId"), rs.getDouble("weight"), rs.getString("date"));
                _list.add(production);
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

    public void searchProduction(String date, String itemId, DataListCallback<Production> callback) {

        String sql = "";
        if (date != "" && itemId != "")
            sql = "SELECT * FROM " + PRODUCTION_TABLE + " WHERE date = '" + date + "' AND itemId = " + itemId;
        else if (date != "" && itemId == "")
            sql = "SELECT * FROM " + PRODUCTION_TABLE + " WHERE date = '" + date + "'";
        else if (date == "" && itemId != "")
            sql = "SELECT * FROM " + PRODUCTION_TABLE + " WHERE itemId = " + itemId;

        System.out.println(sql);

        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<Production> _list = FXCollections.observableArrayList();
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Production production = new Production
                        (rs.getLong("id"), rs.getLong("itemId"), rs.getDouble("weight"), rs.getString("date"));
                _list.add(production);
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

    public Production getProductionById(long id) {
        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Production production = null;
        try {
            String sql = "SELECT * FROM " + PRODUCTION_TABLE + " WHERE id = " + id;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                production = new Production
                        (rs.getLong("id"), rs.getLong("itemId"), rs.getDouble("weight"), rs.getString("date"));
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
        return production;
    }

    //Production Item
    public void createProductionItemTable() {
        conn = this.connect();
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS \"" + PRODUCTION_ITEM_TABLE + "\" (\n" +
                "\t\"id\"\tINTEGER,\n" +
                "\t\"productionId\"\tINTEGER,\n" +
                "\t\"itemId\"\tINTEGER,\n" +
                "\t\"weight\"\tNUMERIC,\n" +
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

    public void insertProductionItem(ObservableList<ProductionItem> productionItems, long productionId, ActionCallback callback) {
        String sql = "INSERT INTO " + PRODUCTION_ITEM_TABLE + "(productionId,itemId,weight) VALUES(?,?,?)";

        for (ProductionItem item : productionItems) {
            conn = this.connect();

            try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setLong(1, productionId);
                pstmt.setLong(2, item.getItemId());
                pstmt.setDouble(3, item.getWeight());
                pstmt.executeUpdate();
//                ResultSet rs = pstmt.getGeneratedKeys();
//                int generatedKey = 0;
//                if (rs.next()) {
//                    generatedKey = rs.getInt(1);
//                    productionItem.setId(generatedKey);
//                }
                conn.close();
                //get next key
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                callback.OnCancel();

            }
        }
        callback.OnAction();
    }

    public void getProductionItemListByItemId(long itemId, DataListCallback<ProductionItem> callback) {
        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<ProductionItem> _list = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM " + PRODUCTION_ITEM_TABLE + " WHERE itemId = " + itemId;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ProductionItem pi = new ProductionItem(
                        rs.getLong("id"),
                        rs.getLong("itemId"),
                        rs.getLong("productionId"),
                        rs.getDouble("weight"));
                _list.add(pi);
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
    public void getProductionItemListByProductionId(long productionId, DataListCallback<ProductionItem> callback) {
        conn = connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<ProductionItem> _list = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM " + PRODUCTION_ITEM_TABLE + " WHERE productionId = " + productionId;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ProductionItem pi = new ProductionItem(
                        rs.getLong("id"),
                        rs.getLong("itemId"),
                        rs.getLong("productionId"),
                        rs.getDouble("weight"));
                _list.add(pi);
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

    public void deleteProductionItems(long productionId) {
        String sql = "DELETE FROM " + PRODUCTION_ITEM_TABLE + " WHERE productionId = ?";
        conn = this.connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, productionId);

            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}














