package com.unityTest.testrunner.restImpl;

import com.unityTest.testrunner.entity.SourceFile;
import com.unityTest.testrunner.exception.EmptyFileException;
import com.unityTest.testrunner.exception.NoFilesUploadedException;
import com.unityTest.testrunner.models.response.Submission;
import com.unityTest.testrunner.repository.SourceFileRepository;
import com.unityTest.testrunner.restApi.SubmissionApi;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<Submission> uploadSourceFiles(MultipartFile[] files, Integer assignmentId) throws IOException {
        // Check the number of files uploaded
        if(files == null || files.length == 0) {
            throw new NoFilesUploadedException();
        }
        // TODO Add limit of 10 files for uploads

        // Obtain a new submission id to group files by
        Integer submissionId = sourceFileRepository.nextSubmissionId();
        Date submissionDate = new Date();
        // TODO Get the author id
        String authorId = "";
        // Create response for upload endpoint
        Submission submission = new Submission(submissionId, assignmentId, submissionDate);

        // Save files to repo
        for(MultipartFile file : files) {
            // Check that file is not empty
            if(file.isEmpty()) {
                throw new EmptyFileException(file.getOriginalFilename());
            }
            // Create a new source file and save it to the repository
            SourceFile sourceFile = new SourceFile(0, submissionId, assignmentId, file.getOriginalFilename(), submissionDate, "TODO ADD HERE", file.getBytes());
            sourceFileRepository.save(sourceFile);

            // Add saved file to submission response
            submission.addSourceFile(file.getOriginalFilename(), file.getSize());
        }
        return new ResponseEntity<>(submission, HttpStatus.OK);
    }

    @Override
    public void downloadSourceFiles(HttpServletResponse response, Integer submissionId) throws IOException {
        final String ZIP_NAME = "submission.zip";

        // Set response headers
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", ZIP_NAME));

        // Get SourceFiles from submission
        List<SourceFile> files = sourceFileRepository.getSourceFilesBySubmissionId(submissionId);

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
