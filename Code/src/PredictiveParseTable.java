/*
        ===========================================================
        CPCS-302 Project Phase 02 (Non-recursive Predictive Parser)
        Mahmued Alardawi    2135209    CS1
        ===========================================================

Question I

CFG: -
    E → E + T | T
    T → T * F | F
    F → ( E ) | id

Non-left recursive grammar: -
    E  → TE'
    E' → +TE' | ϵ
    T  → FT'
    T' → *FT' | ϵ
    F  → (E) | id



Question II

Non-left recursive grammar: -
    E  → TE'
    E' → +TE' | ϵ
    T  → FT'
    T' → *FT' | ϵ
    F  → (E) | id

FIRST() : -
    FIRST(E) = FIRST(T) = FIRST(F) = { (, id }
    FIRST(E') = { +, ϵ }
    FIRST(T') = { *, ϵ }

FOLLOW()
    FOLLOW(E) = { ), $ }
    FOLLOW(E') = { ), $ }
    FOLLOW(T) = { +, ), $ }
    FOLLOW(T') = { +, ), $ }
    FOLLOW(F) = { *, +, ), $ }
*/

import java.util.*;

public class PredictiveParseTable {
    HashSet<String> terminals = new HashSet<>();
    HashSet<String> nonTerminals = new HashSet<>();
    HashMap<String, String[]> productions = new HashMap<>();
    HashMap<String, Set<String>> firstSets = new HashMap<>();
    HashMap<String, Set<String>> followSets = new HashMap<>();
    HashMap<String, HashMap<String, String[]>> parseTable = new HashMap<>();

    void constructTable() {
        for (Map.Entry<String, Set<String>> first : firstSets.entrySet()) {
            HashMap<String, String[]> temp = new HashMap<>();
            for (String terminal : first.getValue()) {
                for (Map.Entry<String, String[]> production : productions.entrySet()) {
                    if (Objects.equals(first.getKey(), production.getKey())) {
                        if (Objects.equals(terminal,  "ϵ")) {
                            for (Map.Entry<String, Set<String>> follow : followSets.entrySet()) {
                                for (String terminal1 : follow.getValue()) {
                                    if (Objects.equals(follow.getKey(), production.getKey())) {
                                        temp.put(terminal1, new String[]{"ϵ"});
                                        parseTable.put(first.getKey(), temp);
                                    }
                                }
                            }
                        }
                        else if (Objects.equals(production.getValue()[production.getValue().length - 1],  "ϵ")) {
                            String [] tempArray =  new String[production.getValue().length - 1];
                            System.arraycopy(production.getValue(), 0, tempArray, 0, production.getValue().length - 1);
                            temp.put(terminal, tempArray);
                            parseTable.put(first.getKey(), temp);
                        }
                        else if (Objects.equals(first.getKey(),  "F")) {
                            List<String> tempList1 = new ArrayList<>();
                            List<String> tempList2 = new ArrayList<>();
                            for (String p : production.getValue()) {
                                if (Objects.equals(p, "id")) {
                                    tempList1.add(p);
                                }
                                else {
                                    tempList2.add(p);
                                }
                            }

                            String [] tempArray1 =  new String[tempList1.size()];
                            String [] tempArray2 =  new String[tempList2.size()];

                            for (int i = 0; i < tempArray1.length; i++) {
                                tempArray1[i] = tempList1.get(i);
                            }

                            for (int i = 0; i < tempArray2.length; i++) {
                                tempArray2[i] = tempList2.get(i);
                            }

                            if (Objects.equals(terminal, "id")) {
                                temp.put(terminal, tempArray1);
                                parseTable.put(first.getKey(), temp);
                            }
                            else {
                                temp.put(terminal, tempArray2);
                                parseTable.put(first.getKey(), temp);
                            }

                        }
                        else {
                            temp.put(terminal, production.getValue());
                            parseTable.put(first.getKey(), temp);
                        }
                    }
                }
            }
        }
    }

    void data() {
        System.out.println("Parse Table: -");
        System.out.println("--------------------");
        for (Map.Entry<String, HashMap<String, String[]>> entry : parseTable.entrySet()) {
            System.out.println(entry.getKey() + "= {");
            for (Map.Entry<String, String[]> entry1 : entry.getValue().entrySet()) {
                System.out.println(entry1.getKey() + "=" + Arrays.toString(entry1.getValue()));
            }
            System.out.println("}");
        }
        System.out.println("--------------------");
    }
}