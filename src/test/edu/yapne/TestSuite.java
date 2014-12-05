package edu.yapne;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by konstantin on 24/10/14.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({edu.yapne.io.TestSuite.class, edu.yapne.model.TestSuite.class, edu.yapne.utils.TestSuite.class})
public class TestSuite {
}
