package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();
		List <ChessPiece> captured = new ArrayList<>();
		
		while(!chessMatch.getCheckMate()) { //terminou o enquanto quer dizer que aconteceu o checkmate
			try {
				UI.clearScreen();
				UI.printMatch(chessMatch, captured);
				System.out.println();
				System.out.print("Origem: ");
				ChessPosition source = UI.readChessPosition(sc);
				boolean[][] possibleMoves = chessMatch.possibleMoves(source);
				UI.clearScreen();
				UI.printBoard(chessMatch.getPieces(), possibleMoves); //vai ser responsável por imprimir o tabuleiro só que colorindo os possiveis movimentos			
				System.out.println();
				System.out.print("Destino: ");
				ChessPosition target = UI.readChessPosition(sc);
				
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
				
				if (capturedPiece != null) { //sempre quando retornar um movimento e esse retorno resultar uma peça capturada vou adicionar na lista de peças capturadas
					captured.add(capturedPiece);
				}
				
				if (chessMatch.getPromoted() != null) { //significa que a peça foi promovida
					System.out.print("Entre com a peca a ser promovida (B/N/R/Q): ");
					String type = sc.nextLine();
					chessMatch.replacePromotedPiece(type); //esse jeito é mais fácil de interagir com o usuário sem perder a integridade do xadrez
				}
			}
			catch (ChessException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		UI.clearScreen();
		UI.printMatch(chessMatch, captured);
	}

}
