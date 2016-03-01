//import package for project
package osp.Threads;

//import of required libraries for project
import osp.Devices.Device;
import osp.IFLModules.Event;
import osp.IFLModules.IflThreadCB;
import osp.Memory.MMU;
import osp.Resources.ResourceCB;
import osp.Tasks.TaskCB;

/**
 * This class is responsible for actions related to threads, including creating,
 * killing, dispatching, resuming, and suspending threads.
 * 
 * @OSPProject Threads
 */
public class ThreadCB extends IflThreadCB {

	// Declaring the priority queue I created for this project/
	private static BrazPriorityQue readyQueue;
	// declaring a static variable to use in order to set thread priority
	static int priority = 0;

	/**
	 * The thread constructor. Must call
	 * 
	 * super();
	 * 
	 * as its first statement.
	 * 
	 * @OSPProject Threads
	 */
	public ThreadCB() {
		// super constructor
		super();
	}

	/**
	 * This method will be called once at the beginning of the simulation. The
	 * student can set up static variables here.
	 * 
	 * @OSPProject Threads
	 */
	public static void init() {
		// creating new priority que
		readyQueue = new BrazPriorityQue();
	}

	/**
	 * Sets up a new thread and adds it to the given task. The method must set
	 * the ready status and attempt to add thread to task. If the latter fails
	 * because there are already too many threads in this task, so does this
	 * method, otherwise, the thread is appended to the ready queue and
	 * dispatch() is called.
	 * 
	 * The priority of the thread can be set using the getPriority/setPriority
	 * methods. However, OSP itself doesn't care what the actual value of the
	 * priority is. These methods are just provided in case priority scheduling
	 * is required.
	 * 
	 * @return thread or null
	 * 
	 * @OSPProject Threads
	 */
	static public ThreadCB do_create(TaskCB task) {

		// If task is at max threads (15), do dispatch.
		if (task.getThreadCount() == MaxThreadsPerTask) {
			// call to dispatch threads
			ThreadCB.dispatch();
			// return null
			return null;
		}

		// declare new thread
		ThreadCB thread = new ThreadCB();

		// if add thread fails, try dispatching threads
		if (task.addThread(thread) == FAILURE) {
			// dispatch threads
			ThreadCB.dispatch();
			// return null
			return null;
		}

		// creating a random int between 1 and 3
		priority = (int) (Math.random() * 3.0) + 1;
		// setting threads task
		thread.setTask(task);
		// setting threads priority
		thread.setPriority(priority);
		// setting threads status to ready
		thread.setStatus(ThreadReady);
		// adding thread to my priority queue
		readyQueue.insert(thread);
		// dispatching threads
		ThreadCB.dispatch();
		// returning the thread and thread data
		return thread;
	}

	/**
	 * Kills the specified thread.
	 * 
	 * The status must be set to ThreadKill, the thread must be removed from the
	 * task's list of threads and its pending IORBs must be purged from all
	 * device queues.
	 * 
	 * If some thread was on the ready queue, it must removed, if the thread was
	 * running, the processor becomes idle, and dispatch() must be called to
	 * resume a waiting thread.
	 * 
	 * @OSPProject Threads
	 */
	public void do_kill() {
		// getting this() task
		TaskCB task = this.getTask();
		// using case/switch for status of thread, to determine action
		switch (this.getStatus()) {
		// if thread is ready
		case ThreadReady:
			// remove the thread from priority que
			readyQueue.remove(this);
			// break out of case/switch
			break;
		// if thread is running on cpu
		case ThreadRunning:
			// set page table to null
			MMU.setPTBR(null);
			// set the current thread to null
			task.setCurrentThread(null);
			// break out of case/switch
			break;
		// default for case switch
		default:
			// for every device using table, cancel the I/O for it
			for (int i = 0; i < Device.getTableSize(); i++)
				// cancelling IO
				Device.get(i).cancelPendingIO(this);
			// break out
			break;
		}
		// set the status of current object to kill state
		this.setStatus(ThreadKill);
		// remove this thread from task
		task.removeThread(this);
		// unallocate resources used for thread
		ResourceCB.giveupResources(this);
		// if no running threads for task, kill it
		if (task.getThreadCount() == 0)
			// kill task
			task.kill();
		// dispatch threads
		ThreadCB.dispatch();
	}

