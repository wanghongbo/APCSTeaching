package edu.illinois.cs.cs125.mp5.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Analyzes a given organic molecule.
 */
public class MoleculeAnalyzer {

    private List<BondedAtom> allAtoms;

    /**
     * Return the list of all atoms in this molecule.
     * <p>
     * This is a convenience method used by the test suite.
     *
     * @return a list of all atoms in this molecule.
     */
    public List<BondedAtom> getAllAtoms() {
        return allAtoms;
    }

    /**
     * Creates an MoleculeAnalyzer for analyzing a given molecule.
     *
     * @param molecule an atom belonging to the molecule that will be analyzed.
     */
    public MoleculeAnalyzer(final BondedAtom molecule) {
        this.allAtoms = findAllAtoms(molecule, new ArrayList<BondedAtom>());
    }

    /**
     * Recursively adds connected atoms to the allAtoms list.
     *
     * @param current      The atom we're currently examining
     * @param visitedAtoms List of all atoms we've found so far
     * @return All atoms found in the molecule
     */
    public ArrayList<BondedAtom> findAllAtoms(BondedAtom current, ArrayList<BondedAtom> visitedAtoms) {
        visitedAtoms.add(current);
        for (BondedAtom neighbor: current) {
            if (!visitedAtoms.contains(neighbor)) {
                findAllAtoms(neighbor, visitedAtoms);
            }
        }
        return visitedAtoms;
    }

    /**
     * Get the weight of this molecular.
     *
     * @return The molecular weight of the molecule in grams per mole
     */
    public double getMolecularWeight() {
        double weight = 0.0;
        for (BondedAtom atom : this.allAtoms) {
            weight += atom.getElement().getWeight();
        }
        return weight;
    }

    /**
     * Determines whether this molecule contains any charged atoms.
     *
     * @return True if there is at least one charged atom in the molecule, false otherwise
     */
    public boolean hasChargedAtoms() {
        for (BondedAtom atom : this.allAtoms) {
            int valence = atom.getElement().getValence();
            int bondsCount = 0;
            for (BondedAtom.BondInfo bondInfo : atom.getBondInfo()) {
                bondsCount += bondInfo.getCount();
            }
            if (valence != bondsCount) {
                return true;
            }
        }
        return false;
    }

    /**
     * Store isRing
     */
    private boolean isRing = false;

    /**
     * Returns whether the molecule is a ring.
     *
     * @return True if the molecule is a ring and false otherwise
     */
    public boolean isRing() {
        if (this.allAtoms.size() == 0) {
            return false;
        }
        this.isRing = false;
        BondedAtom firstAtom = this.allAtoms.get(0);
        ArrayList<BondedAtom> visitedAtoms = new ArrayList<>();
        for (BondedAtom neighbor : firstAtom) {
            visitedAtoms.add(firstAtom);
            searchRing(neighbor, firstAtom, visitedAtoms);
        }
        return this.isRing;
    }

    /**
     * Returns whether the molecule is a ring.
     *
     * @param currentAtom  The current searching atom.
     * @param parentAtom   The parent atom of the current atom.
     * @param visitedAtoms The list of atom which has been visited.
     * @return True if the molecule is a ring and false otherwise
     */
    private void searchRing(BondedAtom currentAtom, BondedAtom parentAtom, ArrayList<BondedAtom> visitedAtoms) {
        visitedAtoms.add(currentAtom);
        for (BondedAtom neighbor : currentAtom) {
            if (!visitedAtoms.contains(neighbor)) {
                searchRing(neighbor, currentAtom, visitedAtoms);
            } else if (neighbor != parentAtom) {
                this.isRing = true;
            }
        }
    }

    /**
     * Searches the molecule for a ring.
     *
     * @return A list containing the atoms in the ring if a ring exists, null otherwise
     */
    public java.util.List<BondedAtom> getRing() {
        if (this.allAtoms.size() < 0 || !this.isRing()) {
            return null;
        } else {
            BondedAtom current = this.allAtoms.get(0);
            return this.getRing​(current, new ArrayList<>());
        }
    }

    /**
     * Store the parent atom of i atom;
     */
    private Map<BondedAtom, BondedAtom> parentsMap = new HashMap<>();

