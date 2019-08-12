package pro.tremblay.roi.service;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = {"classpath:pro/tremblay/roi/service/commission.feature"},
    glue = {"pro.tremblay.roi.service"},
    plugin = {"pretty"}
)
public class CommissionCucumberTest {
}
