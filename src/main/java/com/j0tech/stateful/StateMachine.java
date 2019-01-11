/*
Copyright (c) 2019 j0 tech

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

No person may sell an unmodified version of the Software as a standalone,
or shall in any way monetize the Software without modification. Modified versions
of the Software, or versions of the Software used in other applications are
exempt from this.

THIS SOFTWARE IS PROVIDED ON AN 'AS-IS' BASIS, AND NO WARRANTY, NEITHER EXPRESS NOR IMPLIED,
IS PROVIDED WITH THE SOFTWARE. J0 TECH IS NOT RESPONSIBLE, NOR MAY BE HELD LIABLE, FOR ANY
DAMAGE, LOSSES, ETC. CAUSED BY THE SOFTWARE. USAGE OF THE SOFTWARE IS AN IMPLICIT AGREEMENT
TO THESE TERMS. FAILURE TO ABIDE BY THESE TERMS REPRESENTS A BREACH OF LICENSE.
 */

package com.j0tech.stateful;

import com.j0tech.stateful.exceptions.NoEntryStateSetException;
import com.j0tech.stateful.exceptions.StateAlreadyManagedException;
import com.j0tech.stateful.exceptions.StateNotManagedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * A simple implementation of a non-deterministic finite state machine.
 */
public final class StateMachine {

    private List<State> managedStates;

    private State entryState, currentState;

    private final Object lock = new Object();

    /**
     * Construct a new state machine.
     */
    public StateMachine() {
        managedStates = new ArrayList<>();
    }

    private boolean checkIfStateIsManaged(State state) {
        for (State existingState : managedStates) {
            if (existingState == state) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a state to the state machine.
     * @param state state to add
     */
    public void addState(State state) {
        // check if we already manage this state
        if (checkIfStateIsManaged(state)) {
            // if so, reject it
            throw new StateAlreadyManagedException(state);
        }

        managedStates.add(state);
    }

    /**
     * Adds multiple states to the state machine.
     * @param states states to add
     */
    public void addStates(State... states) {
        // check if any of the states are already managed
        for (int i = 0; i < states.length; i++) {
            if (checkIfStateIsManaged(states[i])) {
                // if so, reject it, but explain which state it was
                throw new StateAlreadyManagedException(states[i], String.format(Locale.getDefault(), "This " +
                        "state was added by a call to addStates(), at index %d", i));
            }
        }

        managedStates.addAll(Arrays.asList(states));
    }

    /**
     * Sets the state the state machine starts on.
     *
     * Note: {@link State#onEnterState()} will not be called on the entry state.
     * @param state entry state
     */
    public void setEntryState(State state) {
        synchronized (lock) {
            if (!checkIfStateIsManaged(state)) {
                throw new StateNotManagedException(state);
            }

            entryState = state;
        }
    }

    /**
     * Forces the state machine to switch to the specified state.
     * @param state the state to switch to
     */
    public void setCurrentState(State state) {
        synchronized (lock) {
            if (!checkIfStateIsManaged(state)) {
                throw new StateNotManagedException(state);
            }

            currentState.onExitState();
            currentState = state;
            currentState.onEnterState();
        }
    }

    private void validateTransitionsOrThrow() {
        for (State managedState : managedStates) {
            for (Transition managedTransition : managedState.managedTransitions) {
                if (!checkIfStateIsManaged(managedTransition.endState)) {
                    throw new StateNotManagedException(managedTransition.endState, "Transition points to unmanaged state");
                }
            }
        }
    }

    /**
     * Updates the state machine.
     *
     * Note: make sure to set the entry state with {@link #setEntryState(State)} before calling this function.
     */
    public void update() {
        synchronized (lock) {
            if (entryState == null) {
                throw new NoEntryStateSetException();
            }
            if (currentState == null) {
                currentState = entryState;
            }
            validateTransitionsOrThrow();
            currentState.whileInState();
            for (Transition managedTransition : currentState.managedTransitions) {
                if (managedTransition.shouldActivate()) {
                    currentState.onExitState();
                    managedTransition.onActivated();
                    currentState = managedTransition.endState;
                    currentState.onEnterState();
                }
            }
        }
    }
}
