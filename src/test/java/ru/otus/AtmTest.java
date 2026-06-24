package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.assertj.core.data.MapEntry.entry;

class AtmTest {
    private DispenserImpl dispenser;
    private Atm atm;

    @BeforeEach
    void setUp() {
        dispenser = new DispenserImpl();
        atm = new Atm(dispenser);
    }

    @Test
    void shouldDepositAndGetCorrectBalance() throws AtmException {
        atm.deposit(1000, 5);
        atm.deposit(5000, 2);

        assertThat(atm.getBalance()).isEqualTo(15000);
    }

    @Test
    void shouldSuccessfullyWithdrawMoneyWithGreedyAlgorithmFailureCase() throws AtmException {
        atm.deposit(500, 1);
        atm.deposit(400, 2);

        Map<Integer, Integer> withdrawn = atm.withdraw(800);

        assertThat(withdrawn).contains(entry(400, 2));
        assertThat(atm.getBalance()).isEqualTo(500);
    }

    @Test
    void shouldThrowExceptionWhenNotEnoughFunds() throws AtmException {
        atm.deposit(1000, 1);

        AtmException exception = catchThrowableOfType(() -> atm.withdraw(2000), AtmException.class);

        assertThat(exception).hasMessageContaining("недостаточно средств");
    }

    @Test
    void shouldThrowExceptionWhenImpossibleToComposeAmount() throws AtmException {
        atm.deposit(1000, 1);
        atm.deposit(2000, 1);

        AtmException exception = catchThrowableOfType(() -> atm.withdraw(1500), AtmException.class);

        assertThat(exception).hasMessageContaining("Не хватает комбинации купюр");
    }
}