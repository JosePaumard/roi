package pro.tremblay.roi.service;

import cucumber.api.Transform;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.assertj.core.api.SoftAssertions;
import pro.tremblay.roi.domain.*;
import pro.tremblay.roi.service.dto.ReportingDTO;
import pro.tremblay.roi.service.util.BigDecimalTransformer;
import pro.tremblay.roi.service.util.LocalDateTransformer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;

public class GetReportStepdefs {

    private static Map<String, Security> securityRegistry;

    private LocalDate reportStartDate;
    private LocalDate reportEndDate;
    private ReportingDTO report;
    private List<SecurityProxy> securityProxies;
    private List<AccountProxy> accountProxies;
    private List<Account> accounts;
    private List<PositionProxy> positionProxies;
    private List<Position> positions;
    private List<TransactionProxy> transactionProxies;
    private List<Transaction> transactions;
    private UserDataService userDataService;

    @Given("^a starting date of (\\d+-\\d+-\\d+) and a current date of (\\d+-\\d+-\\d+)$")
    public void a_starting_date_and_a_current_date(
        @Transform(LocalDateTransformer.class) LocalDate start, @Transform(LocalDateTransformer.class) LocalDate end) {

        this.reportStartDate = start;
        this.reportEndDate = end;
    }

    @Given("^the following securities$")
    public void the_following_securities(List<SecurityProxy> securityProxies) {
        this.securityProxies = securityProxies;
        securityRegistry = securityProxies.stream().map(SecurityProxy::toSecurity)
            .collect(toMap(Security::getSymbol, identity()));
    }

    @And("^the following accounts$")
    public void the_following_accounts(List<AccountProxy> accountProxies) {
        this.accountProxies = accountProxies;
        this.accounts = accountProxies.stream().map(AccountProxy::toAccount).collect(Collectors.toList());
    }

    @And("^the following positions$")
    public void the_following_positions(List<PositionProxy> positionProxies) {
        this.positionProxies = positionProxies;
        this.positions = positionProxies.stream().map(PositionProxy::toPosition).collect(Collectors.toList());
    }

    @And("^the following transactions$")
    public void the_following_transactions(List<TransactionProxy> transactionProxies) {
        this.transactionProxies = transactionProxies;
    }

    @When("^an empty report is generated in (CAD|USD)$")
    public void an_empty_report_is_generated_in(Currency currency) {
        this.userDataService = new UserDataService(currency);
        PriceService priceService = new PriceService();
        ExchangeRateService exchangeRateService = new ExchangeRateService();
        MessageService messageService = new MessageService();
        ReportingService reportingService =
            new ReportingService(userDataService, priceService, exchangeRateService, messageService);
        report = reportingService.getReport(reportStartDate, reportEndDate);
    }

    @When("^the report is generated in (CAD|USD) between (\\d+-\\d+-\\d+) and (\\d+-\\d+-\\d+)$")
    public void the_report_is_generated_in(
        Currency currency,
        @Transform(LocalDateTransformer.class) LocalDate reportStartDate,
        @Transform(LocalDateTransformer.class) LocalDate reportEndDate
    ) {

        PriceService priceService = new PriceService();
        securityProxies.stream().map(SecurityProxy::toPricedSecurity).forEach(priceService::addPrice);

        userDataService = new UserDataService(currency);
        accountProxies.stream().map(AccountProxy::toAccount).forEach(userDataService::addAccount);

        Account account = accountProxies.get(0).toAccount();
        positionProxies.stream().map(PositionProxy::toPosition).forEach(account::addPosition);

        transactions = transactionProxies.stream().map(TransactionProxy::toTransaction).collect(Collectors.toList());
        transactions.forEach(userDataService::addTransaction);

        ExchangeRateService exchangeRateService = new ExchangeRateService();
        MessageService messageService = new MessageService();
        ReportingService reportingService = new ReportingService(userDataService, priceService, exchangeRateService, messageService);

        this.report = reportingService.getReport(reportStartDate, reportEndDate);
    }

