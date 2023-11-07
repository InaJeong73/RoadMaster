
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class Highway {
	private ArrayList<Vehicle> vehiclesOnHighway; // 현재 고속도로에 있는 차량들을 저장하는 ArrayList
	private ArrayList<Vehicle> vehiclesOffHighway; // 현재 고속도로에서 나간 차량들을 저장하는 ArrayList
	private ArrayList<City> citiesOnHighway; // 현재 고속도로에 있는 도시들을 저장하는 ArrayList
	private static String date;

	public Highway() {
		vehiclesOnHighway = new ArrayList<>();
		vehiclesOffHighway = new ArrayList<>();
		citiesOnHighway = new ArrayList<City>();
		citiesOnHighway.add(new City("서울", 0));
		citiesOnHighway.add(new City("수원", 40));
		citiesOnHighway.add(new City("대전", 160));
		citiesOnHighway.add(new City("대구", 290));
		citiesOnHighway.add(new City("부산", 390));
	}

	// 차량을 고속도로로 진입시키고 진입 정보를 출력하는 함수
	public void enterHighway(Vehicle vehicle) {
		vehiclesOnHighway.add(vehicle);
		Collections.sort(vehiclesOnHighway);

		if (vehicle instanceof Car == true) {
			if (vehicle instanceof HybridCar == true)
				System.out.println("하이브리드 승용차 " + vehicle.getVehicleNum() + " 진입");
			System.out.println("승용차 " + vehicle.getVehicleNum() + " 진입");
		}

		else if (vehicle instanceof Bus == true)
			System.out.println("버스 " + vehicle.getVehicleNum() + " 진입");
		else if (vehicle instanceof Truck == true)
			System.out.println("트럭 " + vehicle.getVehicleNum() + " 진입");
		else
			return;

		String entryTime = vehicle.getEntryTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd:HH:mm"));
		System.out.println("진입시간:" + entryTime);
		System.out.println("진입장소: " + vehicle.getEntryLocation());
		System.out.println("진출장소: " + vehicle.getExitLocation());
		System.out.println("시속: " + (int) vehicle.getRate() + "km");
	}

	public ArrayList<Vehicle> getVehiclesOnHighway() {
		return vehiclesOnHighway;
	}

	public ArrayList<City> getCitiesOnHighway() {
		return citiesOnHighway;
	}

	public static String getDate() {
		return date;
	}

	// 현재 시각 기준 진출 차량 재 등록하는 함수
	public void exitHighway(LocalDateTime currentTime) {
		ArrayList<Vehicle> tempVehicles = new ArrayList<>();
		for (int i = 0; i < vehiclesOnHighway.size(); i++) {
			Vehicle v = vehiclesOnHighway.get(i); // 현재 시각이 재설정 되기 전에 고속도로에 있는 각 차량 객체를 돌아가며 v에 저장한다.
			LocalDateTime exitTime = v.calcExitTime();
			if (currentTime.isAfter(exitTime)) { // 재설정된 현재 시각보다 객체 v의 진출 시각이 이전이라면 이미 진출했음을 의미하므로 객체 v를
													// vehiclesOnHighway에서 제거하고 vehiclesOffHighway에 추가한다.
				tempVehicles.add(v); // vehiclesOffHighway에 추가할 차량들을 vehiclesOnHighway에서 일괄적으로 제거하기 위해 임시차량
										// ArrayList인 tempVehicles를 이용한다.
				for (int j = 0; j < vehiclesOffHighway.size(); j++) { // 재진출하는 차량에 대해 이전 진출 정보는 삭제되고 최근 진출 정보가 등록된다.
					if (vehiclesOffHighway.get(j).getVehicleNum() == v.getVehicleNum()) {
						vehiclesOffHighway.remove(j);
					}
				}
				vehiclesOffHighway.add(v);
			}
		}
		for (int i = 0; i < tempVehicles.size(); i++) {
			vehiclesOnHighway.remove(tempVehicles.get(i)); // tempVehicles에 있는 차량들을 vehiclesOnHighway에서 일괄적으로 제거한다.
		}
		Collections.sort(vehiclesOffHighway); // vehiclesOffHighway ArrrayList를 정렬한다.
	}

	public ArrayList<Vehicle> getVehiclesOffHighway() {
		return vehiclesOffHighway;
	}

	public void setVehiclesOffHighway(ArrayList<Vehicle> vehiclesOffHighway) {
		this.vehiclesOffHighway = vehiclesOffHighway;
	}

	// 고속도로 상에 특정 차량이 있는지 확인하는 함수
	public boolean isVehicleOnHighway(Vehicle vehicle) {
		return vehiclesOnHighway.contains(vehicle);
	}

	// 현재 시각 기준으로 차량들의 위치를 변경 및 계산하는 함수
	public void calcCurrentLocation(LocalDateTime currentTime) {
		for (int i = 0; i < vehiclesOnHighway.size(); i++) {
			Vehicle v = vehiclesOnHighway.get(i);
			Duration diff = Duration.between(v.getEntryTime(), currentTime);
			long hour = diff.toHours();
			double minute = (diff.toMinutes() % 60) / 60.0;
			double totalDiffTime = hour + minute;
			// 차량이 부산 방향으로 가는 경우 (정 방향)
			if (v.getEntryLocation().getDistanceFromSeoul() < v.getExitLocation().getDistanceFromSeoul())
				v.setCurrentLocation(v.getEntryLocation().getDistanceFromSeoul() + totalDiffTime * v.getRate());
			// 차량이 서울 방향으로 가는 경우 (반대방향)
			else if (v.getEntryLocation().getDistanceFromSeoul() > v.getExitLocation().getDistanceFromSeoul())
				v.setCurrentLocation(v.getEntryLocation().getDistanceFromSeoul() - totalDiffTime * v.getRate());
		}
	}
}
