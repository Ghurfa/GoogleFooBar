import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

public class Foobar7 {
    private static class OddsList implements Iterable<Integer> {
        private List<Integer> sortedByCountDesc;
        private Map<Integer, Integer> freqMap;
        private int size;

        public OddsList(Map<Integer, Integer> frequencyList) {
            freqMap = frequencyList;
            size = 0;
            sortedByCountDesc = new ArrayList<>();
            for (int num : frequencyList.keySet()) {
                sortedByCountDesc.add(num);
                size += freqMap.get(num);
            }
            Collections.sort(sortedByCountDesc, (x, y) -> freqMap.get(y) - freqMap.get(x));
        }

        public void useValue(int val) {
            int newFreq = freqMap.get(val) - 1;
            freqMap.put(val, newFreq);

            if (newFreq == 0) {
                sortedByCountDesc.remove(val);
            } else {
                Collections.sort(sortedByCountDesc, (x, y) -> freqMap.get(y) - freqMap.get(x));
            }
            size--;
        }

        public int size() {
            return size;
        }

        // The dominator is the value which comprises more than half the list
        // Returns null if there isn't one
        public Integer getDominator() {
            Integer val = sortedByCountDesc.get(0);
            if (freqMap.get(val) * 2 > size) {
                return val;
            } else {
                return null;
            }
        }

        public int getDominationAmount() {
            return 2 * freqMap.get(sortedByCountDesc.get(0)) - size;
        }

        @Override
        public Iterator<Integer> iterator() {
            return sortedByCountDesc.iterator();
        }
    }

    private static void sortMod2(List<Integer> list, List<Integer> evens, List<Integer> odds) {
        for (int num : list) {
            if (num % 2 == 0) {
                evens.add(num);
            } else {
                odds.add(num);
            }
        }
    }

    private static void addToFreqMap(Map<Integer, Integer> map, Integer val) {
        Integer oldCount = map.get(val);

        if (oldCount == null) {
            map.put(val, 1);
        } else {
            map.put(val, oldCount + 1);
        }
    }

    private static void sortOddsMod4(List<Integer> odds, Map<Integer, Integer> con1, Map<Integer, Integer> con3) {
        for (int num : odds) {
            if (num % 4 == 1) {
                addToFreqMap(con1, num);
            } else {
                addToFreqMap(con3, num);
            }
        }
    }

    private static boolean doesGameEnd(int startA, int startB) {
        int currA = startA;
        int currB = startB;
        while (currA != currB) {
            if (currA % 2 != currB % 2) {   // Odd & even -> infinite
                return false;
            } else if (currA % 2 == 0) {    // Both even -> reduce
                currA /= 2;
                currB /= 2;
            } else {                        // Both odd -> play a round & reduce
                if (currA < currB) {
                    currB = (currB - currA) / 2;
                } else {
                    currA -= (currA - currB) / 2;
                }
            }
        }
        return true;
    }

