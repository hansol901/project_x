package enums;

import java.util.Arrays;

public enum Operation {
    MINUS("-","Minus"),
    PLUS("+","Plus"),
    MULTIPLY("*","Multiply by"),
    DIVIDE("/","Divide by");

    final String sign;
    final String legacyName;

    Operation(String sign, String legacyName) {
        this.sign = sign;
        this.legacyName = legacyName;
    }


    public static Operation getBySign(final String sign) {
        return Arrays.stream(values()).filter(operation -> operation.sign().equals(sign)).findFirst().orElse(null);
    }

    public String sign() {
        return sign;
    }
    public String legacyName() {
        return legacyName;
    }

}
