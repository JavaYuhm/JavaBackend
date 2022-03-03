import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CalculateXY {

	public static void main(String [] args) throws NumberFormatException, IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("나의 X 좌표를 입력하세요.");
		int myX = Integer.parseInt(br.readLine());
		System.out.println("나의 Y 좌표를 입력하세요.");
		int myY = Integer.parseInt(br.readLine());
		ArrayList<Point> points = new ArrayList<>();
		int times = 0;
		while(times<10) {
			
			System.out.printf("%d/10 번째 좌표를 입력하세요.\n", times+1);
			System.out.println("임의의 X 좌표를 입력하세요.");
			int inputX = Integer.parseInt(br.readLine());
			System.out.println("임의의 Y 좌표를 입력하세요.");
			int inputY = Integer.parseInt(br.readLine());
			
			if(duplecateCheck(points, inputX, inputY)) {
				System.out.println("입력받은 좌표값과 동일한 좌표값이 있습니다. 다시 입력하세요");
			} else {
				times++;
				points.add(new Point(inputX,inputY));
			}
		}
		Point calcPoint = calcNearPoint(points, myX, myY);
		System.out.printf("가장 가까운 좌표는 (%d, %d) 입니다",calcPoint.x, calcPoint.y);
	}
	private static boolean duplecateCheck(ArrayList<Point> points, int x, int y) {
		
		for(Point tmp : points) {
			if(x==tmp.x && y==tmp.y) return true;		
		}
		
		return false;
	}
	
	private static Point calcNearPoint(ArrayList<Point> points, int myX, int myY) {
		
		int compDist = Integer.MAX_VALUE;
		Point returnPoint = new Point(0,0);
		for(Point tmp : points) {
			
			int calcDist = Math.abs(myX-tmp.x)+Math.abs(myY-tmp.y); 
			if(calcDist<compDist) {
				compDist = calcDist;
				returnPoint.x = tmp.x;
				returnPoint.y = tmp.y;
			}
		}
		
		return returnPoint;
	}
	private static class Point {
		int x;
		int y;
		
		Point(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
	
}
