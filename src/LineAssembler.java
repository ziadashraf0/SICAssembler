import java.util.List;

/**
 * Created by omar_swidan on 24/03/18.
 */
public class LineAssembler {

    public void writeHeader(String s,int endAdd,String startAdd,List<String> objectProg){
        StringBuilder sb=new StringBuilder();
        int start=Integer.parseInt(startAdd,16);



        sb.append("H").append(" ").append(s).append(" ").
                append(String.format("%06X",start)).
                append(" ").append(String.format("%06X",(endAdd-
                start))).append("\n");
        objectProg.add(sb.toString());

    }


    public void textInit(String startAdd, Text text){

        text.setStartAdd(startAdd);



    }

    public void assembleObjectCodeInst(Text text, String opCode, String operand, int x){

            StringBuilder sb=new StringBuilder();
            sb.append(opCode);
            if(x==0)
                sb.append(String.format("%04X",Integer.parseInt(operand,16)));
            else
            {
                operand= Integer.toHexString(Integer.parseInt(operand,16)|32768);
                sb.append(String.format("%04X",Integer.parseInt(operand,16)));

            }
            text.setInstruction(sb.toString());

    }
    public void assembleObjectCodeInst(Text text,String operand){
        StringBuilder sb=new StringBuilder();
        for (char c :
                operand.toCharArray()){
            sb.append(Integer.toHexString((int)c));
        }
        text.setInstruction(sb.toString());



    }
    public void writeEnd(String startAdd, List<String> objectProg){
        objectProg.add("E" + " " + String.format("%06X", Integer.parseInt(startAdd, 16)));

    }
    public void writeText(Text text, List<String> objectProg,int instructionCount){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("T").append(" ").append(String.format("%06X",
                Integer.parseInt(text.getStartAdd(),16)))
                .append(" ").append(String.format("%02X",
                Integer.parseInt(text.getLength(),16)))
                .append(" ");


        for (int i = 0; i < instructionCount; i++) {
            stringBuilder.append(String.format("%06X",
                    Integer.parseInt(text.getInstructions()[i], 16))).append(" ");
        }
        stringBuilder.append("\n");
        objectProg.add(stringBuilder.toString());

    }
}
