import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class Radkovskyi_PrimeNumbersTest {

    private static Radkovskyi_PrimeNumbers.NumberCruncher cruncher;

    @BeforeAll
    static void setUp() {
        Radkovskyi_PrimeNumbers outerInstance = new Radkovskyi_PrimeNumbers();
        cruncher = outerInstance.new NumberCruncher(1, 1); // Діапазон тут не важливий для цих тестів
    }

    @Test
    void estIsPrime_VariedCases() {
        assertTrue(Radkovskyi_PrimeNumbers.NumberCruncher.isPrime(7L), "7 має бути простим");
        assertFalse(Radkovskyi_PrimeNumbers.NumberCruncher.isPrime(6L), "6 не має бути простим");
        assertTrue(Radkovskyi_PrimeNumbers.NumberCruncher.isPrime(2L), "2 має бути простим");
        assertFalse(Radkovskyi_PrimeNumbers.NumberCruncher.isPrime(1L), "1 не має бути простим");
        assertFalse(Radkovskyi_PrimeNumbers.NumberCruncher.isPrime(0L), "0 не має бути простим");
        assertFalse(Radkovskyi_PrimeNumbers.NumberCruncher.isPrime(Integer.MAX_VALUE + 1L), "Велике складене число");
    }

    @Test
    void testIsNumberSquare_VariedCases() {
        assertTrue(Radkovskyi_PrimeNumbers.NumberCruncher.isNumberSquare(169), "169 має бути квадратом");
        assertFalse(Radkovskyi_PrimeNumbers.NumberCruncher.isNumberSquare(168), "168 не має бути квадратом");
        assertTrue(Radkovskyi_PrimeNumbers.NumberCruncher.isNumberSquare(0), "0 має бути квадратом");
        assertTrue(Radkovskyi_PrimeNumbers.NumberCruncher.isNumberSquare(1), "1 має бути квадратом");
    }

    @Test
    void testGeneratePrimes_TypicalRange() {
        List<Integer> primes = cruncher.generatePrimes(1, 20);
        List<Integer> expectedPrimes = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19);
        assertEquals(expectedPrimes, primes, "Прості числа в діапазоні [1, 20]");
    }

    @Test
    void testGeneratePrimes_EdgeCases() {
        // Діапазон з одним простим числом
        List<Integer> primesSingle = cruncher.generatePrimes(2, 2);
        assertEquals(Collections.singletonList(2), primesSingle, "Діапазон [2, 2]");

        // Порожній діапазон (нижня межа > верхньої)
        List<Integer> primesInvalid = cruncher.generatePrimes(20, 10);
        assertTrue(primesInvalid.isEmpty(), "Невалідний діапазон [20, 10]");

        // Діапазон, де числа менше 2
        List<Integer> primesBelowTwo = cruncher.generatePrimes(0, 1);
        assertTrue(primesBelowTwo.isEmpty(), "Діапазон [0, 1]");
    }

    @Test
    void testComputeFermatDecomposition_OddComposite() {
        // 21 = 3 * 7
        long[] expected = {3, 7};
        assertArrayEquals(expected, cruncher.computeFermatDecomposition(21), "Розклад 21");
    }

    @Test
    void testComputeFermatDecomposition_EvenComposite() {
        // Для парних чисел, метод повертає {2, n/2}
        long[] expected = {2, 50};
        assertArrayEquals(expected, cruncher.computeFermatDecomposition(100), "Розклад 100 (парне)");
    }

    @Test
    void testComputeFermatDecomposition_PerfectSquare() {
        // Для повних квадратів, метод повертає {sqrt(n), sqrt(n)}
        // (Якщо число не парне. Якщо парне, то перша умова (n%2==0) спрацює)
        long[] expected = {7, 7};
        assertArrayEquals(expected, cruncher.computeFermatDecomposition(49), "Розклад 49 (квадрат)");
    }

    @Test
    void testComputeFermatDecomposition_PrimeNumber() {
        // Для непарного простого числа n, метод Ферма знаходить розклад {1, n}
        long[] expected = {1, 997}; // ВИПРАВЛЕНО
        assertArrayEquals(expected, cruncher.computeFermatDecomposition(997), "Розклад простого числа 997");
    }

    @Test
    void testComputeFermatDecomposition_SpecialNumberOneAndTwo() {
        // Метод Ферма повертає new long[0] для 1
        long[] expectedOne = new long[0];
        assertArrayEquals(expectedOne, cruncher.computeFermatDecomposition(1), "Розклад 1");

        // Для 2, метод повертає {2, 1}
        long[] expectedTwo = {2, 1};
        assertArrayEquals(expectedTwo, cruncher.computeFermatDecomposition(2), "Розклад 2");
    }
}