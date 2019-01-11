package com.j0tech.stateful.exceptions;

import com.j0tech.stateful.State;
import com.j0tech.stateful.StateMachine;

import java.util.Locale;

/**
 * Thrown when a {@link State} that is not managed by a {@link StateMachine} is used with said state machine.
 */
public class StateAlreadyManagedException extends RuntimeException {

    public StateAlreadyManagedException(State state) {
        super(String.format(Locale.getDefault(), "State %s is already managed by this state machine, and " +
                "cannot be added again", state.toString()));
    }

    public StateAlreadyManagedException(State state, String additionalInfo) {
        super(String.format(Locale.getDefault(), "State %s is already managed by this state machine, and " +
                "cannot be added again (%s)", state.toString(), additionalInfo));
    }
}
