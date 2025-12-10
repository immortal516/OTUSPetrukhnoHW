import animals.Animal;
import animals.Color;
import factory.AnimalFactory;
import factory.AnimalType;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AnimalApp {

    public static void main(String[] args) {
        List<Animal> animals = new ArrayList<>();
        AnimalFactory animalFactory = new AnimalFactory();

        Scanner scanner = new Scanner(System.in);
        Command currentCommand = null;

        while (currentCommand != Command.EXIT) {
            currentCommand = getCommand(scanner);
            if (currentCommand == Command.LIST) {
                if (animals.isEmpty()) {
                    System.out.println("Список пуст");
                }
                for (Animal animal : animals) {
                    System.out.println(animal);
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

                System.out.print("Введите возраст животного: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Введите число!");
                    scanner.next();
                }
                animal.setAge(scanner.nextInt());
                scanner.nextLine();

                System.out.print("Введите вес животного: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Введите число!");
                    scanner.next();
                }
                animal.setWeight(scanner.nextInt());
                scanner.nextLine();

                System.out.print("Введите цвет (BLACK/WHITE): ");
                String colorInput = scanner.nextLine().trim().toUpperCase();
                try {
                    animal.setColor(Color.valueOf(colorInput));
                } catch (IllegalArgumentException e) {
                    System.out.println("Неверный цвет, установлен по умолчанию.");
                    animal.setColor(null);
                }
                animals.add(animal);
                animal.say();
            }
        }
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