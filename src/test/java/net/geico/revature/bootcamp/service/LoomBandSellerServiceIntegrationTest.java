package net.geico.revature.bootcamp.service;

import net.geico.revature.bootcamp.dao.LoomBandSellerDAO;
import net.geico.revature.bootcamp.model.LoomBandsSeller;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a mentor's example of a simplest integration test
 * a bootcamp graduate will encounter when joining a team.
 */
class LoomBandSellerServiceIntegrationTest {
    public static final String TDB_DRIVER = "org.h2.Driver";
    public static final String TDB_URL = "jdbc:h2:mem:testdb";
    public static final String TDB_USER = "sa";
    public static final String TDB_PASS = "";
    public static final String TDB_DDL_TABLE = "LOOMBANDSELLER";
    public static final String TDB_DDL_ID = "SELLERID";
    public static final String TDB_DDL_NAME = "SELLERNAME";
    public static final String SELECT_ALL_SELLERS = "SELECT * FROM " + TDB_DDL_TABLE + " ORDER BY " + TDB_DDL_ID;
    public static final String[] TDB_DDL_COLUMNS = {TDB_DDL_ID, TDB_DDL_NAME};


    public static final String TDB_DDL_SELLER_TABLE_CREATE = "CREATE TABLE " + TDB_DDL_TABLE + " (" + TDB_DDL_ID + " INT PRIMARY KEY, " + TDB_DDL_NAME + " VARCHAR(255) NOT NULL, UNIQUE(" + TDB_DDL_NAME + "));";
    public static final String TDB_DDL_SELLER_DROP = "DROP TABLE " + TDB_DDL_TABLE;

    public static final String TDB_DEFAULT_SELLERS = "INSERT INTO " + TDB_DDL_TABLE + " VALUES (3001, 'Sally'), (3002, 'Dick'), (3003, 'Harry');";

    public static final Logger logger = LoggerFactory.getLogger(LoomBandSellerServiceIntegrationTest.class);
    Connection h2Connection;
    Statement statement;

    LoomBandSellerService loomBandSellerServiceUnderTest;

    @BeforeEach
    void setUp() throws ClassNotFoundException, SQLException {
        Class.forName(TDB_DRIVER);                                                 // Side effect to register the driver.
        h2Connection = DriverManager.getConnection(TDB_URL, TDB_USER, TDB_PASS);
        statement = h2Connection.createStatement();
        int count = statement.executeUpdate(TDB_DDL_SELLER_TABLE_CREATE);
        logger.info(() -> String.format("TDB_DDL_SELLER_TABLE_CREATE: %d", count));

        loomBandSellerServiceUnderTest = new LoomBandSellerService(new LoomBandSellerDAO(h2Connection));
        int rowCount = statement.executeUpdate(TDB_DEFAULT_SELLERS);
        logger.info(() -> String.format("TDB_DEFAULT_SELLERS: %d", rowCount));
    }

    @AfterEach
    void tearDown(TestInfo testInfo) throws SQLException {
        String displayName = String.format(
                "%s_%s",
                testInfo.getDisplayName().replaceAll("[^A-Za-z0-9]", ""),
                LocalDateTime.now(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("dd_HH-mm-ss")));

        statement.execute("SCRIPT TO 'target/" + displayName + "-dump.sql'");

        statement.execute(TDB_DDL_SELLER_DROP);
        if (!statement.isClosed()) statement.close();
        h2Connection.close();
        loomBandSellerServiceUnderTest = null;
    }

    @Test
    void inspectLoomBandSellerTable() throws SQLException {
        try (var resultSet = statement.executeQuery(SELECT_ALL_SELLERS)) {

            var resultSetMetadata = resultSet.getMetaData();
            var columnCount = resultSetMetadata.getColumnCount();
            assertEquals(2, columnCount);

            var columnNames = new String[]{resultSetMetadata.getColumnName(1), resultSetMetadata.getColumnName(2)};
            assertArrayEquals(TDB_DDL_COLUMNS, columnNames);

            var columnTypes = new String[]{resultSetMetadata.getColumnTypeName(1), resultSetMetadata.getColumnTypeName(2)};
            assertArrayEquals(new String[]{"INTEGER", "CHARACTER VARYING"}, columnTypes);

            var index = 0;
            while (resultSet.next()) {
                assertEquals(3001 + index, resultSet.getInt(1));
                assertEquals(new String[]{"Sally", "Dick", "Harry"}[index], resultSet.getString(2));
                index++;
            }
        }
    }

