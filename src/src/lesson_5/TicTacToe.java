package lesson_5;

import java.util.Random;
import java.util.Scanner;

/**
 * Крестики-Нолики (Tic-Tac-Toe) с расширяемым полем
 * Игра на поле SIZE x SIZE (от 3 до 9), где игрок (X) против ИИ (O).
 * Для победы нужно собрать WIN_LENGTH подряд символов по строке, столбцу или диагонали.
 * ИИ пытается выиграть, блокировать или играет случайно.
 */
public class TicTacToe {
    private static int SIZE; // Размер поля (от 3 до 9)
    private static int WIN_LENGTH; // Длина линии для победы (от 3 до SIZE)
    private static final char DOT_EMPTY = '.'; // Пустая клетка
    private static final char DOT_X = 'X'; // Символ игрока
    private static final char DOT_O = 'O'; // Символ ИИ
    private static char[][] MAP; // Игровое поле (динамическое)
    private static final Scanner SCANNER = new Scanner(System.in); // Сканер для ввода
    private static final Random RANDOM = new Random(); // Генератор случайных чисел

    public static void main(String[] args) {
        // Ввод размера поля с проверкой (3-9)
        do {
            System.out.print("Введите размер поля (3-9): ");
            try {
                SIZE = SCANNER.nextInt();
            } catch (Exception e) {
                SCANNER.nextLine(); // Очищаем буфер
                SIZE = 0;
            }
        } while (SIZE < 3 || SIZE > 9);

        // Ввод длины победной линии с проверкой (3-SIZE)
        do {
            System.out.print("Введите длину победной линии (3-" + SIZE + "): ");
            try {
                WIN_LENGTH = SCANNER.nextInt();
            } catch (Exception e) {
                SCANNER.nextLine(); // Очищаем буфер
                WIN_LENGTH = 0;
            }
        } while (WIN_LENGTH < 3 || WIN_LENGTH > SIZE);

        MAP = new char[SIZE][SIZE]; // Инициализируем поле динамически

        initMap(); // Инициализация поля
        printMap(); // Печать начального поля

        // Основной цикл игры
        while (true) {
            // Ход игрока
            humanTurn();
            printMap();

            if (checkWin(DOT_X)) {
                System.out.println("Победил человек!");
                break;
            }

            if (isMapFull()) {
                System.out.println("Ничья!");
                break;
            }

            // Ход ИИ
            aiTurn();
            printMap();

            if (checkWin(DOT_O)) {
                System.out.println("Победил ИИ!");
                break;
            }

            if (isMapFull()) {
                System.out.println("Ничья!");
                break;
            }
        }

        System.out.println("Игра окончена!");
        SCANNER.close();
    }

    /**
     * Проверка победы для заданного символа (проверяет WIN_LENGTH подряд)
     */
    private static boolean checkWin(char symbol) {
        // Проверка строк
        for (int i = 0; i < SIZE; i++) {
            if (checkLine(MAP[i], symbol)) {
                return true;
            }
        }
        // Проверка столбцов
        for (int j = 0; j < SIZE; j++) {
            char[] col = new char[SIZE];
            for (int i = 0; i < SIZE; i++) {
                col[i] = MAP[i][j];
            }
            if (checkLine(col, symbol)) {
                return true;
            }
        }
        // Проверка диагоналей (главная и побочная)
        return checkDiagonals(symbol);
    }

