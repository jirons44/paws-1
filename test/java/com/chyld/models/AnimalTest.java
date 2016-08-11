package com.chyld.models;

import com.chyld.enums.Sex;
import com.chyld.util.Mysql;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import static org.junit.Assert.*;

public class AnimalTest {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Before
    public void setUp() throws Exception {
        Session session = Mysql.getSession();
        session.beginTransaction();
        session.createNativeQuery("set FOREIGN_KEY_CHECKS = 0").executeUpdate();
        session.createNativeQuery("truncate table animals").executeUpdate();
        session.createNativeQuery("set FOREIGN_KEY_CHECKS = 1").executeUpdate();
        Animal animal = new Animal("Fido", format.parse("2000-01-03"), Sex.M, "Dog", format.parse("2015-12-11"));
        session.save(animal);
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
    public void shouldCreateNewAnimalAndSave() throws Exception {
        Session session = Mysql.getSession();
        session.beginTransaction();
        Animal animal = new Animal("Molly", format.parse("2000-01-03"), Sex.F, "Cat", format.parse("2015-12-11"));
        session.save(animal);
        session.getTransaction().commit();
        session.close();
        assertEquals(2, animal.getId());
    }

    @Test(expected = org.hibernate.PropertyValueException.class)
    public void shouldNotSaveDueToNullName() throws Exception {
        Session session = Mysql.getSession();
        session.beginTransaction();
        Animal animal = new Animal();

        try {
            session.save(animal);
            session.getTransaction().commit();
        }finally {
            session.close();
        }
    }

    // **************
    // *** READ ***
    // **************

    @Test
    public void shouldGetExistingAnimal() throws Exception {
        Session session = Mysql.getSession();
        Animal animal = session.get(Animal.class, 1);
        session.close();
        assertEquals(1, animal.getId());
        assertEquals("Fido", animal.getName());
        assertEquals(Sex.M, animal.getSex());
    }

    // **************
    // *** UPDATE ***
    // **************

    @Test
    public void shouldUpdateExistingAnimal() throws Exception {
        Session session = Mysql.getSession();

        session.beginTransaction();
        Animal a1 = session.get(Animal.class, 1);
        a1.setName("Bilbo");
        session.getTransaction().commit();

        session.refresh(a1);
        session.close();

        assertEquals(1, a1.getId());
        assertEquals("Bilbo", a1.getName());
    }

    // **************
    // *** DELETE ***
    // **************

    @Test
    public void shouldDeleteExistingAnimal() throws Exception {
        Session session = Mysql.getSession();
        session.beginTransaction();
        Animal animal = new Animal();
        animal.setId(1);
        animal.setName("delete it");
        session.delete(animal);
        session.getTransaction().commit();
        session.close();
    }
}
