package pro.tremblay.roi.service;

import cucumber.api.Transform;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.assertj.core.api.Assertions;
import pro.tremblay.roi.domain.Currency;
import pro.tremblay.roi.service.*;
import pro.tremblay.roi.service.util.BigDecimalTransformer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class CalculateCashValueStepdefs {

    private static final BigDecimal ZERO_SCALE_4 = BigDecimal.ZERO.setScale(4);

    private Map<Currency, BigDecimal> positions;
    private BigDecimal cashValue;

    @Given("^an empty position$")
    public void an_empty_position() {
        this.positions = new HashMap<>();
        BigDecimal zero = BigDecimal.ZERO;
    }

    @When("^the cash value is computed in (CAD|USD)$")
    public void the_cash_value_is_computed_in(Currency currency) {
        UserDataService userDataService = new UserDataService(currency);
        PriceService priceService = new PriceService();
        ExchangeRateService exchangeRateService = new ExchangeRateService();
        MessageService messageService = new MessageService();
        ReportingService reportingService = new ReportingService(userDataService, priceService, exchangeRateService, messageService);
        LocalDate now = LocalDate.now();
        cashValue = reportingService.calculateCashValue(now, this.positions);
    }

    @Then("^the resulting cash value is (.*)$")
    public void the_resulting_vash_value_is(@Transform(BigDecimalTransformer.class) BigDecimal expectedCashValue) {
        assertThat(this.cashValue).isEqualTo(expectedCashValue);
    }

    @Given("^the following position$")
    public void the_following_position(List<Position> positions) {
        this.positions =
            positions.stream()
                .collect(
                    Collectors.groupingBy(
                        Position::getCurrency,
                        Collectors.mapping(
                            Position::getAmount, Collectors.reducing(ZERO_SCALE_4, BigDecimal::add)
                        )
                    )
                );
    }

    private static class Position {
        private String amount;
        private Currency currency;

        public BigDecimal getAmount() {
            return new BigDecimalTransformer().transform(this.amount);
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public Currency getCurrency() {
            return currency;
        }

        public void setCurrency(Currency currency) {
            this.currency = currency;
        }

        @Override
        public String toString() {
            return "Position{" +
                "amount='" + amount + '\'' +
                ", currency=" + currency +
                '}';
        }
    }
}