    /**
     * Проверка диагоналей
     */
    private static boolean checkDiagonals(char symbol) {
        // Главная диагональ (слева сверху направо вниз)
        for (int startRow = 0; startRow <= SIZE - WIN_LENGTH; startRow++) {
            for (int startCol = 0; startCol <= SIZE - WIN_LENGTH; startCol++) {
                if (isWinningLine(startRow, startCol, 1, 1, symbol)) {
                    return true;
                }
            }
        }
        // Побочная диагональ (справа сверху налево вниз)
        for (int startRow = 0; startRow <= SIZE - WIN_LENGTH; startRow++) {
            for (int startCol = WIN_LENGTH - 1; startCol < SIZE; startCol++) {
                if (isWinningLine(startRow, startCol, 1, -1, symbol)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Проверка линии (строки, столбца или диагонали) на WIN_LENGTH подряд
     */
    private static boolean checkLine(char[] line, char symbol) {
        for (int i = 0; i <= line.length - WIN_LENGTH; i++) {
            boolean win = true;
            for (int j = 0; j < WIN_LENGTH; j++) {
                if (line[i + j] != symbol) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }
        return false;
    }

    /**
     * Проверка диагональной линии в заданном направлении
     */
    private static boolean isWinningLine(int startRow, int startCol, int rowDir, int colDir, char symbol) {
        for (int k = 0; k < WIN_LENGTH; k++) {
            if (MAP[startRow + k * rowDir][startCol + k * colDir] != symbol) {
                return false;
            }
        }
        return true;
    }

    /**
     * Проверка, заполнено ли поле
     */
    private static boolean isMapFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (MAP[i][j] == DOT_EMPTY) return false;
            }
        }
        return true;
    }

    /**
     * Ход ИИ: пытается выиграть, блокировать или ходит случайно
     */
    private static void aiTurn() {
        // 1. Попытаться выиграть
        if (tryWinOrBlock(DOT_O)) return;

        // 2. Попытаться заблокировать игрока
        if (tryWinOrBlock(DOT_X)) return;

        // 3. Случайный ход
        randomTurn();
    }

    /**
     * Вспомогательный метод для попытки выиграть или заблокировать (simulates a move)
     */
    private static boolean tryWinOrBlock(char symbol) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (MAP[i][j] == DOT_EMPTY) {
                    // Симулируем ход
                    MAP[i][j] = symbol;
                    if (checkWin(symbol)) {
                        MAP[i][j] = DOT_O; // Подтверждаем ход для ИИ
                        return true;
                    }
                    MAP[i][j] = DOT_EMPTY; // Откатываем
                }
            }
        }
        return false;
    }

    /**
     * Случайный ход ИИ
     */
    private static void randomTurn() {
        int x, y;
        do {
            x = RANDOM.nextInt(SIZE);
            y = RANDOM.nextInt(SIZE);
        } while (!isCellValid(x, y));
        MAP[y][x] = DOT_O;
    }

    /**
     * Ход человека с проверкой ввода
     */
    private static void humanTurn() {
        int x, y;
        while (true) {
            System.out.println("Введите координаты в формате X Y (1-" + SIZE + "): ");
            try {
                x = SCANNER.nextInt() - 1;
                y = SCANNER.nextInt() - 1;
                if (isCellValid(x, y)) break;
                System.out.println("Некорректные координаты! Попробуйте снова.");
            } catch (Exception e) {
                System.out.println("Ошибка ввода! Введите числа.");
                SCANNER.nextLine(); // Очищаем буфер
            }
        }
        MAP[y][x] = DOT_X;
    }

    /**
     * Проверка валидности клетки
     */
    private static boolean isCellValid(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE && MAP[y][x] == DOT_EMPTY;
    }

    /**
     * Инициализация поля
     */
    private static void initMap() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                MAP[i][j] = DOT_EMPTY;
            }
        }
    }

    /**
     * Печать поля с номерами (адаптирована к SIZE)
     */
    private static void printMap() {
        // Верхний ряд с номерами колонок
        System.out.print("   "); // Добавляем отступ
        for (int i = 1; i <= SIZE; i++) {
            System.out.printf("%2d ", i);
        }
        System.out.println();

        // Строки поля с номерами строк
        for (int i = 0; i < SIZE; i++) {
            System.out.printf("%2d ", i + 1);
            for (int j = 0; j < SIZE; j++) {
                System.out.print(MAP[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
