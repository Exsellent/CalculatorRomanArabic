import java.util.Scanner;

class Main {

    public static void main(String[] args) {
        System.out.println("Введите выражение [2+2] или два римских числа от I до X:[V+V] + Enter ");
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();

        try {
            String result = calc(userInput);
            System.out.println("---Результат----");
            System.out.println(result);
        } catch (ArithmeticException | IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static String calc(String input) {
        char operation = findOperation(input);
        String[] operands = input.split("[+\\-*/]");

        if (operands.length == 2) {
            if ((isRoman(operands[0].trim()) && isRoman(operands[1].trim())) ||
                    (!isRoman(operands[0].trim()) && !isRoman(operands[1].trim()))) {
                int number1 = parseOperand(operands[0].trim());
                int number2 = parseOperand(operands[1].trim());

                int result = performOperation(number1, number2, operation);

                return isRoman(operands[0].trim()) ? convertNumToRoman(result) : Integer.toString(result);
            } else {
                throw new IllegalArgumentException("Операции с римскими и арабскими числами в одном выражении не поддерживаются");
            }
        } else {
            throw new IllegalArgumentException("Неверный формат математической операции. Ожидается два операнда и один оператор (+, -, /, *)");
        }
    }

    private static char findOperation(String input) {
        for (char c : input.toCharArray()) {
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                return c;
            }
        }
        throw new IllegalArgumentException("Строка не является математической операцией");
    }

    private static int parseOperand(String operand) {
        if (isRoman(operand)) {
            return romanToNumber(operand);
        } else {
            int arabicNumber = Integer.parseInt(operand);
            if (arabicNumber < 1 || arabicNumber > 10) {
                throw new IllegalArgumentException("Число вне диапазона арабских цифр (1-10)");
            }
            return arabicNumber;
        }
    }

    private static int performOperation(int num1, int num2, char op) {
        switch (op) {
            case '+' -> {
                return num1 + num2;
            }
            case '-' -> {
                return num1 - num2;
            }
            case '*' -> {
                return num1 * num2;
            }
            case '/' -> {
                if (num2 == 0) {
                    throw new ArithmeticException("Деление на ноль");
                }
                return num1 / num2;
            }
            default -> throw new IllegalArgumentException("Неверная операция: " + op);
        }
    }

    private static boolean isRoman(String input) {
        return input.matches("^[IVXLCDM]+$");
    }

    private static int romanToNumber(String roman) {
        int result = 0;
        int prevValue = 0;

        for (int i = roman.length() - 1; i >= 0; i--) {
            int currentValue = getRomanValue(roman.charAt(i));

            if (currentValue < prevValue) {
                result -= currentValue;
            } else {
                result += currentValue;
            }

            prevValue = currentValue;
        }

        return result;
    }

    private static int getRomanValue(char romanChar) {
        switch (romanChar) {
            case 'I' -> {
                return 1;
            }
            case 'V' -> {
                return 5;
            }
            case 'X' -> {
                return 10;
            }
            default -> throw new IllegalArgumentException("Неверный символ римской цифры: " + romanChar);
        }
    }

    private static String convertNumToRoman(int numArabian) {
        if (numArabian < 1 || numArabian > 10) {
            throw new IllegalArgumentException("Число вне диапазона римских цифр (1-10)");
        }

        int units = numArabian % 10;
        int tens = (numArabian / 10) % 10;

        return "X".repeat(tens) + "I".repeat(units);
    }
}
