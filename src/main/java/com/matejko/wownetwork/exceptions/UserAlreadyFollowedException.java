package com.matejko.wownetwork.exceptions;

public class UserAlreadyFollowedException extends Throwable {
    public UserAlreadyFollowedException(final String followingUserName, final String toBeFollowedUserName) {
        super(String.format("User %s already follows user %s", followingUserName, toBeFollowedUserName));
    }
}
