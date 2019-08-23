package boardgame;

public abstract class Piece {

	protected Position position;
	private Board board;

	public Piece(Board board) {
		this.board = board;
		position = null;
	}

	protected Board getBoard() { // somente classes dentro do mesmo pacote e subclasses que v�o poder acessar o
									// tabuleiro de uma pe�a
		return board;
	}

	public abstract boolean[][] possibleMoves();

	public boolean possibleMove(Position position) { // m�todo que faz um gancho com a subclasse hookmethod, isso �
														// totalmente possivel em POO
		return possibleMoves()[position.getRow()][position.getColumn()]; // uma classe concreta que implementa a
																			// opera��o abstrata
	}

	public boolean isThereAnyPossibleMove() {
		boolean[][] mat = possibleMoves();
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat.length; j++) {
				if (mat[i][j]) {
					return true;
				}
			}
		}
		return false;
	}

}
