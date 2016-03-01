//import of osp package
package osp.Threads;

/**
 * The Class BrazPriorityQue. This works be in conjuction with BrazList, which
 * is a data structure created to fulfill the needs of the OSP.
 */
public class BrazPriorityQue {

	/** The p1. */
	// declaring new BrazList to be used for different priorities
	BrazList<ThreadCB> p1 = new BrazList<ThreadCB>();

	/** The p2. */
	// declaring new BrazList to be used for different priorities
	BrazList<ThreadCB> p2 = new BrazList<ThreadCB>();

	/** The p3. */
	// declaring new BrazList to be used for different priorities
	BrazList<ThreadCB> p3 = new BrazList<ThreadCB>();

	/**
	 * Insert.
	 *
	 * @param t
	 *            the t
	 */
	public void insert(ThreadCB t) {
		// case/switch to check priority
		switch (t.getPriority()) {
		// if priority = 1
		case 1:
			// add thread to P1
			p1.add(t);
			// break out
			break;
		// if priority = 2
		case 2:
			// add thread to P2
			p2.add(t);
			// break out
			break;
		// if priority = 3
		case 3:
			// add thread to p3
			p3.add(t);
			// break out
			break;
		}
	}

	/**
	 * Removes the tail.
	 *
	 * @return the thread cb
	 */
	public ThreadCB removeTail() {
		// declare a temp var to return a thread
		ThreadCB retVal = null;
		// if my priority que p1 is not empty
		if (!p1.isEmpty()) {
			// remove the first thread in queue, and assign to retval
			retVal = p1.removeFirst();
			// if my priority que p2 is not empty
		} else if (!p2.isEmpty()) {
			// remove the first thread in queue, and assign to retval
			retVal = p2.removeFirst();
			// if my priority que p3 is not empty
		} else if (!p3.isEmpty()) {
			// remove the first thread in queue, and assign to retval
			retVal = p3.removeFirst();
		}
		// return to ThreadCB with thread popped from que
		return retVal;
	}

	/**
	 * Removes the.
	 *
	 * @param t
	 *            the t
	 * @return true, if successful
	 */
	public boolean remove(ThreadCB t) {
		// if the threads priority is 1
		if (t.getPriority() == 1) {
			// remove the thread from priority queue 1
			p1.remove(t);
			// if the threads priority is 2
		} else if (t.getPriority() == 2) {
			// remove the thread from priority queue 2
			p2.remove(t);
			// if the threads priority is 3
		} else if (t.getPriority() == 3) {
			// remove the thread from priority queue 3
			p3.remove(t);
		}
		// return to threadCB
		return true;
	}

	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size() {
		// declare and assign size variable
		// accomplished by adding the sizes of all queues together
		int size = p1.size() + p2.size() + p3.size();
		// return the size of all queues
		return size;
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		// if the size is 0, must be empty
		if (size() == 0) {
			// return true for empty
			return true;
		}
		// return false if not empty
		return false;
	}
}
