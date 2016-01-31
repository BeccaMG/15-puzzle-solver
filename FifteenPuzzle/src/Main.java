import java.util.Arrays;

public class Main {
	static Puzzle p = new Puzzle(6);

	public static void main(String[] args){
		//1 = up, 2 = down, 3 = right, 4 left
		System.out.println(p.toString());
		p.shuffle(10000);
		System.out.println(Arrays.toString(p.puzzle_array));
		System.out.println(p.toString());
        System.out.print(p.isSolvable());
		Solver s = new Solver(p);
		System.out.println(s.oop_fitness());

		
	}
}
