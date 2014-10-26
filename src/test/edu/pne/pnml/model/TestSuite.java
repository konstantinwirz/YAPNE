package edu.pne.pnml.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by konstantin on 24/10/14.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses(value = {PositionTest.class, ArcTest.class, TransitionTest.class, PlaceTest.class, NetTest.class})
public class TestSuite {
}
