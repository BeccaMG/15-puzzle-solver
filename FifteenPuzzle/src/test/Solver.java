import java.io.InputStreamReader;
import java.util.*;

class Solver{
    private int N ; 
    private int minMoves ;
    public static int[] correctRow;
    public static int[] correctCol;

    private class Node implements Comparable<Node>{
        private Board board ; 
        private int moves ; 
        private Node prevNode ; 
        public Node(Board board,int moves,Node prev){
            this.board = board ; 
            this.moves = moves ; 
            this.prevNode = prev ; 
        }
        public int compareTo(Node that){
            int thisPriority = this.moves+this.board.manhattan() ; 
            int thatPriority = that.moves+that.board.manhattan() ; 
            if(thisPriority<thatPriority){
                return -1 ; 
            }else if(thisPriority>thatPriority){
                return 1 ; 
            }else{
                return 0 ; 
            }
        }
    }

    private Node lastNode ; 
    private boolean solvable ; 

    public Solver(Board initial){
        N = initial.dimension() ; 
        PriorityQueue<Node> pq = new PriorityQueue<Node>() ; 
        PriorityQueue<Node> pq2 = new PriorityQueue<Node>() ; 
        pq.add(new Node(initial,0,null)) ; 
        System.out.println("So far so good");
        pq2.add(new Node(initial.twin(),0,null)) ; 
        while(true){
            Node removed = pq.poll();
            Node removed2 = pq2.poll();
            if(removed.board.isGoal()){
                minMoves = removed.moves ; 
                lastNode = removed ; 
                solvable = true ; 
                break ; 
            }
            if(removed2.board.isGoal()){
                minMoves = -1 ; 
                solvable = false ; 
                break ; 
            }

            Iterable<Board> neighbors = removed.board.neighbors() ; 
            Iterable<Board> neighbors2 = removed2.board.neighbors() ; 
            for(Board board : neighbors){
                if(removed.prevNode != null && removed.prevNode.board.equals(board) ){
                    continue ; 
                }
                pq.add(new Node(board,removed.moves+1,removed)) ; 
            }
            for(Board board : neighbors2){
                if(removed2.prevNode != null && removed2.prevNode.board.equals(board) ){
                    continue ; 
                }
                pq2.add(new Node(board,removed2.moves+1,removed2)) ; 
            }
        }
    }

    public boolean isSolvable(){
        return solvable ; 
    }

    public int moves(){
        return minMoves ; 
    }

    public Iterable<Board> solution(){
        if(!isSolvable()){
            return null ;
        }
        Stack<Board> stack = new Stack<Board>() ; 
        Node node = lastNode ; 
        while(true){
            if(node == null) break ; 
            Board board = node.board ; 
            node = node.prevNode ; 
            stack.push(board) ; 
        }
        return stack ; 
    }

    static void initCorrectRowsCols(int N){
        correctRow = new int[N*N] ; 
        int z = 0 ; 
        for(int i = 0 ; i < N ; i++ ){
            for(int j = 0 ; j < N ; j++ ){
                correctRow[z++] = i ; 
            }
        }
        z = 0 ; 
        correctCol = new int[N*N] ; 
        for(int i = 0 ; i < N ; i++ ){
            for(int j = 0 ; j < N ; j++ ){
                correctCol[z++] = j ; 
            }
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        // create initial board from file
        Scanner in = new Scanner(new InputStreamReader(System.in));
        int N = in.nextInt();
        initCorrectRowsCols(N);
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                blocks[i][j] = in.nextInt();
//                 blocks[i][j] = j+1+(i*N);
            }
            
//         blocks[N-1][N-1] = 0;
            
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(blocks[i][j] + " ");
            }
            System.out.println();
        }

        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        long end = System.currentTimeMillis();
        System.out.println("Time: " + (end-start)/1000 + " seconds");

        // print solution to standard output
        if (!solver.isSolvable())
            System.out.println("No solution possible");
        else {
            System.out.println("Minimum number of moves = " + solver.moves());
            Stack<Board> stack = new Stack<Board>();
            for (Board board : solver.solution())
                stack.push(board);
            while(!stack.isEmpty()){
                System.out.println(stack.pop());
            }
        }
    }
}

class Board{
    private int[][] array ; 
    private int N ;
    int emptyRow;
    int emptyCol;
    boolean reached;
    int manhattan = 0;

