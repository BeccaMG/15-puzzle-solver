
public class Solver {
    Puzzle puzzle;
    Solver(Puzzle init){
        //call ff , solve the whole puzzle, gets next move or whatever
        this.puzzle = init;
    }

    /**
     * Out Of Place fitness function
     * for each piece in place the degree adds 0.0625
     * @return The degree of the current puzzle as calculated by the oop
     */
    public double oop_fitness(){ //not object oriented programming :D
        double degree = 1.0;
        for(int i=0;i<16;i++){
            if(puzzle.puzzle_array[i]!=i+1 && i!=15)
                degree-=0.0625;
            else if(puzzle.puzzle_array[i]!=0 && i==15)
                degree-=0.0625;
        }
        return degree;
    }
}