	/**
	 * Suspends the thread that is currenly on the processor on the specified
	 * event.
	 * 
	 * Note that the thread being suspended doesn't need to be running. It can
	 * also be waiting for completion of a pagefault and be suspended on the
	 * IORB that is bringing the page in.
	 * 
	 * Thread's status must be changed to ThreadWaiting or higher, the processor
	 * set to idle, the thread must be in the right waiting queue, and
	 * dispatch() must be called to give CPU control to some other thread.
	 * 
	 * @param event
	 *            - event on which to suspend this thread.
	 * 
	 * @OSPProject Threads
	 */
	public void do_suspend(Event event) {
		// get status of current event
		int status = this.getStatus();
		// get task of current event
		TaskCB task = this.getTask();
		// if the status of thread in current event is running
		if (status == ThreadRunning) {
			// set its status to waiting
			this.setStatus(ThreadWaiting);
			// clear page table
			MMU.setPTBR(null);
			// clear current thread on task
			task.setCurrentThread(null);
			// if status was not running
		} else
			// increment status number by one
			this.setStatus(status + 1);
		// if event didnt contain the thread
		if (!event.contains(this))
			// add the thread to event
			event.addThread(this);
		// dispatch threads
		ThreadCB.dispatch();
	}

	/**
	 * Resumes the thread.
	 * 
	 * Only a thread with the status ThreadWaiting or higher can be resumed. The
	 * status must be set to ThreadReady or decremented, respectively. A ready
	 * thread should be placed on the ready queue.
	 * 
	 * @OSPProject Threads
	 */
	public void do_resume() {
		// get status of current thread
		int status = this.getStatus();
		// if the status is waiting
		if (status == ThreadWaiting) {
			// set its status to ready
			this.setStatus(ThreadReady);
			// then insert it into priority queue
			readyQueue.insert(this);
			// if thread status was not waiting
		} else
			// decrement status number by 1
			this.setStatus(status - 1);
		// dispatch threads
		ThreadCB.dispatch();
	}

	/**
	 * Selects a thread from the run queue and dispatches it.
	 * 
	 * If there is just one theread ready to run, reschedule the thread
	 * currently on the processor.
	 * 
	 * In addition to setting the correct thread status it must update the PTBR.
	 * 
	 * @return SUCCESS or FAILURE
	 * 
	 * @OSPProject Threads
	 */
	public static int do_dispatch() {
		// remove a thread from priority que and add to "thread"
		ThreadCB thread = (ThreadCB) readyQueue.removeTail();

		// If else to switch context. If page table not null and thread not null
		if (MMU.getPTBR() != null && thread != null) {
			// thread is running
			readyQueue.insert(MMU.getPTBR().getTask().getCurrentThread());
			// get thread and set status to ready
			MMU.getPTBR().getTask().getCurrentThread().setStatus(ThreadReady);
			// now set current thread to null
			MMU.getPTBR().getTask().setCurrentThread(null);
			// clear page table
			MMU.setPTBR(null);

			// set thread to running and put on cpu
			thread.setStatus(ThreadRunning);
			// get task from page table
			MMU.setPTBR(thread.getTask().getPageTable());
			// set this thread as this tasks current thread
			thread.getTask().setCurrentThread(thread);
		}// if page table is not null but thread from prioirty que is
		else if (MMU.getPTBR() != null && thread == null) {
			// then current thread keeps running
			return SUCCESS;
		}// if page table null and thread is not null
		else if (MMU.getPTBR() == null && thread != null) {
			// set thread status to running and put on cpu
			thread.setStatus(ThreadRunning);
			// add thread to page table
			MMU.setPTBR(thread.getTask().getPageTable());
			// set thread to task
			thread.getTask().setCurrentThread(thread);
		}// lastly if page table null and thread null, must be wrong or done
		else if (MMU.getPTBR() == null && thread == null) {
			// return failure
			return FAILURE;
		}
		// return failure
		return FAILURE;
	}

	/**
	 * Called by OSP after printing an error message. The student can insert
	 * code here to print various tables and data structures in their state just
	 * after the error happened. The body can be left empty, if this feature is
	 * not used.
	 * 
	 * @OSPProject Threads
	 */
	public static void atError() {
		// your code goes here
	}

	/**
	 * Called by OSP after printing a warning message. The student can insert
	 * code here to print various tables and data structures in their state just
	 * after the warning happened. The body can be left empty, if this feature
	 * is not used.
	 * 
	 * @OSPProject Threads
	 */
	public static void atWarning() {
		// your code goes here
	}
}
