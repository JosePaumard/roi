package pro.tremblay.roi.domain;

import cucumber.api.Transform;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pro.tremblay.roi.service.util.BigDecimalTransformer;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

public class CurrencyStepdefs {

    private Currency sourceCurrency;
    private BigDecimal sourceAmount;
    private BigDecimal computedAmount;

    @Given("^an amount of (.*) (CAD|USD)$")
    public void an_amount(@Transform(BigDecimalTransformer.class) BigDecimal sourceAmount, Currency currency) {
        sourceCurrency = currency;
        this.sourceAmount = sourceAmount;
    }

    @When("^the amount is converted to (CAD|USD)$")
    public void the_amount_is_converted_to(Currency destinationCurrency) {
        BigDecimal conversionRate = sourceCurrency.convertTo(destinationCurrency);
        this.computedAmount = conversionRate.multiply(sourceAmount).setScale(4, RoundingMode.HALF_UP);
    }

    @Then("^the converted amount is (.*)$")
    public void the_converted_amount_is(@Transform(BigDecimalTransformer.class) BigDecimal convertedAmount) {
        assertThat(computedAmount).isEqualTo(convertedAmount);
    }
}
