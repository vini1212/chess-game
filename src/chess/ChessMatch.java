package chess;

import boardgame.Board;

public class ChessMatch {

	private Board board;

	public ChessMatch() {
		board = new Board(8, 8);// quem tem que saber o tamanho do tabuleiro do xadrez é a classe ChessMatch
	}

	public ChessPiece[][] getPieces() { // vai ter que retornar uma matriz de peças de xadrez correspondente a partida,
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()]; // retorna ChessPiece porque estou fazendo um desenvolvimento em camada
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece)board.piece(i, j); //vai interpretar como uma peça de xadrez e não uma peça comum
			}
		}
		return mat;
	}
}
