package edu.illinois.cs.cs125.mp5.lib;

import org.testng.annotations.Test;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Test suite for the organic molecule analysis tests.
 * <p>
 * The provided test suite is correct and complete. You should not need to modify it.
 * However, you should understand it.
 *
 * @see <a href="https://cs125.cs.illinois.edu/MP/5/">MP5 Documentation</a>
 */
public class MoleculeAnalyzerTest {

    /** Timeout for analysis tests. Reference solution takes 15-31 ms from cold start. */
    private static final int ANALYZE_TEST_TIMEOUT = 300;

    /** Timeout for molecule naming tests. Reference solution takes 1-16 ms from warm start. */
    private static final int NAME_TEST_TIMEOUT = 400;

    /** Timeout for helper function tests. */
    private static final int HELPER_TEST_TIMEOUT = 350;

    /** Test molecular weight calculation. */
    @Test(timeOut = ANALYZE_TEST_TIMEOUT)
    public void testMolecularWeight() {
        for (MoleculeAnalysisTestInput input : analysisTestCases) {
            MoleculeAnalyzer analyzer = new MoleculeAnalyzer(input.molecule.build());
            double resultWeight = analyzer.getMolecularWeight();
            if (Math.abs(resultWeight - input.molecularWeight) > 0.1) {
                Assert.assertEquals(resultWeight, input.molecularWeight);
            }
        }
    }

    /** Test determining whether there are any charged atoms. */
    @Test(timeOut = ANALYZE_TEST_TIMEOUT)
    public void testHasCharged() {
        for (MoleculeAnalysisTestInput input : analysisTestCases) {
            MoleculeAnalyzer analyzer = new MoleculeAnalyzer(input.molecule.build());
            Assert.assertEquals(analyzer.hasChargedAtoms(), input.hasChargedAtom);
        }
    }

    /** Test naming straight-chain alkanes with no substituents. */
    @Test(timeOut = NAME_TEST_TIMEOUT)
    public void testNamingSimpleStraight() {
        runNamingTest(MoleculeNamingTestDifficulty.LINEAR_ALKANE);
    }

    /** Test naming cyclic alkanes with no substituents. */
    @Test(timeOut = NAME_TEST_TIMEOUT)
    public void testNamingSimpleCyclic() {
        runNamingTest(MoleculeNamingTestDifficulty.CYCLIC_ALKANE);
    }

    /** Test naming cyclic alkanes with one non-suffix-affecting substituent. */
    @Test(timeOut = NAME_TEST_TIMEOUT)
    public void testNamingOneSubstituentCyclic() {
        runNamingTest(MoleculeNamingTestDifficulty.MONOSUBSTITUTED_RING);
    }

    /** Test naming linear alkanes with one non-suffix affecting substituent. Alkyl substituents cause branching. */
    @Test(timeOut = NAME_TEST_TIMEOUT)
    public void testNamingOneSubstituentLinear() {
        runNamingTest(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR);
    }

    /** EXTRA CREDIT: Test naming molecules with multiple substituents. */
    @Test(timeOut = NAME_TEST_TIMEOUT)
    public void testNamingMultipleSubstituents() {
        runNamingTest(MoleculeNamingTestDifficulty.MULTIPLE_SUBSTITUENTS);
    }

    /** EXTRA CREDIT: Test naming complicated molecules with multiple substituents and priority tiebreaks. */
    @Test(timeOut = NAME_TEST_TIMEOUT)
    public void testNamingPriority() {
        runNamingTest(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK);
    }

    /**
     * Runs the naming test on the given group of molecules.
     * @param difficulty The difficulty/group of molecules to name.
     */
    private void runNamingTest(final MoleculeNamingTestDifficulty difficulty) {
        for (MoleculeNamingTestInput input : namingTestCases) {
            if (input.difficulty == difficulty) {
                MoleculeAnalyzer analyzer = new MoleculeAnalyzer(input.molecule.build());
                String result = analyzer.getIupacName();
                boolean validAnswer = false;
                for (String option : input.validNames) {
                    if (option.equals(result)) {
                        validAnswer = true;
                        break;
                    }
                }
                if (!validAnswer) {
                    /*
                    Only do an assertion if we know the answer is wrong.
                    This way, the easiest-to-compute option will be shown in the error message,
                    despite other fancier names being valid too.
                     */
                    Assert.assertEquals(result, input.validNames[0]);
                }
            }
        }
    }

    private static class MoleculeAnalysisTestInput {
        OrganicMoleculeBuilder molecule;
        double molecularWeight;
        boolean hasChargedAtom;
        public MoleculeAnalysisTestInput(final OrganicMoleculeBuilder setMolecule,
                                         final double setMW, final boolean setCharge) {
            molecule = setMolecule;
            molecularWeight = setMW;
            hasChargedAtom = setCharge;
        }
    }

    private enum MoleculeNamingTestDifficulty {
        LINEAR_ALKANE,
        CYCLIC_ALKANE,
        MONOSUBSTITUTED_RING,
        SINGLE_SUBSTITUENT_LINEAR,
        MULTIPLE_SUBSTITUENTS,
        PRIORITY_TIEBREAK
    }

    private static class MoleculeNamingTestInput {
        OrganicMoleculeBuilder molecule;
        String[] validNames;
        MoleculeNamingTestDifficulty difficulty;

        public MoleculeNamingTestInput(final MoleculeNamingTestDifficulty setDifficulty,
                                       final OrganicMoleculeBuilder setMolecule, final String... setNames) {
            molecule = setMolecule;
            difficulty = setDifficulty;
            validNames = setNames;
        }
    }

