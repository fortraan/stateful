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
