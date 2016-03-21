package ar.fiuba.tdd.template.tp0;

public class InvalidAmountOfResultsException extends Exception {

    private String msg;

    public InvalidAmountOfResultsException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }
}