    /**
     * Store the atoms in ring when execute searchRingAtoms function;
     */
    private List<BondedAtom> ringAtoms = new ArrayList<>();

    /**
     * Search the molecule for a ring from a specific starting point.
     *
     * @param current The current atom we are examining.
     * @param visited A list of previously-visited atom. The previous atom is the last in the list.
     * @return A list containing the atoms in the ring if a ring exists, null otherwise
     */
    public java.util.List<BondedAtom> getRing​(BondedAtom current, java.util.List<BondedAtom> visited) {
        visited.add(current);
        for (BondedAtom neighbor: current) {
            if (!visited.contains(neighbor)) {
                parentsMap.put(neighbor, current);
                getRing​(neighbor, visited);
            } else if (parentsMap.get(current) != null && parentsMap.get(current) != neighbor) {
                ringAtoms = new ArrayList<>();
                ringAtoms.add(current);
                BondedAtom parentAtom = parentsMap.get(current);
                while (parentAtom != neighbor) {
                    ringAtoms.add(parentAtom);
                    parentAtom = parentsMap.get(parentAtom);
                }
                ringAtoms.add(neighbor);
            }
        }
        return ringAtoms;
    }

    /**
     * Identify the linear backbone of the molecule.
     *
     * @return The list of atoms constituting the linear backbone of this atom
     */
    public java.util.List<BondedAtom> getLinearBackbone() {
        List<List<BondedAtom>> backbones = this.getBackbones();
        List<BondedAtom> maxSizeBackBone = backbones.get(0);
        for (List<BondedAtom> backbone : backbones) {
            if (backbone.size() > maxSizeBackBone.size()) {
                maxSizeBackBone = backbone;
            }
        }
        return maxSizeBackBone;
    }

    /**
     * Find all atoms that are molecule tips: carbons that are bonded to at most one other carbon.
     *
     * @return A list of all BondedAtoms that are tips of this molecule, which may be empty if it is a simple ring.
     */
    public java.util.List<BondedAtom> getTips() {
        List<BondedAtom> tips = new ArrayList<>();
        for (BondedAtom atom : this.allAtoms) {
            int carbonCount = 0;
            for (BondedAtom neighbor : atom) {
                if (neighbor.isCarbon()) {
                    carbonCount++;
                }
            }
            if (atom.isCarbon() && (carbonCount == 1 || carbonCount == 0)) {
                tips.add(atom);
            }
        }
        return tips;
    }

    /**
     * Find all possible backbones in a linear molecule.
     *
     * @return A list of all possible backbones, each itself a list of atoms
     */
    public java.util.List<java.util.List<BondedAtom>> getBackbones() {
        List<List<BondedAtom>> backbones = new ArrayList<>();
        List<BondedAtom> tips = this.getTips();
        for (int i = 0; i < tips.size(); i++) {
            for (int j = 0; j < tips.size(); j++) {
                if ((i != j) || (i == j && tips.size() == 1)) {
                    BondedAtom start = tips.get(i);
                    BondedAtom end = tips.get(j);
                    List<BondedAtom> path = findPath​(start, end);
                    backbones.add(path);
                }
            }
        }
        return backbones;
    }

    /**
     * Find a path between two atoms in the molecule.
     *
     * @param start The atom to start from
     * @param end   The atom to end at
     * @return The path from the start atom to the end atom
     */
    public List<BondedAtom> findPath​(BondedAtom start, BondedAtom end) {
        pathList = new ArrayList<>();
        if (start == end) {
            pathList.add(start);
            return pathList;
        } else {
            return findPath​(start, end, new ArrayList<>());
        }
    }

    /**
     * Store the paths from start to end.
     */
    private List<BondedAtom> pathList = new ArrayList<>();

    /**
     * Recursively find a path between two atoms in the molecule.
     *
     * @param current The current atom we are examining
     * @param end     The atom to end at
     * @param path    The atoms we've already visited on our way to the current atom
     * @return The path from the current atom to the end atom
     */
    public List<BondedAtom> findPath​(BondedAtom current, BondedAtom end, List<BondedAtom> path) {
        path.add(current);
        for (BondedAtom neighbor: current) {
            if (!path.contains(neighbor)) {
                parentsMap.put(neighbor, current);
                if (neighbor == end) {
                    pathList.add(end);
                    BondedAtom parentAtom = parentsMap.get(neighbor);
                    while (parentAtom != path.get(0)) {
                        pathList.add(0, parentAtom);
                        parentAtom = parentsMap.get(parentAtom);
                    }
                    pathList.add(0, path.get(0));
                } else {
                    findPath​(neighbor, end, path);
                }
            }
        }
        return pathList;
    }

