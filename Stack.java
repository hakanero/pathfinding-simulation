/* Hakan Eroglu
 * CS231A
 * Grid Based Search
 * Stack.java
 */
public class Stack<T> {
	/** Helper class */
	private class Node {
		private Node next;
		private T data;

		public Node(T t) {
			data = t;
			next = null;
		}

		public Node getNext() {
			return next;
		}

		public void setNext(Node n) {
			next = n;
		}

		public T getData() {
			return data;
		}
	}

	/** The top of the stack */
	Node head;
	/** Number of items on stack */
	int size;

	public Stack() {
		head = null;
		size = 0;
	}

	/** Add a new item to stack */
	public void push(T item) {
		Node nn = new Node(item);
		nn.setNext(head);
		head = nn;
		size++;
	}

	/** Return but not remove the item at the top of the stack */
	public T peek() {
		return head.getData();
	}

	/** Return and remove the item at the top of the stack */
	public T pop() {
		T toReturn = head.getData();
		head = head.getNext();
		size--;
		return toReturn;
	}

	/** Is the tsack empty? */
	public boolean isEmpty() {
		return size == 0;
	}

	/** Return the number of items on the stack */
	public int size() {
		return size;
	}
}
