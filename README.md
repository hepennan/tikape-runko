# tikape-runko

https://github.com/hepennan/tikape-runko




# HELENAN PARHAAT RESEPTIT sovellus

## 1. Etusivu

Sovelluksella on etusivu, joka on nähtävissä sovelluksen juuripolussa (“/”).
Etusivulla listataan sovellukseen tallennettujen reseptien nimet aakkosjärjestyksessä.
Jokainen reseptin nimi on linkki sivulle, joka näyttää kyseisen reseptin sisällön "view" moodissa.
Etusivulta on myös linkki reseptien päivitykseen ja raaka-aineiden tarkasteluun/päivitykseen.


## 2. Reseptin tarkastelu "view" moodissa

Sivu näyttää reseptin nimen, ainekset ja yleisohjeen. Ainesten järjestys noudattaa reseptin kirjoittajan määrittelemää järjestystä.

## 3. Raaka-aineiden tarkastelu ja muokkaus

Raaka-aineet sivulla on listaus uusista raaka-aineista, joitä ei vielä ole linkattu mihinkään reseptiin. Näitä raaka-aineita on mahdollista poistaa listalta.
Toinen lista näyttää ne raaka-aineet, jotka on jo lisätty johonkin reseptiin. Näiden poistaminen ei ole mahdollista.

Käyttäjä voi myös lisätä uuden raaka-aineen.

## 4. Reseptien listaus "edit" moodissa

Sivu näyttää listauksen resepteistä aakkosjärjestyksessä. 
Käyttäjä voi poistaa reseptin, jolloin resepti ja kaikki reseptiin liittyvät ohjerivit poistetaan tietokannasta.

Jokainen reseptin nimi on linkki reseptin muokkaussivulle.

Käyttäjä voi antaa uuden reseptin nimen ja pääsee reseptin muokkaussivulle päivittämään reseptiä.

## 5. Reseptin muokkaus

Reseptin muokkaus sivu listaa reseptin ainekset ja määrät käyttäjän määrittelemässä järjestyksesä.
Käyttäjä voi poistaa aineksia listalta.
Käyttäjä voi lisätä raaka-aineita listalle valitsemalla raaka-aineen alasvetovalikosta ja antamalla tarvittavan määrän.
Jos käyttäjän haluaa määritellä raaka-aineen listalla tiettyyn paikkaan, hänen tulee antaa rivinumero. 
Jos rivinumeroa ei anneta, ohjerivi tallennetaan automaattisesti listan viimeiseksi.
Ohjerivin (raaka-aine ja määrä) lisäyksen/poiston jäkeen aineslista on oikein päivitetyssä järjestyksessä eli aineksen voi lisätä mihin tahansa väliin.

Käyttäjä voi päivittää ohjeen tekstiä.

Reseptiin voi lisätä useita raaka-aineita. Saman raaka-aineen voi lisätä yhteen reseptiin vain kerran. 
Yhden raaka-aineen voi lisätä useaan eri reseptiin.





