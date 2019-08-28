package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece{
	
	private ChessMatch chessMatch;

	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}
	
	@Override
	public String toString() {
		return "K";
	}
	
	private boolean canMove(Position position) { //método que vai dizer se o rei pode mover naquela posição
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p == null || p.getColor() != getColor(); //ou a casa ta vazia ou a casa tem uma peça adversária
	}
	
	private boolean testRookCastling(Position position) { //para testar se a torre ta apta para o roque
		ChessPiece p = (ChessPiece)getBoard().piece(position); //testar se nessa posição que eu informar se existe uma torre e está torre está apta para roque (quando a quantidade de movimentos é 0)
		return p != null && p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0; //instanceof indica se a peça é uma torre 
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0 , 0);
		
		//acima
		p.setValues(position.getRow() - 1, position.getColumn());
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//abaixo
		p.setValues(position.getRow() + 1, position.getColumn());
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//esquerda
		p.setValues(position.getRow(), position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//direita
		p.setValues(position.getRow(), position.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//noroesete
		p.setValues(position.getRow() - 1 , position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//nordeste
		p.setValues(position.getRow() - 1 , position.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//sudoeste
		p.setValues(position.getRow() + 1 , position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//sudeste
		p.setValues(position.getRow() + 1 , position.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// #movimneto especial de roque
		if(getMoveCount() == 0 && !chessMatch.getCheck()) {
			// $movimento especial de roque pequeno torre do canto direito 
			Position posT1 = new Position(position.getRow(), position.getColumn() + 3); //pega a posição onde deve estar a torre do rei
			if(testRookCastling(posT1)) {
				Position p1 = new Position(position.getRow(), position.getColumn() + 1); //casa da direita do rei
				Position p2 = new Position(position.getRow(), position.getColumn() + 2);
				if(getBoard().piece(p1) == null && getBoard().piece(p2) == null) {
					mat[position.getRow()][position.getColumn() + 2] = true;
				}
			}
			// $movimento especial de roque grande, do lado da rainha
			Position posT2 = new Position(position.getRow(), position.getColumn() - 4); //pega a posição onde deve estar a torre do rei
			if(testRookCastling(posT2)) { //testa se a torre é apta para roque
				Position p1 = new Position(position.getRow(), position.getColumn() - 1);
				Position p2 = new Position(position.getRow(), position.getColumn() - 2);
				Position p3 = new Position(position.getRow(), position.getColumn() - 3);
				if(getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3) == null) {
					mat[position.getRow()][position.getColumn() - 2] = true; //significa que meu rei pode mover duas casas para esquerda
				}
			}
		}
		
		return mat;
		
		
	}

}
