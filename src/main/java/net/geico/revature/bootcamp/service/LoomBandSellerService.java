package net.geico.revature.bootcamp.service;

import net.geico.revature.bootcamp.dao.LoomBandSellerDAO;
import net.geico.revature.bootcamp.model.LoomBandsSeller;

import java.sql.SQLException;
import java.util.List;

public class LoomBandSellerService {

    LoomBandSellerDAO loomBandSellerDAO;

    public LoomBandSellerService(LoomBandSellerDAO loomBandSellerDAO) {
        this.loomBandSellerDAO = loomBandSellerDAO;
        System.out.println("In LoomBandSellerService Class & executing Constructor");
    }

    public LoomBandsSeller addLoomBandSeller(LoomBandsSeller a) throws SQLException {
        System.out.println("In LoomBandSellerService addLoomBandSeller() Method");
        System.out.println("Calling LoomBandSellerDAO addLoomBandsSeller() Method");
        return loomBandSellerDAO.addLoomBandsSeller(a);

    }

    public List<LoomBandsSeller> getLoomBandSellerList() {
        System.out.println("In LoomBandSellerService Class getLoomBandSellerList() Method");
        System.out.println("Calling LoomBandSellerDAO Class getLoomBandSellerList() Method and storing the returned value in LoomBandsSeller List Array");
        List<LoomBandsSeller> loomBandsSellerList = loomBandSellerDAO.getLoomBandSellerList();
        return loomBandsSellerList;
    }

    public LoomBandsSeller getLoomBandSellerById(int id) throws SQLException {
        System.out.println("In LoomBandSellerService Class getLoomBandSellerListById() Method");
        System.out.println("Calling LoomBandSellerDAO Class getLoomBandSellerListById() Method and storing the returned value in LoomBandsSeller List Array");
        return loomBandSellerDAO.getLoomBandSellerById(id);
    }

}


