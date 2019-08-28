package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;
	
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
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
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
		
		if(testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("Voce nao pode se colocar em check");
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		if(testCheckMate(opponent(currentPlayer))) { //vai testar se o jogo acabou, se o oponente da peça que mexeu ficou em checkMate a variável vai passar a ser true
			checkMate = true;
		}
		else {
			nextTurn();
		}
		
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
	
	private Color opponent(Color color) { //dado uma cor retorna um oponente de cada cor
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList()); //filtrar uma lista com expressões lambidas 
		for(Piece p : list) { //pode ser que percorra minha lista e nenhuma das peças é um rei sendo assim precisando lançar uma exceção
			if(p instanceof King) {
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("Nao existe o rei " + color + " no tabuleiro"); //isso é para nunca acontecer 
	}
	
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(source); //remove a peça de posição de origem
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target); //removeu a possível peça de posição de destino
		board.placePiece(p, target); //aqui é feito um upcasting naturalmente
		
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece); //adicionando a peça capturada na lista de capturedPieces
		}
		
		// #movimento especial do roque pequeno do rei
		if(p instanceof King && target.getColumn() == source.getColumn() + 2) { // significa que o rei andou duas casas para direita foi um roque pequeno
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3); // pega a posição que está a 3 casas a direita do rei
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		
		// #movimento especial do roque grande da rainha
		if(p instanceof King && target.getColumn() == source.getColumn() - 2) { // significa que o rei andou duas casas para direita foi um roque pequeno
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4); // pega a posição que está a 3 casas a direita do rei
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount(); //incrementa a quantidade de movimentos dela
		}
		return capturedPiece;
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);
		
		if(capturedPiece != null) { //retorna a jogada para o lugar de origem
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
		
		// #desfazendo o movimento do roque pequeno
		if(p instanceof King && target.getColumn() == source.getColumn() + 2) { // significa que o rei andou duas casas para direita foi um roque pequeno
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3); // pega a posição que está a 3 casas a direita do rei
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
		
		// #movimento especial do roque grande da rainha
		if(p instanceof King && target.getColumn() == source.getColumn() - 2) { // significa que o rei andou duas casas para direita foi um roque pequeno
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4); // pega a posição que está a 3 casas a direita do rei
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetT); // retira a torre do lugar de destino
			board.placePiece(rook, sourceT); // volta a torre no lugar de origem
			rook.decreaseMoveCount(); //decrementa a quantidade de movimentos dela
		}
	}
	
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();//pego a peça do meu rei no formato de matriz
		List <Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for(Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves(); //tenho a matriz de movimentos possivei dessa peça adversária p
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) { //se tiver na mesma posição significa que o rei está em check
				return true;
			}
		}
		return false; //se esgotar o for o rei não está em check 
	}
	
	private boolean testCheckMate(Color color) {
		if(!testCheck(color)) {
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList()); //pega todas as peças do tabuleiro e filtra todos as peças que forem igual a essa cor
		for (Piece p : list) { //se existir alguma peça P nessa lista que retire o movimento do check ai retorno falso, caso esgote o for e não encontre nenhum movimento possível vai retornar true
			boolean [][] mat = p.possibleMoves();
			for (int i = 0; i < board.getRows(); i++) {
				for(int j = 0; j < board.getColumns(); j++) {
					if(mat[i][j]) { //se estiver no i e no j que é possível, vai pegar a peça P vai mover para a posição que é movimento possível e ai vou verificar o Check ainda se não ele vai retornar false
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source,target); 
						boolean testCheck = testCheck(color); //vai testar se ainda o rei da minha cor está em check
						undoMove(source, target, capturedPiece); //tem que retornar o movimento se não vai dar erro no tabuleiro
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;		
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition()); //tenho uma operação de colocar peça passando as posições de colocar peça passando as coordenadas do xadrez
		piecesOnTheBoard.add(piece);
	}
	
	private void initialSetup() { //esse método vai ser responsável por colocar as peças no xadrez, ou seja, está iniciando o tabuleiro
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE));
        
        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK));
	}
}
