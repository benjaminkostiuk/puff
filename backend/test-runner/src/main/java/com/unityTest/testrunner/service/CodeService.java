package com.unityTest.testrunner.service;

import com.unityTest.testrunner.entity.Case;
import com.unityTest.testrunner.entity.Submission;
import com.unityTest.testrunner.entity.Suite;
import com.unityTest.testrunner.exception.code.InvalidFunctionName;
import com.unityTest.testrunner.models.PLanguage;
import com.unityTest.testrunner.models.ResultStatus;
import com.unityTest.testrunner.models.response.TestResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class CodeService {

    @Value("${code-runner.dir.path}")
    private String workingDirectoryPath;

    @Async("threadPoolTaskExecutor")
    public void runTestCasesInSuite(ResponseBodyEmitter emitter, Submission submission, Suite suite, List<Case> testCases) {

    }


    @Async("threadPoolTaskExecutor")
    public void asyncDoSomething(ResponseBodyEmitter emitter) throws IOException, InterruptedException {
        System.out.println("Starting asyncDoSomething on thread " + Thread.currentThread().getName());
        for(int i = 0; i <= 10; i++) {
            TestResult result = new TestResult(i, ResultStatus.PASS, "testing");
            emitter.send(result, MediaType.APPLICATION_JSON);
            Thread.sleep(1000);
        }
        this.asyncRun();
        System.out.println("Now on" + Thread.currentThread().getName());
        emitter.complete();
    }

    @Async("threadPoolTaskExecutor")
    public void asyncRun() {
        System.out.println("Starting asyncRun on thread " + Thread.currentThread().getName());
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
        return def.concat(indent(body, 1).concat("\n"));
    }

    // Indents a function body by *n* indents, can be replaced project upgrades to java 12
    private String indent(String body, int n) {
        String tab = String.join("", Collections.nCopies(n, "\t"));
        return tab + body.replace("\n", "\n"+tab);
    }
}