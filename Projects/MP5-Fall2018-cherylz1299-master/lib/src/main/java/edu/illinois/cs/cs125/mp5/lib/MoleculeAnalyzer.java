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

    /**
     * All the atoms.
     */
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
        this.allAtoms = findAllAtoms(molecule, new ArrayList<>());
    }

    /**
     * Recursively adds connected atoms to the allAtoms list.
     *
     * @param current      The atom we're currently examining
     * @param visitedAtoms List of all atoms we've found so far
     * @return All atoms found in the molecule
     */
    public ArrayList<BondedAtom> findAllAtoms(final BondedAtom current, final ArrayList<BondedAtom> visitedAtoms) {
        visitedAtoms.add(current);
        for (BondedAtom neighbor : current) {
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
     * Store isRing.
     */
    private boolean isR = false;

    /**
     * Returns whether the molecule is a ring.
     *
     * @return True if the molecule is a ring and false otherwise
     */
    public boolean isRing() {
        if (this.allAtoms.size() == 0) {
            return false;
        }
        this.isR = false;
        BondedAtom firstAtom = this.allAtoms.get(0);
        ArrayList<BondedAtom> visitedAtoms = new ArrayList<>();
        for (BondedAtom neighbor : firstAtom) {
            visitedAtoms.add(firstAtom);
            searchRing(neighbor, firstAtom, visitedAtoms);
        }
        return this.isR;
    }

    /**
     * Returns whether the molecule is a ring.
     *
     * @param currentAtom  The current searching atom.
     * @param parentAtom   The parent atom of the current atom.
     * @param visitedAtoms The list of atom which has been visited.
     */
    private void searchRing(final BondedAtom currentAtom, final BondedAtom parentAtom,
                            final ArrayList<BondedAtom> visitedAtoms) {
        visitedAtoms.add(currentAtom);
        for (BondedAtom neighbor : currentAtom) {
            if (!visitedAtoms.contains(neighbor)) {
                searchRing(neighbor, currentAtom, visitedAtoms);
            } else if (neighbor != parentAtom) {
                this.isR = true;
            }
        }
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
     * Store the paths from start to end.
     */
    private List<BondedAtom> pathList = new ArrayList<>();

    /**
     * Find a path between two atoms in the molecule.
     *
     * @param start The atom to start from
     * @param end   The atom to end at
     * @return The path from the start atom to the end atom
     */
    public List<BondedAtom> findPath(final BondedAtom start, final BondedAtom end) {
        pathList = new ArrayList<>();
        if (start == end) {
            pathList.add(start);
            return pathList;
        } else {
            return findPath(start, end, new ArrayList<>());
        }
    }

    /**
     * Recursively find a path between two atoms in the molecule.
     *
     * @param current The current atom we are examining
     * @param end     The atom to end at
     * @param path    The atoms we've already visited on our way to the current atom
     * @return The path from the current atom to the end atom
     */
    public List<BondedAtom> findPath(final BondedAtom current, final BondedAtom end, final List<BondedAtom> path) {
        path.add(current);
        for (BondedAtom neighbor : current) {
            if (!path.contains(neighbor)) {
                parentsMap.put(neighbor, current);
                if (neighbor == end) {
                    pathList.add(end);
                    BondedAtom parentAtom = parentsMap.get(neighbor);
                    // path.get(0) is the start bondedAtom
                    while (parentAtom != path.get(0)) {
                        pathList.add(0, parentAtom);
                        parentAtom = parentsMap.get(parentAtom);
                    }
                    pathList.add(0, path.get(0));
                } else {
                    findPath(neighbor, end, path);
                }
            }
        }
        return pathList;
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
                if (i != j || tips.size() == 1) {
                    BondedAtom start = tips.get(i);
                    BondedAtom end = tips.get(j);
                    List<BondedAtom> path = findPath(start, end);
                    backbones.add(path);
                }
            }
        }
        return backbones;
    }

    /**
     * Identify the linear backbone of the molecule.
     *
     * @return The list of atoms constituting the linear backbone of this atom
     */
    public java.util.List<BondedAtom> getLinearBackbone() {
        /** Easy way.
        List<List<BondedAtom>> backbones = this.getBackbones();
        List<BondedAtom> maxSizeBackBone = backbones.get(0);
        for (List<BondedAtom> backbone : backbones) {
            if (backbone.size() > maxSizeBackBone.size()) {
                maxSizeBackBone = backbone;
            }
        }
        return sortLinearBackbone(maxSizeBackBone);**/

        // All high-priority substituents must be attached to the backbone.
        List<List<BondedAtom>> backbones = this.getBackbones();
        List<BondedAtom> maxSizeBackBone = backbones.get(0);
        for (List<BondedAtom> backbone : backbones) {
            if (backbone.size() > maxSizeBackBone.size()) {
                if (checkBackboneValid(backbone)) {
                    maxSizeBackBone = backbone;
                }
            }
        }
        return sortLinearBackbone(maxSizeBackBone);
    }

    /**
     * Check if the backbone is valid.
     * @param backbone The backbone to be checked
     * @return True if the backbone is valid
     */
    public boolean checkBackboneValid(final List<BondedAtom> backbone) {
        for (BondedAtom atom : this.allAtoms) {
            if (!backbone.contains(atom)) {
                for (BondedAtom neighbor : atom) {
                    if (neighbor.getElement() == ChemicalElement.OXYGEN) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sort the linear to the right order.
     *
     * @param linearBackbone The backbone
     * @return The Sorted backbone
     */
    private List<BondedAtom> sortLinearBackbone(final List<BondedAtom> linearBackbone) {
        System.out.println("--------start-------------------------------- ");
        int maxPriorityIndex = 0;
        String maxPriority = "";
        int size = linearBackbone.size();
        for (int i = 0; i < size; i++) {
            BondedAtom atom = linearBackbone.get(i);
            /* Easy way.
            if (atom.hasSubstituent(linearBackbone)) {
                maxPriorityIndex = i;
                break;
            }*/
            String priority = getSubstituentPriority(atom, linearBackbone);
            if (priority.compareTo(maxPriority) > 0) {
                maxPriorityIndex = i;
                maxPriority = priority;
            }
        }
        if (maxPriorityIndex > (size - 1) / 2) {
            List<BondedAtom> sortedBackbone = new ArrayList<>();
            for (int i = size - 1; i >= 0; i--) {
                sortedBackbone.add(linearBackbone.get(i));
            }
            return sortedBackbone;
        } else {
            return linearBackbone;
        }
    }

    /**
     * Define atom priority.
     *
     * @param atom The atom
     * @param backbone The backbone this atom attached to
     * @return The priority of this atom
     */
    private String getSubstituentPriority(final BondedAtom atom, final List<BondedAtom> backbone) {
        String priority = "";
        if (atom.hasSubstituent(backbone)) {
            for (int i = 0; i < atom.getBondInfo().size(); i++) {
                BondedAtom neighbor = atom.getConnectedAtom(i);
                if (!backbone.contains(neighbor)) {
                    int bondCount = atom.getBondInfo().get(i).getCount();
                    if (neighbor.getElement() != ChemicalElement.HYDROGEN) {
                        System.out.println("----neighbor: " + neighbor.getElement().toString());
//                    System.out.println("----neighbor index: " + i);
                        System.out.println("----bondCount: " + bondCount);
                    }
                    String value = "";
                    if (neighbor.getElement() == ChemicalElement.OXYGEN && bondCount == 2) {
                        value = "7";
                    } else if (neighbor.getElement() == ChemicalElement.OXYGEN && bondCount == 1) {
                        value = "6";
                    } else if (neighbor.getElement() == ChemicalElement.BROMINE) {
                        value = "4";
                    } else if (neighbor.getElement() == ChemicalElement.CHLORINE) {
                        value = "3";
                    } else if (neighbor.getElement() == ChemicalElement.FLUORINE) {
                        value = "2";
                    } else if (neighbor.getElement() == ChemicalElement.CARBON) {
                        value = "1";
                    }
                    priority = insert(priority, value);
                }
            }
            if (priority.equals("11")) {
                priority = "5";
            }
        }
        if (!priority.equals("")) {
            System.out.println("----priority: " + priority);
        } else {
            System.out.println("\"\"");
        }
        return priority;
    }

    /**
     * Insert the priority value and keep the priority string sorted.
     *
     * @param priority The priority string
     * @param value    The priority value to be insert
     * @return The new priority string
     */
    private static String insert(final String priority, final String value) {
        int index = priority.length() - 1;
        while (index >= 0) {
            String s = priority.substring(index, index + 1);
            if (value.compareTo(s) > 0) {
                index--;
            } else {
                break;
            }
        }
        return priority.substring(0, index + 1) + value + priority.substring(index + 1, priority.length());
    }

    /**
     * Searches the molecule for a ring.
     *
     * @return A list containing the atoms in the ring if a ring exists, null otherwise
     */
    public java.util.List<BondedAtom> getRing() {
        if (this.allAtoms.size() == 0 || !this.isRing()) {
            return null;
        } else {
            BondedAtom current = this.allAtoms.get(0);
            return this.getRing(current, new ArrayList<>());
        }
    }

    /**
     * Store the parent atom of i atom.
     */
    private Map<BondedAtom, BondedAtom> parentsMap = new HashMap<>();

    /**
     * Store the atoms in ring when execute searchRingAtoms function.
     */
    private List<BondedAtom> ringAtoms = new ArrayList<>();

    /**
     * Search the molecule for a ring from a specific starting point.
     *
     * @param current The current atom we are examining.
     * @param visited A list of previously-visited atom. The previous atom is the last in the list.
     * @return A list containing the atoms in the ring if a ring exists, null otherwise
     */
    public java.util.List<BondedAtom> getRing(final BondedAtom current, final java.util.List<BondedAtom> visited) {
        visited.add(current);
        for (BondedAtom neighbor : current) {
            if (!visited.contains(neighbor)) {
                parentsMap.put(neighbor, current);
                getRing(neighbor, visited);
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
     * Rotate a backbone ring into the correct position for naming.
     *
     * @param ring The backbone ring to rotate
     * @return The backbone ring rotated into the correct position
     */
    public java.util.List<BondedAtom> rotateRing(final java.util.List<BondedAtom> ring) {
        /** Easy way.
        int priorityIndex = 0;
        for (int i = 0; i < ring.size(); i++) {
            BondedAtom atom = ring.get(i);
            if (atom.hasSubstituent(ring)) {
                priorityIndex = i;
                break;
            }
        }
        List<BondedAtom> sortedRing = new ArrayList<>();
        int i = 0;
        while (i < ring.size()) {
            int index = (i + priorityIndex) % ring.size();
            sortedRing.add(ring.get(index));
            i++;
        }
        return sortedRing;**/

        int size = ring.size();
        List<BondedAtom> sortedRing = (List<BondedAtom>) ((ArrayList<BondedAtom>) ring).clone();
        List<BondedAtom> reverseSortedRing = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            reverseSortedRing.add(sortedRing.get(size - i - 1));
        }
        List<String> priorities = getRingPriorities(sortedRing);
        List<String> reversePriorities = getRingPriorities(reverseSortedRing);
        List<String> maxPriorities = priorities;
        List<BondedAtom> maxRing = sortedRing;
        if (comparePriorities(reversePriorities, maxPriorities) > 0) {
            maxPriorities = reversePriorities;
            maxRing = reverseSortedRing;
        }
        int count = 1;
        while (count < size) {
            sortedRing = rotateRingOnce(sortedRing);
            reverseSortedRing = rotateRingOnce(reverseSortedRing);
            priorities = getRingPriorities(sortedRing);
            reversePriorities = getRingPriorities(reverseSortedRing);
            if (comparePriorities(priorities, maxPriorities) > 0) {
                maxPriorities = priorities;
                maxRing = sortedRing;
            }
            if (comparePriorities(reversePriorities, maxPriorities) > 0) {
                maxPriorities = reversePriorities;
                maxRing = reverseSortedRing;
            }
            count++;
        }

        for (BondedAtom atom: maxRing) {
            System.out.println("********" + getSubstituentPriority(atom, maxRing));
        }

        return maxRing;
    }

    /**
     * Get the substituent priority list of ring.
     *
     * @param ring The ring
     * @return the substituent priority list
     */
    private List<String> getRingPriorities(final List<BondedAtom> ring) {
        List<String> priorities = new ArrayList<>();
        for (int i = 0; i < ring.size(); i++) {
            String priority = getSubstituentPriority(ring.get(i), ring);
            priorities.add(priority);
        }
        return priorities;
    }

    /**
     * Compare the two priority list.
     * @param priorities1 First priority
     * @param priorities2 Second priority
     * @return 1: first > second, -1: first < second, 0: first == second
     */
    private int comparePriorities(final List<String> priorities1, final List<String> priorities2) {
        int i = 0;
        while (i < priorities1.size() && i < priorities2.size()) {
            if (priorities1.get(i).compareTo(priorities2.get(i)) > 0) {
                return 1;
            } else if (priorities1.get(i).compareTo(priorities2.get(i)) < 0) {
                return -1;
            } else {
                i++;
            }
        }
        return 0;
    }

    /**
     * Check if list is ordered.
     *
     * @param list The list to be checked
     * @return -1: not ordered, 0: desc ordered, 1: asc ordered
     */
    private int checkOrdered(final List<String> list) {
        int start = 0;
        int end = list.size() - 1;
        while (start < list.size() && list.get(start).isEmpty()) {
            start++;
        }
        while (end >= 0 && list.get(end).isEmpty()) {
            end--;
        }
        if (end > start) {
            for (int i = start; i < end; i++) {
                if (list.get(i).isEmpty()) {
                    list.remove(i);
                    end--;
                }
            }
        }
        boolean desc = true;
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).compareTo(list.get(i + 1)) < 0) {
                desc = false;
                break;
            }
        }
        boolean asc = true;
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).compareTo(list.get(i + 1)) > 0) {
                asc = false;
                break;
            }
        }
        if (desc) {
            return 0;
        } else if (asc) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Rotate ring by 1 index.
     * @param ring The ring
     * @return The rotated ring
     */
    private List<BondedAtom> rotateRingOnce(final List<BondedAtom> ring) {
        List<BondedAtom> rotatedRing = new ArrayList<>();
        int i = 0;
        while (i < ring.size()) {
            int index = (i + 1) % ring.size();
            rotatedRing.add(ring.get(index));
            i++;
        }
        return rotatedRing;
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
        ChemicalElement[] elements = {
            ChemicalElement.CARBON, ChemicalElement.HYDROGEN, ChemicalElement.BROMINE,
            ChemicalElement.CHLORINE, ChemicalElement.FLUORINE, ChemicalElement.OXYGEN
        };
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
