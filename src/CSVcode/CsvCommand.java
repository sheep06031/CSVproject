package CSVcode;

//import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class CsvCommand {

    File csv = new File("/Users/kimjuyeop/Desktop/CSV Project/info.csv");
    List<List<String>> csvList = new ArrayList<List<String>>();
    List<String> categoryList = new ArrayList<String>();
    List<List<String>> resultList = new ArrayList<List<String>>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    BufferedReader br = null;
    BufferedWriter bw = null;


    /**
     * 주어진 csv 파일의 모든 정보들을 csvList에 추가하여 반환하는 메소드
     * @return csv의 모든 데이터가 담겨있는 리스트
     */
    public List<List<String>> readCSV(){
        String line = "";

        try {
            br = new BufferedReader(new FileReader(csv));
            while((line = br.readLine()) != null) {
                List<String> aLine = new ArrayList<String>();
                String[] lineArr = line.split(",");
                aLine = Arrays.asList(lineArr);
                csvList.add(aLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        csvList.remove(0);
        return csvList;
    }

    /**
     * 날짜별 소비내역이 담긴 리스트를 반환하는 메소드
     * @return 입력된 날짜의 거래내역만 추가된 리스트
     */
    public List<List<String>> getDailyConsumption(String date) {
        List<List<String>> dailyConList = new ArrayList<List<String>>();
        LocalDate pickedDate = LocalDate.parse(date, formatter);

        for (List<String> list : csvList) {
            String[] str = list.get(0).split(" ");
            LocalDate strDate = LocalDate.parse(str[0], formatter);
            if (strDate.equals(pickedDate)) dailyConList.add(list);
        }
        return dailyConList;
    }


    /**
     * 두 날짜 사이의 거래내역들을 반환하는 메소드
     * @param date1 시작 날짜
     * @param date2 마지막 날짜, 마지막 날짜는 시작 날짜보다 이후어야 함
     * @return 두 날짜 사이의 거래내역이 담긴 리스트
     */
    public List<List<String>> getDailyConsumption(String date1, String date2) {
        List<List<String>> dailyConList = new ArrayList<List<String>>();

        LocalDate startDate = LocalDate.parse(date1, formatter);
        LocalDate endDate = LocalDate.parse(date2, formatter);
        getDatesBetweenTwoDates(startDate, endDate);

        for (List<String> list : csvList) {
            String[] str = list.get(0).split(" ");
            LocalDate strDate = LocalDate.parse(str[0], formatter);

            if (getDatesBetweenTwoDates(startDate, endDate).contains(strDate)) dailyConList.add(list);
        }
        return dailyConList;
    }


    /**
     * 주어진 두 날짜의 날짜들을 담은 리스트를 반환하는 메소드
     * @param startDate 시작 날짜
     * @param endDate 마지막 날짜
     * @return 날짜들 사이의 모든 날짜들이 담긴 리스트
     */
    public static List<LocalDate> getDatesBetweenTwoDates(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> betweenDateList = startDate.datesUntil(endDate).collect(Collectors.toList());
        betweenDateList.add(endDate);
        return betweenDateList;
    }


    /**
     * 소비유형별 거래내역을 반환하는 메소드
     * @param category 소비유형 종류
     * @return 입력된 소비유형의 거래내역이 담긴 리스트
     */
    public List<List<String>> getCategoryCon(String category) {
        List<List<String>> categoryConList = new ArrayList<List<String>>();

        if (categoryList.contains(category)) {
            for (List<String> list : csvList) {
                if (list.get(6).equals(category)) {
                    categoryConList.add(list);
                }
            }
        } else {
            System.out.println("주어진 소비유형(" + category + ")은 거래내역 목록에 존재하지 않습니다.");
        }
        return categoryConList;
    }

    /**
     * 소비유형의 종류들을 반환하는 메소드
     * @return 소비유형의 종류가 담겨있는 메소드
     */
    public List<String> getCategory() {
        for(List<String> list : csvList) {
            if (!(categoryList.contains(list.get(6)))) categoryList.add(list.get(6));
        }
        return categoryList;
    }

    /**
     * 거래내역중 출금 내역을 반환하는 메소드
     * @return 출금내역만 담겨있는 리스트
     */
    public List<List<String>> getWithdraw() {
        List<List<String>> withdrawList = new ArrayList<List<String>>();
        for(List<String> list : csvList) {
            if(Integer.parseInt((list.get(3))) > 0) withdrawList.add(list);
        }
        return withdrawList;
    }

    /**
     * 거래내역중 입금 내역을 반환하는 메소드
     * @return 입금내역만 담겨있는 리스트
     */
    public List<List<String>> getDeposit() {
        List<List<String>> depositList = new ArrayList<List<String>>();
        for(List<String> list : csvList) {
            if(Integer.parseInt((list.get(4))) > 0) depositList.add(list);
        }
        return depositList;
    }

    /**
     * 제일 최근 거래내역을 기준으로 계좌의 잔액을 반환하는 메소드
     * @return 계좌의 잔액
     */
    public int getCurrentBalance() {
        int balance = Integer.parseInt(csvList.get(0).get(5));
        return balance;
    }

    /*
    public List<String> categoryPicked() {

    }

     */

    /**
     * 주어진 날짜 사이와, 주어진 소비유형 모두 만족하는 거래내역 리스트를 반환하는 메소드
     * @param startDate 시작 날짜
     * @param endDate 마지막 날짜, 마지막 날짜는 시작 날짜보다 이후여야 함
     * @param categoryPickedList 소비유형들이 담긴 리스트
     * @return 주어진 조건을 모두 만족시키는 리스트
     */
    public List<List<String>> getComplexCon(String startDate, String endDate, List<String> categoryPickedList) {
        List<List<String>> complexConList = new ArrayList<List<String>>();
        List<List<String>> dailyList = getDailyConsumption(startDate, endDate);
        for(List<String> list : dailyList) {
            if(categoryPickedList.contains(list.get(6))) complexConList.add(list);
        }
        return complexConList;
    }
}
