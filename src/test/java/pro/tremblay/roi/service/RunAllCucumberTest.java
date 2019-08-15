package pro.tremblay.roi.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import pro.tremblay.roi.domain.CurrencyCucumberTest;

@RunWith(Suite.class)
@Suite.SuiteClasses(
    {CurrencyCucumberTest.class,
    CalculateCashValueCucumberTest.class,
    CalculateCommissionCucumberTest.class,
    CalculateNetDepositCucumberTest.class,
    GetExchangeRateCucumberTest.class}
)
public class RunAllCucumberTest {
}
