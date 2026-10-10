package dsa.arrays;

public class AddBorder {

    String[] addBorder(String[] picture) {
        int x = picture.length;
        String[] output = new String[x+2];

        String s = "*";
        String copy = new String(picture[0]);
        copy = copy.replaceAll("\\w","*");
        String firstRow = s + copy + s;
        output[0]=firstRow;
        output[x+1]=firstRow;
        int i = 1;
        for(String str : picture){

            String newStr = s +str+ s;
            output[i] = newStr;
            i++;
        }
        return  output;
    }
}
