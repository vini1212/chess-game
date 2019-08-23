package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;

	public ChessMatch() {
		board = new Board(8, 8);// quem tem que saber o tamanho do tabuleiro do xadrez é a classe ChessMatch
		initialSetup(); //na hora que for criada a partida é criado um tabuleiro 8x8 e é chamado o initialSetup
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
	
	private void initialSetup() { //esse método vai ser responsável por colocar as peças no xadrez, ou seja, está iniciando o tabuleiro
		board.placePiece(new Rook(board, Color.WHITE), new Position(2,1)); //o position é da camada de board 
		board.placePiece(new King(board, Color.BLACK), new Position(0,4));
		board.placePiece(new King(board, Color.WHITE), new Position(7,4));
	}
}
