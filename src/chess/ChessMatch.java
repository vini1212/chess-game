package chess;

import boardgame.Board;
import boardgame.Piece;
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
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);//essa opera��o vai ser respons�vel por validar essa posi��o de origem
		Piece capturedPiece = makeMove(source, target);
		return (ChessPiece)capturedPiece; //faz um downcasting pois minha vari�vel � to tipo Piece
	}
	
	private void validateSourcePosition(Position position) {
		if(!board.thereIsAPiece(position)) { //se n�o existir uma pe�a nessa posi��o vou fazer um tratamento de exce��o, nisso ela tamb�m vai ser uma exce��o de tabuleiro
			throw new ChessException("Nao existe peca na posicao de origem");
		}
		if (!board.piece(position).isThereAnyPossibleMove()) { //testando se n�o tem movimento poss�vel
			throw new ChessException("Nao existe movimentos possiveis para a peca escolhida");
		}
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source); //remove a pe�a de posi��o de origem
		Piece capturedPiece = board.removePiece(target); //removeu a poss�vel pe�a de posi��o de destino
		board.placePiece(p, target);
		return capturedPiece;
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition()); //tenho uma opera��o de colocar pe�a passando as posi��es de colocar pe�a passando as coordenadas do xadrez
	}
	
	private void initialSetup() { //esse m�todo vai ser respons�vel por colocar as pe�as no xadrez, ou seja, est� iniciando o tabuleiro
		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));

        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK));
	}
}
