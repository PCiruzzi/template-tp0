package ar.fiuba.tdd.template.tp0;

public class InvalidRegExException extends Exception {

    private String msg;

    public InvalidRegExException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }
}
