public class CellQueueTests {
	public static void main(String[] args) {
		CellQueue queue = new CellQueue();
		assert queue.isEmpty() : "problem in isempty ";
		queue.offer(new Cell(0, 1));
		assert queue.size() == 1 : "problem in size";
		assert queue.poll().getCol() == 1 : "problem in poll";
	}
}
