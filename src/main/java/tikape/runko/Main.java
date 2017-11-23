package tikape.runko;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.OhjeRiviDao;

import tikape.runko.database.RaakaAineDao;
import tikape.runko.database.ReseptiDao;
import tikape.runko.domain.OhjeRivi;
import tikape.runko.domain.RaakaAine;
import tikape.runko.domain.Resepti;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:reseptit.db");
        database.init();
        RaakaAineDao raakaAineDao = new RaakaAineDao(database);
        ReseptiDao reseptiDao = new ReseptiDao(database);
        OhjeRiviDao ohjeRiviDao = new OhjeRiviDao(database);

        //Etusivu
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            List<Resepti> reseptit = reseptiDao.findAll();
            map.put("reseptit", reseptit );

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        //Reseptin tarkastelu "view" moodissa        
        get("/reseptit", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("reseptit", reseptiDao.findAll());

            return new ModelAndView(map, "reseptit");
        }, new ThymeleafTemplateEngine());

        //Raaka-aineiden tarkastelu ja muokkaus        
        get("/raakaaineet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaaineet", raakaAineDao.findAll());

            return new ModelAndView(map, "raakaaineet");
        }, new ThymeleafTemplateEngine());

        //Raaka-aineen lisäys        
        Spark.post("/raakaaineet", (req,res) ->{
            if(req.queryParams("nimi").isEmpty()){
                res.redirect("/raakaaineet");
                return "";
            }
            RaakaAine raakaAine= new RaakaAine(-1,req.queryParams("nimi").toLowerCase());
            raakaAineDao.add(raakaAine);
            res.redirect("/raakaaineet");
            return "";
        });

        //Ohjerivin lisäys reseptiin
        Spark.post("/lisaarivi", (req,res) ->{
            int raakaAineId = Integer.parseInt(req.queryParams("raakaAine"));
            RaakaAine raakaAine = raakaAineDao.findOne(raakaAineId);
            int rivinumero;
            try{
                rivinumero = Integer.parseInt(req.queryParams("rivinumero"));
            }catch (Exception e){
                rivinumero = -1;
            }
            String maara = req.queryParams("maara");
            int reseptinumero = Integer.parseInt(req.queryParams("reseptinumero"));
            OhjeRivi ohjerivi = new OhjeRivi(-1, raakaAine, maara, rivinumero);
            ohjeRiviDao.addNew(ohjerivi, reseptinumero);
            
            res.redirect("/reseptit/"+reseptinumero);
            return "";
        });

        //Ohjerivin poisto reseptistä        
        Spark.post("/poistarivi", (req,res) -> {
            int riviId = Integer.parseInt(req.queryParams("riviid"));
            System.out.println("rivinumero = " + riviId);
            int reseptinumero = Integer.parseInt(req.queryParams("reseptinumero"));
            ohjeRiviDao.delete(riviId);
            res.redirect("/reseptit/"+reseptinumero);
            return "";
        });

        //Raaka-aineen poisto        
        Spark.post("/poistaraakaaine", (req,res) -> {
            int raakaAineId = Integer.parseInt(req.queryParams("raakaaine"));
            raakaAineDao.delete(raakaAineId);
            res.redirect("/raakaaineet");
            return "";
        });

        //Ohjeen lisäys/päivitys reseptiin        
        Spark.post("/lisaaohje", (req,res)->{
            int reseptinumero = Integer.parseInt(req.queryParams("reseptinumero"));
            reseptiDao.paivitaReseptinOhje(reseptinumero, req.queryParams("ohje"));
            res.redirect("/reseptit/"+reseptinumero);
            return "";
        });


        //Lisää uusi resepti        
        Spark.post("/reseptit/lisaa", (req,res) ->{
            if(req.queryParams("nimi").isEmpty()){
                res.redirect("/reseptit");
                return "";
            }
            reseptiDao.addNew(req.queryParams("nimi").toUpperCase());
            res.redirect("/reseptit");
            return "";
        });

        //Editoi reseptiä        
        Spark.get("/reseptit/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer reseptiId = Integer.parseInt(req.params(":id"));
            map.put("resepti", reseptiDao.findOne(reseptiId));
            map.put("ohjeRivit", ohjeRiviDao.findAllForResepti(reseptiId));
            map.put("raakaAineet", raakaAineDao.findAll());
            return new ModelAndView(map, "resepti");
        }, new ThymeleafTemplateEngine());

        //Tarkastele reseptiä view moodissa
        Spark.get("/resepti_view/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer reseptiId = Integer.parseInt(req.params(":id"));
            map.put("resepti", reseptiDao.findOne(reseptiId));
            map.put("ohjeRivit", ohjeRiviDao.findAllForResepti(reseptiId));
            map.put("raakaAineet", raakaAineDao.findAll());
            return new ModelAndView(map, "resepti_view");
        }, new ThymeleafTemplateEngine());
        

        //Poista resepti
        Spark.post("/reseptit/:id/poista", (req, res) -> {
            Integer reseptiId = Integer.parseInt(req.params(":id"));
            reseptiDao.delete(reseptiId);
            res.redirect("/reseptit");
            return "";
        });
        

        
    }

}
