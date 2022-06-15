import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class LottoCreate {
    public static void main(String[] args) {

        end:
        while (true) {
            HashMap<Integer, Integer> lottoNumberMap = new HashMap<>();
            while (lottoNumberMap.size() < 6) {
                int c = (int) (Math.random() * 45) + 1;
                if (!lottoNumberMap.containsKey(c)) {
                    lottoNumberMap.put(c, 1);
                }
            }
            String res = "";
            System.out.println("계산할 랜덤 생성 로또 값" + lottoNumberMap.keySet().stream().sorted().toList());

            int flag = 0;
            compNumberLoop:
            for (int i = 900; i < 1013; i++) {
                try {
                    // Lotto API 호출 
                    URL url = new URL("https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=" + i);
                    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

                    res = br.readLine();
                    
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(res);

                    ArrayList<Integer> lottoNumberhistory = new ArrayList<>();
                    lottoNumberhistory.add(Integer.parseInt(String.valueOf(jsonObject.get("drwtNo1"))));
                    lottoNumberhistory.add(Integer.parseInt(String.valueOf(jsonObject.get("drwtNo2"))));
                    lottoNumberhistory.add(Integer.parseInt(String.valueOf(jsonObject.get("drwtNo3"))));
                    lottoNumberhistory.add(Integer.parseInt(String.valueOf(jsonObject.get("drwtNo4"))));
                    lottoNumberhistory.add(Integer.parseInt(String.valueOf(jsonObject.get("drwtNo5"))));
                    lottoNumberhistory.add(Integer.parseInt(String.valueOf(jsonObject.get("drwtNo6"))));
                    //System.out.println(i + "회차 " + lottoNumberhistory.stream().toList());

                    int cnt = 0;
                    // Simple 이전 이력 회차들의 로또번호랑 3개 이상일치하지 않은 로또번호 생성
                    for (int lottoNum : lottoNumberhistory) {
                        if (lottoNumberMap.containsKey(lottoNum)) {
                            if (cnt > 2) {
                                flag = 1;
                                break compNumberLoop;
                            }
                            cnt++;
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (flag == 0) {
                System.out.println("생성된 로또 값" + lottoNumberMap.keySet().stream().sorted().toList());
                break end;
            }
        }
    }
}
