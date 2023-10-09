/* Hakan Eroglu
 * CS231A
 * Grid Based Search
 * GridSearch.java
 */
public class GridSearch {
	/** Our landscape, which our cells are on */
	Landscape scape;
	/** Class that shows the landscape */
	LandscapeDisplay display;

	public GridSearch() {
		// If the current landscape is not solvable, find a new one
		do
			scape = new Landscape(34, 74, 0.3);
		while (!breadthFirstSearch(0));
		scape.reset();
		display = new LandscapeDisplay(scape, 10);
	}

	public static void main(String[] args) throws InterruptedException {
		GridSearch search = new GridSearch();
		// Runs the BFS algorithm with 25ms delay
		search.depthFirstSearch(100);
		search.scape.reset();
		Thread.sleep(1000);
		search.breadthFirstSearch(100);
		search.scape.reset();
		Thread.sleep(1000);
		search.DFSWeightedSearch(100);
	}

	/** Implementation of default DFS algorithm */
	public boolean depthFirstSearch(int delay) {
		Stack<Cell> stack = new Stack<>();
		scape.getStart().visitFrom(null);
		stack.push(scape.getStart());
		if (delay > 0)
			System.out.println("For DFS");
		while (!stack.isEmpty() && !scape.getTarget().isVisited()) {
			delayDraw(delay); // For visual purposes
			Cell cur = stack.pop();
			scape.setCurCell(cur); // For visual purposes
			for (Cell neigh : scape.getNeighbors(cur)) {
				if (neigh.getType() != Cell.Type.OBSTACLE && !neigh.isVisited()) {
					neigh.visitFrom(cur);
					delayDraw(delay); // For visual purposes
					if (neigh.getType() == Cell.Type.TARGET) {
						scape.setCurCell(neigh); // For visual purposes
						break;
					}
					stack.push(neigh);
				}
			}
		}
		// For visual purposes
		for (int i = 0; i < Cell.maxCallCountForColor; i++)
			delayDraw(delay);
		// return if the target is visited, meaning if the maze is solved or not
		return scape.getTarget().isVisited();
	}

	/** Implementation of default BFS algorithm */
	public boolean breadthFirstSearch(int delay) {
		CellQueue queue = new CellQueue();
		scape.getStart().visitFrom(null);
		queue.offer(scape.getStart());
		while (!queue.isEmpty() && !scape.getTarget().isVisited()) {
			delayDraw(delay); // For visual purposes
			Cell cur = queue.poll();
			scape.setCurCell(cur); // For visual purposes
			for (Cell neigh : scape.getNeighbors(cur)) {
				if (neigh.getType() != Cell.Type.OBSTACLE && !neigh.isVisited()) {
					neigh.visitFrom(cur);
					delayDraw(delay); // For visual purposes
					if (neigh.getType() == Cell.Type.TARGET) {
						scape.setCurCell(neigh); // For visual purposes
						break;
					}
					queue.offer(neigh);
				}
			}
		}
		// For visual purposes
		for (int i = 0; i < Cell.maxCallCountForColor; i++)
			delayDraw(delay);
		// return if target is visited, meaning the maze is solved
		return scape.getTarget().isVisited();
	}

	/** Implementation of DFS with weights, similar to A* algorithm */
	public boolean DFSWeightedSearch(int delay) {
		Stack<Cell> stack = new Stack<>();
		scape.getStart().visitFrom(null);
		stack.push(scape.getStart());
		while (!stack.isEmpty() && !scape.getTarget().isVisited()) {
			delayDraw(delay); // For visual purposes
			Cell cur = stack.pop();
			scape.setCurCell(cur); // For visual purposes
			for (Cell neigh : scape.getNeighborsWeighted(cur)) { // The only difference between DFS is in this line
				if (neigh.getType() != Cell.Type.OBSTACLE && !neigh.isVisited()) {
					neigh.visitFrom(cur);
					delayDraw(delay); // For visual purposes
					if (neigh.getType() == Cell.Type.TARGET) {
						scape.setCurCell(neigh); // For visual purposes
						break;
					}
					stack.push(neigh);
				}
			}
		}
		// For visual purposes
		for (int i = 0; i < Cell.maxCallCountForColor; i++)
			delayDraw(delay);
		// return if the target is visited, meaning if the maze is solved or not
		return scape.getTarget().isVisited();

	}

	/** Stops for some milliseconds and draws the landscape */
	private void delayDraw(int delay) {
		if (delay > 0) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			display.repaint();
		}
	}

}
