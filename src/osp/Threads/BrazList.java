/** DAVID BRAZEAU
 *  bp2012gs@metrostate.edu
 */

/***************************************************************************
 * A simply data collection designed to accommodate the needs of ThreadCB. 
 * For perfect harmony, this should be used with BrazPriorityQue 
 * Implemented for use with any objects, and to be able to be expanded upon. *
 *****************************************************************************/
//import the OSP package
package osp.Threads;

//import of java utility library
import java.util.*;

/**
 * The Class BrazQue. Essentially this is a overly simplified Linked List that
 * was designed to serve the needs of TaskCB.
 *
 * @param <AnyType>
 *            the generic type
 */
public class BrazList<AnyType> implements Iterable<AnyType> {

	/** The head, data point to front of list. */
	private Node<AnyType> head;
	/** The size, stores the number of objects in list */
	private int size = 0;

	/** Constructs/Initializes an empty list */
	public BrazList() {
		// initializing head to null
		head = null;
	}

	/**
	 * Returns true if the list is empty. Useful method for error checking.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		// return true if the list is empty.(will be empty if head is null)
		return head == null;
	}

	/**
	 * Returns the number of elements in this list.
	 *
	 */
	public int size() {
		// returns the current size of list
		return size;
	}

	/**
	 * Adds an object to the end of this list via the call to addLast(). Also,
	 * this is where size incremented
	 * 
	 * @param item
	 *            the item
	 */
	public boolean add(AnyType item) {
		// increments size by one
		size++;
		// calls method to add object at end of list
		addLast(item);
		// returns true to signal the add was success
		return true;
	}

	/**
	 * Inserts an object at the beginning of this list. This is used to manually
	 * add to front of list, or add to front of list when it is a new list.
	 *
	 * @param item
	 *            the item
	 */
	public void addFirst(AnyType item) {
		// add the passed object to the head or front of list
		head = new Node<AnyType>(item, head);
	}

	/**
	 * Returns the first object in the list.
	 *
	 * @return the first
	 */
	public AnyType getFirst() {
		// if head is null, error that list empty
		if (head == null) {
			// error
			throw new NoSuchElementException();
		}
		// else, returns the head of list
		return head.data;
	}

	/**
	 * Removes the first object in the list.
	 *
	 * @return the any type
	 */
	public AnyType removeFirst() {
		// first makes a call to method to get first object
		AnyType tmp = getFirst();
		// sets pointer the next position because current one is
		// about to be removed
		head = head.next;
		// returns the object to be removed
		return tmp;
	}

	/**
	 * Inserts a new object to the end of this list.
	 *
	 * @param item
	 *            the item
	 */
	public void addLast(AnyType item) {
		// check to see if head null, if it is, then its a new list
		// and should add to first
		if (head == null)
			// adding to front of list
			addFirst(item);
		else {
			// assigning head to tmp to use as pointer
			Node<AnyType> tmp = head;
			// looping to end of list
			while (tmp.next != null) {
				// assigning the next object to tmp
				tmp = tmp.next;
			}
			// adding in the object to end of list
			tmp.next = new Node<AnyType>(item, null);
		}
	}

	/**
	 * Returns the last object in the list.
	 *
	 * @return the last
	 */
	public AnyType getLast() {
		// if head null, error out because list empty
		if (head == null) {
			// error
			throw new NoSuchElementException();
		}
		// assigning head to tmp to use as pointer
		Node<AnyType> tmp = head;
		// looping to end of list
		while (tmp.next != null) {
			// assigning the next object to tmp
			tmp = tmp.next;
		}
		// returning last object in list
		return tmp.data;
	}

	/**
	 * Removes all nodes from the list. Handy for wiping all objects from list.
	 */
	public void clear() {
		// simply sets the head to null
		head = null;
	}

	/**
	 * Returns true if this list contains the specified element.
	 *
	 * @param x
	 *            the x
	 * @return true, if successful
	 */
	public boolean contains(AnyType x) {
		// for each object, check it versus passed object
		for (AnyType tmp : this) {
			// if the objects are equal, return true vale
			if (tmp.equals(x)) {
				// return true because its equal
				return true;
			}
		}
		// object did not match any objects in list, return false
		return false;
	}

