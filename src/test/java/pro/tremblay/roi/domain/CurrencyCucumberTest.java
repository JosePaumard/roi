package pro.tremblay.roi.domain;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = {"classpath:pro/tremblay/roi/domain/currency.feature"},
    glue = {"pro.tremblay.roi.domain"},
    plugin = {"pretty"}
)
public class CurrencyCucumberTest {
}
