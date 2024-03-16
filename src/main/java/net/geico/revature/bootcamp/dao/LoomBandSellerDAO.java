package net.geico.revature.bootcamp.dao;

import net.geico.revature.bootcamp.model.LoomBandsSeller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoomBandSellerDAO {
    Connection conn;

    public LoomBandSellerDAO(Connection conn) {
        this.conn = conn;
        System.out.println("In LoomBandSellerDAO Class Constructor");
    }

    public LoomBandsSeller addLoomBandsSeller(LoomBandsSeller a) throws SQLException {
        System.out.println("In LoomBandSellerDAO Class addLoomBandsSeller() Method and Adding Loom Band Seller to Table");
        System.out.println("==================================================================================================================");

        PreparedStatement ps = conn.prepareStatement("insert into " +
                "LOOMBANDSELLER (sellerid, sellername) values (?, ?)");
        ps.setInt(1, a.getId());
        ps.setString(2, a.getName());
        int changeRowCount =  ps.executeUpdate();
        if(changeRowCount == 0) return null;
        else return getLoomBandSellerById(a.id);
    }

    public List<LoomBandsSeller> getLoomBandSellerList() {
        System.out.println("In LoomBandSellerDAO Class getLoomBandSellerList() Method and Getting all Loom Band Suppliers from Table");
        System.out.println("==================================================================================================================");
        List<LoomBandsSeller> loomBandsSellerList = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("select * from LOOMBANDSELLER");
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                int loomBandSellerId = resultSet.getInt("sellerid");
                String loomBandSellerName = resultSet.getString("sellername");
                System.out.println(loomBandSellerId + "," + loomBandSellerName);
                LoomBandsSeller a = new LoomBandsSeller(loomBandSellerId, loomBandSellerName);
                loomBandsSellerList.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loomBandsSellerList;
    }

    public LoomBandsSeller getLoomBandSellerById(int id) throws SQLException {
        System.out.println("In LoomBandSellerDAO Class getLoomBandSellerListById() Method and Getting All Loom Band Sellers by Id from Table");
        System.out.println("==================================================================================================================");
        List<LoomBandsSeller> loomBandsSellerList = new ArrayList<>();

        PreparedStatement ps = conn.prepareStatement("select * from LOOMBANDSELLER WHERE SELLERID = ?");
        ps.setInt(1, id);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            int loomBandSellerId = resultSet.getInt("sellerid");
            String loomBandSellerName = resultSet.getString("sellername");
            //System.out.println(loomBandSellerId + "," + loomBandSellerName);
            LoomBandsSeller a = new LoomBandsSeller(loomBandSellerId, loomBandSellerName);
            loomBandsSellerList.add(a);

        }
        if (loomBandsSellerList.isEmpty()) {
            return null;
        } else {
            return loomBandsSellerList.get(0);
        }

    }

}
