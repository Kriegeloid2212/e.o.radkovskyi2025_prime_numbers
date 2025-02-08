import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Radkovskyi_PrimeNumbers extends JFrame {

    // Константні "зерна" для перевірки.  Можна було б і згенерувати, але так наглядніше.
    private static final int[] CONST_SEEDS = {983, 991, 997, 1009, 1013};
    // Стандартна ширина вікна.  Змінив би, якби дизайн був адаптивним, а так норм.
    private static final int DEFAULT_WIDTH = 700;
    // Стандартна висота.  Аналогічно.
    private static final int DEFAULT_HEIGHT = 600;
    // Шрифт для заголовків - жирний.  Dialog - щоб на різних платформах виглядало однаково.
    private static final Font DEFAULT_FONT_BOLD = new Font("Dialog", Font.BOLD, 14);
    // Звичайний шрифт.
    private static final Font DEFAULT_FONT_PLAIN = new Font("Dialog", Font.PLAIN, 14);
    // Відступи для компонентів.  Щоб не прилипали один до одного.
    private static final EmptyBorder DEFAULT_MARGIN = new EmptyBorder(5, 10, 5, 10);

    // Поля вводу для меж діапазону.  Зробив final, бо змінювати їх не треба.
    private final JTextField fieldMin = new JTextField();
    private final JTextField fieldMax = new JTextField();
    // Текстова область для виведення результатів.  Теж final.
    private final JTextArea areaOutput = new JTextArea();
    // Кнопка "Обчислити".
    private final JButton buttonAction = new JButton("Обчислити");
    // Підписи для полів і результатів.  Теж final, і тепер вони оголошені як поля класу.
    private final JLabel labelMin = new JLabel("Нижня границя:");
    private final JLabel labelMax = new JLabel("Верхня границя:");
    private final JLabel labelResult = new JLabel("Результати:");


    // Конструктор класу.  Тут все ініціалізуємо і запускаємо.
    public Radkovskyi_PrimeNumbers() {
        initializeComponents(); // Ініціалізація компонентів (поля, кнопки, і т.д.).
        setupLayout();        // Налаштування розміщення компонентів.
        setupActions();       // Додавання обробників подій (натискання кнопки).
        launch();             // Запуск вікна.
    }

    // Ініціалізація компонентів.
    private void initializeComponents() {
        // Область виведення результатів - тільки для читання.
        areaOutput.setEditable(false);
        // Дозволяємо перенесення рядків.
        areaOutput.setLineWrap(true);
        // Перенесення по словах.
        areaOutput.setWrapStyleWord(true);
        // Встановлюємо шрифт.
        areaOutput.setFont(DEFAULT_FONT_PLAIN);

        // Застосовуємо відступи і шрифти.  Трохи спростив цикл через IntStream.
        IntStream.of(0, 1, 2).forEach(i -> {
            JComponent[] components = {labelMin, labelMax, labelResult, fieldMin, fieldMax, buttonAction};
            components[i].setBorder(DEFAULT_MARGIN); // Відступи
            if (i < 3) {
                components[i].setFont(DEFAULT_FONT_BOLD); // Жирний шрифт для підписів.
            }
        });
        // Для кнопки теж відступи і шрифт.
        buttonAction.setBorder(DEFAULT_MARGIN);
        buttonAction.setFont(DEFAULT_FONT_BOLD);

    }


    // Налаштування розміщення компонентів (Layout).  Використовуємо GridBagLayout.
    private void setupLayout() {
        // Основна панель.
        JPanel panelBase = new JPanel(new GridBagLayout());
        setContentPane(panelBase); // Встановлюємо її як основну.

        // Об'єкт для налаштування обмежень GridBagLayout.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Відступи
        gbc.fill = GridBagConstraints.HORIZONTAL; // Розтягувати по горизонталі.
        gbc.weightx = 1.0; // Коефіцієнт розтягування.


        // Додаємо підписи і текстові поля.
        addComponent(panelBase, labelMin, 0, 0, gbc, GridBagConstraints.WEST); // Підпис "Нижня границя"
        addComponent(panelBase, labelMax, 0, 1, gbc, GridBagConstraints.WEST); // Підпис "Верхня границя"
        addComponent(panelBase, fieldMin, 1, 0, gbc, GridBagConstraints.EAST); // Поле для нижньої границі
        addComponent(panelBase, fieldMax, 1, 1, gbc, GridBagConstraints.EAST); // Поле для верхньої границі

        // Кнопка "Обчислити".
        gbc.gridwidth = 2;   // Розтягуємо на дві колонки.
        gbc.fill = GridBagConstraints.NONE;    // Не розтягувати кнопку.
        gbc.anchor = GridBagConstraints.CENTER; // Центруємо.
        addComponent(panelBase, buttonAction, 0, 2, gbc, GridBagConstraints.CENTER);
        gbc.gridwidth = 1;   // Повертаємо ширину на 1.
        gbc.fill = GridBagConstraints.HORIZONTAL; // Повертаємо розтягування.


        // Підпис "Результати" і область виведення.
        addComponent(panelBase, labelResult, 0, 3, gbc, GridBagConstraints.WEST);

        // Додаємо область виведення в ScrollPane (щоб був скролінг).
        JScrollPane paneScrollArea = new JScrollPane(areaOutput);
        paneScrollArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // Вертикальний скролбар завжди.
        paneScrollArea.setBorder(DEFAULT_MARGIN); // Відступи.

        gbc.gridwidth = 2;   // На дві колонки.
        gbc.gridy = 4;      // Рядок 4.
        gbc.weighty = 1.0;   // Дозволяємо області виведення займати весь доступний простір по вертикалі.
        gbc.fill = GridBagConstraints.BOTH;     // Розтягувати і по горизонталі, і по вертикалі.
        panelBase.add(paneScrollArea, gbc); // Додаємо.
    }

    // Допоміжний метод для додавання компонентів.  Щоб не повторювати код.
    private void addComponent(JPanel panel, JComponent component, int x, int y, GridBagConstraints gbc, int anchor) {
        gbc.gridx = x;     // Колонка.
        gbc.gridy = y;     // Рядок.
        gbc.anchor = anchor; // Вирівнювання.
        panel.add(component, gbc); // Додаємо компонент на панель.
    }


    // Налаштування обробників подій.
    private void setupActions() {
        // Додаємо слухача до кнопки "Обчислити".
        buttonAction.addActionListener(e -> triggerComputation()); // При натисканні - викликаємо triggerComputation.
    }


    // Запуск вікна.
    private void launch() {
        setTitle("Квантовий Шифратор Простих Чисел та Ферма Факторизація"); // Заголовок вікна.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Закривати програму при закритті вікна.
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT)); // Розмір вікна.
        pack();             // Підганяємо розмір вікна під вміст.
        setLocationRelativeTo(null); // Центруємо вікно на екрані.
        setVisible(true);    // Робимо вікно видимим.
    }


    // Метод, який викликається при натисканні кнопки "Обчислити".
    private void triggerComputation() {
        try {
            // Зчитуємо межі діапазону з текстових полів.
            int lowerBound = Integer.parseInt(fieldMin.getText());
            int upperBound = Integer.parseInt(fieldMax.getText());

            // Перевіряємо, чи нижня межа менша за верхню.
            if (lowerBound >= upperBound) {
                showError("Нижня межа має бути меншою за верхню."); // Виводимо помилку.
                return; // Виходимо з методу.
            }

            areaOutput.setText("Обчислення...\n"); // Пишемо, що почали обчислення.
            buttonAction.setEnabled(false);      // Блокуємо кнопку.

            // Створюємо і запускаємо SwingWorker для обчислень у фоновому потоці.
            new NumberCruncher(lowerBound, upperBound).execute();

        } catch (NumberFormatException ex) {
            // Якщо користувач ввів не число - виводимо помилку.
            showError("Введіть коректні цілі числа для меж діапазону.");
        }
    }

    // Метод для виведення повідомлення про помилку.
    private void showError(String message) {
        areaOutput.setText("Помилка: " + message + "\n");
    }

    // Внутрішній клас для виконання обчислень у фоновому потоці.
    private class NumberCruncher extends SwingWorker<Void, String> {
        private final int rangeStart; // Початкове значення діапазону.
        private final int rangeEnd;   // Кінцеве значення діапазону.
        private long productPrime;    // Добуток простих чисел у діапазоні.

        // Конструктор.
        public NumberCruncher(int rangeStart, int rangeEnd) {
            this.rangeStart = rangeStart;
            this.rangeEnd = rangeEnd;
        }

        // Метод, який виконується у фоновому потоці.
        @Override
        protected Void doInBackground() {
            publish("Генерація простих чисел...\n"); // Виводимо повідомлення.
            productPrime = calculatePrimeProduct();  // Обчислюємо добуток простих чисел.

            publish("\n---\nБлок перевірки чисел на простоту:\n"); // Виводимо.
            checkPrimes();                             // Перевіряємо числа на простоту.

            publish("\n---\nБлок факторизації чисел методом Ферма:\n"); // Виводимо.
            factorizeNumbers();                        // Факторизуємо числа.

            return null; // SwingWorker має повертати значення, але нам нічого повертати.
        }

        // Обчислення добутку простих чисел у заданому діапазоні.
        private long calculatePrimeProduct() {
            // Генеруємо список простих чисел.
            List<Integer> primes = generatePrimes(rangeStart, rangeEnd);
            long product = 1; // Початкове значення добутку.
            // Формуємо рядок для виведення.
            StringBuilder sb = new StringBuilder("  Прості числа в діапазоні [" + rangeStart + ", " + rangeEnd + "]: ");

            // Якщо простих чисел немає - пишемо "відсутні".
            if (primes.isEmpty()) {
                sb.append("відсутні.\n");
            } else {
                // Інакше - перераховуємо прості числа і обчислюємо добуток.
                for (int prime : primes) {
                    sb.append(prime).append(" ");
                    product *= prime;
                }
                sb.append("\n  Добуток простих чисел: ").append(product).append("\n"); // Додаємо добуток.
            }
            publish(sb.toString()); // Виводимо рядок.
            return product; // Повертаємо добуток.
        }

        // Перевірка чисел на простоту (productPrime + CONST_SEEDS[i]).
        private void checkPrimes() {
            for (int i = 0; i < CONST_SEEDS.length; i++) {
                long numberToCheck = productPrime + CONST_SEEDS[i]; // Число для перевірки.
                boolean isPrime = isNumberPrime(numberToCheck);     // Перевіряємо.
                // Виводимо результат.
                publish("  Число " + numberToCheck + (isPrime ? " є простим.\n" : " не є простим.\n"));
            }
        }

        // Факторизація чисел методом Ферма.
        private void factorizeNumbers() {
            for (int i = 0; i < CONST_SEEDS.length; i++) {
                long numberToFactor = productPrime + CONST_SEEDS[i]; // Число для факторизації.
                boolean isPrime = isNumberPrime(numberToFactor);   // Одразу перевіряємо на простоту.
                if (isPrime) {
                    // Якщо число просте - виводимо відповідне повідомлення.
                    publish("  Число " + numberToFactor + " є простим, множники відсутні.\n");
                } else {
                    // Інакше - виводимо "Множники числа ...: {" і викликаємо рекурсивну факторизацію.
                    publish("  Множники числа " + numberToFactor + ": {");
                    decomposeRecursively(numberToFactor); // Рекурсивна факторизація.
                    publish("}\n"); // Закриваємо фігурну дужку.
                }
            }
        }


        // Метод, який викликається для публікації проміжних результатів.
        @Override
        protected void process(List<String> chunks) {
            // Додаємо всі отримані рядки в текстову область.
            chunks.forEach(areaOutput::append);
        }

        // Метод, який викликається після завершення doInBackground.
        @Override
        protected void done() {
            buttonAction.setEnabled(true); // Розблоковуємо кнопку.
            areaOutput.append("\nОбчислення завершено."); // Пишемо, що обчислення завершено.

            try {
                get(); // Перевіряємо, чи не було винятків у doInBackground.  Якщо були - вони "прокинуться" тут.
            } catch (InterruptedException | ExecutionException e) {
                // Обробляємо винятки.
                showError("Помилка під час обчислень: " + e.getMessage());
            }
        }

        // Генерація списку простих чисел у заданому діапазоні.
        private List<Integer> generatePrimes(int lower, int upper) {
            // Нижня межа не може бути меншою за 2.
            if (lower < 2) lower = 2;
            // Використовуємо IntStream для генерації і фільтрації чисел.
            return IntStream.rangeClosed(lower, upper) // Генеруємо потік чисел від lower до upper.
                    .filter(NumberCruncher::isPrime)    // Фільтруємо, залишаючи тільки прості.
                    .boxed()                         // Перетворюємо IntStream в Stream<Integer>.
                    .collect(Collectors.toList());  // Збираємо в список.
        }

        // Перевірка, чи є число простим (оптимізований алгоритм).
        private static boolean isPrime(int number) {
            if (number <= 1) return false; // 1 і менші - не прості.
            if (number <= 3) return true;  // 2 і 3 - прості.
            if (number % 2 == 0 || number % 3 == 0) return false; // Ділиться на 2 або 3 - не просте.
            // Перевіряємо дільники до sqrt(number).
            for (int i = 5; i * i <= number; i += 6) {
                if (number % i == 0 || number % (i + 2) == 0) return false; // Якщо ділиться на i або i+2 - не просте.
            }
            return true; // Інакше - просте.
        }

        // Перевірка на простоту для long (викликає isPrime(int)).
        private static boolean isNumberPrime(long number) {
            return isPrime((int) number); // Приводимо long до int, бо у нас діапазон обмежений int.
        }

        // Перевірка, чи є число повним квадратом.
        private static boolean isNumberSquare(long num) {
            if (num < 0) return false; // Від'ємні - не повні квадрати.
            long root = (long) Math.sqrt(num); // Обчислюємо корінь.
            return root * root == num;       // Перевіряємо, чи квадрат кореня дорівнює числу.
        }

        // Факторизація методом Ферма.
        private long[] computeFermatDecomposition(long n) {
            if (n <= 1 || isNumberPrime(n)) return new long[]{n}; // Якщо число просте або <= 1 - повертаємо його ж.

            long a = (long) Math.ceil(Math.sqrt(n)); // Початкове значення a.
            long b2 = a * a - n;                  // b^2.

            // Шукаємо таке a, щоб b^2 було повним квадратом.
            while (!isNumberSquare(b2)) {
                a++;
                b2 = a * a - n;
                if (a > (n + 1) / 2) return new long[]{}; // Запобігаємо нескінченному циклу.
            }

            long b = (long) Math.sqrt(b2); // Обчислюємо b.
            return new long[]{a - b, a + b};     // Повертаємо a-b і a+b.
        }

        // Рекурсивна факторизація числа і виведення простих множників.
        private void decomposeRecursively(long number) {
            if (number <= 1) return;         // Якщо число <= 1 - виходимо.
            if (isNumberPrime(number)) {  // Якщо число просте - виводимо його і виходимо.
                publish(number + " ");
                return;
            }

            // Факторизуємо методом Ферма.
            long[] factors = computeFermatDecomposition(number);
            if (factors.length == 0) { // Додав перевірку чи метод Ферма спрацював
                publish("Не вдалося розкласти методом Ферма: " + number + ". ");
                return;
            }

            // Рекурсивно викликаємо decomposeRecursively для кожного множника.
            for (long factor : factors) {
                decomposeRecursively(factor);
            }
        }
    }

    // Головний метод програми.
    public static void main(String[] args) {
        // Запускаємо GUI в Event Dispatch Thread (потік обробки подій).
        SwingUtilities.invokeLater(Radkovskyi_PrimeNumbers::new);
    }
}