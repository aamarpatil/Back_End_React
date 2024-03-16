package net.geico.revature.bootcamp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import net.geico.revature.bootcamp.controller.LoomBandsController;
import net.geico.revature.bootcamp.dao.LoomBandDAO;
import net.geico.revature.bootcamp.dao.LoomBandSellerDAO;
import net.geico.revature.bootcamp.exception.LoomBandsException;
import net.geico.revature.bootcamp.model.LoomBands;
import net.geico.revature.bootcamp.model.LoomBandsSeller;
import net.geico.revature.bootcamp.service.LoomBandSellerService;
import net.geico.revature.bootcamp.service.LoomBandsService;
import net.geico.revature.bootcamp.util.ConnectionSingleton;

import java.sql.Connection;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        //        this line is just for testing that your tables get set up correctly
//        if not, you'll get a stack trace
             System.out.println("In Main Program");

             System.out.println("Calling ConnectionSingleton getConnection() Method");

        Connection conn = ConnectionSingleton.getConnection();

             System.out.println("Created LoomBandSellerDAO Class Object & calling Constructor by Passing Connection Object");
        LoomBandSellerDAO loomBandSellerDAO = new LoomBandSellerDAO(conn);
        LoomBandDAO loomBandDAO = new LoomBandDAO(conn);

             System.out.println("Created LoomBandSellerService Class Object & calling Constructor by Passing LoomBandSellerDAO Object");

        LoomBandSellerService loomBandSellerService = new LoomBandSellerService(loomBandSellerDAO);
        LoomBandsService loomBandsService = new LoomBandsService(loomBandSellerService, loomBandDAO);

             System.out.println("Created LoomBandController Class Object & calling Constructor by Passing LoomBandSellerService Object");

        LoomBandsController loomBandsController = new LoomBandsController(loomBandSellerService, loomBandsService);

             System.out.println("Calling LoomBandsController getAPI() Method");
        Javalin api = loomBandsController.getAPI();

        api.start(9003);



    }
}
