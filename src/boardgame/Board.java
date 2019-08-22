package boardgame;

public class Board { //a classe board vai retornar apenas uma peça por vez
	
	private int rows; //quantidade de linhas do tabuleiro
	private int columns; //quantidade de colunas do tabuleiro
	private Piece[][] pieces;

	public Board(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}
	
	
	
	
}
