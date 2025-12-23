package dao;

import animals.Animal;
import animals.Color;
import utils.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AnimalDaoImpl implements AnimalDao {
    @Override
    public void save(Animal animal) {
        String sql = "INSERT INTO animals_otus_petrukhno (name, age, weight, color, type) VALUES (?, ?, ?, ?, ?)";
        try (ConnectionManager cm = new ConnectionManager();
             PreparedStatement ps = cm.getConnection().prepareStatement(sql)) {
            ps.setString(1, animal.getName());
            ps.setInt(2, animal.getAge());
            ps.setInt(3, animal.getWeight());
            ps.setString(4, animal.getColor() != null ? animal.getColor().name() : null);
            ps.setString(5, animal.getClass().getSimpleName().toUpperCase());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Animal> findAll() {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT * FROM animals_otus_petrukhno ORDER BY id ASC ";
        try (ConnectionManager cm = new ConnectionManager();
             Statement stmt = cm.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String type = rs.getString("type");
                Animal animal;
                switch (type) {
                    case "CAT":
                        animal = new animals.Cat();
                        break;
                    case "DOG":
                        animal = new animals.Dog();
                        break;
                    case "DUCK":
                        animal = new birds.Duck();
                        break;
                    default:
                        continue;
                }
                fillAnimalFromResultSet(animal, rs);
                animals.add(animal);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return animals;
    }

    @Override
    public void update(Animal animal) {
        String sql = "UPDATE animals_otus_petrukhno SET name = ?, age = ?, weight = ?, color = ?, type = ? WHERE id = ?";
        try (ConnectionManager cm = new ConnectionManager();
             PreparedStatement ps = cm.getConnection().prepareStatement(sql)) {
            ps.setString(1, animal.getName());
            ps.setInt(2, animal.getAge());
            ps.setInt(3, animal.getWeight());
            ps.setString(4, animal.getColor() != null ? animal.getColor().name() : null);
            ps.setString(5, animal.getClass().getSimpleName().toUpperCase());
            ps.setInt(6, animal.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Animal> findByType(String type) {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT * FROM animals_otus_petrukhno WHERE type = ? ORDER BY id ASC";
        try (ConnectionManager cm = new ConnectionManager();
             PreparedStatement ps = cm.getConnection().prepareStatement(sql)) {
            ps.setString(1, type.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Animal animal;
                    switch (type.toUpperCase()) {
                        case "CAT":
                            animal = new animals.Cat();
                            break;
                        case "DOG":
                            animal = new animals.Dog();
                            break;
                        case "DUCK":
                            animal = new birds.Duck();
                            break;
                        default:
                            continue;
                    }
                    fillAnimalFromResultSet(animal, rs);
                    animals.add(animal);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return animals;
    }

    @Override
    public Animal findById(int id) {
        String sql = "SELECT * FROM animals_otus_petrukhno WHERE id = ?";
        try (ConnectionManager cm = new ConnectionManager();
             PreparedStatement ps = cm.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String type = rs.getString("type");
                    Animal animal;
                    switch (type) {
                        case "CAT":
                            animal = new animals.Cat();
                            break;
                        case "DOG":
                            animal = new animals.Dog();
                            break;
                        case "DUCK":
                            animal = new birds.Duck();
                            break;
                        default:
                            return null;
                    }
                    fillAnimalFromResultSet(animal, rs);
                    return animal;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private void fillAnimalFromResultSet(Animal animal, ResultSet rs) throws SQLException {
        animal.setName(rs.getString("name"));
        animal.setAge(rs.getInt("age"));
        animal.setWeight(rs.getInt("weight"));
        String colorStr = rs.getString("color");
        if (colorStr != null) {
            try {
                animal.setColor(Color.valueOf(colorStr));
            } catch (IllegalArgumentException e) {
                animal.setColor(null);
            }
        }
        animal.setId(rs.getInt("id"));
    }
}