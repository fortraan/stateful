package com.j0tech.stateful.exceptions;

import com.j0tech.stateful.State;
import com.j0tech.stateful.Transition;

import java.util.Locale;

/**
 * Thrown when a {@link Transition} is added to a {@link State} more than once.
 */
public class TransitionAlreadyManagedException extends RuntimeException {

    public TransitionAlreadyManagedException(Transition transition) {
        super(String.format(Locale.getDefault(), "Transition %s is already managed by this state, and " +
                "cannot be added again", transition.toString()));
    }

    public TransitionAlreadyManagedException(Transition transition, String additionalInfo) {
        super(String.format(Locale.getDefault(), "Transition %s is already managed by this state, and " +
                "cannot be added again (%s)", transition.toString(), additionalInfo));
    }
}
