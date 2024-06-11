package database.hibernate;

import database.hibernate.models.Animal;
import database.hibernate.models.Places;
import database.hibernate.models.Workman;
import database.hibernate.models.Zoo;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactoryCreator {
    private static SessionFactory sessionFactory = null;
    // Метод для создания или возврата существующего объекта SessionFactory
    public static SessionFactory createSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory();
        }
        return sessionFactory;
    }
    //метод для построения объекта SessionFactory
    private static SessionFactory buildSessionFactory() {
        // Создание конфигурации Hibernate и загрузка конфигурационного файла hibernate.cfg.xml
        Configuration configuration = new Configuration().configure();
        // Добавление классов, которые будут использоваться в Hibernate и отображены на таблицы в базе данных
        configuration.addAnnotatedClass(Animal.class);
        configuration.addAnnotatedClass(Places.class);
        configuration.addAnnotatedClass(Workman.class);
        configuration.addAnnotatedClass(Zoo.class);
        // Построение реестра сервисов Hibernate для создания объекта SessionFactory
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        sessionFactory = configuration.buildSessionFactory(builder.build());
        // Возвращение готового объекта SessionFactory
        return sessionFactory;
    }
}
