import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar_swidan on 20/03/18.
 */
public class LineAnalyser2 {

    private Outcomes outcomes;
    private List<String> objectProg;

    public LineAnalyser2(Outcomes outcomes) {
        this.outcomes = outcomes;
        objectProg = outcomes.getObjectProg();
        objectProg = new ArrayList<>();
    }

    public void analyse() throws Exception {

        LineAssembler la = new LineAssembler();
        Text text = null;
        int instructionsCount = 0;
        int i = 0;
        String[] s = outcomes.getInterFile().get(i).split("\\s");
        la.writeHeader(s[1], Integer.parseInt(outcomes.getEndAdd(),16), outcomes.getStartAdd(),objectProg);
        i++;
        String opCode="", operand="";
        s = outcomes.getInterFile().get(i).split("\\s");
        int x;

        while (s[0].compareTo("END") != 0) {
            x=0;
            if (s[0].startsWith(".")) {
                i++;
                continue;
            }
            if (s.length!=2) {
                opCode = s.length == 4 ? s[2] : s[1];
                operand = s.length == 4 ? s[3] : s[2];
            }  else {
                opCode="RSUB";
                operand="0";
            }
            if(operand.matches("\\w+\\,X")) {
                operand = operand.substring(0, operand.length() - 2);
                x=1;
            }
            if (instructionsCount == 10) {

                text.setLength("1e");
                la.writeText(text,objectProg,instructionsCount);
                instructionsCount = 0;
            }

            if (instructionsCount == 0) {

                text = outcomes.getText();
                la.textInit(s[0], text);

            }
            if (opCode.compareTo("RESW") == 0 && instructionsCount != 0) {
                text.setLength(Integer.toHexString(3 * instructionsCount));
                la.writeText(text, objectProg,instructionsCount);
                instructionsCount = 0;
            }
            else if (opCode.compareTo("RESB") == 0&& instructionsCount!=0){
                text.setLength(Integer.toHexString(3*instructionsCount));
                la.writeText(text, objectProg,instructionsCount);
                instructionsCount = 0;

            }

           else if (outcomes.getOpTable().get(opCode) != null) {


                operand = dealWithOperand(operand);
                la.assembleObjectCodeInst(text,
                        outcomes.getOpTable().get(opCode), operand, x);
                instructionsCount++;
            } else if (opCode.compareTo("WORD") == 0) {

                opCode = "00";
                la.assembleObjectCodeInst(text, opCode,
                        Integer.toHexString(Integer.parseInt(operand,16)), 0);
                instructionsCount++;

            } else if (opCode.compareTo("BYTE") == 0) {
                opCode="00";
                if (operand.startsWith("C")) {
                    if ((10 - instructionsCount < Math.ceil(operand.length() / 3))) {
                        text.setLength(Integer.toHexString(3 * instructionsCount));
                        la.writeText(text, objectProg,instructionsCount);
                        instructionsCount = 0;
                        i--;
                    }
                    else {

                        la.assembleObjectCodeInst(text,
                                operand.substring(2, operand.length() - 1));

                        instructionsCount++;
                    }
                }
               else if (operand.startsWith("X")) {
                    opCode = "00";

                    operand = operand.substring(2, operand.length() - 1);
                    la.assembleObjectCodeInst(text, opCode, operand, 0);
                    instructionsCount++;
                }

            }

            i++;
            s = outcomes.getInterFile().get(i).split("\\s");


        }
        if (instructionsCount!=0) {
            text.setLength(Integer.toHexString(instructionsCount*3));
            la.writeText(text, objectProg, instructionsCount);
        }
        la.writeEnd(outcomes.getStartAdd(),objectProg);
        new AssemblyFileWriter(objectProg,"ObjectProgram").write();

    }

    public int dealWithInstCount(String opCode, int instructionsCount, Text text, LineAssembler la, String s) {
        if (instructionsCount == 10 || opCode.compareTo("RESW") == 0 || opCode.compareTo("RESB") == 0) {

            la.writeText(text,objectProg,instructionsCount);
            instructionsCount = 0;
        }

        return instructionsCount;
    }

    public String dealWithOperand(String operand) throws Exception {


        if (!operand.matches("[0-9]+")) {
            if (outcomes.getSymbTable().get(operand) != null) {
                operand = outcomes.getSymbTable().get(operand);
            } else {
                System.out.println(operand);

                operand = "0";
                throw new Exception("undefined symbol");
            }
        }

        return operand;
    }
}