    @Then("^the report has the following values$")
    public void the_report_has_the_following_values(List<ReportProxy> expectedReports) {

        ReportProxy expectedReport = expectedReports.get(0);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(report.getInitial()).isEqualTo(expectedReport.getInitial());
        softly.assertThat(report.getCurrent()).isEqualTo(expectedReport.getCurrent());
        softly.assertThat(report.getFee()).isEqualTo(expectedReport.getFee());
        softly.assertThat(report.getNetDeposits()).isEqualTo(expectedReport.getNetDeposits());
        softly.assertThat(report.getNetVariation().getAmount()).isEqualTo(expectedReport.getNetVariationAmount());
        softly.assertThat(report.getNetVariation().getPercentage()).isEqualTo(expectedReport.getNetVariationPercentage());
        softly.assertAll();
    }

    @And("^there are (\\d+) values per day$")
    public void values_per_day(int numberOfValuesPerDay) {
        assertThat(report.getValuePerDay()).hasSize(numberOfValuesPerDay);
    }

    @And("^there is no anomaly$")
    public void there_is_no_anomaly() {
        assertThat(report.getAnomalies()).isEmpty();
    }

    @And("^the price is (\\d+) for the day (\\d+) (\\d+-\\d+-\\d+)$")
    public void price_for(
        @Transform(BigDecimalTransformer.class) BigDecimal price,
        int day,
        @Transform(LocalDateTransformer.class) LocalDate date) {

        assertThat(report.getValuePerDay().get(day).getDate()).isEqualTo(date);
    }

    @Then("^the report has the following securities$")
    public void the_report_has_the_following_securities(List<SecurityProxy> securityProxies) {

        Security[] expectedSecurities = securityProxies.stream().map(SecurityProxy::toSecurity).toArray(Security[]::new);

        List<Security> securities =
            transactions.stream().map(Transaction::getSecurity).collect(Collectors.toList());

        assertThat(securities).containsExactly(expectedSecurities);
    }

    @And("^the report has the following accounts$")
    public void the_report_has_the_following_accounts(List<AccountProxy> accountProxies) {

        Account[] expectedAccounts = accountProxies.stream().map(AccountProxy::toAccount).toArray(Account[]::new);
        assertThat(this.accounts).containsExactly(expectedAccounts);

        String[] expectedAccountNames = accountProxies.stream().map(AccountProxy::toAccount).map(Account::getName).toArray(String[]::new);
        List<String> accountNames = transactions.stream().map(Transaction::getAccountName).collect(Collectors.toList());
        assertThat(accountNames).containsExactly(expectedAccountNames);
    }

    @And("^the report has the following positions$")
    public void the_report_has_the_following_positions(List<PositionProxy> positionProxies) {

        Position[] expectedPositions = positionProxies.stream().map(PositionProxy::toPosition).toArray(Position[]::new);

        assertThat(this.positions).containsExactly(expectedPositions);
    }

    @And("^the report has the following transactions$")
    public void the_report_has_the_following_transactions(List<TransactionProxy> transactionProxies) {

        Transaction[] expectedTransactions = transactionProxies.stream().map(TransactionProxy::toTransaction).toArray(Transaction[]::new);

        assertThat(this.transactions).containsExactly(expectedTransactions);
    }


    private static class ReportProxy {
        private String initial;
        private String current;
        private String fee;
        private String netDeposits;
        private String netVariationAmount;
        private String netVariationPercentage;

        public BigDecimal getInitial() {
            return new BigDecimal(initial).setScale(2, RoundingMode.HALF_UP);
        }

        public void setInitial(String initial) {
            this.initial = initial;
        }

        public BigDecimal getCurrent() {
            return new BigDecimal(current).setScale(2, RoundingMode.HALF_UP);
        }

        public void setCurrent(String current) {
            this.current = current;
        }

