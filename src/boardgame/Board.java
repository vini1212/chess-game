package boardgame;

public class Board { //a classe board vai retornar apenas uma pe�a por vez
	
	private int rows; //quantidade de linhas do tabuleiro
	private int columns; //quantidade de colunas do tabuleiro
	private Piece[][] pieces;

	public Board(int rows, int columns) {
		if(rows < 1 || columns < 1) {
			throw new BoardException("Erro criando tabuleiro: � necess�rio que contenha no m�nimo 1 linha e 1 coluna"); //exce��o personalizada
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public Piece piece(int row, int column) {
		if (!positionExists(row, column)) {
			throw new BoardException("Posi��o n�o est� no tabuleiro");
		}
		return pieces[row][column];
	}
	
	public Piece piece(Position position) { //vai ter que retornar a pe�a em rela��o a uma posi��o
		if (!positionExists(position)) {
			throw new BoardException("Posi��o n�o est� no tabuleiro");
		}
		return pieces[position.getRow()][position.getColumn()]; //vai retornar a pe�a que estiver nessa matriz na dada posi��o
	}
	
	public void placePiece(Piece piece, Position position) {
		if(thereIsAPiece(position)) {
			throw new BoardException("J� existe uma pe�a nessa posi��o " + position);
		}
		pieces[position.getRow()][position.getColumn()] = piece; //matriz de pe�as que foi declarada no tabuleiro, fazendo a matriz na posi��o dada e atribuir a ela a pe�a que informei
		piece.position = position; //n�o est� sendo atribuida como nula mais ela � acessivel diretamente porque foi colocada como protected
	}
	
	private boolean positionExists(int row, int column) { //dentro da classe vai ter um momento que vai ser mais f�cil testar pela linha e pela coluna do que pela posi��o
		return row >= 0 && row < rows && column >= 0 && column < columns; //ela existe quando a posi��o est� dentro do tabuleiro, condi��o completa para ver se uma posi��o existe
	}
	
	public boolean positionExists(Position position) {
		return positionExists(position.getRow(), position.getColumn());
	}
	
	public boolean thereIsAPiece(Position position) {
		if (!positionExists(position)) { //antes de testar se cont�m uma pe�a na posi��o ele testa se a posi��o informada existe
			throw new BoardException("Posi��o n�o est� no tabuleiro"); //se n�o existir ele para e j� aparece a exce��o
		}
		return piece(position) != null; //se for diferente de nulo significa que tem uma pe�a nessa posi��o
	}
	
	
	
	
}
