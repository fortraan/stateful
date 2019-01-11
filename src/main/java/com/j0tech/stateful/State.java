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
