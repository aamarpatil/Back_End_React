package net.geico.revature.bootcamp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Header;
import net.geico.revature.bootcamp.exception.LoomBandsException;
import net.geico.revature.bootcamp.model.LoomBands;
import net.geico.revature.bootcamp.model.LoomBandsSeller;
import net.geico.revature.bootcamp.service.LoomBandSellerService;
import net.geico.revature.bootcamp.service.LoomBandsService;

import java.sql.SQLException;
import java.util.List;

public class LoomBandsController {

    LoomBandsService loomBandsService;
    LoomBandSellerService loomBandSellerService;

    public LoomBandsController(LoomBandSellerService loomBandSellerService, LoomBandsService loomBandsService) {
        this.loomBandSellerService = loomBandSellerService;
        this.loomBandsService = loomBandsService;
        System.out.println("In LoomBandsControllerService Class & executing Constructor");
    }


        public Javalin getAPI(){
            Javalin api = Javalin.create();
            api.before (ctx -> {

                    ctx.header("Access-Control-Allow-Origin", "*");
                    ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                    ctx.header("Access-Control-Allow-Headers", "*");
                });




            //Javalin to handle preflight requests (sent via OPTIONS)
            api.options("/*", ctx -> {
                ctx.header(Header.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:3000");
                ctx.header(Header.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS");
                ctx.header(Header.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, Authorization");
                ctx.status(200);
            });

        api.get("health", context -> {
            System.out.println("Getting Health Of the Server");
            context.result("Server is Up");
        });



        api.get("seller", context -> {
            System.out.println("Calling LoomBandSellerService getLoomBandSellerList() method and storing the returned list in List Array of LoomBandsSeller");
            List<LoomBandsSeller> loomBandsSellerList = loomBandSellerService.getLoomBandSellerList();
            if(loomBandsSellerList.isEmpty()){
                context.status(404);
            }
            context.json(loomBandsSellerList);
        });

        api.get("seller/{id}", context -> {
            System.out.println("Calling LoomBandSellerService getLoomBandSellerListById() method and storing the returned list in List Array of LoomBandsSeller");
            int id = -1;

            try{
                id = Integer.parseInt(context.pathParam("id"));
            }  catch(NumberFormatException e){
                context.status(400);
                context.result("You can only search by Product Id.");
                return;
            }

            //ObjectMapper om = new ObjectMapper();
            //LoomBandsSeller loomBandsSeller = om.readValue(context.body(), LoomBandsSeller.class);
            try {
                LoomBandsSeller loomBandsSeller = loomBandSellerService.getLoomBandSellerById(id);
                if (loomBandsSeller == null) {
                    context.status(404);
                    context.result("Seller ID " + id + " does not exist.");
                } else
                    context.json(loomBandsSeller);

            } catch (SQLException e) {
                context.status(400);
                context.result("Failed retrieving " + id + " the requested Item.");
            }


        });

        api.post("seller", context -> {
            System.out.println("Calling LoomBandSellerService addLoomBandSeller() method to Add Loom Band Seller to the Table");
            ObjectMapper om = new ObjectMapper();
            LoomBandsSeller a = om.readValue(context.body(), LoomBandsSeller.class);
            if(a.id == 0){

                 a.id = (int) (Math.random() * Integer.MAX_VALUE);

            }
            try {
               LoomBandsSeller loomBandsSeller = loomBandSellerService.addLoomBandSeller(a);
               context.status(201);
               context.json(loomBandsSeller);

            } catch (SQLException e) {
                context.status(409);
                context.result("You have entered duplicate Loomband Seller.");


            }

        });

        api.get("loomband", context -> {
            List<LoomBands> loomBandsList = loomBandsService.getLoomBandsList();
            if(loomBandsList.isEmpty()){
                context.status(404);
            }
            context.json(loomBandsList);
        });


        api.post("loomband", context -> {
            try {
                ObjectMapper om = new ObjectMapper();
                LoomBands loomBands = om.readValue(context.body(), LoomBands.class);
                List<LoomBandsSeller> loomBandsSellerList = loomBandSellerService.getLoomBandSellerList();
                String sellerName = loomBands.getLoomSellerName();
                for (LoomBandsSeller loomBandsSeller : loomBandsSellerList) {
                    if (sellerName.equals(loomBandsSeller.getName())) {
                        LoomBands newloomBands = loomBandsService.addLoomBands(loomBands);
                        context.status(201);
                        context.json(newloomBands);
                        return;
                    }

                }
                context.status(304);

            } catch (JsonProcessingException e) {
                context.status(400);
            } catch (LoomBandsException e) {
                context.result(e.getMessage());
                context.status(400);
            }
        });


        api.get("loomband/{id}", context -> {
            System.out.println("Calling LoomBandService getLoomBandsById() method and storing the returned list in List Array of LoomBands");
            int id = -1;
            try{
                id = Integer.parseInt(context.pathParam("id"));
            }  catch(NumberFormatException e){
                context.status(400);
                context.result("You can only search by Loom/Product Id.");
                return;
            }


            try {
                id = Integer.parseInt(context.pathParam("id"));
                //ObjectMapper om = new ObjectMapper();
                //LoomBandsSeller loomBandsSeller = om.readValue(context.body(), LoomBandsSeller.class);
                LoomBands loomBands = loomBandsService.getLoomBandsById(id);
                if(loomBands == null){
                    context.status(404);
                    context.result("Loom ID " + id + " does not exist.");
                }
                else    context.json(loomBands);
            }
            catch(NumberFormatException e){
                context.status(400);
                context.result("Failed retrieving " + id + " the requested Item.");
                return;
            }


        });



        api.delete("loomband/{id}", context -> {
            System.out.println("Calling LoomBandService getLoomBandsById() method and storing the returned list in List Array of LoomBands");
            int id = Integer.parseInt(context.pathParam("id"));
            //ObjectMapper om = new ObjectMapper();
            //LoomBandsSeller loomBandsSeller = om.readValue(context.body(), LoomBandsSeller.class);
            String deleteLoomBand = loomBandsService.deleteLoomBandsById(id);
            context.json(deleteLoomBand);
        });


        api.put("loomband/{id}", context -> {
            System.out.println("Calling LoomBandService getLoomBandsById() method and storing the returned list in List Array of LoomBands");
            int id = Integer.parseInt(context.pathParam("id"));
            ObjectMapper om = new ObjectMapper();
            LoomBands loomBands = om.readValue(context.body(), LoomBands.class);
            String updateLoomBand = loomBandsService.updateLoomBands(id, loomBands);
            context.json(updateLoomBand);
        });


        return api;

    }


}