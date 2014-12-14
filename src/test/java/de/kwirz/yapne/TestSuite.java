package de.kwirz.yapne;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by konstantin on 24/10/14.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({de.kwirz.yapne.io.TestSuite.class, de.kwirz.yapne.model.TestSuite.class, de.kwirz.yapne.utils.TestSuite.class})
public class TestSuite {
}
