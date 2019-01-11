package com.j0tech.stateful.exceptions;

import com.j0tech.stateful.State;
import com.j0tech.stateful.StateMachine;

import java.util.Locale;

/**
 * Thrown when a {@link State} is used with a {@link StateMachine} that does not manage it.
 */
public class StateNotManagedException extends RuntimeException {

    public StateNotManagedException(State state) {
        super(String.format(Locale.getDefault(), "State %s is not managed by this state machine, and " +
                "cannot be used", state.toString()));
    }

    public StateNotManagedException(State state, String additionalInfo) {
        super(String.format(Locale.getDefault(), "State %s is not managed by this state machine, and " +
                "cannot be used (%s)", state.toString(), additionalInfo));
    }
}
