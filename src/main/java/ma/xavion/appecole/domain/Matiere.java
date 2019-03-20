package ma.xavion.appecole.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Matiere.
 */
@Entity
@Table(name = "matiere")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "matiere")
public class Matiere implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titre")
    private String titre;

    @Column(name = "description")
    private String description;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "matiere_niveau",
               joinColumns = @JoinColumn(name = "matiere_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "niveau_id", referencedColumnName = "id"))
    private Set<Niveau> niveaus = new HashSet<>();

    @OneToMany(mappedBy = "matiere")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Note> ids = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public Matiere titre(String titre) {
        this.titre = titre;
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public Matiere description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Niveau> getNiveaus() {
        return niveaus;
    }

    public Matiere niveaus(Set<Niveau> niveaus) {
        this.niveaus = niveaus;
        return this;
    }

    public Matiere addNiveau(Niveau niveau) {
        this.niveaus.add(niveau);
        niveau.getIds().add(this);
        return this;
    }

    public Matiere removeNiveau(Niveau niveau) {
        this.niveaus.remove(niveau);
        niveau.getIds().remove(this);
        return this;
    }

    public void setNiveaus(Set<Niveau> niveaus) {
        this.niveaus = niveaus;
    }

    public Set<Note> getIds() {
        return ids;
    }

    public Matiere ids(Set<Note> notes) {
        this.ids = notes;
        return this;
    }

    public Matiere addId(Note note) {
        this.ids.add(note);
        note.setMatiere(this);
        return this;
    }

    public Matiere removeId(Note note) {
        this.ids.remove(note);
        note.setMatiere(null);
        return this;
    }

    public void setIds(Set<Note> notes) {
        this.ids = notes;
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
        Matiere matiere = (Matiere) o;
        if (matiere.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), matiere.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Matiere{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