	/**
	 * Returns the data at the specified position in the list.
	 *
	 * @param pos
	 *            the pos
	 * @return the any type
	 */
	public AnyType get(int pos) {
		// if head is null, error out
		if (head == null) {
			// error
			throw new IndexOutOfBoundsException();
		}
		// assigning head to tmp to use as pointer
		Node<AnyType> tmp = head;
		// Positing to the passed value.
		// using loop because it my not be valid value
		// so in a sense we start at beginning and "approach it"
		for (int k = 0; k < pos; k++) {
			// assigning the next position to tmp holder
			tmp = tmp.next;
		}
		// if we hit null before passes position, error out
		if (tmp == null) {
			// error
			throw new IndexOutOfBoundsException();
		}
		// if object is found at passed value, return the value
		return tmp.data;
	}

	/**
	 * Returns a string of the objects in list.
	 *
	 * @return the string
	 */
	public String toString() {
		// creates new string buffer
		StringBuffer result = new StringBuffer();
		// loops all objects and adds them to string buffer
		for (Object x : this) {
			// adding current object to string buffer
			result.append(x + " ");
		}
		// returning a string of all objects
		return result.toString();
	}

	public AnyType removeTail(AnyType key) {
		// decrement size to keep accurate
		size--;
		// if head is null error out
		if (head == null) {
			// error
			throw new RuntimeException("cannot delete");
		}

		// assigning head to tmp to use as pointer
		Node<AnyType> tmp = head;
		// looping to end of list
		while (tmp.next != null) {
			// assigning the next object to tmp
			tmp = tmp.next;
		}
		// returning last object in list
		return tmp.data;
	}

	/**
	 * Removes the first occurrence of the specified object in this list. Size
	 * is also decremented here to keep size correct.
	 * 
	 * 
	 * @param key
	 *            the key
	 * @return
	 */
	public void remove(AnyType key) {
		// decrement size to keep accurate
		size--;
		// if head is null error out
		if (head == null) {
			// error
			throw new RuntimeException("cannot delete");
		}
		// if the head equals the object sent
		if (head.data.equals(key)) {
			// assign the next object to head
			head = head.next;
			// return out
			return;
		}
		// setting the head of list to cur
		Node<AnyType> cur = head;
		// setting the previous object to null
		Node<AnyType> prev = null;
		// looping list as long as cur is not null and cur equals object passed
		while (cur != null && !cur.data.equals(key)) {
			// setting curr to prev to keep track of pointers for where we are
			prev = cur;
			// setting cur to next so we know what object is next in list
			cur = cur.next;
		}
		// if we hit end, throw error
		if (cur != null) {
			// error
			// throw new RuntimeException("cannot delete");
			prev.next = cur.next;
		}
		// otherwise shift the object locations for removing
		// prev.next = cur.next;
	}

	/**
	 * ********************************************************
	 * 
	 * The Node class, this is data structure that the objects are stored in.
	 * 
	 * ********************************************************
	 *
	 * @param <AnyType>
	 *            the generic type
	 */
	private static class Node<AnyType> {

		/** The data. */
		private AnyType data;
		/** The next node of data. */
		private Node<AnyType> next;

		/**
		 * Instantiates a new node.
		 *
		 * @param data
		 *            the data
		 * @param next
		 *            the next
		 */
		public Node(AnyType data, Node<AnyType> next) {
			// sets the passed data to data in Node
			this.data = data;
			// sets next object to passed next
			this.next = next;
		}
	}

	/**
	 * *****************************************************
	 * 
	 * The Iterator class, used to iterate the data in Node
	 * 
	 * ******************************************************.
	 *
	 * @return the iterator
	 */
	public Iterator<AnyType> iterator() {
		// returns a new linked list iterator
		return new LinkedListIterator();
	}

	/**
	 * The Class LinkedListIterator.
	 */
	private class LinkedListIterator implements Iterator<AnyType> {

		/** The next node. */
		private Node<AnyType> nextNode;

		/** Instantiates a new linked list iterator. */
		public LinkedListIterator() {
			// getting head for next Node
			nextNode = head;
		}

		/**
		 * Method call to return true if the exists a next object
		 */
		public boolean hasNext() {
			// return true as long as next is not null
			return nextNode != null;
		}

		/**
		 * Returns the next object in the list
		 */
		public AnyType next() {
			// if next object null
			if (!hasNext()) {
				// error out
				throw new NoSuchElementException();
			}
			// Assigning next nod to res
			AnyType res = nextNode.data;
			// getting the node that is next to next node
			nextNode = nextNode.next;
			// returning the next node
			return res;
		}

		/**
		 * Unable to directly call a remove
		 */
		public void remove() {
			// error out
			throw new UnsupportedOperationException();
		}
	}
}
