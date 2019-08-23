package chess;

import boardgame.BoardException;

public class ChessException extends BoardException{ //vai capturar tanto ChessException quanto poss�veis BoardException
	private static final long serialVersionUID = 1L;

	public ChessException(String msg) {
		super(msg);
	}
}
