package com.unityTest.testrunner.restImpl;

import com.unityTest.testrunner.entity.SourceFile;
import com.unityTest.testrunner.exception.EmptyFileException;
import com.unityTest.testrunner.exception.NoFilesUploadedException;
import com.unityTest.testrunner.models.response.Submission;
import com.unityTest.testrunner.repository.SourceFileRepository;
import com.unityTest.testrunner.restApi.SubmissionApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
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
    public void downloadSourceFiles(OutputStream out) throws FileNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"test.zip\"");
        Optional<SourceFile> sf = sourceFileRepository.findById(1000);

        SourceFile sourceFile = sf.get();

        // Write to the file
        File file = new File("filename.hs");


        try(FileOutputStream outputStream = new FileOutputStream(String.valueOf(file))) {
            outputStream.write(sourceFile.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }



        // create a list to add files to be zipped
//        ArrayList<File> files = new ArrayList<>(2);
//        files.add(new File("README.md"));
//
//        // package files
//        for (File file : files) {
//            //new zip entry and copying inputstream with file to zipOutputStream, after all closing streams
//            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
//            FileInputStream fileInputStream = new FileInputStream(file);
//
//            IOUtils.copy(fileInputStream, zipOutputStream);
//
//            fileInputStream.close();
//            zipOutputStream.closeEntry();
//        }
        FileSystemResource resource = new FileSystemResource(file);
        try (ZipOutputStream zippedOut = new ZipOutputStream(out)) {
            ZipEntry e = new ZipEntry(resource.getFilename());
            // Configure the zip entry, the properties of the file

            e.setSize(resource.contentLength());
            e.setTime(System.currentTimeMillis());
            // etc.
            zippedOut.putNextEntry(e);
            // And the content of the resource:
            StreamUtils.copy(resource.getInputStream(), zippedOut);
            zippedOut.closeEntry();
            zippedOut.finish();
        } catch (Exception e) {
            // Do something with Exception
            e.printStackTrace();
        }
    }
}
