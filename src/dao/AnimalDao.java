package dao;

import animals.Animal;

import java.util.List;

public interface AnimalDao {
    void save(Animal animal);

    List<Animal> findAll();

    void update(Animal animal);

    List<Animal> findByType(String type);

    Animal findById(int id);
}