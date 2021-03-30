package com.unityTest.testrunner.restImpl;

import com.unityTest.testrunner.entity.SourceFile;
import com.unityTest.testrunner.exception.ElementNotFoundException;
import com.unityTest.testrunner.exception.EmptyFileException;
import com.unityTest.testrunner.exception.NoFilesUploadedException;
import com.unityTest.testrunner.exception.TooManyFileUploadException;
import com.unityTest.testrunner.models.response.Submission;
import com.unityTest.testrunner.repository.SourceFileRepository;
import com.unityTest.testrunner.restApi.SubmissionApi;
import com.unityTest.testrunner.utils.Utils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Rest Controller for the /upload and /download endpoints
 */
@RestController
public class SubmissionController implements SubmissionApi {

    @Autowired
    private SourceFileRepository sourceFileRepository;

    @Override
    public ResponseEntity<Submission> uploadSourceFiles(Principal principal, MultipartFile[] files, Integer assignmentId) throws IOException {
        final int MAX_FILE_LIMIT = 10;

        // Check the number of files uploaded
        if(files == null || files.length == 0) {
            throw new NoFilesUploadedException();
        } else if(files.length >= MAX_FILE_LIMIT) {
            throw new TooManyFileUploadException(MAX_FILE_LIMIT);
        }

        // Obtain a new submission id to group files by
        Integer submissionId = sourceFileRepository.nextSubmissionId();
        Date submissionDate = new Date();

        // Get author id
        String authorId = Utils.getAuthToken(principal).getId();

        // Create response for upload endpoint
        Submission submission = new Submission(submissionId, assignmentId, submissionDate);

        // Save files to repo
        for(MultipartFile file : files) {
            // Throw exception if file is empty
            if(file.isEmpty()) {
                throw new EmptyFileException(file.getOriginalFilename());
            }
            // Create a new source file and save it to the repository
            SourceFile sourceFile = new SourceFile(0, submissionId, assignmentId, file.getOriginalFilename(), submissionDate, authorId, file.getBytes());
            sourceFileRepository.save(sourceFile);
            // Add saved file to submission response
            submission.addSourceFile(file.getOriginalFilename(), file.getSize());
        }
        return new ResponseEntity<>(submission, HttpStatus.OK);
    }

    @Override
    public void downloadSourceFiles(Principal principal, HttpServletResponse response, Integer submissionId) throws IOException {
        final String ZIP_NAME = "submission.zip";

        // Set response headers
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", ZIP_NAME));

        // Get SourceFiles from submission
        List<SourceFile> files = sourceFileRepository.getSourceFilesBySubmissionId(submissionId);
        // Get id from token in request
        String authorId = Utils.getAuthToken(principal).getId();

        if(files == null || files.size() == 0) {        // Check that it found anything
            throw new ElementNotFoundException(Submission.class, "submissionId", String.valueOf(submissionId));
        } else if(!authorId.equals(files.get(0).getAuthorId())) {     // Check that the author is the person requesting the files
            throw new AccessDeniedException("Access denied");
        }

        // Create output stream to stream zipped files
        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
        for(SourceFile file : files) {
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
