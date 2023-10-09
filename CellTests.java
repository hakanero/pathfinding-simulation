public class CellTests {
	public static void main(String[] args) {
		Cell cell0 = new Cell(0, 0);
		Cell cell1 = new Cell(10, 10, Cell.Type.OBSTACLE);
		assert cell0.isVisited() : "problem in isvisited";
		cell0.visitFrom(cell1);
		assert cell0.getPrev() == cell1 : "problem in visitfrom";
		cell1.setType(Cell.Type.START);
		assert cell1.getType() == Cell.Type.START : "problem in settype";
		assert cell1.getRow() == 10 : "problem in getRow";
		assert cell0.getCol() == 0 : "problem in getCol";
	}

}
