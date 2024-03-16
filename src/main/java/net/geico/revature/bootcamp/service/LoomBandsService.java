package net.geico.revature.bootcamp.service;

import net.geico.revature.bootcamp.dao.LoomBandDAO;
import net.geico.revature.bootcamp.dao.LoomBandSellerDAO;
import net.geico.revature.bootcamp.exception.LoomBandsException;
import net.geico.revature.bootcamp.model.LoomBands;
import net.geico.revature.bootcamp.model.LoomBandsSeller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoomBandsService {

    private final LoomBandDAO loomBandDAO;

    private final LoomBandSellerService loomBandSellerService;
    List<LoomBands> loomBandsList;

    public LoomBandsService(LoomBandSellerService loomBandSellerService, LoomBandDAO loomBandDAO) {
        this.loomBandSellerService = loomBandSellerService;
        this.loomBandDAO = loomBandDAO;
        loomBandsList = new ArrayList<>();
    }


    public List<LoomBands> getLoomBandsList() {
        return loomBandDAO.getLoomBandsList();

    }


    public LoomBands addLoomBands(LoomBands loomBands) throws Exception {
        //code & exceptions need to be added here//
        if (loomBands.getLoomName() == null || loomBands.getLoomSellerName() == null) {
            throw new LoomBandsException("LoomBand Name and LoomBand Seller Name cannot be Null");

        }
        if (loomBands.getLoomPrice() < 0)
            throw new LoomBandsException(("Price cannot be negative"));

        int loomId = (int) (Math.random() * Integer.MAX_VALUE);
        loomBands.setLoomId(loomId);
        loomBandDAO.addLoomBands(loomBands);
        return loomBands;

    }


    public LoomBands getLoomBandsById(int loomId) throws SQLException {
        LoomBands loomBands = loomBandDAO.getLoomBandById(loomId);
        return loomBands;

    }

    public String deleteLoomBandsById(int loomId) {
        String loomBandDelete = loomBandDAO.deleteLoomBandById(loomId);
        return loomBandDelete;

    }

    public String updateLoomBands(int id, LoomBands n) {

        String s = loomBandDAO.updateLoomBands(id, n);
        return s;
    }


}


