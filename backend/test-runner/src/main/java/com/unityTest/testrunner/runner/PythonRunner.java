package com.unityTest.testrunner.runner;

import com.unityTest.testrunner.entity.Case;
import com.unityTest.testrunner.entity.SuiteFile;
import com.unityTest.testrunner.models.ResultStatus;
import com.unityTest.testrunner.models.response.TestResult;
import com.unityTest.testrunner.utils.Utils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class PythonRunner implements Runner {

    private String pythonTestCmd;

    private long timeoutInSeconds;

    public PythonRunner(String pythonTestCmd, long timeoutInSeconds) {
        this.pythonTestCmd = pythonTestCmd;
        this.timeoutInSeconds = timeoutInSeconds;
    }

    @Override
    public TestResult runTestCase(Case caze, String suiteFileName, String path) {
        // Parse the python test command to run
        ArrayList<String> args = new ArrayList<>(Arrays.asList(this.pythonTestCmd.split(" ")));
        args.add(suiteFileName);

        ProcessBuilder pb = new ProcessBuilder(args);
        pb.directory(new File(path));       // Set working directory
        pb.redirectErrorStream(true);       // Combine error and with standard output
        try {
            Process p = pb.start();                         // Start the process
            InputStream stream = p.getInputStream();        // Capture the process output
            String output = IOUtils.toString(stream, StandardCharsets.UTF_8);

            if(!p.waitFor(this.timeoutInSeconds, TimeUnit.SECONDS)) {       // Check for timeout
                stream.close();         // close the stream
                p.destroyForcibly();    // kill the process
                return new TestResult(caze.getId(), ResultStatus.TIMEOUT_ERROR, "");
            } else {
                stream.close();
                switch (p.exitValue()) {
                    case 0:
                        return new TestResult(caze.getId(), ResultStatus.PASS, output);
                    default:
                        return new TestResult(caze.getId(), ResultStatus.FAIL, output);
                }
            }
        } catch (IOException | InterruptedException e) {
            return new TestResult(caze.getId(), ResultStatus.RUNTIME_ERROR, e.getMessage());
        }
    }

    @Override
    public void writeToTestFile(File testFile, SuiteFile suiteFile, Case caze) throws IOException {
        // Open a buffered writer to write the suite file contents with the test case
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(testFile, false));
        // Write suite file content
        bos.write(suiteFile.getContent());
        // Write test case content to file
        bos.write(String.format("\n%s", Utils.indent(caze.getCode(), 1)).getBytes());
        bos.flush();
        bos.close();
    }
}
