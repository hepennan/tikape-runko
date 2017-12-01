/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.RaakaAine;

/**
 *
 * @author Helena
 */
public class RaakaAineDao implements Dao<RaakaAine,Integer>{
    private Database database;
    
    public RaakaAineDao(Database database){
        this.database = database;
    }

    @Override
    public RaakaAine findOne(Integer key) throws SQLException {
        RaakaAine raakaAine = null;
        try(Connection conn = database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM RaakaAine WHERE id=?");
            stmt.setInt(1, key);
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                String nimi = res.getString("nimi");
                raakaAine = new RaakaAine(key, nimi);
            }
            
        }
        if(raakaAine==null){
            System.out.println("Raaka-aine not found!!");
        }
        return raakaAine;
    }

//palauttaa kaikki raakaAineet aakkosjärjestyksessä
    @Override
    public List<RaakaAine> findAll() throws SQLException {
        List<RaakaAine> raakaAineet = new ArrayList<>();
        try(Connection conn = database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM RaakaAine ORDER BY nimi ASC");
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                String nimi = res.getString("nimi");
                int id = res.getInt("id");
                RaakaAine raakaAine = new RaakaAine(id,nimi);
                raakaAineet.add(raakaAine);
            }
        }
        return raakaAineet;
    }

    public List<RaakaAine> findAllKaytossa() throws SQLException {
        List<RaakaAine> raakaAineet = new ArrayList<>();
        try(Connection conn = database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM RaakaAine ORDER BY nimi ASC");
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                String nimi = res.getString("nimi");
                int id = res.getInt("id");
                RaakaAine raakaAine = new RaakaAine(id,nimi);
                if(sisaltyyJonkonkinReseptiin(id)){
                    raakaAineet.add(raakaAine);
                }
                
            }
        }
        return raakaAineet;
    }
    
       public List<RaakaAine> findAllEiKaytossa() throws SQLException {
        List<RaakaAine> raakaAineet = new ArrayList<>();
        try(Connection conn = database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM RaakaAine ORDER BY nimi ASC");
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                String nimi = res.getString("nimi");
                int id = res.getInt("id");
                RaakaAine raakaAine = new RaakaAine(id,nimi);
                if(!sisaltyyJonkonkinReseptiin(id)){
                    raakaAineet.add(raakaAine);
                }
                
            }
        }
        return raakaAineet;
    }
        
    
    
    public boolean sisaltyyJonkonkinReseptiin(Integer key) throws SQLException {
        try(Connection conn = database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ReseptiRaakaAine WHERE raaka_aine_id = ?");
            stmt.setInt(1, key);
            ResultSet res = stmt.executeQuery();
            if(res.next()){
                return true;
            }
        }
        
        return false;
    }

    //deletoi raakaAineen jos sitä ei ole linkattu johonkin reseptiin
    @Override
    public void delete(Integer key) throws SQLException {
        try(Connection conn = database.getConnection()){
            if(raakaAineKuuluuReseptiin(key)){
                return;
            }
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM RaakaAine WHERE id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
        }
    }
    
    //tsekkaa kuuluuko raaka-aine johonkin reseptiin
    public boolean raakaAineKuuluuReseptiin(Integer key)throws SQLException{
        try(Connection conn = database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ReseptiRaakaAine WHERE raaka_aine_id = ?");
            stmt.setInt(1, key);
            ResultSet res = stmt.executeQuery();
            if(res.next()){
                return true;
            }
            return false;
        }
    }
    
    //lisää raaka-aineen jos sitä ei löydy taulusta
    public RaakaAine add(RaakaAine lisattava) throws SQLException {
        try(Connection conn= this.database.getConnection()){
            RaakaAine raakaAine = this.findOneByNimi(lisattava.getNimi());
            if(raakaAine!= null){
                return raakaAine;
            }
                
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO RaakaAine (nimi) VALUES(?)");
            stmt.setString(1, lisattava.getNimi());
            stmt.executeUpdate();
            return this.findOneByNimi(lisattava.getNimi());
            
        }
    }
    
    //etsii raaka-aineen nimen perusteella
    public RaakaAine findOneByNimi(String key) throws SQLException {
        RaakaAine raakaAine = null;
        try(Connection conn = database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM RaakaAine WHERE LOWER(nimi)=?");
            stmt.setString(1, key.toLowerCase());
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                String nimi = res.getString("nimi");
                int id = res.getInt("id");
                raakaAine = new RaakaAine(id, nimi);
            }
            
        }
        return raakaAine;
    }
    
}
