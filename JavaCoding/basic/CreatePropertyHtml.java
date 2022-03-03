import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

public class CreatePropertyHtml {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String htmlString = "";
		
		htmlString=	"<head>\n";
		htmlString +=     "<meta charset='UTF-8'/>";
		htmlString	+="<style>\n";
		htmlString	+="table { border-collapse: collapse; width: 100%; }\n";
		htmlString	+="th, td { border: solid 1px #000; }\n";
		htmlString	+="</style>\n";
		htmlString	+="</head>\n";
		htmlString +="<h1>자바 환경정보</h1>\n";
		htmlString += "<table>\n";
		htmlString += "<thead>\n";
		htmlString += "<th>키</th>\n";
		htmlString += "<th>속성</th>\n";
		htmlString += "</thead>\n";
		htmlString += "<tbody>\n";
		try {
			File file = new File("property.html");
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"utf-8"));
			for(Object k : System.getProperties().keySet()) {
				String key = k.toString();
				String value = System.getProperty(key);
				
				htmlString+="<tr>\n";
				htmlString+="<td>"+ key+ "</td>";
				htmlString+="<td>"+ value+ "</td>";
				htmlString+="</tr>\n";
			}
			htmlString+="</tbody>\n";
			htmlString+="</table>\n";
			htmlString+="</body>\n";
			System.out.println(htmlString);
			writer.write(htmlString);
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

}
