package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        tags = {"@Run"},
        glue = {"steps",
                "configs"},
        features = "src/test/resources/features",
        stepNotifications = true,
        plugin = {"pretty",
                "json:target/cucumber/jsons/cucumber.json"
        }
)
public class RunCucumberTest {
}