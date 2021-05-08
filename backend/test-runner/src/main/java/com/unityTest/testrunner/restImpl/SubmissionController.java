package com.unityTest.testrunner.restImpl;

import com.unityTest.testrunner.entity.SourceFile;
import com.unityTest.testrunner.entity.Submission;
import com.unityTest.testrunner.entity.SubmissionFile;
import com.unityTest.testrunner.exception.ElementNotFoundException;
import com.unityTest.testrunner.exception.EmptyFileException;
import com.unityTest.testrunner.exception.NoFilesUploadedException;
import com.unityTest.testrunner.exception.TooManyFileUploadException;
import com.unityTest.testrunner.models.response.SubmissionEvent;
import com.unityTest.testrunner.repository.SubmissionFileRepository;
import com.unityTest.testrunner.repository.SubmissionRepository;
import com.unityTest.testrunner.restApi.SubmissionApi;
import com.unityTest.testrunner.service.SubmissionService;
import com.unityTest.testrunner.utils.Utils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Rest Controller for the /upload and /download endpoints
 */
@RestController
public class SubmissionController implements SubmissionApi {

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private SubmissionFileRepository sourceFileRepository;

    @Override
    public ResponseEntity<SubmissionEvent> uploadSourceFiles(Principal principal, MultipartFile[] files, Integer assignmentId) throws IOException {
        final int MAX_FILE_LIMIT = 10;

        // Check the number of files uploaded
        if(files == null || files.length == 0) {
            throw new NoFilesUploadedException();
        } else if(files.length >= MAX_FILE_LIMIT) {
            throw new TooManyFileUploadException(MAX_FILE_LIMIT);
        }
        // Get author id
        String authorId = Utils.getAuthToken(principal).getSubject();

        // Save submission to repo
        Submission submission = submissionService.saveSubmission(new Submission(assignmentId, authorId));

        // Save files to repo
        for(MultipartFile file : files) {
            // Throw exception if file is empty
            if(file.isEmpty()) throw new EmptyFileException(file.getOriginalFilename());
            // Create a new source file add it to the submission
            SubmissionFile submissionFile = new SubmissionFile(submission, file.getOriginalFilename(), file.getSize(), authorId, file.getBytes());
            submission.addSubmissionFile(submissionFile);
        }
        // Save all source files
        sourceFileRepository.saveAll(submission.getSourceFiles());

        // Generate event to return as response
        return new ResponseEntity<>(submission.generateSubmissionEvent(), HttpStatus.OK);
    }

    @Override
    public void downloadSourceFiles(Principal principal, HttpServletResponse response, Integer submissionId) throws IOException {
        final String ZIP_NAME = "submission.zip";

        // Set response headers
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", ZIP_NAME));

        // Fetch Submission from repository
        Submission submission = submissionService.getSubmissionById(submissionId);
        // Get id from token in request
        String authorId = Utils.getAuthToken(principal).getSubject();
        // Check that the author is the person requesting the files
        if(!authorId.equals(submission.getAuthorId())) throw new AccessDeniedException("Access denied");

        // Create output stream to stream zipped files
        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
        for(SourceFile file : submission.getSourceFiles()) {
            // Create new entry for file
            zipOutputStream.putNextEntry(new ZipEntry(file.getFileName()));
            // Create byte stream to send file contents to zip stream
            // This keeps all content IN MEMORY so files cannot be too large
            ByteArrayInputStream byteArrayStream = new ByteArrayInputStream(file.getContent());
            IOUtils.copy(byteArrayStream, zipOutputStream);

            // Close byte stream and entry in zip
            byteArrayStream.close();
            zipOutputStream.closeEntry();
        }
        zipOutputStream.close();        // Close zip stream
        response.flushBuffer();         // Flush response buffer
    }
}
