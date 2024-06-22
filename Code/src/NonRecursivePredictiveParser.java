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

import java.io.*;
import java.util.*;

public class NonRecursivePredictiveParser {
    public static void main (String[] args) throws IOException {
        PredictiveParseTable parseTable = new PredictiveParseTable();

        initializingData(parseTable);
        data(parseTable);

        System.out.println();

        parseTable.constructTable();
        parseTable.data();

        // ( id + id ) * id $
        BufferedReader input = new BufferedReader(new FileReader("input.txt"));
        PrintWriter output = new PrintWriter("output.txt");
        parser(parseTable, input, output);

        // ) id * + id $
        BufferedReader input1 = new BufferedReader(new FileReader("input1.txt"));
        PrintWriter output1 = new PrintWriter("output1.txt");
        parser(parseTable, input1, output1);

        System.out.println();
        System.out.println("Program Terminated!");
    }

    static void initializingData (PredictiveParseTable parseTable) {
        // Initialize the terminals
        parseTable.terminals = new HashSet<>(Arrays.asList("id", "+", "*", "(", ")", "$"));

        // Initialize the nonTerminals
        parseTable.nonTerminals = new HashSet<>(Arrays.asList("E", "E'", "T", "T'", "F"));

        // Initialize the productions
        parseTable.productions.put("E", new String[]{"T", "E'"});
        parseTable.productions.put("E'", new String[]{"+", "T", "E'", "ϵ"});
        parseTable.productions.put("T", new String[]{"F", "T'"});
        parseTable.productions.put("T'", new String[]{"*", "F", "T'", "ϵ"});
        parseTable.productions.put("F", new String[]{"(", "E", ")", "id"});

        // Initialize the FIRST sets
        parseTable.firstSets.put("E", new HashSet<>(Arrays.asList("(", "id")));
        parseTable.firstSets.put("E'", new HashSet<>(Arrays.asList("+", "ϵ")));
        parseTable.firstSets.put("T", new HashSet<>(Arrays.asList("(", "id")));
        parseTable.firstSets.put("T'", new HashSet<>(Arrays.asList("*", "ϵ")));
        parseTable.firstSets.put("F", new HashSet<>(Arrays.asList("(", "id")));

        // Initialize the FOLLOW sets
        parseTable.followSets.put("E", new HashSet<>(Arrays.asList(")", "$")));
        parseTable.followSets.put("E'", new HashSet<>(Arrays.asList(")", "$")));
        parseTable.followSets.put("T", new HashSet<>(Arrays.asList("+", ")", "$")));
        parseTable.followSets.put("T'", new HashSet<>(Arrays.asList("+", ")", "$")));
        parseTable.followSets.put("F", new HashSet<>(Arrays.asList("*", "+", ")", "$")));
    }

    static void data(PredictiveParseTable parseTable) {
        System.out.println("Data: -");
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.print("Terminals: ");
        for (String entry : parseTable.terminals) {
            System.out.print(entry + ", ");
        }
        System.out.println();

        System.out.print("Non-terminals: ");
        for (String entry : parseTable.nonTerminals) {
            System.out.print(entry + ", ");
        }
        System.out.println();

        System.out.print("Productions: ");
        for (Map.Entry<String, String[]> entry : parseTable.productions.entrySet()) {
            System.out.print(entry.getKey() + "=" + Arrays.toString(entry.getValue()) + ", ");
        }
        System.out.println();

        System.out.print("FIRST(): ");
        for (Map.Entry<String, Set<String>> entry : parseTable.firstSets.entrySet()) {
            System.out.print(entry + ", ");
        }
        System.out.println();

        System.out.print("FOLLOW(): ");
        for (Map.Entry<String, Set<String>> entry : parseTable.followSets.entrySet()) {
            System.out.print(entry + ", ");
        }
        System.out.println();
        System.out.println("--------------------------------------------------------------------------------------");
    }

    static void parser(PredictiveParseTable parseTable, BufferedReader input, PrintWriter output)
            throws IOException {
        ArrayList<Stack<Object>> stackData = new ArrayList<>();
        ArrayList<ArrayList<String>> inputData = new ArrayList<>();
        ArrayList<StringBuilder> outputData = new ArrayList<>();

        String line = input.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line);

        String[] stringArray = line.split(" ");
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(stringArray));

        boolean error = false;
        boolean error1 = true;

        Stack<Object> stack = new Stack<>();
        stack.push("$");
        stack.push("E");

        Stack<Object> s0 = new Stack<>();
        s0.addAll(stack);
        stackData.add(s0);


        ArrayList<String> a0 = new ArrayList<>(arrayList);
        inputData.add(a0);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            String iterator = stack.pop().toString();

            while (!Objects.equals(token, iterator)) {
                for (Map.Entry<String, HashMap<String, String[]>> nonTerminal : parseTable.parseTable.entrySet()) {
                    if (Objects.equals(iterator, nonTerminal.getKey())) {
                        for (Map.Entry<String, String[]> terminal : nonTerminal.getValue().entrySet()) {
                            if (Objects.equals(token, terminal.getKey())) {
                                StringBuilder p = new StringBuilder(nonTerminal.getKey() + " ---> ");
                                for (int i = terminal.getValue().length - 1; i >= 0; i--) {
                                    if (!Objects.equals(terminal.getValue()[i], "ϵ")) {
                                        stack.push(terminal.getValue()[i]);
                                        p.append(terminal.getValue()[terminal.getValue().length - 1 - i]);
                                    }
                                    else {p.append("ϵ");}
                                }
                                error1 = false;
                                outputData.add(p);
                            }
                        }
                    }
                }
                if (Objects.equals(stack.peek(), "$") && !Objects.equals(token, "$")) {
                    stack.push(iterator);
                    outputData.add(new StringBuilder("Error, Skip " + token));
                    error = true;
                    break;
                }

                else if (error1) {
                    outputData.add(new StringBuilder("Error, Pop " + iterator));
                }

                Stack<Object> s1 = new Stack<>();
                s1.addAll(stack);
                stackData.add(s1);

                ArrayList<String> a1 = new ArrayList<>(arrayList);
                inputData.add(a1);

                error= false;
                error1 = true;

                iterator = stack.pop().toString();
            }
            if (Objects.equals(token, "$")) {
                outputData.add(new StringBuilder("Parsing successfully halted"));
                break;
            }

            else if (!stack.isEmpty()) {
                arrayList.remove(token);

                Stack<Object> s3 = new Stack<>();
                s3.addAll(stack);
                stackData.add(s3);

                ArrayList<String> a3 = new ArrayList<>(arrayList);
                inputData.add(a3);
            }

            if (!error) {
                outputData.add(new StringBuilder());
            }
        }

        output.printf("%-30s", "Stack");
        output.printf("%-30s", "Input");
        output.printf("%-30s", "Output");
        output.println();

        output.println("-------------------------------------------------------------------------");

        for (int i = 0; i < stackData.size(); i++) {
            output.printf("%-30s", stackData.get(i));
            output.printf("%-30s", inputData.get(i));
            output.printf("%-30s", outputData.get(i));
            output.println();
        }
        input.close();
        output.close();
    }
}