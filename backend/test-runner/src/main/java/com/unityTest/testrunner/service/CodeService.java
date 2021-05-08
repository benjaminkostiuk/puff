package com.unityTest.testrunner.service;

import com.unityTest.testrunner.entity.*;
import com.unityTest.testrunner.exception.code.InvalidFunctionName;
import com.unityTest.testrunner.exception.code.UnsupportedProgrammingLanguage;
import com.unityTest.testrunner.models.PLanguage;
import com.unityTest.testrunner.runner.PythonRunner;
import com.unityTest.testrunner.runner.Runner;
import com.unityTest.testrunner.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CodeService {

    @Value("${code-runner.dir.path}")
    private String workingDirectoryPath;

    @Value("${code-runner.cmds.python3}")
    private String pytestCmd;

    @Value("${code-runner.max-timeout}")
    private long timeoutInSeconds;

    @Async("threadPoolTaskExecutor")
    public void runTestCasesInSuite(ResponseBodyEmitter emitter, Submission submission, Suite suite, SuiteFile suiteFile, List<Case> testCases) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
        final String NEW_DIR_NAME = String.format("dir_%d_%d_%s", suite.getId(), suiteFile.getId(), dateFormat.format(new Date()));
        final String PATH = Utils.buildPath(this.workingDirectoryPath, NEW_DIR_NAME);

        // Create the new working directory
        new File(PATH).mkdirs();
        // Write all files from submission into directory
        for(SourceFile sourceFile : submission.getSourceFiles()) {
            FileUtils.writeByteArrayToFile(new File(Utils.buildPath(PATH, sourceFile.getFileName())), sourceFile.getContent());
        }
        // Write suite file into directory
        File tests = new File(Utils.buildPath(PATH, suiteFile.getFileName()));

        // Get code runner based on language
        // TODO IMPLEMENT RUNNERS FOR HASKELL AND JAVA
        Runner runner;
        switch (suite.getLanguage()) {
            case PYTHON3:
                runner = new PythonRunner(this.pytestCmd, this.timeoutInSeconds);
                break;
            default:
                throw new UnsupportedProgrammingLanguage(suite.getLanguage());
        }
        // Run all test cases and emit results
        for(Case testCase : testCases) {
            runner.writeToTestFile(tests, suiteFile, testCase);                             // Write test case content to suite file
            emitter.send(runner.runTestCase(testCase, suiteFile.getFileName(), PATH));      // Run test case and emit result
        };
        // Delete the directory after use
        FileUtils.deleteDirectory(new File(PATH));
        emitter.complete();
    }

    public String buildTestCaseCode(PLanguage lang, String functionName, String functionBody) {
        // Check for invalid characters in the function name
        if(!functionName.matches("[a-zA-Z][0-9a-zA-Z_]+")) throw new InvalidFunctionName(functionName, "Function identifier does not match regex [a-zA-Z][0-9a-zA-Z_]+");

        switch(lang) {
            case JAVA:
                // TODO Implement java code builder
                break;
            case HASKELL:
                // TODO Implement haskell code builder
                break;
            case PYTHON3:
                return buildPythonTestFunction(functionName, functionBody);
            default:
                throw new IllegalArgumentException("Programming language not supported");
        }
        return null;
    }

    private String buildPythonTestFunction(String funcName, String body) {
        // Define definition
        final String def = String.format("def test_%s(self):\n", funcName);
        // Create code block by indenting the body by one tab
        return def.concat(Utils.indent(body, 1).concat("\n"));
    }
}
