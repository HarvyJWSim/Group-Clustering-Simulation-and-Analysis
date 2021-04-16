package sample;

/**
 * Created by HP on 22/4/2017.
 */
public class BinaryDecimalConverter
{
    int integerPart, decimalPart;
    String binary;

    public String convertDecimalToBinary(double decimal)
    {
        integerPart = (int)decimal;
        decimalPart = (int)((decimal - integerPart) * 10);

        switch(integerPart)
        {
            case 0:
                binary = "0000";
                break;
            case 1:
                binary = "0001";
                break;
            case 2:
                binary = "0010";
                break;
            case 3:
                binary = "0011";
                break;
            case 4:
                binary = "0100";
                break;
            case 5:
                binary = "0101";
                break;
            case 6:
                binary = "0110";
                break;
            case 7:
                binary = "0111";
                break;
            case 8:
                binary = "1000";
                break;
            case 9:
                binary = "1001";
        }

        switch(decimalPart)
        {
            case 0:
                return binary + "0000";
            case 1:
                return binary + "0001";
            case 2:
                return binary + "0010";
            case 3:
                return binary + "0011";
            case 4:
                return binary + "0100";
            case 5:
                return binary + "0101";
            case 6:
                return binary + "0110";
            case 7:
                return binary + "0111";
            case 8:
                return binary + "1000";
            case 9:
                return binary + "1001";
        }
        return "";
    }

    public double convertBinaryToDecimal(String binary)
    {
        switch(binary)
        {
            case "0000":
                return 0;
            case "0001":
                return 1;
            case "0010":
                return 2;
            case "0011":
                return 3;
            case "0100":
                return 4;
            case "0101":
                return 5;
            case "0110":
                return 6;
            case "0111":
                return 7;
            case "1000":
                return 8;
            case "1001":
                return 9;
        }
        return 0;
    }


}
