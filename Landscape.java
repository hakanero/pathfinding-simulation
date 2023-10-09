
/* Hakan Eroglu
 * CS231A
 * Grid Based Search
 * Landscape.java
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.awt.Graphics;
import java.awt.Color;

public class Landscape {
	/** row count */
	private int rows;
	/** Column count */
	private int cols;
	/** Chance of a cell being obstacle */
	private double chance;
	/** Our starting cell */
	private Cell start;
	/** Our target cell */
	private Cell target;
	/** 2D Array of our cells */
	private Cell[][] cells;

	/** For visual purposes, highlights the cell we are checking neighbors for */
	private Cell curCell;

	public Landscape(int rows0, int cols0, double chance0) {
		rows = rows0;
		cols = cols0;
		chance = chance0;
		cells = new Cell[rows][cols];
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				cells[r][c] = new Cell(r, c);
			}
		}
		//randomize();
		maze();
	}

	/** Generates the board randomly */
	public void randomize() {
		Random random = new Random();
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (random.nextDouble() < chance)
					cells[r][c] = new Cell(r, c, Cell.Type.OBSTACLE);
				else
					cells[r][c] = new Cell(r, c, Cell.Type.FREE);
			}
		}
		// Make the start cell be from the first part of the board if the board is
		// divided into 16 parts
		start = cells[random.nextInt(rows / 4)][random.nextInt(cols / 4)];
		start.setType(Cell.Type.START);
		// Make the target cell be from the last part of the board if the board is
		// divided into 16 parts
		do {
			target = cells[random.nextInt(3 * rows / 4, rows)][random.nextInt(3 * cols / 4, cols)];
		} while (target.equals(start));
		target.setType(Cell.Type.TARGET);
	}

	/** Generate the board as a maze using Depth First Search */
	public void maze(){
		Random random = new Random();

		// Create walls around empty cells, this is necessary for this maze generation algorithm
		for (int r = 0; r < rows; r++) 
			for (int c = 0; c < cols; c++) {
				cells[r][c] = new Cell(r,c);
				if(r%2 == 1 || c%2 == 1)
					cells[r][c].setType(Cell.Type.OBSTACLE);
			}

		// Pick the start and target cells
		start = cells[0][0];
		start.setType(Cell.Type.START);
		
		target = cells[rows%2==0 ? rows-2 : rows-1][cols%2==0?cols-2:cols-1];
		target.setType(Cell.Type.TARGET);

		Stack<Cell> stack = new Stack<>();
		stack.push(start);

		ArrayList<int[]> directions = new ArrayList<int[]> 
		(Arrays.asList(new int[] { 0, 1 }, new int[] { 1, 0 }, new int[] { 0, -1 }, new int[] { -1, 0 }));

		while (!stack.isEmpty()){
			Cell cell = stack.pop();
			int[] direction;
			int r,c,r1,c1;
			for(int i = 0;i<4;i++){
				direction = directions.get(random.nextInt(4));
				r1 = cell.getRow() + direction[0];
				c1 = cell.getCol() + direction[1];
				r = r1 + direction[0];
				c = c1 + direction[1];
				if((r >= 0 && r < getRows() && c >= 0 && c < getCols())){
					if(cells[r][c].getType() != Cell.Type.FREE){
						if(cells[r][c].getType() != Cell.Type.TARGET)
							cells[r][c].setType(Cell.Type.FREE);
						stack.push(cells[r][c]);
						cells[r1][c1].setType(Cell.Type.FREE);
					}
				}
			}		
		}

	}

	/** Return the starting cell */
	public Cell getStart() {
		return start;
	}

	/** Return the target cell */
	public Cell getTarget() {
		return target;
	}

	/** Return no of rows */
	public int getRows() {
		return rows;
	}

	/** Return no of columns */
	public int getCols() {
		return cols;
	}

	/** Get a cell from the board */
	public Cell getCell(int row, int col) {
		return cells[row][col];
	}

	/** Reset all the cells into their initial state */
	public void reset() {
		for (Cell[] cells : cells) {
			for (Cell cell : cells) {
				cell.reset();
			}
		}
	}

	/** Returns all the neighbors of a cell -> top, left, right, bottom */
	public ArrayList<Cell> getNeighbors(Cell cell) {
		ArrayList<Cell> neighbors = new ArrayList<>();
		for (int[] directions : new int[][] { new int[] { 0, 1 }, new int[] { 1, 0 }, new int[] { 0, -1 },
				new int[] { -1, 0 } }) {
			int r = cell.getRow() + directions[0];
			int c = cell.getCol() + directions[1];
			if (r >= 0 && r < getRows() && c >= 0 && c < getCols())
				neighbors.add(cells[r][c]);
		}
		return neighbors;
	}

	/** Returns the distance the cell has to the target */
	public int distanceToTarget(Cell c) {
		return (int) (Math.pow(c.getRow() - getTarget().getRow(), 2) + Math.pow(c.getCol() - getTarget().getCol(), 2));
	}

	/**
	 * Sorts the neighbors by their distance to target and returns the neighbors
	 * list
	 */
	public ArrayList<Cell> getNeighborsWeighted(Cell cell) {
		ArrayList<Cell> neighbors = getNeighbors(cell);
		neighbors.sort((a, b) -> distanceToTarget(b) - distanceToTarget(a));
		return neighbors;
	}

	/** For visual purposes, interpolates a color between two colors */
	public static Color colorInterpolate(Color a, Color b, double t, boolean transparent) {
		int r = a.getRed() + (int) ((b.getRed() - a.getRed()) * t);
		int g = a.getGreen() + (int) ((b.getGreen() - a.getGreen()) * t);
		int bl = a.getBlue() + (int) ((b.getBlue() - a.getBlue()) * t);
		return new Color(r, g, bl, transparent ? 255 - (int) (t * 255) : 255);
	}

	public int getPathLength(Cell c) {
		int count = 0;
		Cell w = c;
		while (w != getStart()) {
			count++;
			w = w.getPrev();
		}
		return count;
	}

	/**
	 * For visual purposes, draws a path starting from the start to the given cell
	 */
	private int drawPathFromStart(Graphics g, int scale, Cell c) {
		Cell cur = c;
		int pathLength = 0;
		while (cur != getStart()) {
			pathLength++;
			cur = cur.getPrev();
		}
		cur = c;
		int i = 0;
		if (!getTarget().isVisited()) {
			g.setColor(Color.GREEN);
			g.fillOval(cur.getCol() * scale, cur.getRow() * scale, scale, scale);
		}
		while (cur != getStart()) {
			g.setColor(colorInterpolate(getTarget().isVisited() ? Color.RED : Color.GREEN, Color.BLUE,
					i * 1.0 / pathLength, false));
			int x0 = cur.getCol() * scale + scale / 2;
			int y0 = cur.getRow() * scale + scale / 2;
			int x1 = cur.getPrev().getCol() * scale + scale / 2;
			int y1 = cur.getPrev().getRow() * scale + scale / 2;
			int x = Math.min(x0, x1);
			int y = Math.min(y0, y1);
			int w = Math.abs(x0 - x1);
			int h = Math.abs(y0 - y1);
			g.fillRect(x - scale / 4, y - scale / 4, w + scale / 2, h + scale / 2);
			i++;
			cur = cur.getPrev();
		}
		return pathLength;
	}

	/** For visual purposes, to color the current cell we are checking */
	public void setCurCell(Cell c) {
		curCell = c;
	}

	/** Draw the board */
	public void draw(Graphics g, int scale) {
		g.setColor(Color.gray);
		CellQueue queue = new CellQueue();
		queue.offer(getStart());
		int x0, x1, y0, y1, x, y, w, h;

		/** Draw which direction that cell was visited */
		while (!queue.isEmpty()) {
			Cell cur = queue.poll();
			for (Cell neighbor : getNeighbors(cur)) {
				if (neighbor.getPrev() == cur) {
					queue.offer(neighbor);
					x0 = cur.getCol() * scale + scale / 2;
					y0 = cur.getRow() * scale + scale / 2;
					x1 = neighbor.getCol() * scale + scale / 2;
					y1 = neighbor.getRow() * scale + scale / 2;
					x = Math.min(x0, x1);
					y = Math.min(y0, y1);
					w = Math.abs(x0 - x1);
					h = Math.abs(y0 - y1);
					g.fillRect(x - scale / 6, y - scale / 6, w + scale / 3, h + scale / 3);

				}
			}
		}

		for (int r = 0; r < getRows(); r++) {
			for (int c = 0; c < getCols(); c++) {
				getCell(r, c).draw(g, scale, this);
			}
		}

		/** Draw the current path that was taken to visit the cell */
		if (curCell != null){
			g.setColor(Color.DARK_GRAY);
			g.drawRect(curCell.getCol() *scale, curCell.getRow() * scale, scale, scale);
			drawPathFromStart(g, scale, curCell);
			
		}

	}

}