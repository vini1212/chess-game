package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Rook extends ChessPiece{

	public Rook(Board board, Color color) {
		super(board, color); //vai repassar os dados para o construtor da superclasse
	}
	
	@Override
	public String toString() {
		return "R"; //est� sendo apenas uma letra porque na hora que imprimir o tabuleiro vai aparecer essa letra
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0, 0);
		
		//acima
		p.setValues(position.getRow() - 1, position.getColumn());
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true; // indicando que minha pe�a pode mover para l�
			p.setRow(p.getRow() - 1);
		}
		
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//esquerda
		p.setValues(position.getRow(), position.getColumn() - 1);
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true; // indicando que minha pe�a pode mover para l�
			p.setColumn(p.getColumn() - 1);
		}
				
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//direita
		p.setValues(position.getRow(), position.getColumn() + 1);
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true; // indicando que minha pe�a pode mover para l�
			p.setColumn(p.getColumn() + 1);
		}
				
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//baixo
		p.setValues(position.getRow() + 1, position.getColumn());
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true; // indicando que minha pe�a pode mover para l�
			p.setRow(p.getRow() + 1);
		}
				
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		return mat;
	}
	
}
