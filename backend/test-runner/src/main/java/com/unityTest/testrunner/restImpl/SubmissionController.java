package com.unityTest.testrunner.restImpl;

import com.unityTest.testrunner.entity.SourceFile;
import com.unityTest.testrunner.repository.SourceFileRepository;
import com.unityTest.testrunner.restApi.SubmissionApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class SubmissionController implements SubmissionApi {
    @Autowired
    private SourceFileRepository sourceFileRepository;

    @Override
    public ResponseEntity<String> uploadSourceFiles(MultipartFile[] files) throws IOException {
        if(files.length == 0) {
            // Throw exception here
        }
        for(MultipartFile file : files) {
            // Create a new source file and save
            System.out.println(file.getName());
            try {
                SourceFile sourceFile = new SourceFile();
                sourceFile.setSubmissionId(2);
                sourceFile.setAuthorId("testing");
                sourceFile.setContent(file.getBytes());
                sourceFileRepository.save(sourceFile);
            } catch(Exception e) {
                e.printStackTrace();
            }

        }

        return new ResponseEntity<>("File(s) saved!", HttpStatus.OK);
    }
}
