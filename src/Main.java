import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Введите выражение [2+2] или два римских числа от I до X:[V+V] + Enter ");
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();

        try {
            String result = calc(userInput);
            System.out.println("---Результат---");
            System.out.println(result);
        } catch (ArithmeticException | IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static String calc(String input) {
        input = input.replaceAll("\\s+", ""); // Удаляем пробелы

        char operation = findOperation(input);
        String[] operands = input.split("[+\\-*/]");

        if (operands.length == 2) {
            if ((isRoman(operands[0].trim()) && isRoman(operands[1].trim())) ||
                    (!isRoman(operands[0].trim()) && !isRoman(operands[1].trim()))) {
                int number1 = parseOperand(operands[0].trim());
                int number2 = parseOperand(operands[1].trim());

                int result = performOperation(number1, number2, operation);

                // Конвертируем результат в римское число
                String resultRoman = convertNumToRoman(result);

                // Проверяем, является ли результат валидным римским числом
                if (isRoman(resultRoman)) {
                    // Возвращаем результат в том же формате, что и операнды
                    return isRoman(operands[0].trim()) ? resultRoman : Integer.toString(result);
                } else {
                    // Если результат невалидный, выбрасываем исключение
                    throw new IllegalArgumentException("Результат операции над римскими числами не может быть отрицательным или равным нулю");
                }
            } else {
                throw new IllegalArgumentException("Операции с римскими и арабскими числами в одном выражении не поддерживаются, или введены неверные римские цифры");
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
        throw new IllegalArgumentException("Неверный оператор: " + input);
    }

    private static int parseOperand(String operand) {
        if (isRoman(operand)) {
            int romanNumber = romanToNumber(operand);
            if (romanNumber < 1 || romanNumber > 10) {
                throw new IllegalArgumentException("Число вне диапазона римских цифр (I-X)");
            }
            return romanNumber;
        } else {
            int arabicNumber = Integer.parseInt(operand);
            if (arabicNumber < 1 || arabicNumber > 10) {
                throw new IllegalArgumentException("Число вне диапазона арабских цифр (1-10)");
            }
            return arabicNumber;
        }
    }

    private static int performOperation(int num1, int num2, char op) {
        int result = switch (op) {
            case '+' -> num1 + num2;
            case '-' -> num1 - num2;
            case '*' -> num1 * num2;
            case '/' -> {
                if (num2 == 0) {
                    throw new ArithmeticException("Деление на ноль");
                }
                yield num1 / num2;
            }
            default -> throw new IllegalArgumentException("Неверная операция: " + op);
        };

        if (isRoman(Integer.toString(result)) && result <= 0) {
            throw new IllegalArgumentException("Результат операции над римскими числами не может быть отрицательным или равным нулю");
        }

        return result;
    }

    private static boolean isRoman(String input) {
        // Регулярное выражение для проверки римских чисел от I до C (включительно)
        String romanRegex = "^(M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})|C)$";

        // Регулярное выражение для проверки повторяющихся символов (например, XXXX)
        String repeatingCharsRegex = ".*CCCC|.*XXXX|.*IIII|.*LXL|.*CDC|.*XCX|.*IXI|.*VIV";

        // Регулярное выражение для проверки комбинаций типа X1X, X2X, ..., X9X
        String invalidCombinationsRegex = ".*X[1-9]X|.*XX[1-9]X|.*XXX[1-9]X|.*XL[1-9]X|.*LX[1-9]X|.*LXX[1-9]X|.*LXXX[1-9]X";

        return input.matches(romanRegex) && !input.matches(repeatingCharsRegex) && !input.matches(invalidCombinationsRegex);
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
        if (numArabian < 1 || numArabian > 100) {
            throw new IllegalArgumentException("Число вне диапазона римских цифр (1-100)");
        }

        if (numArabian == 100) {
            return "C";
        }

        String[] romanNumbers =
                {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X",
                        "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX",
                        "XXI", "XXII", "XXIII", "XXIV", "XXV", "XXVI", "XXVII", "XXVIII", "XXIX", "XXX",
                        "XXXI", "XXXII", "XXXIII", "XXXIV", "XXXV", "XXXVI", "XXXVII", "XXXVIII", "XXXIX", "XL",
                        "XLI", "XLII", "XLIII", "XLIV", "XLV", "XLVI", "XLVII", "XLVIII", "XLIX", "L",
                        "LI", "LII", "LIII", "LIV", "LV", "LVI", "LVII", "LVIII", "LIX", "LX",
                        "LXI", "LXII", "LXIII", "LXIV", "LXV", "LXVI", "LXVII", "LXVIII",  "LXIX", "LXX",
                        "LXXI", "LXXII", "LXXIII", "LXXIV", "LXXV", "LXXVI", "LXXVII", "LXXVIII", "LXXIX", "LXXX",
                        "LXXXI", "LXXXII", "LXXXIII", "LXXXIV", "LXXXV", "LXXXVI", "LXXXVII", "LXXXVIII", "LXXXIX", "XC",
                        "XCI", "XCII", "XCIII", "XCIV", "XCV", "XCVI", "XCVII", "XCVIII", "XCIX", "C"
                };

        int units = numArabian % 10;
        int tens = (numArabian / 10) % 10;

        return romanNumbers[tens * 10 + units];
    }
}