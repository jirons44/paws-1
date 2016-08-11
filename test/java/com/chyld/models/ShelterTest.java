package com.chyld.models;

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
        session.createNativeQuery("truncate table shelters").executeUpdate();
        Shelter shelter = new Shelter("Furry Friends", format.parse("2013-02-11"));
        session.save(shelter);
        session.getTransaction().commit();
        session.close();
    }

    @After
    public void tearDown() throws Exception {
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
        session.beginTransaction();
        Shelter shelter = session.get(Shelter.class, 1);
        session.getTransaction().commit();
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

    // **************
    // *** DELETE ***
    // **************

    @Test
    public void shouldDeleteExistingShelter() throws Exception {
        Session session = Mysql.getSession();
        session.beginTransaction();
        Shelter shelter = new Shelter();
        shelter.setId(1);
        shelter.setName("nuke it");
        session.delete(shelter);
        session.getTransaction().commit();
        session.close();
    }
}
