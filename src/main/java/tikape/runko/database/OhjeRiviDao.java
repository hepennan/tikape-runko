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
import tikape.runko.domain.OhjeRivi;
import tikape.runko.domain.RaakaAine;

/**
 *
 * @author Helena
 */
public class OhjeRiviDao implements Dao<OhjeRivi, Integer>{
    
    private Database database;
    private RaakaAineDao raakaAineDao;

    
    public OhjeRiviDao(Database database){
        this.database = database;
        this.raakaAineDao = new RaakaAineDao(database);

    }

    
    
    @Override
    public OhjeRivi findOne(Integer key) throws SQLException {
        OhjeRivi ohjeRivi = null;
        try(Connection conn = database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM OhjeRivi WHERE id = ?");
            stmt.setInt(1, key);
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                int raakaAineId = res.getInt("raaka_aine_id");
                RaakaAine raakaAine = this.raakaAineDao.findOne(raakaAineId);
                String maara = res.getString("maara");
                int rivinumero = res.getInt("rivinumero");
                int id = res.getInt("id");
                ohjeRivi = new OhjeRivi(id, raakaAine, maara, rivinumero);
            }
        }
        return ohjeRivi;
    }

    @Override
    public List<OhjeRivi> findAll() throws SQLException {
        List<OhjeRivi> ohjerivit = new ArrayList<>();
        try(Connection conn = database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM OhjeRivi");
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                int raakaAineId = res.getInt("raaka_aine_id");
                RaakaAine raakaAine = this.raakaAineDao.findOne(raakaAineId);
                String maara = res.getString("maara");
                int rivinumero = res.getInt("rivinumero");
                int id = res.getInt("id");
                OhjeRivi ohjeRivi = new OhjeRivi(id, raakaAine, maara, rivinumero);
                ohjerivit.add(ohjeRivi);
            }
        }
        
        return ohjerivit;
    }

//palauttaa ohjerivin reseptiId:n
    public int getReseptiId(Integer key) throws SQLException{
        try(Connection conn = database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT resepti_id FROM OhjeRivi WHERE id=?");
            stmt.setInt(1, key);
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                return res.getInt("resepti_id");
            }
            
        }
        return -1;
    }
    
    //poistaa kaikki reseptiin linkatut ohjerivit
    //metodia kutsutaan silloin kun resepti deletoidaan
    public void deleteAllForResepti(Integer key) throws SQLException {
        try(Connection conn = database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM OhjeRivi WHERE resepti_id = ?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
            
        }
    }
    
    //palauttaa kaikki ohjerivit reseptille
    public List<OhjeRivi> findAllForResepti (int reseptiId)throws SQLException {
        List<OhjeRivi> ohjerivit = new ArrayList<>();
        try(Connection conn = database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM OhjeRivi WHERE resepti_id = ? ORDER BY rivinumero ASC");
            stmt.setInt(1, reseptiId);
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                int raakaAineId = res.getInt("raaka_aine_id");
                RaakaAine raakaAine = this.raakaAineDao.findOne(raakaAineId);
                String maara = res.getString("maara");
                int rivinumero = res.getInt("rivinumero");
                int id = res.getInt("id");
                OhjeRivi ohjeRivi = new OhjeRivi(id, raakaAine, maara, rivinumero);
                ohjerivit.add(ohjeRivi);
            }
        }
        
        return ohjerivit;
        
    }
    
    //reseptille lisätään uusi ohjerivi
    public void addNew(OhjeRivi ohjerivi, int reseptiId) throws SQLException {
        try(Connection conn = database.getConnection()){
            if(reseptiRaakaAineExists(reseptiId, ohjerivi.getRaakaAine().getId())){
                return;
            }
            int maxRivinumero = findAllForResepti(reseptiId).size()+1;
            if(ohjerivi.getRivinumero()<1){
                ohjerivi.setRivinumero(maxRivinumero);
            } else if(ohjerivi.getRivinumero()>maxRivinumero){
                ohjerivi.setRivinumero(maxRivinumero);
            }
            paivitaOhjeriviNumerot(reseptiId,ohjerivi.getRivinumero(),1);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO OhjeRivi(raaka_aine_id, resepti_id,maara,rivinumero)\n"+
                    "VALUES (?,?,?,?)");
            stmt.setInt(1, ohjerivi.getRaakaAine().getId());
            stmt.setInt(2, reseptiId);
            stmt.setString(3, ohjerivi.getMaara());
            stmt.setInt(4, ohjerivi.getRivinumero());
            stmt.executeUpdate();

        }
        
    }
    
//    päivittää ohjerivien numerot silloin kun reseptiin lisätään tai reseptistä poistetaan aineksia
    public void paivitaOhjeriviNumerot(int reseptiId, int start, int muutos) throws SQLException {
        try(Connection conn = database.getConnection()){
            PreparedStatement stmt;
            stmt = conn.prepareStatement("UPDATE OhjeRivi SET rivinumero=rivinumero+? WHERE resepti_id=? AND rivinumero>=?");
            stmt.setInt(2, reseptiId);
            stmt.setInt(3, start);
            stmt.setInt(1, muutos);
            stmt.executeUpdate();
        }
        
    }



    @Override
    public void delete(Integer key) throws SQLException {
        try(Connection conn = database.getConnection()){
            int rivinumero = findOne(key).getRivinumero();
            int reseptiId = getReseptiId(key);
            //rivinumeroiden järjestys päivitetään
            paivitaOhjeriviNumerot(reseptiId, rivinumero,-1);
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM OhjeRivi WHERE id =?");
            stmt.setInt(1, key);
            stmt.executeUpdate();
            
        }
    }
    
      

    
    public boolean reseptiRaakaAineExists(int reseptiId, int raakaAineId) throws SQLException {
        try(Connection conn = database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM OhjeRivi WHERE raaka_aine_id = ? AND resepti_id = ?");
            stmt.setInt(2, reseptiId);
            stmt.setInt(1, raakaAineId);
            ResultSet res = stmt.executeQuery();
            if(res.next()){
                return true;
            }
       }
        return false;
        
    }
    

    

}
