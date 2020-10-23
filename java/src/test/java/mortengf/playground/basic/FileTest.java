package mortengf.playground.basic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * File "file-to-read" is present in src/test/resources.
 *
 * Output produced:
 *
 * filePath: /Users/mgf/Source/Playground/java/target/test-classes/file-to-read
 * filename: /Users/mgf/Source/Playground/java/target/test-classes/file-to-read
 * fileUrl: file:/Users/mgf/Source/Playground/java/target/test-classes/file-to-read
 * filename2: /Users/mgf/Source/Playground/java/target/test-classes/file-to-read
 * temp file path: /var/folders/fl/g73kbcjx1tvc1yfdz0zsz15c0000gn/T/temp-212471819728037974.tmp
 */
public class FileTest {
    public static void main(String[] args) throws IOException {
        FileTest fileTest = new FileTest();
        fileTest.printFilename("file-to-read");
    }

    private void printFilename(String filenameToRead) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        // Will not work from a jar
        File file = new File(classLoader.getResource(filenameToRead).getFile());

        Path filePath = file.toPath();
        System.out.println("filePath: " + filePath);

        String filename = filePath.toString();
        System.out.println("filename: " + filename);

        // Will also not work from a jar
        final URL fileUrl = this.getClass().getClassLoader().getResource("file-to-read");
        System.out.println("fileUrl: " + fileUrl);

        final String filename2 = fileUrl.getPath();
        System.out.println("filename2: " + filename2);

        // *Will* work from a jar - but of course reads the file into memory and a temp file.
        // TODO: what is the correct/easiest way to get the filename of a file on the classpath from a jar? Should be easy!
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("file-to-read");
        final Path tempFilePath = Files.createTempFile("temp-", ".tmp");
        System.out.println("temp file path: " + tempFilePath.toAbsolutePath());
    }
}
