package net.geico.revature.bootcamp.service;

import net.geico.revature.bootcamp.dao.LoomBandSellerDAO;
import net.geico.revature.bootcamp.model.LoomBandsSeller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class LoomBandSellerServiceUnitTest {

    private LoomBandSellerDAO loomBandSellerDAOMock;
    private LoomBandSellerService loomBandSellerService;

    @BeforeEach
    void setUp() {
        // Mock the DAO
        loomBandSellerDAOMock = mock(LoomBandSellerDAO.class);
        // Inject the mock DAO into the service
        loomBandSellerService = new LoomBandSellerService(loomBandSellerDAOMock);
    }

    @Test
    void getLoomBandSellerList() {
        // Arrange data DAO mocks
        List<LoomBandsSeller> mockSellers = Arrays.asList(
                new LoomBandsSeller(3001, "Sally"),
                new LoomBandsSeller(3002, "Dick"),
                new LoomBandsSeller(3003, "Harry"),
                new LoomBandsSeller(100, "Dolly"),
                new LoomBandsSeller(200, "Molly"),
                new LoomBandsSeller(300, "Lolly")
        );

        when(loomBandSellerDAOMock.getLoomBandSellerList()).thenReturn(mockSellers);

        // Act on service under test
        List<LoomBandsSeller> loomBandSellers = loomBandSellerService.getLoomBandSellerList();

        // Assert response from service under test is as mocked
        assertEquals(6, loomBandSellers.size());

        Set<LoomBandsSeller> expectedSellers = new HashSet<>(mockSellers);
        Set<LoomBandsSeller> actualSellers = new HashSet<>(loomBandSellers);
        assertEquals(expectedSellers, actualSellers);

        // Verify interaction with mock, i.e., that getLoomBandSellerList was actually called!
        // Note: breakpoints can help examine this.
        verify(loomBandSellerDAOMock).getLoomBandSellerList();
    }

    @Test
    void addLoomBandSeller() throws SQLException {

        // Arrange
        LoomBandsSeller expectedSeller = new LoomBandsSeller(1, "Active Pete");

        // Stage impostor to respond to the same add call DAO does.
        when(loomBandSellerDAOMock.addLoomBandsSeller(any(LoomBandsSeller.class))).thenReturn(expectedSeller);

        // Act on the service under test
        LoomBandsSeller resultSeller = loomBandSellerService.addLoomBandSeller(expectedSeller);

        // Assert response from service under test in several ways.
        assertNotNull(resultSeller);
        assertEquals(expectedSeller.getId(), resultSeller.getId());
        assertEquals(expectedSeller.getName(), resultSeller.getName());

        // Capture and validate the argument passed to DAO - i.e., the impostor not gets data, so we need to look.
        //captures an argument.
        ArgumentCaptor<LoomBandsSeller> sellerCaptor = ArgumentCaptor.forClass(LoomBandsSeller.class);
        verify(loomBandSellerDAOMock).addLoomBandsSeller(sellerCaptor.capture());  // This is how we actually intercept;
        LoomBandsSeller capturedSeller = sellerCaptor.getValue();                  // This is what we intercepted;

        assertEquals(expectedSeller.getName(), capturedSeller.getName());              // Validate the names that were used.
    }

    @Test
    void getLoomBandSellerById_ExistingSeller() throws SQLException {
        // Arrange
        int sellerId = 1;
        LoomBandsSeller expectedSeller = new LoomBandsSeller(sellerId, "Shlomo");
        when(loomBandSellerDAOMock.getLoomBandSellerById(sellerId)).thenReturn(expectedSeller);

        // Act
        LoomBandsSeller resultSeller = loomBandSellerService.getLoomBandSellerById(sellerId);

        // Assert
        assertNotNull(resultSeller, "The seller should not be null");
        assertEquals(expectedSeller.getId(), resultSeller.getId(), "The seller IDs should match");
        assertEquals(expectedSeller.getName(), resultSeller.getName(), "The seller names should match");

        // Verify the interaction with the mock
        verify(loomBandSellerDAOMock).getLoomBandSellerById(sellerId);
    }

    @Test
    void getLoomBandSellerById_NonExistingSeller() throws SQLException {
        // Arrange
        int nonExistingSellerId = -99;
        when(loomBandSellerDAOMock.getLoomBandSellerById(nonExistingSellerId)).thenReturn(null);

        // Act
        LoomBandsSeller resultSeller = loomBandSellerService.getLoomBandSellerById(nonExistingSellerId);

        // Assert
        assertNull(resultSeller, "The seller should be null for a non-existing ID");

        // Verify the interaction with the mock
        verify(loomBandSellerDAOMock).getLoomBandSellerById(nonExistingSellerId);
    }


}
