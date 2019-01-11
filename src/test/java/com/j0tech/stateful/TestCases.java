package com.j0tech.stateful;

import com.j0tech.stateful.exceptions.StateNotManagedException;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class TestCases {

    public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();
        AtomicBoolean shouldExit = new AtomicBoolean(false);
        State closedState = new State("Closed") {
            @Override
            public void onEnterState() {
                System.out.println("Door is closed");
            }

            @Override
            public void whileInState() {
                final long deltaTime = System.currentTimeMillis() - startTime;
                if (deltaTime > 7000) {
                    shouldExit.set(true);
                }
            }

            @Override
            public void onExitState() {
                System.out.println("Door is beginning to open");
            }
        };
        State openedState = new State("Opened") {
            @Override
            public void onEnterState() {
                System.out.println("Door is open");
            }

            @Override
            public void whileInState() {

            }

            @Override
            public void onExitState() {
                System.out.println("Door is beginning to close");
            }
        };
        Transition opening = new Transition(openedState) {
            @Override
            public void onActivated() {
                System.out.println("Door is opening");
            }

            @Override
            public boolean shouldActivate() {
                final long deltaTime = System.currentTimeMillis() - startTime;
                return deltaTime > 3000 && deltaTime < 3500;
            }
        };
        Transition closing = new Transition(closedState) {
            @Override
            public void onActivated() {
                System.out.println("Door is closing");
            }

            @Override
            public boolean shouldActivate() {
                final long deltaTime = System.currentTimeMillis() - startTime;
                return deltaTime > 6000 && deltaTime < 6500;
            }
        };
        openedState.addTransition(closing);
        closedState.addTransition(opening);

        StateMachine stateMachine = new StateMachine();

        stateMachine.addStates(openedState, closedState);

        stateMachine.setEntryState(closedState);

        while (!shouldExit.get()) {
            stateMachine.update();
        }
    }

    @Test
    public void testUnmanagedStateDetection() {
        boolean success = false;
        State state1 = new State("state1") {
            @Override
            public void onEnterState() {

            }

            @Override
            public void whileInState() {

            }

            @Override
            public void onExitState() {

            }
        };
        StateMachine stateMachine = new StateMachine();
        // test setting entry state
        try {
            stateMachine.setEntryState(state1);
        } catch (StateNotManagedException e) {
            success = true;
        }
        Assert.assertTrue(success);
        success = false;
        // test setting current state
        try {
            stateMachine.setCurrentState(state1);
        } catch (StateNotManagedException e) {
            success = true;
        }
        Assert.assertTrue(success);
    }
}
