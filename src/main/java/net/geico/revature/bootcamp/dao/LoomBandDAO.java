package net.geico.revature.bootcamp.dao;

import net.geico.revature.bootcamp.model.LoomBands;
import net.geico.revature.bootcamp.model.LoomBandsSeller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoomBandDAO {


    Connection conn;

    public LoomBandDAO(Connection conn) {
        this.conn = conn;
        System.out.println("In LoomBandSellerDAO Class Constructor");


    }

    public LoomBands addLoomBands(LoomBands loomBands) throws Exception {
        System.out.println("In LoomBandDAO Class addLoomBands() Method and Adding Loom Band to Table");
        System.out.println("==================================================================================================================");
        try {
            PreparedStatement ps = conn.prepareStatement("insert into " +
                    "LOOMBANDS (loomId, loomName, loomPrice, loomSellerName) values (?, ?, ?, ?)");
            ps.setInt(1, loomBands.getLoomId());
            ps.setString(2, loomBands.getLoomName());
            ps.setFloat(3, loomBands.getLoomPrice());
            ps.setString(4, loomBands.getLoomSellerName());
            System.out.println(loomBands.getLoomId() + "," + loomBands.getLoomName() + "," + loomBands.getLoomPrice() + "," + loomBands.getLoomSellerName());
            ps.executeUpdate();
        } catch (
                SQLException e) {
            e.printStackTrace();
        }

        return loomBands;
    }


    public List<LoomBands> getLoomBandsList() {
        System.out.println("In LoomBandSellerDAO Class getLoomBandSellerList() Method and Getting all Loom Band Suppliers from Table");
        System.out.println("==================================================================================================================");
        List<LoomBands> loomBandsList = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("select * from LOOMBANDS");
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {


                int loomId = resultSet.getInt("loomId");
                String loomName = resultSet.getString("loomName");
                float loomPrice = resultSet.getFloat("loomPrice");
                String loomSellerName = resultSet.getString("loomSellerName");

                //System.out.println(loomBandSellerId + "," + loomBandSellerName);
                LoomBands a = new LoomBands(loomId, loomName, loomPrice, loomSellerName);

                loomBandsList.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return loomBandsList;

    }


    public LoomBands getLoomBandById(int id) throws SQLException {
        System.out.println("In LoomBandSellerDAO Class getLoomBandSellerListById() Method and Getting All Loom Band Sellers by Id from Table");
        System.out.println("==================================================================================================================");
        List<LoomBands> loomBandsList = new ArrayList<>();

            PreparedStatement ps = conn.prepareStatement("select * from LOOMBANDS WHERE loomId = ?");
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int loomId = resultSet.getInt("loomId");
                String loomName = resultSet.getString("loomName");
                float loomPrice = resultSet.getFloat("loomPrice");
                String loomSellerName = resultSet.getString("loomSellerName");
                //System.out.println(loomBandSellerId + "," + loomBandSellerName);
                LoomBands a = new LoomBands(loomId, loomName, loomPrice, loomSellerName);
                loomBandsList.add(a);

            }

        if (loomBandsList.isEmpty()) {
            return null;
        } else {
            return loomBandsList.get(0);
        }

    }

    public String deleteLoomBandById(int id) {
        System.out.println("In LoomBandSellerDAO Class getLoomBandSellerListById() Method and Getting All Loom Band Sellers by Id from Table");
        System.out.println("==================================================================================================================");
        List<LoomBands> loomBandsList = new ArrayList<>();

        try {
            PreparedStatement ps = conn.prepareStatement("select * from LOOMBANDS WHERE loomId = ?");
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                PreparedStatement ps1 = conn.prepareStatement("delete from LOOMBANDS WHERE loomId = ?");
                ps1.setInt(1, id);
                ps1.executeUpdate();
                return "Delete Successful";
            } else {
                return "Loom ID doesn't exist in the table";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }


    //-----
    public String updateLoomBands(int id, LoomBands loomBands) {
        System.out.println("In LoomBandSellerDAO Class getLoomBandSellerListById() Method and Getting All Loom Band Sellers by Id from Table");
        System.out.println("==================================================================================================================");
        List<LoomBands> loomBandsList = new ArrayList<>();

        try {
            PreparedStatement ps = conn.prepareStatement("select * from LOOMBANDS WHERE loomId = ?");
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                PreparedStatement ps1 = conn.prepareStatement("update LOOMBANDS set loomName = ? , loomPrice = ? WHERE loomId = ?");
                ps1.setString(1, loomBands.getLoomName());
                ps1.setFloat(2, loomBands.getLoomPrice());
                ps1.setInt(3,id);
                ps1.executeUpdate();
                return "Update Successful";
            } else {
                return "Loom ID doesn't exist in the table";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }



}