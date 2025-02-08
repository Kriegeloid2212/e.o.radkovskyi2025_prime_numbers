import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

class Radkovskyi_PrimeNumbersTest {

    // Статичні методи можна викликати безпосередньо
    @Test
    void testIsPrime1() {
        // Перевіряємо, чи є число 997 простим за допомогою статичного методу isPrime.
        assertTrue(Radkovskyi_PrimeNumbers.NumberCruncher.isPrime(997));
    }

    @Test
    void testIsPrime2() {
        // Перевіряємо, чи є число 13445 простим (очікуємо false).
        assertFalse(Radkovskyi_PrimeNumbers.NumberCruncher.isPrime(13445));
    }

    @Test
    void testIsNumberPrime1() {
        // Перевіряємо, чи є число 997L простим, використовуючи статичний метод isNumberPrime.
        assertTrue(Radkovskyi_PrimeNumbers.NumberCruncher.isNumberPrime(997L));
    }

    @Test
    void testIsNumberPrime2() {
        // Перевіряємо, чи є число 13445L простим (очікуємо false).
        assertFalse(Radkovskyi_PrimeNumbers.NumberCruncher.isNumberPrime(13445L));
    }

    @Test
    void testIsNumberPrime3() {
        // Перевіряємо, чи є число (Integer.MAX_VALUE + 1L) простим (очікуємо false).
        assertFalse(Radkovskyi_PrimeNumbers.NumberCruncher.isNumberPrime(Integer.MAX_VALUE + 1L));
    }

    @Test
    void testIsNumberSquare1() {
        // Перевіряємо, чи є число 169 квадратом цілого числа.
        assertTrue(Radkovskyi_PrimeNumbers.NumberCruncher.isNumberSquare(169));
    }

    @Test
    void testIsNumberSquare2() {
        // Перевіряємо, чи є число 168 квадратом цілого числа (очікуємо false).
        assertFalse(Radkovskyi_PrimeNumbers.NumberCruncher.isNumberSquare(168));
    }

    // Тести для методів екземпляра (використовуючи *один* екземпляр NumberCruncher)
    private final Radkovskyi_PrimeNumbers.NumberCruncher cruncher = new Radkovskyi_PrimeNumbers().new NumberCruncher(1, 1); // Фіктивний діапазон, лише для ініціалізації

    @Test
    void testGeneratePrimes1() {
        // Генеруємо прості числа в діапазоні від 1 до 20.
        List<Integer> primes = cruncher.generatePrimes(1, 20);
        // Очікуваний список простих чисел.
        List<Integer> expectedPrimes = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19);
        // Перевіряємо, чи збігається згенерований список з очікуваним.
        assertEquals(expectedPrimes, primes);
    }

    @Test
    void testGeneratePrimes2() {
        // Генеруємо прості числа в діапазоні від 10 до 28.
        List<Integer> primes = cruncher.generatePrimes(10, 28);
        // Очікуваний список простих чисел.
        List<Integer> expectedPrimes = Arrays.asList(11, 13, 17, 19, 23);
        // Перевіряємо, чи збігається згенерований список з очікуваним.
        assertEquals(expectedPrimes, primes);
    }

    @Test
    void testGeneratePrimes3() {
        // Генеруємо прості числа в діапазоні від 100 до 100 (очікуємо порожній список).
        List<Integer> primes = cruncher.generatePrimes(100, 100);
        assertTrue(primes.isEmpty());
    }

    @Test
    void testGeneratePrimes4() {
        // Генеруємо прості числа в діапазоні від 1 до 1 (очікуємо порожній список).
        List<Integer> primes = cruncher.generatePrimes(1, 1);
        assertTrue(primes.isEmpty());
    }
    @Test
    void testGeneratePrimes5() {
        // Генеруємо прості числа в діапазоні від 2 до 2 (очікуємо список з одним елементом [2]).
        List<Integer> primes = cruncher.generatePrimes(2, 2);
        List<Integer> expected = Arrays.asList(2);
        assertEquals(expected, primes);
    }
    @Test
    void testComputeFermatDecomposition() {
        // Обчислюємо розклад Ферма для числа 249919.
        long[] arr = {491, 509};
        // Перевіряємо, чи збігається результат з очікуваним масивом.
        assertArrayEquals(arr, cruncher.computeFermatDecomposition(249919));
    }

    @Test
    void testComputeFermatDecomposition_prime() {
        // Обчислюємо розклад Ферма для простого числа 997 (очікуємо масив з одним елементом [997]).
        long[] arr = {997};
        assertArrayEquals(arr, cruncher.computeFermatDecomposition(997));
    }

    @Test
    void testComputeFermatDecomposition_one() {
        // Обчислюємо розклад Ферма для числа 1 (очікуємо масив з одним елементом [1]).
        long[] arr = {1};
        assertArrayEquals(arr, cruncher.computeFermatDecomposition(1));
    }

    @Test
    void testComputeFermatDecomposition2() {
        // Обчислюємо розклад Ферма для числа 21.
        long[] expected = {3, 7};
        // Перевіряємо чи збігається результат з очікуваним.
        assertArrayEquals(expected, cruncher.computeFermatDecomposition(21));
    }
}