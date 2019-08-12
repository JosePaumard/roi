package pro.tremblay.roi.service;

import cucumber.api.Transform;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.assertj.core.api.SoftAssertions;
import pro.tremblay.roi.domain.Currency;
import pro.tremblay.roi.domain.Transaction;
import pro.tremblay.roi.domain.TransactionType;
import pro.tremblay.roi.service.util.BigDecimalTransformer;
import pro.tremblay.roi.service.util.LocalDateTransformer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class CommissionStepdefs {

    private List<Transaction> transactions;
    private UserDataService userDataService;
    private PriceService priceService;
    private ExchangeRateService exchangeRateService;
    private MessageService messageService;
    private List<TransactionProxy> transactionProxies;
    private List<BigDecimal> calculatedCommissions;
    private BigDecimal calculatedCommissionFee;

    @Given("^an empty collection of transactions$")
    public void an_empty_collections_of_transactions() {
        transactions = Collections.emptyList();
    }

    @Given("^the following list of transaction fees$")
    public void the_following_list_of_transaction_fees(List<TransactionProxy> transactions) {
        this.transactionProxies = transactions;
    }

    @Given("^the following transaction fee$")
    public void the_following_transaction_fee(List<TransactionProxy> transactions) {
        this.transactions = transactions.stream().map(TransactionProxy::buildTransaction).collect(Collectors.toList());
    }

    @Given("^a transaction of fee (\\d+) (CAD|USD) dated from (.*)$")
    public void a_transaction(@Transform(BigDecimalTransformer.class) BigDecimal fee, Currency currency, @Transform(LocalDateTransformer.class) LocalDate tradeDate) {
        Transaction transaction = new Transaction().fee(fee).currency(currency).tradeDate(tradeDate);
        this.transactions = List.of(transaction);
    }

    @Given("^the following list of transaction fees of different types$")
    public void a_list_of_transactions_of_different_types(List<TransactionProxy> transactionProxies) {
        this.transactions = transactionProxies.stream().map(TransactionProxy::buildTransactionWithType).collect(Collectors.toList());
    }

    @Given("^a transaction of type (deposit|withdrawal|sell|buy) of fee (\\d+) (CAD|USD) dated from (.*)$")
    public void a_transaction(
        TransactionType transactionType,
        @Transform(BigDecimalTransformer.class) BigDecimal fee, Currency currency, @Transform(LocalDateTransformer.class) LocalDate tradeDate) {
        Transaction transaction = new Transaction().fee(fee).currency(currency).tradeDate(tradeDate);
        this.transactions = List.of(transaction);
    }

    @When("^the commission is calculated in (CAD|USD)$")
    public void the_commission_is_calculated_in_currency(Currency currency) {
        userDataService = new UserDataService(currency);
        priceService = new PriceService();
        exchangeRateService = new ExchangeRateService();
        messageService = new MessageService();

        ReportingService reportingService = new ReportingService(userDataService, priceService, exchangeRateService, messageService);
        calculatedCommissionFee = reportingService.calculateCommission(transactions);
    }

    @When("^the commission is calculated in (CAD|USD) for each transaction$")
    public void the_commission_is_calculated_in_currency_for_each_transaction(Currency currency) {
        userDataService = new UserDataService(currency);
        priceService = new PriceService();
        exchangeRateService = new ExchangeRateService();
        messageService = new MessageService();

        ReportingService reportingService = new ReportingService(userDataService, priceService, exchangeRateService, messageService);

        List<Transaction> transactions = transactionProxies.stream()
            .map(TransactionProxy::buildTransaction)
            .collect(Collectors.toList());

        calculatedCommissions = transactions.stream()
            .map(List::of)
            .map(reportingService::calculateCommission)
            .collect(Collectors.toList());
    }

    @Then("^the calculated commission is (.*) for this transaction$")
    public void the_calculated_commission_is(@Transform(BigDecimalTransformer.class) BigDecimal expectedCommission) {
        assertThat(calculatedCommissionFee).isEqualTo(expectedCommission);
    }

    @Then("^the calculated commission is (.*) for all the transactions$")
    public void the_calculated_commission_for_all_the_transactions_is(@Transform(BigDecimalTransformer.class) BigDecimal expectedCommission) {
        SoftAssertions softly = new SoftAssertions();
        calculatedCommissions
            .forEach(calculatedCommission -> softly.assertThat(calculatedCommission).isEqualTo(expectedCommission));

        softly.assertAll();
    }

    private static class TransactionProxy {

        private String fee;
        private String currency;
        private String date;
        private String type;

        public BigDecimal getFee() {
            return fee.equals("null") ? BigDecimal.ZERO.setScale(4) : new BigDecimal(fee).setScale(4);
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public Currency getCurrency() {
            return currency.equals("null") ? null : Currency.valueOf(currency);
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public LocalDate getDate() {
            return date.equals("null") ? null : LocalDate.parse(date);
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setType(String type) {
            this.type = type;
        }

        public TransactionType getType() {
            return TransactionType.valueOf(type);
        }

        public Transaction buildTransaction() {
            return new Transaction().fee(getFee()).currency(getCurrency()).tradeDate(getDate());
        }

        public Transaction buildTransactionWithType() {
            return new Transaction().fee(getFee()).currency(getCurrency()).type(getType()).tradeDate(getDate());
        }
    }
}
