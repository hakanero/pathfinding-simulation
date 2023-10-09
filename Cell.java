
/* Hakan Eroglu
 * CS231A
 * Grid Based Search
 * Cell.java
 */
import java.awt.Graphics;
import java.awt.Color;

public class Cell {

	/** Types the cells can be */
	public enum Type {
		FREE, OBSTACLE, START, TARGET, NONDEFINED
	}

	/** HAs this cell been visitied? */
	private boolean visited;
	/** The cell which we visited this cell from */
	private Cell prev;
	/** The row and column of this cell */
	private int row, col;
	/** The type of this cell */
	private Type type;

	/** For visual purposes, for color change over time */
	private int drawCallsSinceVisited;
	/** For visual purposes, for color change over time */
	public static final int maxCallCountForColor = 100;

	public Cell(int row, int col, Type type) {
		visited = false;
		this.row = row;
		this.col = col;
		this.type = type;
		drawCallsSinceVisited = 0;
	}

	public Cell(int row, int col) {
		this(row, col, Type.NONDEFINED);
	}

	/** Which row is this cell in */
	public int getRow() {
		return row;
	}

	/** Which column is this cell in */
	public int getCol() {
		return col;
	}

	/** What type is this cell? */
	public Type getType() {
		return type;
	}

	/** Has this cell been visited? */
	public boolean isVisited() {
		return visited;
	}

	/** Return the cell we visited this cell from */
	public Cell getPrev() {
		return prev;
	}

	/** Visit this cell from another cell */
	public void visitFrom(Cell c) {
		this.prev = c;
		this.visited = true;
	}

	/** Make this cell unvisited */
	public void reset() {
		this.prev = null;
		this.visited = false;
	}

	/** Change type of this cell */
	public void setType(Type t) {
		this.type = t;
	}

	/** Draw the cell on board */
	public void draw(Graphics g, int scale, Landscape scape) {
		g.setColor(Color.BLACK);
		switch (getType()) {
			case FREE:
				g.setColor(Color.WHITE);
				break;
			case OBSTACLE:
				g.setColor(Color.BLACK);
				g.fillRect(getCol() * scale, getRow() * scale, scale, scale);
				break;
			case START:
				g.setColor(Color.BLUE);
				break;
			case TARGET:
				g.setColor(Color.RED);
				break;
			case NONDEFINED:
				g.setColor(Color.WHITE);
				break;
			default:
				g.setColor(Color.PINK);
				break;

		}
		// Draw a rectangle for cell if it is not an obstacle
		if (!isVisited() && getType() != Type.OBSTACLE){
			g.fillRect(getCol() * scale, getRow() * scale, scale, scale);
		}else if (isVisited() && drawCallsSinceVisited < maxCallCountForColor) {
		// Change color of cell to yellow when it is recently visited and gradually fade away
			drawCallsSinceVisited++;
			g.setColor(Landscape.colorInterpolate(Color.GREEN, Color.LIGHT_GRAY,
					drawCallsSinceVisited * 1.0 / maxCallCountForColor, true));
			g.fillRect(getCol() * scale, getRow() * scale, scale, scale);
		}
	}
}