    /**
     * Rotate a backbone ring into the correct position for naming.
     *
     * @param ring The backbone ring to rotate
     * @return The backbone ring rotated into the correct position
     */
    public java.util.List<BondedAtom> rotateRing​(java.util.List<BondedAtom> ring) {
        int priorityIndex = 0;
        for (int i = 0; i < ring.size(); i++) {
            BondedAtom atom = ring.get(i);
            for (BondedAtom neighbor: atom) {
                if (!ring.contains(neighbor)) {
                    BondedAtom priorityAtom = ring.get(priorityIndex);
                    //这是不在环上的取代基或者氢原子
                    if (priorityAtom == null) {
                        priorityAtom = atom;
                    }
                    if (getSubstituentAtomPriority(neighbor) > getSubstituentAtomPriority(priorityAtom)) {
                        priorityIndex = i;
                    }
                }
            }
        }

        List<BondedAtom> rotatedRing = new ArrayList<>();
        int i = 0;
        while (i < ring.size()) {
            rotatedRing.set(i, ring.get((i + priorityIndex) % ring.size()));
        }
        return rotatedRing;
    }

    /**
     * Define atom priority.
     * @param atom The atom
     * @return The priority of this atom
     */
    private int getSubstituentAtomPriority(BondedAtom atom) {
        if (atom.getElement() == ChemicalElement.OXYGEN && atom.getBondInfo().size() == 1) {
            return 5;
        } else if (atom.getElement() == ChemicalElement.OXYGEN && atom.getBondInfo().size() == 2) {
            return 4;
        } else if (atom.getElement() == ChemicalElement.BROMINE) {
            return 3;
        } else if (atom.getElement() == ChemicalElement.CHLORINE) {
            return 2;
        } else if (atom.getElement() == ChemicalElement.FLUORINE) {
            return 1;
        } else if (atom.getElement() == ChemicalElement.HYDROGEN) {
            return 0;
        }
        return -1;
    }

    /*
     * You should not need to modify code below this point.
     */

    /**
     * Names the molecule according to IUPAC rules for organic compounds.
     * <p>
     * See the MP page for information on naming. This function will not work until you complete
     * the functions above: getRing, getLinearBackbone, and rotateRing.
     *
     * @return The systematic IUPAC name of the molecule.
     */
    public String getIupacName() {
        boolean isRing = true;
        List<BondedAtom> backbone = getRing();

        if (backbone == null) {
            // It's a linear molecule, not a ring
            isRing = false;
            backbone = getLinearBackbone();
        } else {
            // It's a ring
            backbone = rotateRing(backbone);
        }
        // Find, name, and number substituents
        int position = 1;
        String suffixGroup = null;
        List<Integer> suffixGroupPositions = new ArrayList<>();
        Map<String, List<Integer>> substituentCounts = new HashMap<>();
        for (BondedAtom atom : backbone) {
            if (!isRing && position == 1) {
                String endGroup = atom.nameEndGroup();
                if (endGroup != null) {
                    suffixGroup = endGroup;
                    position++;
                    continue;
                }
            }
            for (BondedAtom neighbor : atom) {
                if (neighbor.isSubstituent(backbone)) {
                    String subName = atom.nameSubstituent(neighbor);
                    if (neighbor.getElement() == ChemicalElement.OXYGEN) {
                        suffixGroup = subName;
                        suffixGroupPositions.add(position);
                    } else if (substituentCounts.containsKey(subName)) {
                        substituentCounts.get(subName).add(position);
                    } else {
                        ArrayList<Integer> newList = new ArrayList<>();
                        newList.add(position);
                        substituentCounts.put(subName, newList);
                    }
                }
            }
            position++;
        }
        // We're almost done! Put all the parts together
        return assembleName(backbone.size(), isRing, substituentCounts, suffixGroup, suffixGroupPositions);
    }

