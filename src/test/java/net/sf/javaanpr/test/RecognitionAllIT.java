/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.javaanpr.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import net.sf.javaanpr.imageanalysis.CarSnapshot;
import net.sf.javaanpr.intelligence.Intelligence;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author jonassimonsen
 */
@RunWith(Parameterized.class)
public class RecognitionAllIT {

    private final File plateFile;
    private final String expectedPlate;

    public RecognitionAllIT(File testImage, String expectedResult) {
        this.plateFile = testImage;
        this.expectedPlate = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testInput() throws IOException {
        List<Object[]> list = new ArrayList<>();
        String snapshotDirPath = "src/test/resources/snapshots";
        String resultsPath = "src/test/resources/results.properties";
        Properties properties;
        try (InputStream resultsStream = new FileInputStream(new File(resultsPath))) {
            properties = new Properties();
            properties.load(resultsStream);
        }
        assertTrue(properties.size() > 0);

        File snapshotDir = new File(snapshotDirPath);

        for (File snap : snapshotDir.listFiles()) {
            list.add(new Object[]{
                snap,
                properties.getProperty(snap.getName())
            });
        }

        return list;
    }

    @Test
    public void testAllSnapshots() throws Exception {

        Intelligence intel = new Intelligence();
        assertNotNull(intel);

        CarSnapshot carSnapshot = new CarSnapshot(new FileInputStream(plateFile));
        assertNotNull("carSnap is null", carSnapshot);
        assertNotNull("carSnap.image is null", carSnapshot.getImage());

        String numberPlate = intel.recognize(carSnapshot, false);
        assertThat(numberPlate, is(expectedPlate));
    }
}
