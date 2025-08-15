package test.java.storyTests;

import io.cucumber.core.cli.Main;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.cucumber.core.cli.CommandlineOptions.ORDER;
import static io.cucumber.core.options.Constants.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("src/test/resources/storyTests/features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "test.java.storyTests.steps")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-reports.html")

public class CucumberTestRunner {
    public static void main(String[] args) {
        List<String> featureFiles = getFeatureFiles("src/test/resources/storyTests/features");

        List<String> cucumberArgs = new ArrayList<>();
        cucumberArgs.add("--glue");
        cucumberArgs.add("test.java.storyTests.steps");
        cucumberArgs.add("--plugin");
        cucumberArgs.add("pretty");
        cucumberArgs.add("--plugin");
        cucumberArgs.add("html:target/cucumber-reports.html");
        cucumberArgs.add("--order");
        cucumberArgs.add("random"); // This forces Cucumber to execute in a random order
        cucumberArgs.addAll(featureFiles);

        Main.main(cucumberArgs.toArray(new String[0])); // Executes Cucumber tests
    }

    private static List<String> getFeatureFiles(String directoryPath) {
        List<String> featureFiles = new ArrayList<>();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".feature"));
            if (files != null) {
                for (File file : files) {
                    featureFiles.add(file.getAbsolutePath());
                }
                Collections.sort(featureFiles); // Ensure a consistent order before shuffling
            }
        }
        return featureFiles;
    }
}