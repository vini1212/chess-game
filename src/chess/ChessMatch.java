package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;

	public ChessMatch() {
		board = new Board(8, 8);// quem tem que saber o tamanho do tabuleiro do xadrez � a classe ChessMatch
		initialSetup(); //na hora que for criada a partida � criado um tabuleiro 8x8 e � chamado o initialSetup
	}

	public ChessPiece[][] getPieces() { // vai ter que retornar uma matriz de pe�as de xadrez correspondente a partida,
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()]; // retorna ChessPiece porque estou fazendo um desenvolvimento em camada
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece)board.piece(i, j); //vai interpretar como uma pe�a de xadrez e n�o uma pe�a comum
			}
		}
		return mat;
	}
	
	private void initialSetup() { //esse m�todo vai ser respons�vel por colocar as pe�as no xadrez, ou seja, est� iniciando o tabuleiro
		board.placePiece(new Rook(board, Color.WHITE), new Position(2,1)); //o position � da camada de board 
		board.placePiece(new King(board, Color.BLACK), new Position(0,4));
		board.placePiece(new King(board, Color.WHITE), new Position(7,4));
	}
}