    /** All the analysis (non-naming) test cases. */
    private static List<MoleculeAnalysisTestInput> analysisTestCases = new ArrayList<>();
    /** All the naming test cases. */
    private static List<MoleculeNamingTestInput> namingTestCases = new ArrayList<>();

    static {
        // Analysis test cases
        analysisTestCases.add(new MoleculeAnalysisTestInput(
                new LinearOrganicMoleculeBuilder(1), 16.04, false));
        analysisTestCases.add(new MoleculeAnalysisTestInput(
                new LinearOrganicMoleculeBuilder(2), 30.07, false));
        analysisTestCases.add(new MoleculeAnalysisTestInput(
                new LinearOrganicMoleculeBuilder(3)
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1)),
                58.12, false));
        analysisTestCases.add(new MoleculeAnalysisTestInput(
                new LinearOrganicMoleculeBuilder(3)
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1)),
                72.15, false));
        analysisTestCases.add(new MoleculeAnalysisTestInput(
                new LinearOrganicMoleculeBuilder(2)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol(0)),
                45.06, true));
        analysisTestCases.add(new MoleculeAnalysisTestInput(
                new LinearOrganicMoleculeBuilder(6), 86.18, false));
        analysisTestCases.add(new MoleculeAnalysisTestInput(
                new LinearOrganicMoleculeBuilder(5)
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createAlcohol(2)),
                89.16, true));
        analysisTestCases.add(new MoleculeAnalysisTestInput(
                new LinearOrganicMoleculeBuilder(3)
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createCarbonyl()),
                58.08, false));
        analysisTestCases.add(new MoleculeAnalysisTestInput(
                new LinearOrganicMoleculeBuilder(2)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE))
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE)),
                179.39, false));
        analysisTestCases.add(new MoleculeAnalysisTestInput(
                new LinearOrganicMoleculeBuilder(12)
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlkyl(5))
                        .addSubstituent(4, OrganicMoleculeBuilder.Substituent.createAlcohol(0))
                        .addSubstituent(10, OrganicMoleculeBuilder.Substituent.createAlcohol(2)),
                272.48, true));
        analysisTestCases.add(new MoleculeAnalysisTestInput(
                new LinearOrganicMoleculeBuilder(323),
                4532.74, false));

        // Naming simple straight alkanes
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.LINEAR_ALKANE,
                new LinearOrganicMoleculeBuilder(1), "methane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.LINEAR_ALKANE,
                new LinearOrganicMoleculeBuilder(2), "ethane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.LINEAR_ALKANE,
                new LinearOrganicMoleculeBuilder(9), "nonane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.LINEAR_ALKANE,
                new LinearOrganicMoleculeBuilder(3), "propane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.LINEAR_ALKANE,
                new LinearOrganicMoleculeBuilder(6), "hexane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.LINEAR_ALKANE,
                new LinearOrganicMoleculeBuilder(4), "butane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.LINEAR_ALKANE,
                new LinearOrganicMoleculeBuilder(7), "heptane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.LINEAR_ALKANE,
                new LinearOrganicMoleculeBuilder(5), "pentane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.LINEAR_ALKANE,
                new LinearOrganicMoleculeBuilder(8), "octane"));
        /*
        The "substituents" on the remaining test cases in this section are at the ends of
        the molecule, so they actually just elongate the chain instead of add branching.
        This is to make sure the solution can't rely on the array indexing pattern of the builder,
        since what matters is the BondedAtom data structure, not the builder implementation.
        Several test cases in other sections use this trick as well.
         */
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.LINEAR_ALKANE,
                new LinearOrganicMoleculeBuilder(3)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlkyl(1)), "butane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.LINEAR_ALKANE,
                new LinearOrganicMoleculeBuilder(4)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlkyl(2))
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createAlkyl(1)), "heptane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.LINEAR_ALKANE,
                new LinearOrganicMoleculeBuilder(1)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlkyl(4))
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlkyl(4)), "nonane"));

        // Naming simple cyclic alkanes
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.CYCLIC_ALKANE,
                new CyclicOrganicMoleculeBuilder(3), "cyclopropane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.CYCLIC_ALKANE,
                new CyclicOrganicMoleculeBuilder(10), "cyclodecane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.CYCLIC_ALKANE,
                new CyclicOrganicMoleculeBuilder(6), "cyclohexane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.CYCLIC_ALKANE,
                new CyclicOrganicMoleculeBuilder(8), "cyclooctane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.CYCLIC_ALKANE,
                new CyclicOrganicMoleculeBuilder(4), "cyclobutane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.CYCLIC_ALKANE,
                new CyclicOrganicMoleculeBuilder(5), "cyclopentane"));

        // Naming cyclic alkanes with one low-priority substituent
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MONOSUBSTITUTED_RING,
                new CyclicOrganicMoleculeBuilder(3)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE)), "1-fluorocyclopropane", "fluorocyclopropane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MONOSUBSTITUTED_RING,
                new CyclicOrganicMoleculeBuilder(6)
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createAlkyl(1)), "1-methylcyclohexane", "methylcyclohexane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MONOSUBSTITUTED_RING,
                new CyclicOrganicMoleculeBuilder(8)
                        .addSubstituent(5, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE)), "1-bromocyclooctane", "bromocyclooctane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MONOSUBSTITUTED_RING,
                new CyclicOrganicMoleculeBuilder(4)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlkyl(3)), "1-propylcyclobutane", "propylcyclobutane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MONOSUBSTITUTED_RING,
                new CyclicOrganicMoleculeBuilder(5)
                        .addSubstituent(4, OrganicMoleculeBuilder.Substituent.createAlkyl(8)), "1-octylcyclopentane", "octylcyclopentane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MONOSUBSTITUTED_RING,
                new CyclicOrganicMoleculeBuilder(10)
                        .addSubstituent(9, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE)), "1-chlorocyclodecane", "chlorocyclodecane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MONOSUBSTITUTED_RING,
                new CyclicOrganicMoleculeBuilder(7)
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(7)), "1-heptylcycloheptane", "heptylcycloheptane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MONOSUBSTITUTED_RING,
                new CyclicOrganicMoleculeBuilder(9)
                        .addSubstituent(5, OrganicMoleculeBuilder.Substituent.createAlkyl(6)), "1-hexylcyclononane", "hexylcyclononane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MONOSUBSTITUTED_RING,
                new CyclicOrganicMoleculeBuilder(5)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE)), "1-bromocyclopentane", "bromocyclopentane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MONOSUBSTITUTED_RING,
                new CyclicOrganicMoleculeBuilder(4)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol()), "cyclobutan-1-ol", "cyclobutanol"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MONOSUBSTITUTED_RING,
                new CyclicOrganicMoleculeBuilder(9)
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlcohol()), "cyclononan-1-ol", "cyclononanol"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MONOSUBSTITUTED_RING,
                new CyclicOrganicMoleculeBuilder(5)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createCarbonyl()), "cyclopentan-1-one", "cyclopentanone"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MONOSUBSTITUTED_RING,
                new CyclicOrganicMoleculeBuilder(6)
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createCarbonyl()), "cyclohexan-1-one", "cyclohexanone"));


        // Naming linear/branching alkanes with one low-priority substituent on the main chain
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(3)
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1)), "2-methylpropane", "isobutane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(1)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE)), "1-chloromethane", "chloromethane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(5)
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlkyl(2)), "3-ethylpentane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(4)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlkyl(2))
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE)), "1-bromohexane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(4)
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1)), "2-methylbutane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(3)
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(2)), "2-methylbutane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(4)
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlkyl(1)), "2-methylbutane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(9)
                        .addSubstituent(7, OrganicMoleculeBuilder.Substituent.createAlkyl(2)), "3-methyldecane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(7)
                        .addSubstituent(6, OrganicMoleculeBuilder.Substituent.createAlkyl(2))
                        .addSubstituent(4, OrganicMoleculeBuilder.Substituent.createAlkyl(3)), "5-propylnonane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(7)
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createAlkyl(4)), "4-propyloctane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(6)
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE)), "3-fluorohexane"));
        // ^^^ ABOVE: low-priority substituents
        // vvv BELOW: high-priority substituents
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(5)
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createCarbonyl()), "pentan-3-one"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(1)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol()), "methan-1-ol", "methanol"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(2)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol()), "ethan-1-ol", "ethanol"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(2)
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlcohol()), "ethan-1-ol", "ethanol"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(3)
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlcohol()), "propan-2-ol", "isopropanol"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(3)
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlcohol()), "propan-1-ol"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(6)
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createCarbonyl()), "hexan-2-one"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(6)
                        .addSubstituent(4, OrganicMoleculeBuilder.Substituent.createCarbonyl()), "hexan-2-one"));
        // ^^^ ABOVE: high-priority substituents
        // vvv BELOW: end groups
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(4)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createCarbonyl()), "butanal"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(3)
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createCarbonyl()), "propanal"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(1)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createCarbonyl()), "methanal", "formaldehyde"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(10)
                        .addSubstituent(9, OrganicMoleculeBuilder.Substituent.createCarbonyl()), "decanal"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(6)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createCarbonyl()), "hexanal"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(1)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol()), "methanoic acid", "formic acid"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(2)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol()), "ethanoic acid", "acetic acid"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(2)
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlcohol()), "ethanoic acid", "acetic acid"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(8)
                        .addSubstituent(7, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(7, OrganicMoleculeBuilder.Substituent.createAlcohol()), "octanoic acid"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(5)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol()), "pentanoic acid"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(3)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol()), "propanoic acid"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.SINGLE_SUBSTITUENT_LINEAR,
                new LinearOrganicMoleculeBuilder(4)
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createAlcohol()), "butanoic acid"));

        // Naming structures with multiple substituents but no difficult priority tiebreaks
        // This is extra credit!
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MULTIPLE_SUBSTITUENTS,
                new LinearOrganicMoleculeBuilder(5)
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createAlkyl(1)), "2,4-dimethylpentane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MULTIPLE_SUBSTITUENTS,
                new LinearOrganicMoleculeBuilder(3)
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1)), "2,2-dimethylpropane", "neopentane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MULTIPLE_SUBSTITUENTS,
                new LinearOrganicMoleculeBuilder(6)
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlkyl(2)), "3-ethyl-2-methylhexane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MULTIPLE_SUBSTITUENTS,
                new LinearOrganicMoleculeBuilder(2)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlcohol()), "ethane-1,2-diol"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MULTIPLE_SUBSTITUENTS,
                new LinearOrganicMoleculeBuilder(1)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol()), "methane-1,1,1,1-tetrol", "methanetetrol"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MULTIPLE_SUBSTITUENTS,
                new LinearOrganicMoleculeBuilder(4)
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createCarbonyl()), "butane-2,3-dione"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MULTIPLE_SUBSTITUENTS,
                new LinearOrganicMoleculeBuilder(8)
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(4, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(5, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(6, OrganicMoleculeBuilder.Substituent.createCarbonyl()), "octane-2,3,4,5,6-pentone"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MULTIPLE_SUBSTITUENTS,
                new LinearOrganicMoleculeBuilder(8)
                        .addSubstituent(5, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                        .addSubstituent(4, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createAlkyl(2))
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1)), "4-ethyl-2,3,5,6-tetramethyloctane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MULTIPLE_SUBSTITUENTS,
                new LinearOrganicMoleculeBuilder(6)
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createAlkyl(2))
                        .addSubstituent(4, OrganicMoleculeBuilder.Substituent.createAlkyl(2)), "4-ethyl-3-methylheptane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MULTIPLE_SUBSTITUENTS,
                new LinearOrganicMoleculeBuilder(8)
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))
                        .addSubstituent(6, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE))
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createAlkyl(3)), "6-bromo-2-fluoro-5-propyloctane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MULTIPLE_SUBSTITUENTS,
                new LinearOrganicMoleculeBuilder(2)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE)), "1,2-difluoroethane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MULTIPLE_SUBSTITUENTS,
                new LinearOrganicMoleculeBuilder(6)
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlkyl(2))
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createAlkyl(2))
                        .addSubstituent(5, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE)), "1-bromo-3,4-diethylhexane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MULTIPLE_SUBSTITUENTS,
                new LinearOrganicMoleculeBuilder(4)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE)), "2-bromo-3-chloro-1-fluoro-2-methylbutane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MULTIPLE_SUBSTITUENTS,
                new CyclicOrganicMoleculeBuilder(4)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createAlkyl(1)), "1,2,3,4-tetramethylcyclobutane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MULTIPLE_SUBSTITUENTS,
                new CyclicOrganicMoleculeBuilder(9)
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE))
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlkyl(8)), "1-chloro-1-octylcyclononane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MULTIPLE_SUBSTITUENTS,
                new CyclicOrganicMoleculeBuilder(6)
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(4))
                        .addSubstituent(4, OrganicMoleculeBuilder.Substituent.createAlkyl(4)), "1,4-dibutylcyclohexane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MULTIPLE_SUBSTITUENTS,
                new CyclicOrganicMoleculeBuilder(3)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlcohol()), "cyclopropane-1,1,2,2,3,3-hexol"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.MULTIPLE_SUBSTITUENTS,
                new CyclicOrganicMoleculeBuilder(5)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(4, OrganicMoleculeBuilder.Substituent.createCarbonyl()), "cyclopentane-1,2,3,4,5-pentone"));

        // Naming structures with multiple substituents requiring priority tiebreaks
        // This is extra credit and very difficult!
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new LinearOrganicMoleculeBuilder(2)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE)), "2-bromo-2-chloroethan-1-ol"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new LinearOrganicMoleculeBuilder(4)
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlkyl(1)), "4-methylpentanoic acid"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new LinearOrganicMoleculeBuilder(7)
                        .addSubstituent(6, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE)), "5-bromoheptanal"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new LinearOrganicMoleculeBuilder(4)
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE)), "4-fluorobutan-2-ol"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new LinearOrganicMoleculeBuilder(3)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(5))
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlcohol()), "2-pentylpropane-1,3-diol"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new LinearOrganicMoleculeBuilder(3)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE))
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE)), "2-bromo-1,1,3-trifluoropropane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new LinearOrganicMoleculeBuilder(4)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createAlcohol()), "3,4-difluoro-3-methylbutanoic acid"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new LinearOrganicMoleculeBuilder(3)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE)), "1-bromo-3-chloropropane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new LinearOrganicMoleculeBuilder(3)
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE)), "1-bromo-3-chloropropane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new LinearOrganicMoleculeBuilder(3)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE))
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlcohol()), "3-bromo-1-chloropropan-1-ol"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new LinearOrganicMoleculeBuilder(2)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE)), "1,2-dibromo-1-chloro-2-fluoroethane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new LinearOrganicMoleculeBuilder(2)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE))
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE)), "1,2-dibromo-1-chloro-2-fluoroethane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new LinearOrganicMoleculeBuilder(4)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE))
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE))
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE))
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createCarbonyl()), "4,4,4-trichlorobutan-2-one"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new LinearOrganicMoleculeBuilder(5)
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createAlkyl(3))
                        .addSubstituent(4, OrganicMoleculeBuilder.Substituent.createCarbonyl()), "4-bromo-3-methyl-2-propylpentanal"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new CyclicOrganicMoleculeBuilder(3)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlcohol()), "cyclopropane-1,2-diol"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new CyclicOrganicMoleculeBuilder(3)
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlcohol()), "cyclopropane-1,2-diol"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new CyclicOrganicMoleculeBuilder(5)
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))
                        .addSubstituent(4, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE)), "1,3-dibromocyclopentane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new CyclicOrganicMoleculeBuilder(6)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE)), "1-bromo-2-methylcyclohexane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new CyclicOrganicMoleculeBuilder(6)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlkyl(2))
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlkyl(2))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE)), "2-bromo-1,1-diethylcyclohexane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new CyclicOrganicMoleculeBuilder(5)
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlkyl(5)), "3-pentylcyclopentan-1-ol"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new CyclicOrganicMoleculeBuilder(5)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlkyl(3))
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createAlcohol()), "4-propylcyclopentane-1,2-diol"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new CyclicOrganicMoleculeBuilder(4)
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createAlkyl(1)), "2,2-dibromo-4-methylcyclobutan-1-one"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new CyclicOrganicMoleculeBuilder(4)
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createCarbonyl()), "2,4-dibromo-2-methylcyclobutane-1,3-dione"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new CyclicOrganicMoleculeBuilder(7)
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE))
                        .addSubstituent(4, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                        .addSubstituent(5, OrganicMoleculeBuilder.Substituent.createAlkyl(2))
                        .addSubstituent(5, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE)), "2-chloro-7-ethyl-7-fluorocycloheptane-1,4-dione"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new CyclicOrganicMoleculeBuilder(8)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlkyl(2))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(5, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(5, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(6, OrganicMoleculeBuilder.Substituent.createAlkyl(3)), "6-ethyl-6-methyl-8-propylcyclooctane-1,1,3,5-tetrol"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new CyclicOrganicMoleculeBuilder(3)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE))
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE)), "1-bromo-2-chloro-3-fluorocyclopropane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new CyclicOrganicMoleculeBuilder(3)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE)), "1-bromo-2-chloro-3-fluorocyclopropane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new CyclicOrganicMoleculeBuilder(3)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE))
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE))
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE)), "1,2-dibromo-1-chloro-3-fluorocyclopropane"));
        namingTestCases.add(new MoleculeNamingTestInput(MoleculeNamingTestDifficulty.PRIORITY_TIEBREAK,
                new CyclicOrganicMoleculeBuilder(3)
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE))
                        .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE))
                        .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlcohol())
                        .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE)), "2,3-dibromo-2-chloro-1-fluorocyclopropan-1-ol"));
    }

    /*

    ^^^^^^^^^^^^^^^^^^^
    ABOVE: GRADED TESTS

    The remaining tests are to help you test your helper functions.
    They are not graded; you may comment them out if you know what you're doing
    and your set of helper functions is incompatible with ours.

    Some of these tests rely on the order in which OrganicMoleculeBuilder
    attaches neighbors to each atom. You should not do this in your code;
    it's finicky and not guaranteed to work. (In general, relying on undocumented
    behavior of other people's code is bad.)

    Note that these don't necessarily check functionality required for getting extra credit -
    for that, you're on your own.

    BELOW: HELPER TESTS
    vvvvvvvvvvvvvvvvvvv

     */

    /** getAllAtoms returns a collection of all the atoms in the molecule */
    @Test(timeOut = HELPER_TEST_TIMEOUT)
    public void helpGetAllAtoms() {
        MoleculeAnalyzer methane = new MoleculeAnalyzer(new LinearOrganicMoleculeBuilder(1).build());
        Assert.assertEquals(methane.getAllAtoms().stream().distinct().count(), 5, "findAllAtoms failed on a 1-carbon molecule");

        MoleculeAnalyzer butane = new MoleculeAnalyzer(new LinearOrganicMoleculeBuilder(4).build());
        Assert.assertEquals(butane.getAllAtoms().stream().distinct().count(), 14, "findAllAtoms failed on a 4-carbon molecule");

        MoleculeAnalyzer isopropanol = new MoleculeAnalyzer(new LinearOrganicMoleculeBuilder(3)
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlcohol()).build());
        Assert.assertEquals(isopropanol.getAllAtoms().stream().distinct().count(), 12, "findAllAtoms failed on a chain with a substituent");

        MoleculeAnalyzer ethoxide = new MoleculeAnalyzer(new LinearOrganicMoleculeBuilder(2)
                .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol(0)).build());
        Assert.assertEquals(ethoxide.getAllAtoms().stream().distinct().count(), 8, "findAllAtoms failed on a charged molecule");

        MoleculeAnalyzer cyclopropane = new MoleculeAnalyzer(new CyclicOrganicMoleculeBuilder(3).build());
        Assert.assertEquals(cyclopropane.getAllAtoms().stream().distinct().count(), 9, "findAllAtoms failed on a cyclic molecule");

        MoleculeAnalyzer ethylcyclobutane = new MoleculeAnalyzer(new CyclicOrganicMoleculeBuilder(4)
                .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlkyl(2)).build());
        Assert.assertEquals(ethylcyclobutane.getAllAtoms().stream().distinct().count(), 18, "findAllAtoms failed on a substituted cyclic molecule");
    }

    /** getTips finds all the tip carbons in the molecule */
    @Test(timeOut = HELPER_TEST_TIMEOUT)
    public void helpGetTips() {
        List<BondedAtom> ethaneTips = new MoleculeAnalyzer(new LinearOrganicMoleculeBuilder(2).build()).getTips();
        Assert.assertEquals(ethaneTips.size(), 2, "getTips didn't find the tips of a 2-carbon molecule");
        Assert.assertTrue(ethaneTips.get(0).isCarbon() && ethaneTips.get(1).isCarbon(), "getTips returned non-carbon atoms");

        BondedAtom propanal = new LinearOrganicMoleculeBuilder(3)
                .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createCarbonyl()).build();
        List<BondedAtom> propaneTips = new MoleculeAnalyzer(propanal).getTips();
        Assert.assertEquals(propaneTips.size(), 2, "getTips didn't find the tips of a substituted 3-carbon molecule");
        Assert.assertTrue(propaneTips.contains(propanal), "getTips missed the first backbone carbon");
        Assert.assertTrue(propaneTips.contains(Objects.requireNonNull(propanal.getConnectedAtom(1)).getConnectedAtom(1)), "getTips missed the last backbone carbon");

        List<BondedAtom> methaneTips = new MoleculeAnalyzer(new LinearOrganicMoleculeBuilder(1).build()).getTips();
        Assert.assertEquals(methaneTips.size(), 1, "getTips didn't recognize a lone carbon as a tip");

        BondedAtom isopentane = new LinearOrganicMoleculeBuilder(4)
                .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlkyl(1)).build();
        List<BondedAtom> isopentaneTips = new MoleculeAnalyzer(isopentane).getTips();
        Assert.assertEquals(isopentaneTips.stream().distinct().count(), 3, "getTips failed on a branched linear molecule");
        final String isopentaneFail = "getTips misidentified the tips of 2-methylbutane";
        Assert.assertTrue(isopentaneTips.contains(isopentane), isopentaneFail);
        Assert.assertTrue(isopentaneTips.contains(Objects.requireNonNull(Objects.requireNonNull(isopentane.getConnectedAtom(1)).getConnectedAtom(1)).getConnectedAtom(1)), isopentaneFail);
        Assert.assertTrue(isopentaneTips.contains(Objects.requireNonNull(Objects.requireNonNull(isopentane.getConnectedAtom(1)).getConnectedAtom(1)).getConnectedAtom(2)), isopentaneFail);
        List<BondedAtom> cyclohexaneTips = new MoleculeAnalyzer(new CyclicOrganicMoleculeBuilder(6).build()).getTips();

        Assert.assertEquals(cyclohexaneTips.size(), 0, "getTips found tips on an unsubstituted ring");
        List<BondedAtom> methylcyclopropaneTips = new MoleculeAnalyzer(new CyclicOrganicMoleculeBuilder(3)
                .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlkyl(1)).build()).getTips();

        Assert.assertEquals(methylcyclopropaneTips.size(), 1, "getTips missed the tip of a substituted ring");
        Assert.assertFalse(Objects.requireNonNull(methylcyclopropaneTips.get(0).getConnectedAtom(1)).isCarbon(), "getTips misidentified the tip of a substituted ring");
    }

    /** findPath finds the path from one atom to another in a linear molecule */
    @Test(timeOut = HELPER_TEST_TIMEOUT)
    public void helpFindPath() {
        BondedAtom ethane = new LinearOrganicMoleculeBuilder(2).build();
        List<BondedAtom> ethanePath = new MoleculeAnalyzer(ethane).findPath(ethane, ethane.getConnectedAtom(1));
        Assert.assertEquals(ethanePath.size(), 2, "findPath found a path of the wrong length on ethane");
        Assert.assertSame(ethanePath.get(0), ethane, "findPath's path on ethane started at the wrong atom");
        Assert.assertSame(ethanePath.get(1), ethane.getConnectedAtom(1), "findPath's path on ethane ended at the wrong atom");

        BondedAtom isobutane = new LinearOrganicMoleculeBuilder(3)
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1)).build();
        List<BondedAtom> isobutanePath = new MoleculeAnalyzer(isobutane).findPath(Objects.requireNonNull(isobutane.getConnectedAtom(1)).getConnectedAtom(2), Objects.requireNonNull(isobutane.getConnectedAtom(1)).getConnectedAtom(1));
        Assert.assertEquals(isobutanePath.size(), 3, "findPath found a path of the wrong length from one tip of 2-methylpropane to another");
        Assert.assertSame(isobutanePath.get(0), Objects.requireNonNull(isobutane.getConnectedAtom(1)).getConnectedAtom(2), "findPath's path on 2-methylpropane started at the wrong atom");
        Assert.assertSame(isobutanePath.get(1), isobutane.getConnectedAtom(1), "findPath's path on 2-methylpropane was incorrect");
        Assert.assertSame(isobutanePath.get(2), Objects.requireNonNull(isobutane.getConnectedAtom(1)).getConnectedAtom(1), "findPath's path on 2-methylpropane ended at the wrong atom");
    }

    /** isCyclic checks whether the molecule contains a cycle */
    @Test(timeOut = HELPER_TEST_TIMEOUT)
    public void helpIsCyclic() {
        MoleculeAnalyzer propane = new MoleculeAnalyzer(new LinearOrganicMoleculeBuilder(3).build());
        Assert.assertFalse(propane.isRing(), "isCyclic misidentified a linear molecule");

        MoleculeAnalyzer methane = new MoleculeAnalyzer(new LinearOrganicMoleculeBuilder(1).build());
        Assert.assertFalse(methane.isRing(), "isCyclic misidentified a one-carbon molecule");

        MoleculeAnalyzer isobutane = new MoleculeAnalyzer(new LinearOrganicMoleculeBuilder(3)
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1)).build());
        Assert.assertFalse(isobutane.isRing(), "isCyclic misidentified a branched linear molecule");

        MoleculeAnalyzer cyclopropane = new MoleculeAnalyzer(new CyclicOrganicMoleculeBuilder(3).build());
        Assert.assertTrue(cyclopropane.isRing(), "isCyclic misidentified a small cyclic molecule");

        MoleculeAnalyzer cyclooctane = new MoleculeAnalyzer(new CyclicOrganicMoleculeBuilder(8).build());
        Assert.assertTrue(cyclooctane.isRing(), "isCyclic misidentified a large cyclic molecule");

        MoleculeAnalyzer ethylcyclobutane = new MoleculeAnalyzer(new CyclicOrganicMoleculeBuilder(4)
                .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlkyl(2)).build());
        Assert.assertTrue(ethylcyclobutane.isRing(), "isCyclic misidentified a cyclic molecule with a substituent");

        MoleculeAnalyzer mess = new MoleculeAnalyzer(new CyclicOrganicMoleculeBuilder(7)
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(2))
                .addSubstituent(4, OrganicMoleculeBuilder.Substituent.createCarbonyl())
                .addSubstituent(6, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.CHLORINE)).build());
        Assert.assertTrue(mess.isRing(), "isCyclic misidentified a complicated cyclic molecule");
    }

    /** getRing gets a list of the carbon atoms in the cycle */
    @Test(timeOut = HELPER_TEST_TIMEOUT)
    public void helpGetRing() {
        MoleculeAnalyzer cyclopentane = new MoleculeAnalyzer(new CyclicOrganicMoleculeBuilder(5).build());
        List<BondedAtom> cyclopentaneRing = cyclopentane.getRing();
        Assert.assertNotNull(cyclopentaneRing, "getRing didn't find a ring on a simple cyclic molecule");
        Assert.assertEquals(cyclopentaneRing.size(), 5, "getRing found a ring of the wrong size on a simple cyclic molecule");

        MoleculeAnalyzer cyclobutanone = new MoleculeAnalyzer(new CyclicOrganicMoleculeBuilder(4)
                .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createCarbonyl()).build());
        List<BondedAtom> cyclobutanoneRing = cyclobutanone.getRing();
        Assert.assertNotNull(cyclobutanoneRing, "getRing didn't find a ring on a cyclic molecule with a substituent");
        Assert.assertEquals(cyclobutanoneRing.size(), 4, "getRing found a ring of the wrong size on a cyclic molecule with a substituent");

        MoleculeAnalyzer tripropylcyclopropane = new MoleculeAnalyzer(new CyclicOrganicMoleculeBuilder(3)
                .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlkyl(3))
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(3))
                .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlkyl(3)).build());
        List<BondedAtom> tripropylcyclopropaneRing = tripropylcyclopropane.getRing();
        Assert.assertNotNull(tripropylcyclopropaneRing, "getRing didn't find a ring on a cyclic molecule with alkyl substituents");
        Assert.assertEquals(tripropylcyclopropaneRing.size(), 3, "getRing found a ring of the wrong size on a cyclic molecule with alkyl substituents");
    }

    /** getBackbones finds all possible backbones (paths between two tips) for a linear molecule */
    @Test(timeOut = HELPER_TEST_TIMEOUT)
    public void helpGetBackbones() {
        List<List<BondedAtom>> ethaneBackbones = new MoleculeAnalyzer(new LinearOrganicMoleculeBuilder(2).build()).getBackbones();
        ethaneBackbones.forEach(lba -> lba.forEach(bondedAtom -> Assert.assertTrue(bondedAtom.isCarbon(), "getBackbones included non-carbon atoms in the backbone")));
        Assert.assertEquals(ethaneBackbones.size(), 2, "getBackbones didn't return both directions for traversing a 2-carbon molecule");

        List<List<BondedAtom>> isopropanolBackbones = new MoleculeAnalyzer(new LinearOrganicMoleculeBuilder(3)
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlcohol()).build()).getBackbones();
        Assert.assertEquals(isopropanolBackbones.size(), 2, "getBackbones failed on a substituted 3-carbon molecule");

        List<List<BondedAtom>> isobutaneBackbones = new MoleculeAnalyzer(new LinearOrganicMoleculeBuilder(3)
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1)).build()).getBackbones();
        Assert.assertEquals(isobutaneBackbones.size(), 6, "getBackbones failed on a singly-branched molecule");

        List<List<BondedAtom>> trimethylbutaneBackbones = new MoleculeAnalyzer(new LinearOrganicMoleculeBuilder(4)
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlkyl(1)).build()).getBackbones();
        Assert.assertEquals(trimethylbutaneBackbones.size(), 20, "getBackbones failed on a highly branched molecule");
    }

    /** getLinearBackbone finds the carbons in a linear molecule's backbone (longest chain, under most circumstances) */
    @Test(timeOut = HELPER_TEST_TIMEOUT)
    public void helpGetLinearBackbone() {
        MoleculeAnalyzer methane = new MoleculeAnalyzer(new LinearOrganicMoleculeBuilder(1).build());
        List<BondedAtom> methaneBackbone = methane.getLinearBackbone();
        Assert.assertEquals(methaneBackbone.size(), 1, "getLinearBackbone didn't find the backbone of a one-carbon molecule");
        Assert.assertTrue(methaneBackbone.get(0).isCarbon(), "getLinearBackbone selected a non-carbon atom as the backbone for a one-carbon molecule");

        MoleculeAnalyzer ethane = new MoleculeAnalyzer(new LinearOrganicMoleculeBuilder(2).build());
        Assert.assertEquals(ethane.getLinearBackbone().size(), 2, "getLinearBackbone didn't find the backbone of a two-carbon molecule");

        MoleculeAnalyzer ethanol = new MoleculeAnalyzer(new LinearOrganicMoleculeBuilder(2)
                .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createAlcohol()).build());
        Assert.assertEquals(ethanol.getLinearBackbone().size(), 2, "getLinearBackbone misidentified the backbone of an unbranched substituted molecule");

        MoleculeAnalyzer bromopropane = new MoleculeAnalyzer(new LinearOrganicMoleculeBuilder(3)
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE)).build());
        List<BondedAtom> bromopropaneBackbone = bromopropane.getLinearBackbone();
        Assert.assertEquals(bromopropaneBackbone.size(), 3, "getLinearBackbone misidentified the backbone of an unbranched substituted three-carbon molecule");
        for (BondedAtom a : bromopropaneBackbone) {
            Assert.assertEquals(a.getElement().getSymbol(), ChemicalElement.CARBON.getSymbol(), "getLinearBackbone included a non-carbon atom in the backbone");
        }

        MoleculeAnalyzer methylbutane = new MoleculeAnalyzer(new LinearOrganicMoleculeBuilder(3)
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(2)).build());
        Assert.assertEquals(methylbutane.getLinearBackbone().size(), 4, "getLinearBackbone misidentified the backbone of a branched molecule with 3 tips");

        MoleculeAnalyzer neopentane = new MoleculeAnalyzer(new LinearOrganicMoleculeBuilder(3)
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1)).build());
        Assert.assertEquals(neopentane.getLinearBackbone().size(), 3, "getLinearBackbone misidentified the backbone of a symmetrical branched molecule with 4 tips");

        MoleculeAnalyzer manyTips = new MoleculeAnalyzer(new LinearOrganicMoleculeBuilder(6)
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createAlkyl(1))
                .addSubstituent(3, OrganicMoleculeBuilder.Substituent.createAlkyl(2))
                .addSubstituent(4, OrganicMoleculeBuilder.Substituent.createAlkyl(5))
                .addSubstituent(5, OrganicMoleculeBuilder.Substituent.createAlkyl(3)).build());
        Assert.assertEquals(manyTips.getLinearBackbone().size(), 10, "getLinearBackbone misidentified the backbone of a molecule with 6 tips");
    }

    /** rotateRing rotates and/or flips the ring such that a substituent, if present, is at the first position */
    @Test(timeOut = HELPER_TEST_TIMEOUT, dependsOnMethods = "helpGetRing")
    public void helpRotateRing() {
        MoleculeAnalyzer cyclopropane = new MoleculeAnalyzer(new CyclicOrganicMoleculeBuilder(3).build());
        List<BondedAtom> cyclopropaneRing = cyclopropane.getRing();
        Assert.assertEqualsNoOrder(cyclopropane.rotateRing(cyclopropaneRing).toArray(), cyclopropaneRing.toArray(), "rotateRing didn't preserve the atoms in the ring");

        MoleculeAnalyzer bromocyclopropane = new MoleculeAnalyzer(new CyclicOrganicMoleculeBuilder(3)
                .addSubstituent(0, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.BROMINE)).build());
        List<BondedAtom> bromocyclopropaneRing = bromocyclopropane.getRing();
        List<BondedAtom> bromocyclopropaneRotated = bromocyclopropane.rotateRing(bromocyclopropaneRing);
        Assert.assertEqualsNoOrder(bromocyclopropaneRotated.toArray(), bromocyclopropaneRing.toArray(), "rotateRing didn't preserve the atoms in a substituted ring");
        Assert.assertSame(Objects.requireNonNull(bromocyclopropaneRotated.get(0).getConnectedAtom(0)).getElement(), ChemicalElement.BROMINE, "rotateRing unnecessarily rotated an already-correct substituted ring");

        MoleculeAnalyzer fluorocyclopropane = new MoleculeAnalyzer(new CyclicOrganicMoleculeBuilder(3)
                .addSubstituent(1, OrganicMoleculeBuilder.Substituent.createHalogen(ChemicalElement.FLUORINE)).build());
        List<BondedAtom> fluorocyclopropaneRing = fluorocyclopropane.getRing();
        List<BondedAtom> fluorocyclopropaneRotated = fluorocyclopropane.rotateRing(fluorocyclopropaneRing);
        Assert.assertEqualsNoOrder(fluorocyclopropaneRing.toArray(), fluorocyclopropaneRing.toArray(), "rotateRing didn't preserve the atoms in a substituted ring with necessary rotation");
        Assert.assertSame(Objects.requireNonNull(fluorocyclopropaneRotated.get(0).getConnectedAtom(2)).getElement(), ChemicalElement.FLUORINE, "rotateRing didn't rotate a substituted ring correctly");

        BondedAtom methylcylobutaneMol = new CyclicOrganicMoleculeBuilder(4)
                .addSubstituent(2, OrganicMoleculeBuilder.Substituent.createAlkyl(1)).build();
        MoleculeAnalyzer methylcyclobutane = new MoleculeAnalyzer(methylcylobutaneMol);
        List<BondedAtom> methylcyclobutaneRing = methylcyclobutane.getRing();
        List<BondedAtom> methylcyclobutaneRotated = methylcyclobutane.rotateRing(methylcyclobutaneRing);
        Assert.assertFalse(methylcyclobutaneRotated.contains(Objects.requireNonNull(Objects.requireNonNull(methylcylobutaneMol.getConnectedAtom(1)).getConnectedAtom(1)).getConnectedAtom(2)), "rotateRing included a methyl substituent in the ring");
        Assert.assertEqualsNoOrder(methylcyclobutaneRotated.toArray(), methylcyclobutaneRing.toArray(), "rotateRing didn't preserve the atoms in a methylated ring");
        Assert.assertSame(methylcyclobutaneRotated.get(0), Objects.requireNonNull(methylcylobutaneMol.getConnectedAtom(1)).getConnectedAtom(1), "rotateRing didn't rotate a methylated ring correctly");
    }
}
