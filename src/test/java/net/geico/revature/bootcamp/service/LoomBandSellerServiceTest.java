package net.geico.revature.bootcamp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.geico.revature.bootcamp.dao.LoomBandSellerDAO;
import net.geico.revature.bootcamp.model.LoomBandsSeller;
import net.geico.revature.bootcamp.util.ConnectionSingleton;
import org.h2.engine.ConnectionInfo;
import org.h2.engine.SessionRemote;
import org.h2.jdbc.JdbcConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoomBandSellerServiceTest {

    LoomBandSellerDAO loomBandSellerDAO;
    LoomBandSellerService loomBandSellerService;

    Connection conn;


    @BeforeEach
    public void setUp() {
        conn = ConnectionSingleton.getConnection();
        loomBandSellerDAO = new LoomBandSellerDAO(conn);
        loomBandSellerService = new LoomBandSellerService(loomBandSellerDAO);
        ConnectionSingleton.resetTestDatabase();
    }

    @Test
    void testAddLoomBandSeller1() throws SQLException {

        loomBandSellerService.addLoomBandSeller(new LoomBandsSeller(1, "Name"));
        PreparedStatement ps = conn.prepareStatement("select * from LOOMBANDSELLER");
        ResultSet resultSet = ps.executeQuery();
        assertTrue(resultSet.next());
        assertEquals(1, resultSet.getInt("sellerid"));
        assertEquals("Name", resultSet.getString("sellername"));

    }


    @Test
    void testAddLoomBandSeller2() throws SQLException {

        loomBandSellerService.addLoomBandSeller(new LoomBandsSeller(1, "Name1"));
        loomBandSellerService.addLoomBandSeller(new LoomBandsSeller(2, "Name2"));
        PreparedStatement ps = conn.prepareStatement("select * from LOOMBANDSELLER");
        ResultSet resultSet = ps.executeQuery();
        assertTrue(resultSet.next());
        assertEquals(1, resultSet.getInt("sellerid"));
        assertEquals("Name1", resultSet.getString("sellername"));
        assertTrue(resultSet.next());
        assertEquals(2, resultSet.getInt("sellerid"));
        assertEquals("Name2", resultSet.getString("sellername"));
        assertFalse(resultSet.next());

    }


    @Test
    void testGetLoomBandSellerList1() throws SQLException {

        PreparedStatement ps = conn.prepareStatement("insert into " +
                "LOOMBANDSELLER (sellerid, sellername) values (?, ?)");
        ps.setInt(1, 1);
        ps.setString(2, "Bobby");
        int changeRowCount = ps.executeUpdate();

        List<LoomBandsSeller> loomBandsSellerList = loomBandSellerService.getLoomBandSellerList();
        assertEquals(1, loomBandsSellerList.size());

    }


    @Test
    void testGetLoomBandSellerList2() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("insert into " +
                "LOOMBANDSELLER (sellerid, sellername) values (?, ?)");
        ps.setInt(1, 1);
        ps.setString(2, "Bobby");
        int changeRowCount = ps.executeUpdate();

        ps.setInt(1, 2);
        ps.setString(2, "John");
        changeRowCount = ps.executeUpdate();

        List<LoomBandsSeller> loomBandsSellerList = loomBandSellerService.getLoomBandSellerList();

        assertEquals(2, loomBandsSellerList.size());

        LoomBandsSeller firstSeller = loomBandsSellerList.get(0);
        LoomBandsSeller secondSeller = loomBandsSellerList.get(1);

        assertEquals(1, firstSeller.getId());
        assertEquals(2, secondSeller.getId());

        assertEquals("Bobby", firstSeller.getName());
        assertEquals("John", secondSeller.getName());
    }


    @Test
    void testGetLoomBandSellerById1() throws SQLException {

        PreparedStatement ps = conn.prepareStatement("insert into " +
                "LOOMBANDSELLER (sellerid, sellername) values (?, ?)");
        ps.setInt(1, 1);
        ps.setString(2, "Bobby");
        int changeRowCount = ps.executeUpdate();

        ps.setInt(1, 2);
        ps.setString(2, "John");
        changeRowCount = ps.executeUpdate();

        ps.setInt(1, 3);
        ps.setString(2, "Ray");
        changeRowCount = ps.executeUpdate();

        LoomBandsSeller loomBandsSeller = loomBandSellerService.getLoomBandSellerById(2);

        assertEquals(2, loomBandsSeller.getId());
        assertEquals("John", loomBandsSeller.getName());

    }


}