package me.aleiv.gameengine.exceptions;

public class GameStartException extends Exception {

    public enum GameStartExceptionReason {
        GAME_ALREADY_STARTED,
        GAME_NOT_LOADED,
        NOT_ENOUGTH_PLAYERS,
        UNKNOWN_EXCEPTION;
    }

    private final GameStartExceptionReason reason;
    private final Exception unknownException;

    public GameStartException(GameStartExceptionReason reason, Exception unknownException) {
        super(reason.toString());
        this.reason = reason;
        this.unknownException = unknownException;
    }

    public GameStartException(GameStartExceptionReason reason) {
        this(reason, null);
    }

    public GameStartExceptionReason getReason() {
        return this.reason;
    }

    public Exception getUnknownException() {
        return this.unknownException;
    }

}
