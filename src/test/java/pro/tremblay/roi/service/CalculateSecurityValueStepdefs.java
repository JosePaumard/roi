package pro.tremblay.roi.service;

import cucumber.api.Transform;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.collections4.map.MultiKeyMap;
import pro.tremblay.roi.domain.Currency;
import pro.tremblay.roi.domain.Position;
import pro.tremblay.roi.domain.Security;
import pro.tremblay.roi.service.util.BigDecimalTransformer;
import pro.tremblay.roi.service.util.LocalDateTransformer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CalculateSecurityValueStepdefs {

    private List<Position> positions;
    private LocalDate tradeDate;
    private MultiKeyMap<Object, BigDecimal> prices;
    private BigDecimal securityValue;

    @Given("^an empty collection of positions$")
    public void an_empty_collection_of_positions() {
        positions = List.of();
    }

    @Given("^a position without a price and a currency of (CAD|USD)$")
    public void a_position_without_a_price_and_currency(Currency currency) {
        Security security = new Security().currency(currency).symbol("XXX");
        Position position = new Position().security(security);
        this.positions = List.of(position);
        prices = new MultiKeyMap<>();
    }

    @Given("^a position with a price (\\d+) a currency of (CAD|USD) a quantity of (\\d+) at date (.*)$")
    public void a_position_with_a_price_and_a_currency(
        @Transform(BigDecimalTransformer.class) BigDecimal price,
        Currency currency,
        long quantity,
        @Transform(LocalDateTransformer.class) LocalDate date
    ) {
        String securitySymbol = "XXX";
        Security security = new Security().currency(currency).symbol(securitySymbol);
        Position position = new Position().quantity(quantity).security(security);
        prices = new MultiKeyMap<>();
        prices.put(date, securitySymbol, currency, new BigDecimal("10").setScale(4));
        this.positions = List.of(position);
        this.tradeDate = date;
    }

    @When("^the security value is calculated for currency (CAD|USD)$")
    public void the_security_value_is_calculated_for_currency(Currency currency) {
        ReportingService reportingService = buildReportingService(currency);
        securityValue = reportingService.calculateSecurityValue(tradeDate, positions, prices);
    }

    @Then("^the security value is (\\d+\\.?\\d*)$")
    public void the_calculated_security_value_is(@Transform(BigDecimalTransformer.class) BigDecimal expectedSecurityValue) {
        assertThat(securityValue).isEqualTo(expectedSecurityValue);
    }

    private ReportingService buildReportingService(Currency currency) {
        UserDataService userDataService = new UserDataService(currency);
        PriceService priceService = new PriceService();
        ExchangeRateService exchangeRateService = new ExchangeRateService();
        MessageService messageService = new MessageService();

        return new ReportingService(userDataService, priceService, exchangeRateService, messageService);
    }
}
