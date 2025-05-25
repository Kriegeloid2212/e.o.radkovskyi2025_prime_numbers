import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Radkovskyi_PrimeNumbers extends JFrame {

    // Константи для налаштувань програми
    public static final int[] CONST_SEEDS = {263, 269, 271, 277, 281}; // "Сіди" для перевірки чисел
    public static final int DEFAULT_WIDTH = 700;                  // Ширина вікна
    public static final int DEFAULT_HEIGHT = 600;                 // Висота вікна
    public static final Font DEFAULT_FONT_BOLD = new Font("Dialog", Font.BOLD, 14); // Жирний шрифт
    public static final Font DEFAULT_FONT_PLAIN = new Font("Dialog", Font.PLAIN, 14); // Звичайний шрифт
    public static final EmptyBorder DEFAULT_MARGIN = new EmptyBorder(5, 10, 5, 10); // Відступи

    // UI елементи
    private final JTextField fieldMin = new JTextField();          // Поле для нижньої межі
    private final JTextField fieldMax = new JTextField();          // Поле для верхньої межі
    private final JTextArea areaOutput = new JTextArea();          // Область для виводу результатів
    private final JButton buttonAction = new JButton("Обчислити");   // Кнопка для запуску розрахунків
    private final JLabel labelMin = new JLabel("Нижня границя:");   // Мітка для нижньої межі
    private final JLabel labelMax = new JLabel("Верхня границя:");   // Мітка для верхньої межі
    private final JLabel labelResult = new JLabel("Результати:");  // Мітка для результатів

    public Radkovskyi_PrimeNumbers() {
        initializeComponents();
        setupLayout();
        setupActions();
        launch();
    }

    private void initializeComponents() {
        areaOutput.setEditable(false); // Щоб юзер не міг редагувати
        areaOutput.setLineWrap(true);  // Перенос рядків, якщо текст не влазить
        areaOutput.setWrapStyleWord(true); // Переносити цілі слова
        areaOutput.setFont(DEFAULT_FONT_PLAIN); // Стандартний шрифт

        labelMin.setBorder(DEFAULT_MARGIN); // Відступи і жирний шрифт для міток
        labelMin.setFont(DEFAULT_FONT_BOLD);
        labelMax.setBorder(DEFAULT_MARGIN);
        labelMax.setFont(DEFAULT_FONT_BOLD);
        labelResult.setBorder(DEFAULT_MARGIN);
        labelResult.setFont(DEFAULT_FONT_BOLD);

        fieldMin.setBorder(DEFAULT_MARGIN); // Відступи для текстових полів
        fieldMax.setBorder(DEFAULT_MARGIN);
        fieldMin.setToolTipText("Введіть нижню межу діапазону (ціле число >= 2)"); // Підказка, коли наводиш мишку
        fieldMax.setToolTipText("Введіть верхню межу діапазону (ціле число > за нижню межу)");

        buttonAction.setBorder(DEFAULT_MARGIN); // Відступи і жирний шрифт для кнопки
        buttonAction.setFont(DEFAULT_FONT_BOLD);
        buttonAction.setToolTipText("Запустити обчислення простих чисел та факторизації"); // Підказка для кнопки
    }

    private void setupLayout() {
        JPanel panelBase = new JPanel(new GridBagLayout()); // Основна панель з GridBagLayout
        setContentPane(panelBase);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Відступи між елементами
        gbc.fill = GridBagConstraints.HORIZONTAL; // Розтягувати по горизонталі
        gbc.weightx = 1.0; // Розподіл горизонтального простору

        // Додаємо мітки та поля для меж
        addComponent(panelBase, labelMin, 0, 0, gbc, GridBagConstraints.WEST);
        addComponent(panelBase, labelMax, 0, 1, gbc, GridBagConstraints.WEST);
        addComponent(panelBase, fieldMin, 1, 0, gbc, GridBagConstraints.EAST);
        addComponent(panelBase, fieldMax, 1, 1, gbc, GridBagConstraints.EAST);

        // Кнопка "Обчислити" по центру, на всю ширину
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; // Не розтягувати кнопку
        gbc.anchor = GridBagConstraints.CENTER; // По центру
        addComponent(panelBase, buttonAction, 0, 2, gbc, GridBagConstraints.CENTER);


        gbc.gridwidth = 1; // Повертаємо ширину в 1
        gbc.fill = GridBagConstraints.HORIZONTAL; // Повертаємо розтягування
        gbc.anchor = GridBagConstraints.WEST;     // Повертаємо вирівнювання

        // Мітка "Результати"
        addComponent(panelBase, labelResult, 0, 3, gbc, GridBagConstraints.WEST);

        // Область виводу зі скролом
        JScrollPane paneScrollArea = new JScrollPane(areaOutput);
        paneScrollArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // Вертикальний скрол завжди
        paneScrollArea.setBorder(DEFAULT_MARGIN);


        // Розміщуємо область виводу
        gbc.gridwidth = 2;
        gbc.gridy = 4;
        gbc.weighty = 1.0; // Розподіл вертикального простору для області виводу
        gbc.fill = GridBagConstraints.BOTH; // Розтягувати в обидві сторони
        panelBase.add(paneScrollArea, gbc);
    }

    private void addComponent(JPanel panel, JComponent component, int x, int y, GridBagConstraints gbc, int anchor) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = anchor;
        panel.add(component, gbc);
    }

    private void setupActions() {
        buttonAction.addActionListener(e -> triggerComputation()); // Кнопка "Обчислити" - запуск розрахунків
    }

    private void launch() {
        setTitle("Інструмент для Простих Чисел"); // Заголовок вікна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        pack(); // Автоматичний розмір вікна
        setLocationRelativeTo(null); // Вікно по центру екрана
        setVisible(true); // Показуємо вікно
    }


    private void triggerComputation() {
        try {
            int lowerBound = Integer.parseInt(fieldMin.getText()); // Беремо нижню межу з поля
            int upperBound = Integer.parseInt(fieldMax.getText()); // Беремо верхню межу з поля

            // Перевірка введених даних
            if (lowerBound >= upperBound) {
                showError("Нижня межа має бути меншою за верхню.");
                return;
            }
            if (lowerBound < 2 || upperBound < 2) {
                showError("Межі діапазону мають бути більшими або рівними 2.");
                return;
            }
            if (upperBound - lowerBound > 100000) { // Щоб не зависло, обмежуємо діапазон
                showError("Діапазон занадто великий. Будь ласка, зменшіть його.");
                return;
            }

            areaOutput.setText("Обчислення...\n"); // Очищаємо вивід і пишем
            buttonAction.setEnabled(false);       // Блокуємо кнопку, поки йдуть розрахунки

            // Запускаємо обчислення в окремому потоці
            new NumberCruncher(lowerBound, upperBound).execute();

        } catch (NumberFormatException ex) {
            showError("Введіть коректні цілі числа для меж діапазону."); // Якщо ввели не число
        }
    }

    private void showError(String message) {
        areaOutput.setText("Помилка: " + message + "\n"); // Виводимо повідомлення про помилку
    }

    public class NumberCruncher extends SwingWorker<Void, String> {
        private final int rangeStart; // Початок діапазону
        private final int rangeEnd;   // Кінець діапазону
        private long productPrime;    // Добуток простих чисел
        private final Set<Long> visited = new HashSet<>(); // Щоб не факторизувати одне і те саме число багато разів

        public NumberCruncher(int rangeStart, int rangeEnd) {
            this.rangeStart = rangeStart;
            this.rangeEnd = rangeEnd;
        }

        @Override
        protected Void doInBackground() {
            publish("Генерація простих чисел...\n"); // Повідомлення про початок генерації
            productPrime = calculatePrimeProduct();  // Рахуємо добуток простих

            publish("\n---\nБлок перевірки чисел на простоту:\n"); // Роздільник у виводі
            checkPrimes(); // Перевірка на простоту

            publish("\n---\nБлок факторизації чисел методом Ферма:\n"); // Роздільник у виводі
            factorizeNumbers(); // Факторизація

            return null; 
        }

        public long calculatePrimeProduct() {
            List<Integer> primes = generatePrimes(rangeStart, rangeEnd); // Генеруємо прості числа
            long product = 1;
            StringBuilder sb = new StringBuilder("  Прості числа в діапазоні [").append(rangeStart).append(", ").append(rangeEnd).append("]: ");
            if (primes.isEmpty()) {
                sb.append("відсутні.\n"); // Якщо простих нема
            } else {
                for (int prime : primes) {
                    product *= prime; // Рахуємо добуток
                }
                sb.append(primes.stream().map(String::valueOf).collect(Collectors.joining(" ", "", "\n"))); // Виводимо список простих
                sb.append("  Добуток простих чисел: ").append(product).append("\n"); // Виводимо добуток
            }
            publish(sb.toString()); // Виводимо в область виводу
            return product;
        }

        public void checkPrimes() {
            for (int seed : CONST_SEEDS) { // Перевіряємо з кожним "сідом"
                long numberToCheck = productPrime + seed; // Число для перевірки
                boolean isPrime = isPrime(numberToCheck); // Перевіряємо
                publish("  Число " + numberToCheck + (isPrime ? " є простим.\n" : " не є простим.\n")); // Виводимо результат
            }
        }

        public void factorizeNumbers() {
            for (int seed : CONST_SEEDS) { // Факторизуємо з кожним "сідом"
                long numberToFactor = productPrime + seed; // Число для факторизації
                boolean isPrime = isPrime(numberToFactor); // Перевіряємо, чи просте
                if (isPrime) {
                    publish("  Число " + numberToFactor + " є простим, множники відсутні.\n"); // Якщо просте, то множників нема
                } else {
                    publish("  Множники числа " + numberToFactor + ": {"); // Початок виводу множників
                    decomposeRecursively(numberToFactor); // Рекурсивна факторизація
                    publish("}\n"); // Кінець виводу множників
                }
            }
        }

        @Override
        protected void process(List<String> chunks) {
            areaOutput.append(String.join("", chunks)); // Додаємо текст до області виводу
        }

        @Override
        protected void done() {
            buttonAction.setEnabled(true);       // Знову включаємо кнопку після розрахунків
            areaOutput.append("\nОбчислення завершено."); // Повідомлення про завершення
            try {
                get(); // Перевіряємо, чи не було помилок
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                showError("Обчислення перервано: " + e.getMessage()); // Якщо перервали розрахунки
            } catch (java.util.concurrent.ExecutionException e) {
                showError("Помилка під час обчислень: " + e.getCause().getMessage()); // Якщо помилка в розрахунках
            }
        }

        public List<Integer> generatePrimes(int lower, int upper) {
            int start = Math.max(2, lower); // Починаємо з 2, якщо нижня межа менша
            if (start > upper) return List.of(); // Якщо діапазон невірний, повертаємо пустий список
            if (upper < 100) { // Для малих діапазонів - простіше так
                return IntStream.rangeClosed(start, upper)
                        .filter(NumberCruncher::isPrime) // Фільтруємо прості числа
                        .boxed()
                        .collect(Collectors.toList());
            }
            return sieveOfEratosthenes(upper, start); // Для великих діапазонів - решето Ератосфена
        }

        private List<Integer> sieveOfEratosthenes(int upperLimit, int lowerLimit) {
            boolean[] isPrime = new boolean[upperLimit + 1]; // Масив для позначок
            java.util.Arrays.fill(isPrime, true); // Спочатку всі - прості
            isPrime[0] = isPrime[1] = false; // 0 і 1 - не прості
            for (int p = 2; p * p <= upperLimit; p++) { // До кореня верхньої межі
                if (isPrime[p]) { // Якщо число просте
                    for (int i = p * p; i <= upperLimit; i += p) // Викреслюємо всі кратні
                        isPrime[i] = false;
                }
            }
            return IntStream.rangeClosed(lowerLimit, upperLimit) // Беремо числа з діапазону
                    .filter(i -> isPrime[i]) // Лишаємо тільки прості
                    .boxed()
                    .collect(Collectors.toList()); // Збираємо в список
        }


        public static boolean isPrime(long number) {
            if (number <= 1) return false; // 1 і менше - не прості
            if (number <= 3) return true;  // 2 і 3 - прості
            if (number % 2 == 0 || number % 3 == 0) return false; // Ділиться на 2 або 3
            if (number < 2500) { // Для малих чисел - швидше перевірка
                return isPrimeSmall((int) number); // Проста перевірка для малих
            }
            for (long i = 5; i * i <= number; i += 6) { // Перевіряємо дільники з кроком 6
                if (number % i == 0 || number % (i + 2) == 0) return false; // Якщо ділиться, то не просте
            }
            return true; // Якщо не знайшли дільників - просте
        }

        private static boolean isPrimeSmall(int number) {
            if (number <= 1) return false;
            if (number <= 3) return true;
            if (number % 2 == 0 || number % 3 == 0) return false;
            for (int i = 5; i * i <= number; i += 6) {
                if (number % i == 0 || number % (i + 2) == 0) return false;
            }
            return true;
        }


        public static boolean isNumberSquare(long num) {
            if (num < 0) return false; // Від'ємні не квадрати
            long root = (long) Math.round(Math.sqrt(num)); // Корінь
            return root * root == num; // Перевірка, чи квадрат кореня дорівнює числу
        }

        public long[] computeFermatDecomposition(long n) {
            if (n == 1) return new long[0]; // 1 не факторизується
            if (n % 2 == 0) return new long[]{2, n / 2}; // Якщо парне, то 2 - множник
            if (isNumberSquare(n)) { // Якщо повний квадрат
                long root = (long) Math.sqrt(n);
                return new long[]{root, root}; // Множники - корінь
            }

            long a = (long) Math.ceil(Math.sqrt(n)); // Початкове значення для методу Ферма
            long b2 = a * a - n; // b^2 = a^2 - n
            long factor;
            long limit = n; // Щоб не зациклилось (теоретично)
            while (a <= limit) { // Шукаємо, поки не знайдемо
                if (isNumberSquare(b2)) { // Якщо b^2 - квадрат
                    factor = (long) Math.sqrt(b2); // b = sqrt(b^2)
                    return new long[]{a - factor, a + factor}; // Множники: (a-b) і (a+b)
                }
                a++; // Збільшуємо a
                b2 = a * a - n; // Перераховуємо b^2
                if (b2 < 0) return new long[0];
            }
            return new long[0]; // Якщо не знайшли методом Ферма
        }


        public void decomposeRecursively(long number) {
            if (number <= 1) return; // Базовий випадок: 1 і менше не факторизуємо
            if (isPrime(number)) { // Якщо просте
                publish(number + " "); // Виводимо як простий множник
                return; // Кінець рекурсії для простого числа
            }
            if (visited.contains(number)) return; // Щоб не зациклитись на числах, що не факторизуються
            visited.add(number); // Позначаємо, що вже пробували факторизувати
            long[] factors = computeFermatDecomposition(number); // Пробуємо методом Ферма
            if (factors.length == 0) { // Якщо Ферма не допоміг
                // Якщо Ферма не знайшов, пробуємо простий перебір дільників.
                // Це запасний варіант, але не ідеальний.
                for (long i = 2; i * i <= number; i++) { // Перебираємо дільники
                    if (number % i == 0) { // Якщо знайшли дільник
                        decomposeRecursively(i); // Факторизуємо дільник
                        decomposeRecursively(number / i); // Факторизуємо частку
                        return; // Виходимо, бо вже розклали
                    }
                }
                publish("Не вдалося розкласти: " + number + " (Ферма і простий поділ не допомогли). "); // Якщо нічого не вийшло
                return;
            }
            decomposeRecursively(factors[0]); // Факторизуємо перший множник від Ферма
            decomposeRecursively(factors[1]); // Факторизуємо другий множник від Ферма
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Radkovskyi_PrimeNumbers::new); // Запуск програми
    }
}
