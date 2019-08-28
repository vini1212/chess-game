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
		board = new Board(8, 8);// quem tem que saber o tamanho do tabuleiro do xadrez � a classe ChessMatch
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup(); //na hora que for criada a partida � criado um tabuleiro 8x8 e � chamado o initialSetup
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
	
	public ChessPiece[][] getPieces() { // vai ter que retornar uma matriz de pe�as de xadrez correspondente a partida,
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()]; // retorna ChessPiece porque estou fazendo um desenvolvimento em camada
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece)board.piece(i, j); //vai interpretar como uma pe�a de xadrez e n�o uma pe�a comum
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
		validateSourcePosition(source);//essa opera��o vai ser respons�vel por validar essa posi��o de origem
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		
		if(testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("Voce nao pode se colocar em check");
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		if(testCheckMate(opponent(currentPlayer))) { //vai testar se o jogo acabou, se o oponente da pe�a que mexeu ficou em checkMate a vari�vel vai passar a ser true
			checkMate = true;
		}
		else {
			nextTurn();
		}
		
		return (ChessPiece)capturedPiece; //faz um downcasting pois minha vari�vel � to tipo Piece
	}
	
	private void validateSourcePosition(Position position) {
		if(!board.thereIsAPiece(position)) { //se n�o existir uma pe�a nessa posi��o vou fazer um tratamento de exce��o, nisso ela tamb�m vai ser uma exce��o de tabuleiro
			throw new ChessException("Nao existe peca na posicao de origem");
		}
		if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
			throw new ChessException("A peca escolhida nao e sua");
		}
		if (!board.piece(position).isThereAnyPossibleMove()) { //testando se n�o tem movimento poss�vel
			throw new ChessException("Nao existe movimentos possiveis para a peca escolhida");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) { //para pe�a de origem a posi��o de destino n�o � um movimento de poss�vel ent�o n�o pode se mover para l�
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
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList()); //filtrar uma lista com express�es lambidas 
		for(Piece p : list) { //pode ser que percorra minha lista e nenhuma das pe�as � um rei sendo assim precisando lan�ar uma exce��o
			if(p instanceof King) {
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("Nao existe o rei " + color + " no tabuleiro"); //isso � para nunca acontecer 
	}
	
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(source); //remove a pe�a de posi��o de origem
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target); //removeu a poss�vel pe�a de posi��o de destino
		board.placePiece(p, target); //aqui � feito um upcasting naturalmente
		
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece); //adicionando a pe�a capturada na lista de capturedPieces
		}
		
		// #movimento especial do roque pequeno do rei
		if(p instanceof King && target.getColumn() == source.getColumn() + 2) { // significa que o rei andou duas casas para direita foi um roque pequeno
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3); // pega a posi��o que est� a 3 casas a direita do rei
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		
		// #movimento especial do roque grande da rainha
		if(p instanceof King && target.getColumn() == source.getColumn() - 2) { // significa que o rei andou duas casas para direita foi um roque pequeno
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4); // pega a posi��o que est� a 3 casas a direita do rei
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
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3); // pega a posi��o que est� a 3 casas a direita do rei
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
		
		// #movimento especial do roque grande da rainha
		if(p instanceof King && target.getColumn() == source.getColumn() - 2) { // significa que o rei andou duas casas para direita foi um roque pequeno
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4); // pega a posi��o que est� a 3 casas a direita do rei
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetT); // retira a torre do lugar de destino
			board.placePiece(rook, sourceT); // volta a torre no lugar de origem
			rook.decreaseMoveCount(); //decrementa a quantidade de movimentos dela
		}
	}
	
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();//pego a pe�a do meu rei no formato de matriz
		List <Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for(Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves(); //tenho a matriz de movimentos possivei dessa pe�a advers�ria p
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) { //se tiver na mesma posi��o significa que o rei est� em check
				return true;
			}
		}
		return false; //se esgotar o for o rei n�o est� em check 
	}
	
	private boolean testCheckMate(Color color) {
		if(!testCheck(color)) {
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList()); //pega todas as pe�as do tabuleiro e filtra todos as pe�as que forem igual a essa cor
		for (Piece p : list) { //se existir alguma pe�a P nessa lista que retire o movimento do check ai retorno falso, caso esgote o for e n�o encontre nenhum movimento poss�vel vai retornar true
			boolean [][] mat = p.possibleMoves();
			for (int i = 0; i < board.getRows(); i++) {
				for(int j = 0; j < board.getColumns(); j++) {
					if(mat[i][j]) { //se estiver no i e no j que � poss�vel, vai pegar a pe�a P vai mover para a posi��o que � movimento poss�vel e ai vou verificar o Check ainda se n�o ele vai retornar false
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source,target); 
						boolean testCheck = testCheck(color); //vai testar se ainda o rei da minha cor est� em check
						undoMove(source, target, capturedPiece); //tem que retornar o movimento se n�o vai dar erro no tabuleiro
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
		board.placePiece(piece, new ChessPosition(column, row).toPosition()); //tenho uma opera��o de colocar pe�a passando as posi��es de colocar pe�a passando as coordenadas do xadrez
		piecesOnTheBoard.add(piece);
	}
	
	private void initialSetup() { //esse m�todo vai ser respons�vel por colocar as pe�as no xadrez, ou seja, est� iniciando o tabuleiro
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