        public BigDecimal getFee() {
            return new BigDecimal(fee).setScale(2, RoundingMode.HALF_UP);
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public BigDecimal getNetDeposits() {
            return new BigDecimal(netDeposits).setScale(2, RoundingMode.HALF_UP);
        }

        public void setNetDeposits(String netDeposits) {
            this.netDeposits = netDeposits;
        }

        public BigDecimal getNetVariationAmount() {
            return new BigDecimal(netVariationAmount).setScale(2, RoundingMode.HALF_UP);
        }

        public void setNetVariationAmount(String netVariationAmount) {
            this.netVariationAmount = netVariationAmount;
        }

        public BigDecimal getNetVariationPercentage() {
            return new BigDecimal(netVariationPercentage).setScale(4, RoundingMode.HALF_UP);
        }

        public void setNetVariationPercentage(String netVariationPercentage) {
            this.netVariationPercentage = netVariationPercentage;
        }
    }

    private static class SecurityProxy {
        private String symbol;
        private String price;
        private Currency currency;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public BigDecimal getPrice() {
            return new BigDecimal(price).setScale(4, RoundingMode.HALF_UP);
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public Currency getCurrency() {
            return currency;
        }

        public void setCurrency(Currency currency) {
            this.currency = currency;
        }

        public PricedSecurity toPricedSecurity() {
            Security security = new Security().symbol(symbol).currency(this.currency);
            return new PricedSecurity(security, getPrice());
        }

        public Security toSecurity() {
            return new Security().symbol(symbol).currency(this.currency);
        }
    }

    private static class AccountProxy {
        private String name;
        private String cash;
        private Currency currency;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getCash() {
            return new BigDecimal(cash).setScale(4, RoundingMode.HALF_UP);
        }

        public void setCash(String cash) {
            this.cash = cash;
        }

        public Currency getCurrency() {
            return currency;
        }

        public void setCurrency(Currency currency) {
            this.currency = currency;
        }

        public Account toAccount() {
            return new Account().name(name).cash(getCash()).currency(getCurrency());
        }
    }

    private static class PositionProxy {
        private String security;
        private long quantity;

        public Security getSecurity() {
            return securityRegistry.get(security);
        }

        public void setSecurity(String security) {
            this.security = security;
        }

        public long getQuantity() {
            return quantity;
        }

        public void setQuantity(long quantity) {
            this.quantity = quantity;
        }

        public Position toPosition() {
            return new Position().quantity(quantity).security(getSecurity());
        }
    }

    private static class TransactionProxy {
        private String account_name;
        private TransactionType transaction_type;
        private String amount;
        private Currency currency;
        private String fee;
        private long quantity;
        private String trade_date;
        private String security;

        public String getAccount_name() {
            return account_name;
        }

        public void setAccount_name(String account_name) {
            this.account_name = account_name;
        }

        public TransactionType getTransaction_type() {
            return transaction_type;
        }

        public void setTransaction_type(TransactionType transaction_type) {
            this.transaction_type = transaction_type;
        }

        public BigDecimal getAmount() {
            return new BigDecimal(amount).setScale(4, RoundingMode.HALF_UP);
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

        public BigDecimal getFee() {
            return new BigDecimal(fee);
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public long getQuantity() {
            return quantity;
        }

        public void setQuantity(long quantity) {
            this.quantity = quantity;
        }

        public LocalDate getTrade_date() {
            return LocalDate.parse(trade_date);
        }

        public void setTrade_date(String trade_date) {
            this.trade_date = trade_date;
        }

        public Security getSecurity() {
            return securityRegistry.get(security);
        }

        public void setSecurity(String security) {
            this.security = security;
        }

        public Transaction toTransaction() {
            return new Transaction()
                .accountName(account_name)
                .type(transaction_type)
                .amount(getAmount())
                .currency(currency)
                .fee(getFee())
                .quantity(quantity)
                .tradeDate(getTrade_date())
                .security(getSecurity());
        }
    }
}