    /**
     * Assembles the name of a molecule.
     *
     * @param backboneLength The number of carbon allAtoms in the backbone.
     * @param cyclicBackbone Whether the backbone is cyclic.
     * @param substituents   A map of low-priority substituent names to the positions at which they appear.
     *                       The lists must be sorted in ascending order. Cannot be null.
     * @param suffixName     The suffix of the molecule (e.g. "ol").
     * @param suffixGroupPos The positions at which the suffix-affecting substituent type appears. Should be null if
     *                       there are no high-priority substituents or if the suffix is from an end group (aldehyde
     *                       or carboxylic acid).
     * @return The IUPAC name of the molecule.
     */
    private static String assembleName(final int backboneLength, final boolean cyclicBackbone,
                                       final Map<String, List<Integer>> substituents,
                                       final String suffixName, final List<Integer> suffixGroupPos) {
        String name = NamingConstants.CHAIN_BASE_NAMES[backboneLength - 1];
        if (cyclicBackbone) {
            name = "cyclo" + name;
        }
        if (suffixName == null) {
            // No high-priority substituents (alkane, maybe with halides)
            name += "ane";
        } else if (suffixName.equals("al") || suffixName.equals("oic acid")) {
            // End groups - aldehydes and carboxylic acids
            name += "an" + suffixName;
        } else {
            // Other high-priority substituents: ketones and alcohols
            String suffix = suffixName;
            if (suffixGroupPos.size() > 1) {
                String suffixMultiplicity = NamingConstants.MULTIPLICITY_NAMES[suffixGroupPos.size() - 1];
                if (suffixMultiplicity.endsWith("a") && suffixName.startsWith("o")) {
                    // It's "tetrol", not "tetraol"
                    suffixMultiplicity = suffixMultiplicity.substring(0,
                            suffixMultiplicity.length() - 1);
                }
                suffix = suffixMultiplicity + suffix;
            }
            if (NamingConstants.VOWELS.contains(suffix.substring(0, 1))) {
                name += "an-";
            } else {
                name += "ane-";
            }
            name += locantString(suffixGroupPos) + "-" + suffix;
        }
        String[] substituentNames = substituents.keySet().toArray(new String[0]);
        Arrays.sort(substituentNames); // Name substituents alphabetically
        List<String> substituentNameFragments = new ArrayList<>();
        for (String s : substituentNames) {
            substituentNameFragments.add(locantString(substituents.get(s)) + "-"
                    + NamingConstants.MULTIPLICITY_NAMES[substituents.get(s).size() - 1] + s);
        }
        if (substituentNameFragments.size() > 0) {
            StringBuilder substituentsPart = new StringBuilder();
            for (String s : substituentNameFragments) {
                substituentsPart.append("-").append(s);
            }
            return substituentsPart.substring(1) + name;
        } else {
            return name;
        }
    }

    /**
     * Combines a set of locants into a comma-separated string.
     *
     * @param locants The sorted list of locants.
     * @return The locants, comma-separated.
     */
    private static String locantString(final List<Integer> locants) {
        StringBuilder indicesText = new StringBuilder();
        for (Integer i : locants) {
            indicesText.append(",").append(i.toString());
        }
        return indicesText.substring(1);
    }

    /**
     * Gets a chemical formula for this molecule.
     * This function is optional and not tested by the test suite; it is only used by the app.
     * You may use any formula format that you like.
     *
     * @return A chemical formula indicating the allAtoms the molecule contains.
     */
    public String getFormula() {
        ChemicalElement[] elements =
                {ChemicalElement.CARBON, ChemicalElement.HYDROGEN,
                        ChemicalElement.BROMINE, ChemicalElement.CHLORINE,
                        ChemicalElement.FLUORINE, ChemicalElement.OXYGEN};
        StringBuilder formula = new StringBuilder();
        for (ChemicalElement e : elements) {
            int count = 0;
            for (BondedAtom a : allAtoms) {
                if (a.getElement() == e) {
                    count++;
                }
            }
            if (count > 1) {
                formula.append(e.getSymbol()).append(String.valueOf(count));
            } else if (count > 0) {
                formula.append(e.getSymbol());
            }
        }
        return formula.toString();
    }
}
