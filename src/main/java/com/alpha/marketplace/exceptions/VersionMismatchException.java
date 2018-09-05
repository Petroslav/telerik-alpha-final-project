package com.alpha.marketplace.exceptions;

public class VersionMismatchException extends Exception {

    public VersionMismatchException(){}

    public VersionMismatchException(String msg){
        super(msg);
    }
}
