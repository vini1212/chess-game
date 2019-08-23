package chess.pieces;

import boardgame.Board;
import chess.ChessPiece;
import chess.Color;

public class Rook extends ChessPiece{

	public Rook(Board board, Color color) {
		super(board, color); //vai repassar os dados para o construtor da superclasse
	}
	
	@Override
	public String toString() {
		return "R"; //está sendo apenas uma letra porque na hora que imprimir o tabuleiro vai aparecer essa letra
	}
	
}
