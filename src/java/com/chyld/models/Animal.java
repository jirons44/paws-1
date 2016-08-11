package com.chyld.models;

import com.chyld.enums.Sex;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "animals")
@Access(AccessType.PROPERTY)
public class Animal {
    private int id;
    private String name;
    private Date birthday;
    private Sex sex;
    private String species;
    private Date placement;
    private Shelter shelter;

    public Animal() {
    }

    public Animal(String name, Date birthday, Sex sex, String species, Date placement) {
        this(name, birthday, sex, species, placement, null);
    }

    public Animal(String name, Date birthday, Sex sex, String species, Date placement, Shelter shelter) {
        this.name = name;
        this.birthday = birthday;
        this.sex = sex;
        this.species = species;
        this.placement = placement;
        this.shelter = shelter;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    @Basic
    @Column(name = "name", nullable = false, length = 45)
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "birthday")
    public Date getBirthday() {return birthday;}
    public void setBirthday(Date birthday) {this.birthday = birthday;}

    @Basic
    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    public Sex getSex() {return sex;}
    public void setSex(Sex sex) {this.sex = sex;}

    @Basic
    @Column(name = "species", length = 45)
    public String getSpecies() {return species;}
    public void setSpecies(String species) {this.species = species;}

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "placement")
    public Date getPlacement() {return placement;}
    public void setPlacement(Date placement) {this.placement = placement;}

    @ManyToOne
    @JoinColumn(name = "shelter_id", referencedColumnName = "id")
    public Shelter getShelter() {return shelter;}
    public void setShelter(Shelter shelter) {this.shelter = shelter;}
}
