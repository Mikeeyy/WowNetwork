package com.matejko.wownetwork.exceptions;

public class SelfFollowingException extends Exception {
    public SelfFollowingException(final String userNickname) {
        super(String.format("User %s tries to follow himself", userNickname));
    }
}
