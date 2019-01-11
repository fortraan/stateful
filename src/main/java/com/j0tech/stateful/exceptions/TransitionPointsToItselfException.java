package com.j0tech.stateful.exceptions;

import com.j0tech.stateful.Transition;

import java.util.Locale;

/**
 * Thrown when a {@link Transition}'s start and end states are the same.
 */
public class TransitionPointsToItselfException extends RuntimeException {

    public TransitionPointsToItselfException(Transition transition) {
        super(String.format(Locale.getDefault(), "Transition %s points to the same state as starting and " +
                "ending point.", transition.toString()));
    }

    public TransitionPointsToItselfException(Transition transition, String additionalInfo) {
        super(String.format(Locale.getDefault(), "Transition %s points to the same state as starting and " +
                "ending point. (%s)", transition.toString(), additionalInfo));
    }
}
