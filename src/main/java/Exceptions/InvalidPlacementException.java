package Exceptions;

public class InvalidPlacementException extends Exception{

    public InvalidPlacementException(String reason, String message){
        super(reason + message);
    }
}