    private static boolean findPairing(Iterable<Integer> listA, Iterable<Integer> listB) {
        for (int a : listA) {
            for (int b : listB) {
                if (!doesGameEnd(a, b)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int solveOdds(List<Integer> odds) {
        Map<Integer, Integer> con1Freq = new HashMap<>(); // Frequency map of values that are congruent to 1 (mod 4)
        Map<Integer, Integer> con3Freq = new HashMap<>();
        sortOddsMod4(odds, con1Freq, con3Freq);
        OddsList con1 = new OddsList(con1Freq); // Nums that are congruent (mod 4) can be paired w/ each other
        OddsList con3 = new OddsList(con3Freq);

        while (con1.size() > 0 && con3.size() > 0) {
            /*
             * Dominator = value that comprises more than half its list. It cannot be paired
             * within its list and should be paired w/ the other list
             */
            Integer con1Dom = con1.getDominator();
            Integer con3Dom = con3.getDominator();
            if (con1Dom == null && con3Dom == null) {
                // No dominators
                if ((con1.size() % 2) == 0 && (con3.size % 2) == 0) {
                    // Both lists can be paired within themselves perfectly
                    return 0;
                } else if ((con1.size() % 2) != (con3.size() % 2)) {
                    // Both lists can be paired within themselves with one extra from one list
                    return 1;
                } else {
                    // One extra from each list, might be able to be paired together
                    return findPairing(con1, con3) ? 0 : 2;
                }
            } else if (con1Dom != null && con3Dom != null) {
                // Try to pair the dominators of the two lists
                int con1DomAmt = con1.getDominationAmount();
                int con3DomAmt = con3.getDominationAmount();
                if (doesGameEnd(con1Dom, con3Dom)) {
                    return con3DomAmt > con1DomAmt ? con3DomAmt - con1DomAmt : con1DomAmt - con3DomAmt;
                } else {
                    return con1DomAmt + con3DomAmt;
                }
            } else {
                // Pair the dominator of one list with any value in the other
                OddsList hasDom;
                OddsList hasntDom;
                int dom;
                if (con1Dom == null) {
                    hasDom = con3;
                    hasntDom = con1;
                    dom = con3Dom;
                } else {
                    hasDom = con1;
                    hasntDom = con3;
                    dom = con1Dom;
                }

                boolean foundPair = false;
                for (Integer val : hasntDom) {
                    if (!doesGameEnd(dom, val)) {
                        hasDom.useValue(dom);
                        hasntDom.useValue(val);
                        foundPair = true;
                        break;
                    }
                }
                if (!foundPair) {
                    // Dominator cannot be paired w/ any in the other list
                    return hasDom.getDominationAmount() + (hasntDom.size() % 2 == 0 ? 0 : 1);
                }
            }
        }

        OddsList nonEmpty;
        if (con1.size() == 0) {
            if (con3.size() == 0) {
                return 0;
            }
            nonEmpty = con3;
        } else {
            nonEmpty = con1;
        }

        // Make pairs within the non-empty list
        Integer dominator = nonEmpty.getDominator();
        if (dominator == null) {
            return nonEmpty.size() % 2 == 0 ? 0 : 1;
        } else {
            return nonEmpty.getDominationAmount();
        }
    }

    private static int solveEvensOdds(List<Integer> list) {
        List<Integer> evens = new LinkedList<>();
        List<Integer> odds = new LinkedList<>();
        sortMod2(list, evens, odds);

        int diff = evens.size() - odds.size();

        if (diff == 0) { // Evens can be paired w/ odds perfectly
            return 0;
        } else if (diff == 1 || diff == -1) { // Evens can be paired w/ odds w/ one extra
            return 1;
        } else if (diff > 1) { // Extra evens; try to pair evens w/ other evens
            List<Integer> halved = new LinkedList<>();
            for (int num : evens) {
                halved.add(num / 2);
            }
            int evensRemaining = solveEvensOdds(halved);
            int evensPaired = evens.size() - evensRemaining;
            if (evensPaired > diff) {
                return diff % 2 == 0 ? 0 : 1;
            } else {
                return diff - evensPaired;
            }
        } else { // Extra odds; try to pair odds w/ other odds
            int oddsRemaining = solveOdds(odds);
            int oddsPaired = odds.size() - oddsRemaining;
            if (oddsPaired > -diff) {
                return diff % 2 == 0 ? 0 : 1;
            } else {
                return -diff - oddsPaired;
            }
        }
    }

    public static int solution(int[] banana_list) {
        List<Integer> list = new LinkedList<Integer>();
        for (int num : banana_list) {
            list.add(num);
        }
        return solveEvensOdds(list);
    }

    public static void main(String[] args) {
        int[] bananaList = { 1, 1 };
        // int[] bananaList = { 1, 7, 3, 13, 19 };
        int solution = solution(bananaList);
        System.out.println(solution);
        if (solution != Foobar7Old.solution(bananaList)) {
            throw new RuntimeException();
        }
    }
}
