public class ExpConv {

    public static String convert(String exp) {
        String[] split = exp.split("e");
        String num = split[0];
        String expNum = split[1];
        String result = "";
        if (expNum.charAt(0) == '-') {
            result = "0.";
            for (int i = 0; i < Integer.parseInt(expNum.substring(1)); i++) {
                result += "0";
            }
            result += num;
        } else {
            result = num;
            for (int i = 0; i < Integer.parseInt(expNum); i++) {
                result += "0";
            }
        }
        return result;
    }
}
