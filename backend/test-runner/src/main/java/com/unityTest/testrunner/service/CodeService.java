package com.unityTest.testrunner.service;

import com.unityTest.testrunner.exception.code.InvalidFunctionName;
import com.unityTest.testrunner.models.PLanguage;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CodeService {

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
