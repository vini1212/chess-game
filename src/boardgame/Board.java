package boardgame;

public class Board { //a classe board vai retornar apenas uma peça por vez
	
	private int rows; //quantidade de linhas do tabuleiro
	private int columns; //quantidade de colunas do tabuleiro
	private Piece[][] pieces;

	public Board(int rows, int columns) {
		if(rows < 1 || columns < 1) {
			throw new BoardException("Erro criando tabuleiro: necessário que contenha no minimo 1 linha e 1 coluna"); //exceção personalizada
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
			throw new BoardException("Posicao nao esta no tabuleiro");
		}
		return pieces[row][column];
	}
	
	public Piece piece(Position position) { //vai ter que retornar a peça em relação a uma posição
		if (!positionExists(position)) {
			throw new BoardException("Posicao nao esta no tabuleiro");
		}
		return pieces[position.getRow()][position.getColumn()]; //vai retornar a peça que estiver nessa matriz na dada posição
	}
	
	public void placePiece(Piece piece, Position position) {
		if(thereIsAPiece(position)) {
			throw new BoardException("Ja existe uma peca nessa posicao " + position);
		}
		pieces[position.getRow()][position.getColumn()] = piece; //matriz de peças que foi declarada no tabuleiro, fazendo a matriz na posição dada e atribuir a ela a peça que informei
		piece.position = position; //não está sendo atribuida como nula mais ela é acessivel diretamente porque foi colocada como protected
	}
	
	public Piece removePiece(Position position) {
		if(!positionExists(position)) {
			throw new BoardException("Posicao nao esta no tabuleiro");
		}
		if (piece(position) == null) {
			return null;
		}
		Piece aux = piece(position);
		aux.position = null; // peça foi retirada do tabuleiro
		pieces[position.getRow()][position.getColumn()] = null; //não tem mais peça nessa posição da matriz
		return aux;
	}
	
	private boolean positionExists(int row, int column) { //dentro da classe vai ter um momento que vai ser mais fácil testar pela linha e pela coluna do que pela posição
		return row >= 0 && row < rows && column >= 0 && column < columns; //ela existe quando a posição está dentro do tabuleiro, condição completa para ver se uma posição existe
	}
	
	public boolean positionExists(Position position) {
		return positionExists(position.getRow(), position.getColumn());
	}
	
	public boolean thereIsAPiece(Position position) {
		if (!positionExists(position)) { //antes de testar se contém uma peça na posição ele testa se a posição informada existe
			throw new BoardException("Posicao nao esta no tabuleiro"); //se não existir ele para e já aparece a exceção
		}
		return piece(position) != null; //se for diferente de nulo significa que tem uma peça nessa posição
	}
	
	
	
	
}
