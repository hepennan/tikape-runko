/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Helena
 */
public class Resepti {
    private String nimi;
    private int id;
    private String ohje = "";
    private List<OhjeRivi> ohjerivit;
    
    public Resepti(int id, String nimi){
        this.id = id;
        this.nimi = nimi;
        this.ohjerivit= new ArrayList<>();
    }
    
    public Resepti(int id, String nimi, String ohje, List<OhjeRivi> ohjerivit){
        this.id = id;
        this.nimi = nimi;
        this.ohje = ohje;
        this.ohjerivit= ohjerivit;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<OhjeRivi> getOhjerivit() {
        return ohjerivit;
    }

    public String getOhje() {
        return ohje;
    }

    public void setOhje(String ohje) {
        this.ohje = ohje;
    }
    
    

    public void setOhjerivit(List<OhjeRivi> ohjerivit) {
        this.ohjerivit = ohjerivit;
    }
    
}
