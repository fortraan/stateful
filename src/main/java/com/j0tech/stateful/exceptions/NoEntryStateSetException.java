package com.j0tech.stateful.exceptions;

import com.j0tech.stateful.State;
import com.j0tech.stateful.StateMachine;

/**
 * Thrown when {@link StateMachine#update()} is called on a state machine, but the state machine does not have a set
 * entry state.
 *
 * @see StateMachine#setEntryState(State)
 */
public class NoEntryStateSetException extends RuntimeException {

    public NoEntryStateSetException() {
        super("There was no entry state specified. Make sure you set it with setEntryState()");
    }
}
