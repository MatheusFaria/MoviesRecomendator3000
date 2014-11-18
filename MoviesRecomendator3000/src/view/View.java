package view;

import model.Movie;
import controller.Controller;

public class View {

	public static void main(String[] args) {
		//Scanner in = new Scanner(System.in);
		
		String mood = "Happy"; //in.next();
		String companion = "Family"; //in.next();
		
		Movie movie = Controller.recomend(mood, companion);
		System.out.println("I recommend to you: " + movie.getTitle());
	}

}
