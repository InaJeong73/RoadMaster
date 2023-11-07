import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class HighwayVehicleManagementSystem {
   public static String[] vehicleTxtStr; // 고속도로에 진입가능한 모든 차량 데이터를 문자형으로 받는 배열
   public static String[] ratesTxtStr = new String[3]; // 차량 종류별 기본 요금 및 거리요율 데이터를 문자형으로 받는 배열
   public static ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>(); // 고속도로 통행 가능한 Vehicle형 객체 저장하는 ArrayList
   public static LocalDateTime currentTime, tempTime;
   private static String date, entryCityName, exitCityName;
   public static Highway highway = new Highway();
   public static City city;

   public static void main(String[] args) throws IOException, CloneNotSupportedException {
      File currentDirectory = new File(new File(".").getAbsolutePath());
      Scanner sc = new Scanner(System.in);
      String runPathString = currentDirectory.getCanonicalPath();
      String dataPathString = runPathString + "\\vehicles.txt";
      String ratePathString = runPathString + "\\rates.txt";
      String[] strArr;
      System.out.println("< Highway Vehicle Management System >\n");
      System.out.println("+ Exec Path : " + runPathString);
      System.out.println("+ Data Path : " + dataPathString);
      System.out.println("+ Rate Path : " + ratePathString);

      // strNum은 vehicleTxtStr 배열의 길이이다.
      // 차량 대수를 의미하는 vehicles.txt의 첫번째 줄은 strNum에 따로 저장한다. 
      String strNum;

      File f;
      // Data 정보 파일의 존재 여부 파악
      f = new File(dataPathString);
      if(!f.exists()) { 
         System.out.println("\n[ERROR] " + dataPathString + " 파일을 읽을 수 없습니다.\n");
         return;
      }
      // Rate 정보 파일의 존재 여부 파악      
      f = new File(ratePathString);
      if(!f.exists()) { 
         System.out.println("\n[ERROR] " + ratePathString + " 파일을 읽을 수 없습니다.\n");
         return;
      }
      
      BufferedReader reader = new BufferedReader(new FileReader(dataPathString));
      strNum = reader.readLine();
      vehicleTxtStr = new String[Integer.parseInt(strNum)];
      String str;
      int k = 0;
      // vehicles.txt 파일로부터 차량 정보를 받아온다.
      while ((str = reader.readLine()) != null) {
         vehicleTxtStr[k] = str;
         k++;
      }
      reader.close();
      str = null;
      reader = new BufferedReader(new FileReader(ratePathString));
      k = 0;
      // rates.txt 파일로부터 차량별 기본요금과 거리 요율 정보를 받아온다.
      while ((str = reader.readLine()) != null) {
         ratesTxtStr[k] = str;
         k++;
      }
      
      reader.close();
      // ratesTxtStr 배열의 각 요소에서 문자를 분리시켜 차종에 따른 기본요금과 거리요율에 대한 정보를 얻어내고 이를 각 차량 클래스에 등록한다. 
      for (int i = 0; i < ratesTxtStr.length; i++) {
         strArr = ratesTxtStr[i].split(" ");
         if (i == 0) {
            Car.setBasicCharge(Integer.parseInt(strArr[0]));
            Car.setDistanceRate(Integer.parseInt(strArr[1]));
         } else if (i == 1) {
            Bus.setBasicCharge(Integer.parseInt(strArr[0]));
            Bus.setDistanceRate(Integer.parseInt(strArr[1]));
         } else {
            Truck.setBasicCharge(Integer.parseInt(strArr[0]));
            Truck.setDistanceRate(Integer.parseInt(strArr[1]));
         }
      }
      strArr = null;
      // vehicleTxtStr 배열의 각 요소에서 차종,차량 번호,차종에 따른 부과 정보(배기량,좌석수, 중량)를 얻어내고 이를 통해 vehicles ArrayList에 새로운 차량 객체를 등록한다. 
      for (int i = 0; i < vehicleTxtStr.length; i++) {
         strArr = vehicleTxtStr[i].split(" ");
         if (strArr[0].equals("c") == true) {
            vehicles.add(new Car(Integer.parseInt(strArr[1]), Integer.parseInt(strArr[2])));
         } else if (strArr[0].equals("h") == true) {
            vehicles.add(new HybridCar(Integer.parseInt(strArr[1]), Integer.parseInt(strArr[2])));
         } else if (strArr[0].equals("b") == true) {
            vehicles.add(new Bus(Integer.parseInt(strArr[1]), Integer.parseInt(strArr[2])));
         } else if (strArr[0].equals("t") == true) {
            vehicles.add(new Truck(Integer.parseInt(strArr[1]), Integer.parseInt(strArr[2])));
         } else {
            System.out.println("txt정보가 올바르지 않습니다.");
         }
      }
      currentTime = Date.of(2023, 5, 12, 0, 0);
      System.out.print("\n");
      // 명령 모드 시작 부분
      while (true) {
         try {
            System.out.print("> ");
            strArr = null;
            str = sc.nextLine();
            strArr = str.split(" ");
            // 명령어 't' 입력시 현재시간인 LocalDateTime형 currentTime을 설정할 수 있다. 
            if (strArr[0].equals("t") == true) {
               // String 형 입력값을 분리시켜 년,월,일,시,분 과 관련된 정보를 얻어낸다. 
               int year = Integer.parseInt(strArr[1]);
               int month = Integer.parseInt(strArr[2]);
               int day = Integer.parseInt(strArr[3]);
               int hour = Integer.parseInt(strArr[4]);
               int minute = Integer.parseInt(strArr[5]);
                  // Date.of()는 유효하지 않은 날짜 입력 시 null을 반환하는 함수이다. 
                  // currentTime을 설정 시 이전의 설정된 currentTime 시각보다 항상 이후의 시각으로 설정되어야 한다.
                  tempTime = Date.of(year, month, day, hour, minute);
                  if (tempTime == null) {  
                     throw new RuntimeException("유효하지 않은 날짜입니다. 다시 입력해주세요!");
                  } else if (tempTime.isBefore(currentTime) == true) { // 이전의 설정된 currentTime 시각보다 이후의 시각이면 Exception 처리 후 명령 모드로 돌아간다.
                     throw new RuntimeException("현재시간보다 이전 시간을 입력하셨습니다!");
                  } else {
                     currentTime = tempTime; // 현재 시간 재설정
                     date = currentTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd, HH:mm"));
                  }

               
               // 설정한 현재 시각을 출력한다. 
               System.out.println("현재시간: " + date);
               // 재설정된 현재 시각 기준으로 고속도로에 있는 차량의 위치가 재배치되므로, 재배치될 차량의 위치를 계산해주어야 한다. 
               highway.calcCurrentLocation(currentTime);
               // 현재 시각을 재설정 시 해당 시각 기준으로 고속도로에서 이미 빠져나간 차량이 있을 수 있으므로,currentTime을 기준으로 고속도로로부터 진출한 차량에 대한 정보를 처리한다. 
               highway.exitHighway(currentTime);
               
            }
            // 명령어 'n' 입력시 vehicles에 있는 차량을 집입시킬 수 있다.  
            if (strArr[0].equals("n") == true) {
               // 승용차 차번호를 입력하면 차번호와 일치하는 차 객체가 진입해야 함
               int carNum = Integer.parseInt(strArr[1]);
               // 차량 번호 입력 오류
               if (carNum < 1000 || carNum > 9999) {
                  System.out.println("입력하신 차 번호가 형식에 맞지 않습니다.네 자리 정수를 입력해주세요");
               }
               boolean vehicleExists = false; // vehicles ArrayList에 존재하는 차량인지 확인하는 boolean 변수 ->Exception 처리
               boolean vehicleOnHighway = false; // 차량이 이미 고속도로 상에 존재하는지 확인하는 boolean 변수 ->Exception 처리
               for (int i = 0; i < vehicles.size(); i++) {
                  if (vehicles.get(i).getVehicleNum() == carNum) {
                     vehicleExists = true;
                     Vehicle v = vehicles.get(i).clone(); // vehicles에 있는 객체 중 입력 받은 차 번호와 동일한 객체가 있다면 해당 객체를 복사하여 v에 저장한다.
                     if (highway.isVehicleOnHighway(v)) { // 이미 진입한 차량인 경우
                        vehicleOnHighway = true;
                        break;
                     } else {
                        v.setEntryTime(currentTime); // 해당 차량의 진입시각을 설정한다. 
                        entryCityName = strArr[2];
                        boolean isInFiveCity = false; // 입력받은 집입도시가 서울,수원,대전,대구,부산 중에 있는지 확인하는 boolean 변수
                        for (int j = 0; j < highway.getCitiesOnHighway().size(); j++) {
                           City city = highway.getCitiesOnHighway().get(j);
                           if (city.getCityName().equals(entryCityName)) {
                              v.setEntryLocation(city); // 입력 받은 도시가 5개의 도시 안에 포함된다면 해당 차량의 진입도시를 설정한다. 
                              isInFiveCity = true;
                           }
                        }
                        if (isInFiveCity == false) {
                           System.out.println("고속도로 상에 없는 도시를 입력하였습니다. 다시 입력해주세요!");
                           break;
                        }

                        exitCityName = strArr[3];
                        isInFiveCity = false; // 입력받은 집출도시가 서울,수원,대전,대구,부산 중에 있는지 확인하는 boolean 변수
                        for (int j = 0; j < highway.getCitiesOnHighway().size(); j++) {
                           city = highway.getCitiesOnHighway().get(j);
                           if (city.getCityName().equals(exitCityName)) {
                              v.setExitLocation(city); // 입력 받은 도시가 5개의 도시 안에 포함된다면 해당 차량의 진출도시를 설정한다. 
                              isInFiveCity = true;
                           }
                        }
                        if (isInFiveCity == false) {
                           System.out.println("고속도로 상에 없는 도시를 입력하였습니다. 다시 입력해주세요!");
                           break;
                        } // 고속도로 상에 없는 도시를 입력받았다면 입력 오류 메세지를 날리고 명령 모드로 돌아간다. 
                        if (entryCityName.equals(exitCityName)) {
                           System.out.println("진입 도시와 진출 도시가 같습니다.다시 입력해주세요!");
                           break;
                        } // 입력받은 진입 도시명과 진출 도시명이 동일할 경우, 입력 오류 메세지를 날리고 명령 모드로 돌아간다. 
                        int carRate = Integer.parseInt(strArr[4]);
                        if (carRate < 50 || carRate > 120) {
                           System.out.println("차량 속도가 범위를 벗어났습니다. 다시 입력해주세요(50~120km)");
                           break;
                        } // 입력 받은 차 속도가 50 이상 120 이하의 범위에 들지 않는다면, 입력 오류 메세지를 날리고 명령 모드로 돌아간다. 
                        v.setRate(Integer.parseInt(strArr[4]));
                        highway.enterHighway(v);
                        break;
                     }
                   }
               }
               if (!vehicleExists) {
                  throw new RuntimeException("차량이 존재하지 않습니다.");
               } // vehicles 객체 중에 입력받은 차 번호와 동일한 차 번호를 가진 객체가 없을 경우 Exception 처리한다.

               if (vehicleOnHighway) {
                  throw new RuntimeException("이미 고속도로에 진입한 차량입니다.");
               } // 입력한 차량이 이미 고속도로 상에 있다면  Exception 처리한다.
            }
            // 명령어 'o' 입력시 고속도로 상의 모든 차량 정보를 볼 수 있다.
            if (strArr[0].equals("o") == true) {
               // 고속도로 상에 차량이 없다면 통행 차량이 없다는 메세지를 출력한다.
               if (highway.getVehiclesOnHighway().isEmpty()) {
                  System.out.println("통행 차량이 없습니다!");
               } else {
               // highway.calcCurrentLocation(currentTime);
                  // 고속도로 상에 있는 차량을 출력한다.
                  for (int i = 0; i < highway.getVehiclesOnHighway().size(); i++) {
                     Vehicle v = highway.getVehiclesOnHighway().get(i);
                     int n = i + 1;
                     System.out.println(n + ". " + v.showVehicleInfo());
                  }
               }
            }
            // 명령어 'x' 입력시 고속도로를 진출한 모든 차량 정보를 볼 수 있다. 
            if (strArr[0].equals("x") == true) {
               if (highway.getVehiclesOffHighway().isEmpty()) {
                  System.out.println("진출한 차량이 없습니다!");
               } else {
                  // 고속도로를 진출한 차량을 출력한다.
                  for (int i = 0; i < highway.getVehiclesOffHighway().size(); i++) {
                     Vehicle v = highway.getVehiclesOffHighway().get(i);
                     int n = i + 1;
                     System.out.println(n + ". " + v.showMovementInfo());
                  }
               }
            }
            // 명령어 'c' 입력시 현재시간을 볼 수 있다.
            if (strArr[0].equals("c") == true) {
               date = currentTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd, HH:mm"));
               System.out.println(date);
            }
            // 명령어 'q' 입력시 시스템을 종료한다.
            if (strArr[0].equals("q") == true) {
               System.out.println("고속도로 차량 관리 시스템을 종료합니다. 이용해 주셔서 감사합니다!");
               break;
            }
         } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            continue; // 예외 발생 시 루프의 처음으로 돌아감
         }
      }
   }
}