    @Test
    void addLoomBandSeller() throws SQLException {
        // Is this intentional?
        assertThrows(NullPointerException.class, () -> loomBandSellerServiceUnderTest.addLoomBandSeller(null));

        LoomBandsSeller newSellerFirst = loomBandSellerServiceUnderTest.addLoomBandSeller(new LoomBandsSeller(-1, "Backwards Willy"));
        LoomBandsSeller newSellerSecond = loomBandSellerServiceUnderTest.addLoomBandSeller(new LoomBandsSeller(0, "Passive Pete"));
        LoomBandsSeller newSellerThird = loomBandSellerServiceUnderTest.addLoomBandSeller(new LoomBandsSeller(1, "Basic Bill"));
        LoomBandsSeller newSellerFourth = loomBandSellerServiceUnderTest.addLoomBandSeller(new LoomBandsSeller(1000, "Wealthy Wally"));

        assertNotNull(newSellerFirst);
        assertNotNull(newSellerSecond);
        assertNotNull(newSellerThird);
        assertNotNull(newSellerFourth);


        logger.info(() -> String.join("\n", new String[]{
                "Four new sellers added:",
                newSellerFirst.toString(),
                newSellerSecond.toString(),
                newSellerThird.toString(),
                newSellerFourth.toString()}));

        var expectedSellers = new HashMap<Integer, String>(7) {
            {
                put(-1, "Backwards Willy");
                put(0, "Passive Pete");
                put(1, "Basic Bill");
                put(1000, "Wealthy Wally");
                put(3001, "Sally");
                put(3002, "Dick");
                put(3003, "Harry");
            }
        };

        try (var resultSet = statement.executeQuery("SELECT * FROM " + TDB_DDL_TABLE + " ORDER BY " + TDB_DDL_ID)) {
            while (resultSet.next()) assertEquals(expectedSellers.get(resultSet.getInt(1)), resultSet.getString(2));
        }

    }

    @Test
    void getLoomBandSellerList() throws SQLException {
        int inserted = statement.executeUpdate("INSERT INTO LOOMBANDSELLER VALUES (100, 'Dolly'), (200, 'Molly'), (300, 'Lolly')");
        assertEquals(3, inserted);

        var loomBandSellers = loomBandSellerServiceUnderTest.getLoomBandSellerList();
        assertEquals(6, loomBandSellers.size());

        var expectedSellers = new HashSet<LoomBandsSeller>(6) {
            {
                add(new LoomBandsSeller(3001, "Sally"));
                add(new LoomBandsSeller(3002, "Dick"));
                add(new LoomBandsSeller(3003, "Harry"));
                add(new LoomBandsSeller(100, "Dolly"));
                add(new LoomBandsSeller(200, "Molly"));
                add(new LoomBandsSeller(300, "Lolly"));
            }
        };

        assertArrayEquals(
                expectedSellers.stream().map(LoomBandsSeller::toString).sorted().toArray(),
                loomBandSellers.stream().map(LoomBandsSeller::toString).sorted().toArray());
    }

    @Test
    void getLoomBandSellerById() throws SQLException {
        int inserted = statement.executeUpdate("INSERT INTO LOOMBANDSELLER VALUES (1, 'Shlomo'), (2, 'Mordechai'), (3, 'Haim'), (4, 'Moshe')");
        assertEquals(4, inserted);

        LoomBandsSeller sellerShouldBeShlomo = loomBandSellerServiceUnderTest.getLoomBandSellerById(1);
        LoomBandsSeller sellerShouldBeMordechai = loomBandSellerServiceUnderTest.getLoomBandSellerById(2);
        LoomBandsSeller sellerShouldBeHaim = loomBandSellerServiceUnderTest.getLoomBandSellerById(3);
        LoomBandsSeller sellerShouldBeMoshe = loomBandSellerServiceUnderTest.getLoomBandSellerById(4);
        LoomBandsSeller sellerShouldBeNull = loomBandSellerServiceUnderTest.getLoomBandSellerById(5);
        LoomBandsSeller sellerShouldBeSally = loomBandSellerServiceUnderTest.getLoomBandSellerById(3001);
        LoomBandsSeller sellerShouldBeDick = loomBandSellerServiceUnderTest.getLoomBandSellerById(3002);
        LoomBandsSeller sellerShouldBeHarry = loomBandSellerServiceUnderTest.getLoomBandSellerById(3003);

        assertEquals( new LoomBandsSeller(1, "Shlomo"), sellerShouldBeShlomo);
        assertEquals( new LoomBandsSeller(2, "Mordechai"), sellerShouldBeMordechai);
        assertEquals( new LoomBandsSeller(3, "Haim"), sellerShouldBeHaim);
        assertEquals( new LoomBandsSeller(4, "Moshe"), sellerShouldBeMoshe);
        assertNull(sellerShouldBeNull);
        assertEquals( new LoomBandsSeller(3001, "Sally"), sellerShouldBeSally);
        assertEquals( new LoomBandsSeller(3002, "Dick"), sellerShouldBeDick);
        assertEquals( new LoomBandsSeller(3003, "Harry"), sellerShouldBeHarry);
    }
}