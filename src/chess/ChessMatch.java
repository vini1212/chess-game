package chess;

import java.util.ArrayList;
import java.util.List;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn;
	private Color currentPlayer;
	private Board board;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	public ChessMatch() {
		board = new Board(8, 8);// quem tem que saber o tamanho do tabuleiro do xadrez é a classe ChessMatch
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup(); //na hora que for criada a partida é criado um tabuleiro 8x8 e é chamado o initialSetup
	}
	
	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
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
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves(); 
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);//essa operação vai ser responsável por validar essa posição de origem
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		nextTurn();
		return (ChessPiece)capturedPiece; //faz um downcasting pois minha variável é to tipo Piece
	}
	
	private void validateSourcePosition(Position position) {
		if(!board.thereIsAPiece(position)) { //se não existir uma peça nessa posição vou fazer um tratamento de exceção, nisso ela também vai ser uma exceção de tabuleiro
			throw new ChessException("Nao existe peca na posicao de origem");
		}
		if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
			throw new ChessException("A peca escolhida nao e sua");
		}
		if (!board.piece(position).isThereAnyPossibleMove()) { //testando se não tem movimento possível
			throw new ChessException("Nao existe movimentos possiveis para a peca escolhida");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) { //para peça de origem a posição de destino não é um movimento de possível então não pode se mover para lá
			throw new ChessException("A peca escolhida nao pode ser movida para a posicao de destino");
		}
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source); //remove a peça de posição de origem
		Piece capturedPiece = board.removePiece(target); //removeu a possível peça de posição de destino
		board.placePiece(p, target);
		
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece); //adicionando a peça capturada na lista de capturedPieces
		}
		return capturedPiece;
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition()); //tenho uma operação de colocar peça passando as posições de colocar peça passando as coordenadas do xadrez
		piecesOnTheBoard.add(piece);
	}
	
	private void initialSetup() { //esse método vai ser responsável por colocar as peças no xadrez, ou seja, está iniciando o tabuleiro
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
