package com.j0tech.stateful;

/**
 * Represents a transition from one {@link State} to another.
 */
public abstract class Transition {

    State startState, endState;

    /**
     * Constructs a new Transition that points to the {@link State} specified by end.
     * @param end end state for this transition
     */
    public Transition(State end) {
        endState = end;
    }

    void registerStartState(State start) {
        startState = start;
    }

    /**
     * Called when this transition is activated.
     */
    public abstract void onActivated();

    /**
     * Checks if the conditions for the state machine to enter this transition are met.
     * @return true if conditions are met, false otherwise
     */
    public abstract boolean shouldActivate();
}
