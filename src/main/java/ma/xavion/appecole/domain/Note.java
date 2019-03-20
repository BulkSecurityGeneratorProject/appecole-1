package ma.xavion.appecole.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Note.
 */
@Entity
@Table(name = "note")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "note")
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "valeur")
    private Float valeur;

    @Column(name = "date_controle")
    private LocalDate dateControle;

    @ManyToOne
    @JsonIgnoreProperties("ids")
    private Matiere matiere;

    @ManyToOne
    @JsonIgnoreProperties("logins")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getValeur() {
        return valeur;
    }

    public Note valeur(Float valeur) {
        this.valeur = valeur;
        return this;
    }

    public void setValeur(Float valeur) {
        this.valeur = valeur;
    }

    public LocalDate getDateControle() {
        return dateControle;
    }

    public Note dateControle(LocalDate dateControle) {
        this.dateControle = dateControle;
        return this;
    }

    public void setDateControle(LocalDate dateControle) {
        this.dateControle = dateControle;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public Note matiere(Matiere matiere) {
        this.matiere = matiere;
        return this;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public User getUser() {
        return user;
    }

    public Note user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Note note = (Note) o;
        if (note.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), note.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Note{" +
            "id=" + getId() +
            ", valeur=" + getValeur() +
            ", dateControle='" + getDateControle() + "'" +
            "}";
    }
}
