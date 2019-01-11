package com.j0tech.stateful;

import com.j0tech.stateful.exceptions.TransitionAlreadyManagedException;
import com.j0tech.stateful.exceptions.TransitionPointsToItselfException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Represents a state in a {@link StateMachine}.
 */
public abstract class State {

    private String stateName;

    List<Transition> managedTransitions;

    /**
     * Constructs a state with the default name.
     */
    public State() {
        stateName = getClass().getName() + "@" + Integer.toHexString(hashCode());
        managedTransitions = new ArrayList<>();
    }

    /**
     * Constructs a state with the given name.
     * @param name the name
     */
    public State(String name) {
        stateName = name;
        managedTransitions = new ArrayList<>();
    }

    /**
     * Called when the state machine enters this state.
     */
    public abstract void onEnterState();

    /**
     * While the state machine is in this state, this function will be called every time {@link StateMachine#update()}
     * is called.
     */
    public abstract void whileInState();

    /**
     * Called when the state machine leaves this state.
     */
    public abstract void onExitState();

    private boolean checkIfTransitionIsManaged(Transition transition) {
        for (Transition managedTransition : managedTransitions) {
            if (transition == managedTransition) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a transition that <b>starts</b> at this state.
     * @param transition the transition to add
     */
    public void addTransition(Transition transition) {
        if (checkIfTransitionIsManaged(transition)) {
            throw new TransitionAlreadyManagedException(transition);
        }
        if (transition.endState == this) {
            throw new TransitionPointsToItselfException(transition);
        }

        transition.registerStartState(this);
        managedTransitions.add(transition);
    }

    /**
     * Adds transitions that <b>start</b> at this state.
     * @param transitions the transitions to add
     */
    public void addTransitions(Transition... transitions) {
        for (int i = 0; i < transitions.length; i++) {
            if (checkIfTransitionIsManaged(transitions[i])) {
                throw new TransitionAlreadyManagedException(transitions[i], String.format(Locale.getDefault(),
                        "This transition was added by a call to addTransitions(), at index %d", i));
            }
            if (transitions[i].endState == this) {
                throw new TransitionPointsToItselfException(transitions[i], String.format(Locale.getDefault(),
                        "This transition was added by a call to addTransitions(), at index %d", i));
            }
        }

        for (Transition transition : transitions) {
            transition.registerStartState(this);
        }
        managedTransitions.addAll(Arrays.asList(transitions));
    }

    @Override
    public String toString() {
        return stateName;
    }
}
