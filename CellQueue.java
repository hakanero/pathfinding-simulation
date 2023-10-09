/* Hakan Eroglu
 * CS231A
 * Grid Based Search
 * CellQueue.java
 */
public class CellQueue {
	/** Helper class */
	private class Node {
		private Node next;
		private Cell cell;

		public Node(Cell c) {
			cell = c;
			next = null;
		}

		public Node getNext() {
			return next;
		}

		public void setNext(Node n) {
			next = n;
		}

		public Cell getCell() {
			return cell;
		}
	}

	Node head;
	Node tail;
	int size;

	public CellQueue() {
		head = null;
		size = 0;
	}

	public void offer(Cell cell) {
		Node newNode = new Node(cell);
		size++;
		if (tail == null) {
			head = tail = newNode;
			return;
		}
		tail.setNext(newNode);
		tail = newNode;
	}

	public Cell poll() {
		if (head == null) {
			return null;
		}
		Cell toReturn = head.getCell();
		head = head.getNext();
		if (head == null) {
			tail = null;
		}
		size--;
		return toReturn;
	}

	/** Is the queue empty? */
	public boolean isEmpty() {
		return size == 0;
	}

	/** Returns the number of elements in the queue */
	public int size() {
		return size;
	}
}
