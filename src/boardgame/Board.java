package boardgame;

public class Board { //a classe board vai retornar apenas uma pe�a por vez
	
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
	
	public Piece piece(int row, int column) {
		return pieces[row][column];
	}
	
	public Piece piece(Position position) { //vai ter que retornar a pe�a em rela��o a uma posi��o
		return pieces[position.getRow()][position.getColumn()];
	}
	
	public void placePiece(Piece piece, Position position) {
		pieces[position.getRow()][position.getColumn()] = piece; //matriz de pe�as que foi declarada no tabuleiro, fazendo a matriz na posi��o dada e atribuir a ela a pe�a que informei
		piece.position = position; //n�o est� sendo atribuida como nula mais ela � acessivel diretamente porque foi colocada como protected
	}
	
	
	
	
}
