package lesson_3;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static int current_number = -1;

    static void main() {
        System.out.println("Ваша задача угодать число");

        for (int i = 50; i <= 150; i += 50) playLevel(i);

        System.out.println("Вы выиграли!");

        scanner.close();
    }

    private static void playLevel(int range) {
        int number = (int)(Math.random() * range);
        while (true) {
            System.out.println("Угадайте число от 0 до " + range);
            int input_number = scanner.nextInt();

            if (current_number == input_number) {
                System.out.println("Долбоеб?");
                continue;
            }


            current_number = input_number;

            if (input_number == number) {
                System.out.println("Вы угадали!");
                break;
            }

            if (input_number > number) {
                System.out.println("Загаданное число меньше");
            } else {
                System.out.println("Загаданное число больше");
            }
        }
    }
}
