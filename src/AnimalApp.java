import animals.Animal;
import animals.Color;
import dao.AnimalDao;
import dao.AnimalDaoImpl;
import factory.AnimalFactory;
import factory.AnimalType;
import utils.AnimalTable;
import utils.ConnectionManager;

import java.util.List;
import java.util.Scanner;

public class AnimalApp {
    public static void main(String[] args) {
        ConnectionManager cm = ConnectionManager.getInstance();

        AnimalTable animalTable = new AnimalTable(cm);
        animalTable.create();

        AnimalDao animalDao = new AnimalDaoImpl(cm);
        AnimalFactory animalFactory = new AnimalFactory();

        Scanner scanner = new Scanner(System.in);
        Command currentCommand = null;

        while (currentCommand != Command.EXIT) {
            currentCommand = getCommand(scanner);
            if (currentCommand == Command.LIST) {
                List<Animal> animals = animalDao.findAll();
                if (animals.isEmpty()) {
                    System.out.println("Список пуст");
                }
                for (Animal animal : animals) {
                    System.out.println("[" + animal.getId() + "] " + animal);
                }
            } else if (currentCommand == Command.ADD) {
                AnimalType animalType = null;
                while (animalType == null) {
                    System.out.print("Какое животное добавить? (cat/dog/duck): ");
                    String typeInput = scanner.next().trim().toUpperCase();
                    try {
                        animalType = AnimalType.valueOf(typeInput);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Неверный тип животного!");
                    }
                }
                Animal animal = animalFactory.create(animalType);

                System.out.print("Введите имя животного: ");
                animal.setName(scanner.next());

                int age;
                do {
                    System.out.print("Введите возраст животного: ");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Введите целое число!");
                        scanner.next();
                    }
                    age = scanner.nextInt();
                    scanner.nextLine();
                    if (age <= 0) {
                        System.out.println("Значение возраста должно быть положительным!");
                    }
                } while (age <= 0);

                animal.setAge(age);

                int weight;
                do {
                    System.out.print("Введите вес животного: ");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Введите целое число!");
                        scanner.next();
                    }
                    weight = scanner.nextInt();
                    scanner.nextLine();
                    if (weight <= 0) {
                        System.out.println("Значение веса должно быть положительным!");
                    }
                } while (weight <= 0);

                animal.setWeight(weight);

                System.out.print("Введите цвет (BLACK/WHITE): ");
                String colorInput = scanner.nextLine().trim().toUpperCase();
                try {
                    animal.setColor(Color.valueOf(colorInput));
                } catch (IllegalArgumentException e) {
                    System.out.println("Неверный цвет, установлен по умолчанию.");
                    animal.setColor(null);
                }
                animalDao.save(animal);
                animal.say();
            } else if (currentCommand == Command.FILTER) {
                System.out.print("Введите тип животного для фильтрации (CAT/DOG/DUCK): ");
                String type = scanner.next().trim().toUpperCase();
                List<Animal> animals = animalDao.findByType(type);
                if (animals.isEmpty()) {
                    System.out.println("Список пуст");
                }
                for (Animal animal : animals) {
                    System.out.println("[" + animal.getId() + "] " + animal);
                }
            } else if (currentCommand == Command.EDIT) {
                System.out.print("Укажите id животного, которое хотите отредактировать: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Введите целое число!");
                    scanner.next();
                }
                int id = scanner.nextInt();
                scanner.nextLine();

                Animal animal = animalDao.findById(id);
                if (animal == null) {
                    System.out.println("Животное с таким id не найдено.");
                    continue;
                }

                System.out.print("Введите новый тип (CAT/DOG/DUCK, или Enter, чтобы оставить прежний): ");
                String typeInput = scanner.nextLine().trim().toUpperCase();
                if (!typeInput.isEmpty()) {
                    try {
                        AnimalType newType = AnimalType.valueOf(typeInput);
                        Animal newAnimal = animalFactory.create(newType);
                        newAnimal.setId(animal.getId());
                        newAnimal.setName(animal.getName());
                        newAnimal.setAge(animal.getAge());
                        newAnimal.setWeight(animal.getWeight());
                        newAnimal.setColor(animal.getColor());
                        animal = newAnimal;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Неверный тип, оставлен прежний.");
                    }
                }

                System.out.print("Введите новое имя (или Enter, чтобы оставить прежнее): ");
                String name = scanner.nextLine();
                if (!name.isEmpty()) animal.setName(name);

                System.out.print("Введите новый возраст (или 0, чтобы оставить прежний): ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Введите целое число!");
                    scanner.next();
                }
                int age = scanner.nextInt();
                scanner.nextLine();
                if (age > 0) animal.setAge(age);

                System.out.print("Введите новый вес (или 0, чтобы оставить прежний): ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Введите целое число!");
                    scanner.next();
                }
                int weight = scanner.nextInt();
                scanner.nextLine();
                if (weight > 0) animal.setWeight(weight);

                System.out.print("Введите новый цвет (BLACK/WHITE, или Enter, чтобы оставить прежний): ");
                String colorInput = scanner.nextLine().trim().toUpperCase();
                if (!colorInput.isEmpty()) {
                    try {
                        animal.setColor(Color.valueOf(colorInput));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Неверный цвет, оставлен прежний.");
                    }
                }

                animalDao.update(animal);
                System.out.println("Данные по животному " + animal.getName() + " успешно обновлены.");
            }
        }
        cm.close();
    }

    private static Command getCommand(Scanner scanner) {
        String commandInput = null;
        while (Command.doesNotContain(commandInput)) {
            if (commandInput != null) {
                System.out.println("Введена неверная команда");
            }
            System.out.printf("Введите одну из команд (%s): ", String.join("/", Command.NAMES));
            commandInput = scanner.next();
        }
        return Command.fromString(commandInput);
    }
}