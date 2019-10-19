package com.cgi.sms.business.service.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber-html-report", "json:target/cucumber-json-report.json"},
        features = "src/test/resources/features",
        format={"html:target/cucumber", "json:target/cucumber.json"}
)
public class CucumberRunnerTest {
}
