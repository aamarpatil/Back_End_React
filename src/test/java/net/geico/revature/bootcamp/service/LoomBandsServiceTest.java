package net.geico.revature.bootcamp.service;

import net.geico.revature.bootcamp.dao.LoomBandDAO;
import net.geico.revature.bootcamp.dao.LoomBandSellerDAO;
import net.geico.revature.bootcamp.model.LoomBands;
import net.geico.revature.bootcamp.model.LoomBandsSeller;
import net.geico.revature.bootcamp.util.ConnectionSingleton;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoomBandsServiceTest {

    LoomBandDAO loomBandDAO;
    LoomBandsService loomBandsService;

    LoomBandSellerService loomBandSellerService;

    Connection conn;

    @BeforeEach

    public void setUp() throws SQLException {
        conn = ConnectionSingleton.getConnection();
        loomBandDAO = new LoomBandDAO(conn);
        loomBandsService = new LoomBandsService(loomBandSellerService, loomBandDAO);
        ConnectionSingleton.resetTestDatabase();

        PreparedStatement ps = conn.prepareStatement("insert into " +
                "LOOMBANDSELLER (sellerid, sellername) values (?, ?)");
        ps.setInt(1, 1);
        ps.setString(2, "Bobby");
        int changeRowCount = ps.executeUpdate();

        ps.setInt(1, 2);
        ps.setString(2, "John");
        changeRowCount = ps.executeUpdate();
    }


    @AfterEach
    void tearDown() {

    }

    @Test
    void getLoomBandsList() throws SQLException {

        PreparedStatement ps = conn.prepareStatement("insert into " +
                "LOOMBANDS (loomId, loomName, loomPrice, loomSellerName) values (?, ?, ?, ?)");

        ps.setInt(1, 1);
        ps.setString(2, "Green");
        ps.setFloat(3, 10.99f);
        ps.setString(4, "Bobby");
        ps.executeUpdate();

        List<LoomBands> loomBandsList = loomBandsService.getLoomBandsList();
        assertEquals(1, loomBandsList.size());
    }


    @Test
    void getLoomBandsList2() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("insert into " +
                "LOOMBANDS (loomId, loomName, loomPrice, loomSellerName) values (?, ?, ?, ?)");

        ps.setInt(1, 1);
        ps.setString(2, "Green");
        ps.setFloat(3, 10.99f);
        ps.setString(4, "Bobby");
        ps.executeUpdate();

        ps.setInt(1, 2);
        ps.setString(2, "Red");
        ps.setFloat(3, 20.99f);
        ps.setString(4, "John");
        ps.executeUpdate();

        List<LoomBands> loomBandsList = loomBandsService.getLoomBandsList();
        assertEquals(2, loomBandsList.size());

        LoomBands firstLoomBand = loomBandsList.get(0);
        LoomBands secondLoomBand = loomBandsList.get(1);

        assertEquals(1, firstLoomBand.loomId);
        assertEquals(2, secondLoomBand.loomId);
        assertEquals("Green",firstLoomBand.getLoomName());
        assertEquals("Red", secondLoomBand.getLoomName());
        assertEquals(10.99f, firstLoomBand.getLoomPrice());
        assertEquals(20.99f, secondLoomBand.getLoomPrice());
        assertEquals("Bobby", firstLoomBand.getLoomSellerName());
        assertEquals("John", secondLoomBand.getLoomSellerName());

    }


    @Test
    void addLoomBands() throws Exception {

        loomBandsService.addLoomBands(new LoomBands(1, "Purple", 5.00f, "Bobby"));
        loomBandsService.addLoomBands(new LoomBands(2, "Brown", 10.00f, "John"));
        PreparedStatement ps = conn.prepareStatement("select * from LOOMBANDS");
        ResultSet resultSet = ps.executeQuery();
        assertTrue(resultSet.next());

        List<LoomBands> loomBandsList = loomBandsService.getLoomBandsList();
        assertEquals(2, loomBandsList.size());

    }

    @Test
    void getLoomBandsById() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("insert into " +
                "LOOMBANDS (loomId, loomName, loomPrice, loomSellerName) values (?, ?, ?, ?)");

        ps.setInt(1, 1);
        ps.setString(2, "White");
        ps.setFloat(3, 5.00f);
        ps.setString(4, "Bobby");
        ps.executeUpdate();

        ps.setInt(1, 2);
        ps.setString(2, "Teal");
        ps.setFloat(3, 10.00f);
        ps.setString(4, "Bobby");
        ps.executeUpdate();

        ps.setInt(1, 3);
        ps.setString(2, "Magenta");
        ps.setFloat(3, 15.00f);
        ps.setString(4, "John");
        ps.executeUpdate();

        LoomBands loomBands = loomBandsService.getLoomBandsById(2);
       // System.out.println(loomBands.getLoomId() + "," + loomBands.getLoomName());
        assertEquals(2,loomBands.getLoomId());
        assertEquals("Teal",loomBands.getLoomName());
        assertEquals(10.00f, loomBands.getLoomPrice());
        assertEquals("Bobby", loomBands.getLoomSellerName());


    }

    @Test
    void deleteLoomBandsById() throws SQLException {

        PreparedStatement ps = conn.prepareStatement("insert into " +
                "LOOMBANDS (loomId, loomName, loomPrice, loomSellerName) values (?, ?, ?, ?)");

        ps.setInt(1, 1);
        ps.setString(2, "Maroon");
        ps.setFloat(3, 4.50f);
        ps.setString(4, "Bobby");
        ps.executeUpdate();

        ps.setInt(1, 2);
        ps.setString(2, "Gray");
        ps.setFloat(3, 6.50f);
        ps.setString(4, "John");
        ps.executeUpdate();

        String loomBands = loomBandsService.deleteLoomBandsById(1);

        PreparedStatement ps1 = conn.prepareStatement("delete from LOOMBANDS WHERE loomId = ?");
        ps1.setInt(1, 1);
        ps1.executeUpdate();

        List<LoomBands> loomBands1 = loomBandsService.getLoomBandsList();
        //System.out.println(loomBands1.getLoomId() + "," + loomBands1.getLoomName());
        assertEquals(1,loomBands1.size());


    }

    @Test
    void updateLoomBands() throws SQLException {

        PreparedStatement ps = conn.prepareStatement("insert into " +
                "LOOMBANDS (loomId, loomName, loomPrice, loomSellerName) values (?, ?, ?, ?)");

        ps.setInt(1, 1);
        ps.setString(2, "Navy Blue");
        ps.setFloat(3, 4.50f);
        ps.setString(4, "Bobby");
        ps.executeUpdate();

        ps.setInt(1, 2);
        ps.setString(2, "Gold");
        ps.setFloat(3, 6.50f);
        ps.setString(4, "John");
        ps.executeUpdate();

        LoomBands updatedLoomBand = new LoomBands(2, "Silver", 7.50f, "John");

        String loomBands = loomBandsService.updateLoomBands(2, updatedLoomBand );

        LoomBands loomBands1 = loomBandsService.getLoomBandsById(2);
        System.out.println(loomBands1.getLoomId() + "," + loomBands1.getLoomName() + " " + loomBands1.getLoomPrice() + " " + loomBands1.getLoomSellerName());
        assertEquals(2,loomBands1.getLoomId());
        assertEquals("Silver",loomBands1.getLoomName());
        assertEquals(7.50f, loomBands1.getLoomPrice());
        assertEquals("John", loomBands1.getLoomSellerName());

    }
}