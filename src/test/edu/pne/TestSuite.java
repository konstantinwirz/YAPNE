package edu.pne;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by konstantin on 24/10/14.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({edu.pne.pnml.io.TestSuite.class, edu.pne.pnml.model.TestSuite.class, edu.pne.utils.TestSuite.class})
public class TestSuite {
}