    public Board(int[][] blocks){
        N = blocks.length ; 
        array = new int[N][N] ;
        reached = true;
        for(int i = 0 ; i < N ; i++ ){
            for(int j = 0 ; j < N ; j++ ) {
                array[i][j] = blocks[i][j] ;
                if(array[i][j] == 0){
                    emptyRow = i;
                    emptyCol = j;
                }
                if(array[i][j] != N*i + j+1){
                    if(!(i==N-1 && j==N-1)){
                        reached = false;
                    }
                }
                int num = array[i][j] ; 
                if(num==0){
                    continue ; 
                }
                int indManhattan = Math.abs(Solver.correctRow[num-1] - i) 
                        + Math.abs(Solver.correctCol[num-1]-j) ; 
                manhattan += indManhattan ;
            }
        }
    }

    public int dimension(){
        return N; 
    }

    public int hamming(){
        int outOfPlace = 0 ; 
        for(int i = 0 ; i < N ; i++ ) {
            for(int j = 0 ; j < N ; j++ ){
                if(i==N-1 && j==N-1) {
                    break ; 
                }
                if(array[i][j] != i*N+j+1){
                    outOfPlace++ ; 
                }
            }
        }
        return outOfPlace ; 
    }

    public int manhattan(){
        return manhattan ; 
    }

    public boolean isGoal(){
        return reached ; 
    }

    public Board twin(){
        int[][] newArray = new int[N][N] ; 
        for(int i = 0 ; i < N ; i++ ){
            for(int j = 0 ; j < N ; j++ ){
                newArray[i][j] = array[i][j] ; 
            }
        }
//         for(int i = 0 ; i < 2 ; i++ ) {
//             if(newArray[i][0]==0 || newArray[i][5]==0){
//                 continue ; 
//             }
//                 int temp = newArray[i][0] ; 
//                 newArray[i][0] = newArray[i][6] ; 
//                 newArray[i][7] = temp ; 
//                 break ; 
// 
//         }
        return new Board(newArray) ; 
    }

    public boolean equals(Object y){
        if(y==this){
            return true ; 
        }
        if(y == null){
            return false ; 
        }
        if(y.getClass() != this.getClass()){
            return false ; 
        }
        Board that = (Board)y ; 
        if(that.array.length != this.array.length){
            return false ;
        }
        for(int i = 0 ; i < N ; i++ ) {
            for(int j =  0 ; j < N ; j++ ) {
                if(that.array[i][j] != this.array[i][j] ){
                    return false ; 
                }
            }
        }
        return true ; 
    }

    public Iterable<Board> neighbors(){
        Queue<Board> q = new ArrayDeque<Board>() ; 
        int firstIndex0 = 0 ; 
        int secondIndex0 = 0 ; 
        for(int i = 0 ; i < N ; i++ ){
            for(int j = 0 ; j < N ; j++ ) {
                if(array[i][j] == 0){
                    firstIndex0 = i ; 
                    secondIndex0 = j ; 
                    break ; 
                }
            }
        }
        if(secondIndex0-1>-1){
            int[][] newArr = getCopy() ; 
            exch(newArr,firstIndex0,secondIndex0,firstIndex0,secondIndex0-1) ; 
            q.add(new Board(newArr)) ; 
        }
        if(secondIndex0+1<N){
            int[][] newArr = getCopy() ; 
            exch(newArr,firstIndex0,secondIndex0,firstIndex0,secondIndex0+1) ; 
            q.add(new Board(newArr)) ; 
        }
        if(firstIndex0-1>-1){
            int[][] newArr = getCopy() ; 
            exch(newArr,firstIndex0,secondIndex0,firstIndex0-1,secondIndex0) ;     
            q.add(new Board(newArr)) ; 
        }
        if(firstIndex0+1<N){
            int[][] newArr = getCopy() ; 
            exch(newArr,firstIndex0,secondIndex0,firstIndex0+1,secondIndex0) ; 
            q.add(new Board(newArr)) ; 
        }
        return q ; 
    }

    private int[][] getCopy(){
        int[][] copy = new int[N][N] ; 
        for(int i = 0 ; i < N ; i++ ) {
            for(int j = 0 ; j < N ; j++ ){
                copy[i][j] = array[i][j] ; 
            }
        }
        return copy ; 
    }

    private void exch(int[][] arr, int firstIndex,int secIndex,int firstIndex2,int secIndex2){
        int temp = arr[firstIndex][secIndex] ; 
        arr[firstIndex][secIndex] = arr[firstIndex2][secIndex2] ;  
        arr[firstIndex2][secIndex2] = temp ; 
    }

    public String toString(){
        StringBuilder s = new StringBuilder() ; 
        s.append(N + "\n") ; 
        for(int i = 0 ; i < N ; i++ ){
            for(int j = 0 ; j  < N ; j++ ) {
                s.append(String.format("%4d",array[i][j])) ; 
            }
            s.append("\n") ; 
        }
        return s.toString() ; 
    }
}