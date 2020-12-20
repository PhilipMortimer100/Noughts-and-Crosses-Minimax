/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package noughtsandcrossesdemo;


/**
 *
 * @author mortimer
 */
public class Game {
    int[][]board;
    int moveNumber;//stores the moveNumber that is to be made next starting at 1
    final int NOUGHT_VALUE=1;
    final int CROSS_VALUE=-1;
    final int EMPTY_SPACE_VALUE=0;
    final int WIN_FOR_NOUGHT_VALUE=1;//win for nought
    final int DRAW_VALUE=0;
    final int LOSS_FOR_NOUGHT_VALUE=-1;//loss for nought
    final int GAME_STILL_GOING_VALUE=2;
    boolean gameIsOver;
    /**
     * Creates a new game and initialises game variables
     */
    public Game(){
        board=new int[3][3];
        moveNumber=1;
        gameIsOver=false;

    }
    /**
     * Adds move to board
     * @param yThenXIndex int array with 0 element storing y index, 1 element storing x index
     * @return returns game status
     */
    public int addMove(int []yThenXIndex){
        if(gameIsOver==false&&board[yThenXIndex[0]][yThenXIndex[1]]==0){
            this.board[yThenXIndex[0]][yThenXIndex[1]]=((this.moveNumber%2)*2)-1;
            moveNumber++;
        }
        return this.gameStatus();
    }
    /**
     * 
     * @param movesAvailable 2d int array with each row storing the current state after the move, y co ord of move and x coord of move
     * @param alpha alpha value
     * @param beta beta value
     * @return returns the move that results in the lowest value terminal game state. Uses an alpha-beta pruned minimax algorithm
     */
    private int[]minC(int[][]movesAvailable,int alpha, int beta){//moves available 0 el is game status 1 is 2 is x
        int minVal=3;
        int currentVal;
        int[][]nextMoves;
        int best[]=new int[3];
        int indexOfBest=-1;
        for(int y=0;y<movesAvailable.length;y++){
            boolean gameBefore=gameIsOver;
            board[movesAvailable[y][1]][movesAvailable[y][2]]=getSymbol();
            moveNumber++;
            currentVal=gameStatus();
            if(currentVal==GAME_STILL_GOING_VALUE){
                nextMoves=getAllAvailableMovesAndStatusMinimx();
                movesAvailable[y][0]=maxC(nextMoves,alpha,beta)[0];
            }else{
               movesAvailable[y][0]=currentVal;
            }
            if(movesAvailable[y][0]<minVal){
                minVal=movesAvailable[y][0];
                indexOfBest=y;
            }
            gameIsOver=gameBefore;
            moveNumber=moveNumber-1;
            board[movesAvailable[y][1]][movesAvailable[y][2]]=EMPTY_SPACE_VALUE;
            if(minVal<=alpha){
                best[0]=minVal;best[1]=movesAvailable[indexOfBest][1];best[2]=movesAvailable[indexOfBest][2];
                return best;
            }
            if(minVal<beta){
                beta=minVal;
            } 
        }
        best[0]=minVal;best[1]=movesAvailable[indexOfBest][1];best[2]=movesAvailable[indexOfBest][2];
        return best;
    }
    /**
     * 
     * @param movesAvailable 2d int array with each row storing the current state after the move, y co ord of move and x coord of move
     * @param alpha alpha value
     * @param beta beta value
     * @return returns the move that results in the greatest value terminal game state. Uses an alpha-beta pruned minimax algorithm
     */
    private int[]maxC(int[][]movesAvailable,int alpha, int beta){//moves available 0 el is game status 1 is 2 is x
        int maxVal=-3;
        int currentVal;
        int[][]nextMoves;
        int best[]=new int[3];
        int indexOfBest=-1;
        for(int y=0;y<movesAvailable.length;y++){
            boolean gameBefore=gameIsOver;
            board[movesAvailable[y][1]][movesAvailable[y][2]]=getSymbol();
            moveNumber++;
            currentVal=gameStatus();
            if(currentVal==GAME_STILL_GOING_VALUE){
                nextMoves=getAllAvailableMovesAndStatusMinimx();
                movesAvailable[y][0]=minC(nextMoves,alpha,beta)[0];
            }else{
               movesAvailable[y][0]=currentVal;
            }
            if(movesAvailable[y][0]>maxVal){
                maxVal=movesAvailable[y][0];
                indexOfBest=y;
            }
            moveNumber=moveNumber-1;
            gameIsOver=gameBefore;
            board[movesAvailable[y][1]][movesAvailable[y][2]]=EMPTY_SPACE_VALUE;
            if(maxVal>=beta){
                best[0]=maxVal;best[1]=movesAvailable[indexOfBest][1];best[2]=movesAvailable[indexOfBest][2];
                return best;
            }
            if(maxVal>alpha){
                alpha = maxVal;
            }
        }
        best[0]=maxVal;best[1]=movesAvailable[indexOfBest][1];best[2]=movesAvailable[indexOfBest][2];
        return best;
    }
    /**
     * gets the best possible move using the minimax algorithm
     * @return the best possible move (o element is y coord and 1 element is xcoord of move)
     */
    public int[]getBestMove(){
        int ret[]=new int[2];
        if(moveNumber%2==0){
            int[]ans=minC(getAllAvailableMovesAndStatusMinimx(),-3,3);
            ret[0]=ans[1];ret[1]=ans[2];
        }else{
            int[]ans=maxC(getAllAvailableMovesAndStatusMinimx(),-3,3);
            ret[0]=ans[1];ret[1]=ans[2];
        }
        return ret;
    }
    /**
     * Gets the current next piece to be placed (i.e. noughts or crosses) given the move number
     * @return the piece to be played next
     */
    private int getSymbol(){
        if(moveNumber%2==0){//first move
            return CROSS_VALUE;
        }else{
            return NOUGHT_VALUE;
        }
    }
    /**
     * Prints the board in integer form
     */
    public  void printBoard(){
        for(int y =2;y>=0;y=y-1){
            for(int x  =0;x<3;x++){
                System.out.print(board[y][x]+",");
            }
            System.out.println("");
        }
    }
    /**
     * Gets all available moves and the board status after said move
     * @return all available moves and board status. 0 element in row is status, 1st element is y, 2 nd element is x
     */
    private int[][]getAllAvailableMovesAndStatusMinimx(){
        int availableMoves[][]=new int[10-moveNumber][3];
      //  printBoard();
        int add=getSymbol();
        
       // System.out.println("next symbol "+add);
        int index=0;
        for(int y=0;y<board.length;y++){
            for(int x =0;x<board[0].length;x++){
                if(board[y][x]==EMPTY_SPACE_VALUE){
                    board[y][x]=add;
                    moveNumber++;
                    boolean gameBefore=gameIsOver;
                    availableMoves[index][0]=gameStatus();
                    board[y][x]=EMPTY_SPACE_VALUE;
                    gameIsOver=gameBefore;
                    moveNumber=moveNumber-1;
                    availableMoves[index][1]=y;
                    availableMoves[index][2]=x;
                    index++;
                }
            }
        }
        return availableMoves;
    }
    /**
     * Gets all availabale moves
     * @return each row represents one available move (nought element is y coord, 1 st element is x coord).
     */
    public int[][]getAllAvailableMoves(){
        int availableMoves[][]=new int[10-moveNumber][2];
        int index=0;
        for(int y=0;y<board.length;y++){
            for(int x =0;x<board[0].length;x++){
                if(board[y][x]==EMPTY_SPACE_VALUE){
                    availableMoves[index][0]=y;
                    availableMoves[index][1]=x;
                    index++;
                }
            }
        }
        return availableMoves;
    }
    /**
     * Returns the current move number
     * @return The current move number
     */
    public int getMoveNumber(){
        return moveNumber;
    }
    /**
     * Returns the current board
     * @return the current board
     */
    public int[][]getBoard(){
        return board;
    }
    /**
     * Returns the current game status
     * @return 2  is returned if the board is at a non-terminal state. 1 is returned noughts win, 0 is returned if its a draw and -1 is returned if crosses win
     */
    public int gameStatus(){//returns 0 if draw, -1 if win for player two, 1 if win for player 2 and a value of 2 if the game is still ongoing
        boolean botLToTopRDiagWinNought=true;
        boolean botLToTopRDiagWinCross=true;
        boolean otherDiagWinNought =true;
        boolean otherDiagWinCross=true;
        boolean allSquaresFilled=true;
        for(int y=0;y<board.length;y++){
            if(board[2-y][y]!=NOUGHT_VALUE){
                otherDiagWinNought=false;
            }
            if(board[2-y][y]!=CROSS_VALUE){
                otherDiagWinCross=false;
            }
            if(board[y][y]!=NOUGHT_VALUE){
                botLToTopRDiagWinNought=false;
            }
            if(board[y][y]!=CROSS_VALUE){
                botLToTopRDiagWinCross=false;
            }
            boolean acrossWinNought=true;
            boolean acrossWinCross=true;
            for(int x=0;x<board[0].length;x++){
                if(board[y][x]==EMPTY_SPACE_VALUE){
                    allSquaresFilled=false;
                }
                if(board[y][x]!=NOUGHT_VALUE){
                    acrossWinNought=false;
                }
                if(board[y][x]!=CROSS_VALUE){
                    acrossWinCross=false;
                }
                boolean upWinNought=true;
                boolean upWinCross=true;
                for(int yVal=0;yVal<board.length;yVal++){
                    if(board[yVal][x]!=NOUGHT_VALUE){
                        upWinNought=false;
                    }
                    if(board[yVal][x]!=CROSS_VALUE){
                        upWinCross=false;
                    }
                }
                if(upWinNought==true){
                    gameIsOver=true;
                    return WIN_FOR_NOUGHT_VALUE;
                }
                if(upWinCross==true){
                    gameIsOver=true;
                    return LOSS_FOR_NOUGHT_VALUE;
                }
            }
            if(acrossWinCross==true){
                gameIsOver=true;
                return LOSS_FOR_NOUGHT_VALUE;
            }
            else if(acrossWinNought){
                gameIsOver=true;
                return WIN_FOR_NOUGHT_VALUE;
            }
        }
        if(botLToTopRDiagWinCross==true){
            gameIsOver=true;
            return LOSS_FOR_NOUGHT_VALUE;
        }
        if(botLToTopRDiagWinNought==true){
            gameIsOver=true;
            return WIN_FOR_NOUGHT_VALUE;
        }
        if(otherDiagWinCross==true){
            gameIsOver=true;
            return LOSS_FOR_NOUGHT_VALUE;
        }
        if(otherDiagWinNought==true){
            gameIsOver=true;
            return WIN_FOR_NOUGHT_VALUE;       
        }
        if(gameIsOver==false&&allSquaresFilled==true){
            gameIsOver=true;
            return DRAW_VALUE;
        }else{
            return GAME_STILL_GOING_VALUE;
        }
    }
}
