
public class Main {
	static Puzzle p = new Puzzle(4);

	public static void main(String[] args){
		//1 = up, 2 = down, 3 = right, 4 left
		System.out.println(p.toString());
		p.shuffle(100);
		System.out.println(p.toString());
        System.out.print(p.isSolvable());
		Solver s = new Solver(p);
		s.oop_fitness();

		/*System.out.println("12 up >"+p.movePiece(12, 1));
		System.out.println(p.toString());
		System.out.println("12 down >"+p.movePiece(12, 2));
		System.out.println(p.toString());
		System.out.println("11 right >"+p.movePiece(11, 3));
		System.out.println(p.toString());
		System.out.println("15 up >"+p.movePiece(15, 1));
		System.out.println(p.toString());
		System.out.println("12 left >"+p.movePiece(12, 4));
		System.out.println(p.toString());
		System.out.println("1 right >"+p.movePiece(1, 3));
		System.out.println(p.toString());
		System.out.println("1 left >"+p.movePiece(1, 4));
		System.out.println(p.toString());
		System.out.println("0 left >"+p.movePiece(0, 4));
		System.out.println(p.toString());*/
	}
}
