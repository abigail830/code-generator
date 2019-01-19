package com.cmb.domain.processor;

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FileUtilsTest {

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCopyExistingSourceFolderToNotExistTarget() {
        String source = "./template/gradle";
        String target = "./copyFolderTest";
        try {
            FileUtils.copyFileOrFolder(source, target);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException happen during testCopyFolder");
        }

        File file = new File(target);
        assertTrue(file.exists());

        File subFile = new File("./copyFolderTest/gradlew.bat");
        assertTrue(subFile.exists());

        FileUtils.delete(target);
    }

    @Test
    public void testCopyExistingSourceFile() {
        String source = "./template/gradle/gradlew.bat";
        String target = "./copyFileTest/gradlew.abc";
        try {
            FileUtils.copyFileOrFolder(source, target);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException happen during testCopyFolder");
        }

        File file = new File(target);
        assertTrue(file.exists());

        FileUtils.delete("./copyFileTest");

    }


}