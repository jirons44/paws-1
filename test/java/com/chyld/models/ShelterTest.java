package com.chyld.models;

import com.chyld.enums.Sex;
import com.chyld.util.Mysql;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;

import static org.junit.Assert.*;

public class ShelterTest {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Before
    public void setUp() throws Exception {
        Session session = Mysql.getSession();
        session.beginTransaction();
        session.createNativeQuery("set FOREIGN_KEY_CHECKS = 0").executeUpdate();
        session.createNativeQuery("truncate table animals").executeUpdate();
        session.createNativeQuery("truncate table shelters").executeUpdate();
        session.createNativeQuery("set FOREIGN_KEY_CHECKS = 1").executeUpdate();
        Shelter shelter = new Shelter("Furry Friends", format.parse("2013-02-11"));
        Animal a1 = new Animal("Fido", format.parse("2000-01-03"), Sex.M, "Dog", format.parse("2013-12-11"), shelter);
        Animal a2 = new Animal("Molly", format.parse("2001-01-03"), Sex.F, "Cat", format.parse("2014-12-11"), shelter);
        Animal a3 = new Animal("Fluffy", format.parse("2002-01-03"), Sex.F, "Cat", format.parse("2015-12-11"), shelter);
        shelter.getAnimals().add(a1);
        shelter.getAnimals().add(a2);
        shelter.getAnimals().add(a3);
        session.save(shelter);
        session.getTransaction().commit();
        session.close();
    }

    @After
    public void tearDown() throws Exception {
    }

    // ************************
    // *** PROOF OF CONCEPT ***
    // ************************

    @Test
    public void notATestProofOfConcept() throws Exception {
        Session session = Mysql.getSession();
        session.beginTransaction();
        session.getTransaction().commit();
        session.close();
    }

    // **************
    // *** CREATE ***
    // **************

    @Test
    public void shouldCreateNewShelterAndSave() throws Exception {
        Session session = Mysql.getSession();
        session.beginTransaction();
        Shelter shelter = new Shelter("Happi Tails", format.parse("2013-02-11"));
        session.save(shelter);
        session.getTransaction().commit();
        session.close();
        assertEquals(2, shelter.getId());
    }

    @Test(expected = org.hibernate.exception.DataException.class)
    public void shouldNotSaveDueToNameTooLong() throws Exception {
        Session session = Mysql.getSession();
        session.beginTransaction();
        Shelter shelter = new Shelter("01234567890123456789012345678901234567890123456789", format.parse("2013-02-11"));

        try {
            session.save(shelter);
            session.getTransaction().commit();
        }finally {
            session.close();
        }
    }

    @Test(expected = org.hibernate.exception.ConstraintViolationException.class)
    public void shouldNotSaveDueToNotUnique() throws Exception {
        Session session = Mysql.getSession();
        session.beginTransaction();
        Shelter shelter = new Shelter("Furry Friends", format.parse("2013-02-11"));

        try {
            session.save(shelter);
            session.getTransaction().commit();
        }finally {
            session.close();
        }
    }

    @Test(expected = org.hibernate.PropertyValueException.class)
    public void shouldNotSaveDueToNullName() throws Exception {
        Session session = Mysql.getSession();
        session.beginTransaction();
        Shelter shelter = new Shelter();

        try {
            session.save(shelter);
            session.getTransaction().commit();
        }finally {
            session.close();
        }
    }

    // **************
    // *** READ ***
    // **************

    @Test
    public void shouldGetExistingShelter() throws Exception {
        Session session = Mysql.getSession();
        Shelter shelter = session.get(Shelter.class, 1);
        session.close();
        assertEquals(1, shelter.getId());
        assertEquals("Furry Friends", shelter.getName());
    }

    // **************
    // *** UPDATE ***
    // **************

    @Test
    public void shouldUpdateExistingShelter() throws Exception {
        Session session = Mysql.getSession();
        session.beginTransaction();
        Shelter shelter = session.get(Shelter.class, 1);
        shelter.setName("Tails");
        session.getTransaction().commit();
        session.close();
        assertEquals(1, shelter.getId());
        assertEquals("Tails", shelter.getName());
    }
}
