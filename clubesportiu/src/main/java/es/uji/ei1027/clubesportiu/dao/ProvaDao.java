package es.uji.ei1027.clubesportiu.dao;

import es.uji.ei1027.clubesportiu.model.Prova;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;


@Repository // En Spring els DAOs van anotats amb @Repository
public class ProvaDao {

    private JdbcTemplate jdbcTemplate;

    // Obté el jdbcTemplate a partir del Data Source
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Afegeix la prova a la base de dades */
    public void addProva(Prova prova) {
        jdbcTemplate.update(
                "INSERT INTO Prova VALUES(?, ?, ?, ?)",
                prova.getNom(), prova.getDescripcio(), prova.getTipus(), prova.getData()
        );
    }

    /* Esborra la prova de la base de dades */
    public void deleteProva(Prova prova) {
        jdbcTemplate.update(
                "DELETE FROM Prova WHERE nom = ?",
                prova.getNom()
        );
    }

    /* Actualitza els atributs de la prova
       (excepte el nom, que és la clau primària) */
    public void updateProva(Prova prova) {
        jdbcTemplate.update(
                "UPDATE Prova SET descripcio = ?, tipus = ?, data = ? WHERE nom = ?",
                prova.getDescripcio(), prova.getTipus(), prova.getNom(), prova.getData()
        );
    }

    /* Obté la prova amb el nom donat. Torna null si no existeix. */
    public Prova getProva(String nomProva) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM Prova WHERE nom =?",
                    new ProvaRowMapper(),
                    nomProva
            );
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    /* Obté tots els proves. Torna una llista buida si no n'hi ha cap. */
    public List<Prova> getProves() {
        try {
            return jdbcTemplate.query("SELECT * FROM Prova",
                    new ProvaRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Prova>();
        }
    }
}
