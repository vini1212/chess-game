package boardgame;

public class BoardException extends RuntimeException{
	private static final long serialVersionUID = 1L; //exce��o opcional de ser tratada
	
	public BoardException(String msg) {
		super(msg);
	}
}
