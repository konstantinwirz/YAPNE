package edu.pne.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by konstantin on 24/10/14.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses(value = {TestPosition.class, TestArc.class, TestTransition.class, TestPlace.class, TestNet.class})
public class TestSuite {
}
