package osp.Threads;

import osp.IFLModules.IflTimerInterruptHandler;

/**
 * The timer interrupt handler. This class is called upon to handle timer
 * interrupts.
 * 
 * @OSPProject Threads
 */
public class TimerInterruptHandler extends IflTimerInterruptHandler {
	/**
	 * This basically only needs to reset the times and dispatch another
	 * process.
	 * 
	 * @OSPProject Threads
	 */

	// Page 9, hwtimer set, this interrupt happens
	public void do_handleInterrupt() {
		// your code goes here
		ThreadCB.dispatch(); // <-- we do the dispatch

	}

	/*
	 * Feel free to add methods/fields to improve the readability of your code
	 */

}

/*
 * Feel free to add local classes to improve the readability of your code
 */
