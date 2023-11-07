import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Vehicle implements Comparable<Vehicle>, Cloneable {
	// instance variables 추가하였습니다.

	private int vehicleNum;
	private int rate = 0;
	private int distance = 0;
	private double currentLocation;
	private LocalDateTime entryTime; // 진입 시각
	private LocalDateTime exitTime; // 진출 시각
	private City entryLocation; // 진입 장소
	private City exitLocation; // 진출 장소

	// abstract method
	public abstract int calcToll(); // 통행료 계산하는 함수

	public abstract String showVehicleInfo(); // 차량 객체의 차량 정보(시속, 위치 등)를 String형으로 반환하는 함수

	public abstract String showMovementInfo(); // 차량 객체의 통행 정보(진출 시간, 통행료 등)를 String형으로 반환하는 함수

	// instance variable을 위한 get/set 메소드
	public int getVehicleNum() {
		return vehicleNum;
	}

	public double getRate() {
		return rate;
	}

	public double getCurrentLocation() {
		return currentLocation;
	}

	public LocalDateTime getEntryTime() {
		return entryTime;
	}

	public City getEntryLocation() {
		return entryLocation;
	}

	public City getExitLocation() {
		return exitLocation;
	}

	public void setVehicleNum(int vehicleNum) {
		this.vehicleNum = vehicleNum;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public void setCurrentLocation(double currentLocation) {
		this.currentLocation = currentLocation;
	}

	public void setEntryTime(LocalDateTime entryTime) {
		this.entryTime = entryTime;
	}

	public void setEntryLocation(City entryLocation) {
		this.entryLocation = entryLocation;
	}

	public void setExitLocation(City exitLocation) {
		this.exitLocation = exitLocation;
	}

	// 진입도시와 진출도시 사이의 거리를 계산하는 함수
	public int calcDistance() {
		distance = this.getExitLocation().getDistanceFromSeoul() - this.getEntryLocation().getDistanceFromSeoul();
		if (distance < 0)
			distance = (-1) * distance;
		return distance;
	}

	// 진입도시에서 진출도시까지 가는데 걸리는 시간을 계산하여 진출 시각을 반환하는 함수
	public LocalDateTime calcExitTime() {
		int movingDistance = calcDistance();
		int t = (int) (((double) movingDistance / this.rate) * 60);// 진출하기까지 걸리는 분
		int hour = t / 60;
		int minute = t % 60;
		exitTime = getEntryTime().plusHours(hour).plusMinutes(minute);
		return exitTime;

	}

	@Override
	// Vehicle 차량을 차량 종류와 진입 순서 기준으로 정렬하는 함수
	public int compareTo(Vehicle other) {
		// 차량 종류를 기준으로 정렬합니다.
		int thisOrder = getVehicleOrder(this);
		int otherOrder = getVehicleOrder(other);
		int typeComparison = Integer.compare(thisOrder, otherOrder);
		if (typeComparison != 0) {
			return typeComparison;
		}

		// 진입 순서를 기준으로 정렬합니다.
		return this.getEntryTime().compareTo(other.getEntryTime());
	}

	// 차량 종류에 따른 순서를 반환하는 함수
	private int getVehicleOrder(Vehicle vehicle) {
		// 차량 종류에 따라 순서를 반환합니다.
		if (vehicle instanceof Car) {
			if (vehicle instanceof HybridCar) {
				return 2;
			}
			return 1;
		} else if (vehicle instanceof Bus) {
			return 3;
		} else if (vehicle instanceof Truck) {
			return 4;
		}
		return 0; // 정의되지 않은 경우 0을 반환합니다.
	}

	// 해당 Vehicle 객체를 복제하는 함수
	@Override
	public Vehicle clone() throws CloneNotSupportedException {
		return (Vehicle) super.clone();
	}

}

class Car extends Vehicle {
	private int volume; // 차량 배기량
	private double volumeRate; // 배기량 요율
	private static int basicPrice; // 기본 요금
	private static int distanceRate; // 거리 요율

	public Car(int carNum, int volume) {
		this.setVehicleNum(carNum);
		this.volume = volume;
		volumeRate = calcVolumeRate(volume);
	}

	// instance variable을 위한 get/set 메소드
	public int getVolume() {
		return volume;
	}

	public double getVolumRate() {
		return volumeRate;
	}

	public int getBasicCharge() {
		return basicPrice;
	}

	public int getDistanceRate() {
		return distanceRate;
	}

	public static void setDistanceRate(int distanceRate) {
		Car.distanceRate = distanceRate;
	}

	public static void setBasicCharge(int basicPrice) {
		Car.basicPrice = basicPrice;
	}

	// 차량 배기량에 따른 배기량 요율을 반환하는 함수
	public double calcVolumeRate(int volume) {
		if (volume >= 2400) {
			volumeRate = 1.2;
			return volumeRate;
		} else if (volume > 1000) {
			volumeRate = 1.0;
			return volumeRate;
		}

		else {
			volumeRate = 0.8;
			return volumeRate;
		}

	}

	@Override
	public int calcToll() {
		calcDistance();
		return (int) (basicPrice + (calcDistance() * distanceRate * getVolumRate()));

	}

	@Override
	public String toString() {
		return "car ";
	}

	@Override
	public String showVehicleInfo() {
		String entryTime = this.getEntryTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd:HH:mm"));
		return this.toString() + this.getVehicleNum() + " " + this.getVolume() + "cc " + entryTime + " "
				+ this.getEntryLocation() + "->" + this.getExitLocation() + " 시속:" + (int) this.getRate() + "km 위치:"
				+ (int) this.getCurrentLocation() + "km";
	}

	@Override
	public String showMovementInfo() {
		String entryTime = this.getEntryTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd:HH:mm"));
		String exitTime = this.calcExitTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd:HH:mm"));
		return this.toString() + this.getVehicleNum() + " " + this.getVolume() + "cc " + entryTime + " "
				+ this.getEntryLocation() + "->" + this.getExitLocation() + " " + exitTime + " " + this.calcToll()
				+ "원";
	}

}

class HybridCar extends Car {
	public HybridCar(int carNum, int volume) {
		super(carNum, volume);
	}

	@Override
	public int calcToll() {
		return (int) (super.calcToll() / 2);
	}

	@Override
	public String toString() {
		return "hybrid car ";
	}

}

class Bus extends Vehicle {
	private static int basicPrice; // 기본 요금
	private static int distanceRate; // 거리 요율
	private int noOfPassengers; // 승객수
	private double passengerRate; // 승객 요율

	public Bus(int carNum, int p) {
		this.setVehicleNum(carNum);
		noOfPassengers = p;
		passengerRate = this.getPassengerRate();
	}

	// instance variable을 위한 get/set 메소드
	public int getBasicCharge() {
		return basicPrice;
	}

	public int getDistanceRate() {
		return distanceRate;
	}

	public double getPassengerRate() {
		return passengerRate;
	}

	public static int getBasicPrice() {
		return basicPrice;
	}

	public int getNoOfPassengers() {
		return noOfPassengers;
	}

	public static void setDistanceRate(int distanceRate) {
		Bus.distanceRate = distanceRate;
	}

	public static void setBasicCharge(int basicPrice) {
		Bus.basicPrice = basicPrice;
	}

	// 승객수에 따른 승객요율을 반환하는 함수
	public double calcPassengerRate(int noOfPassengers) {
		if (noOfPassengers >= 40) {
			passengerRate = 1.2;
			return passengerRate;
		} else if (noOfPassengers >= 20) {
			passengerRate = 1.0;
			return passengerRate;
		}

		else {
			passengerRate = 0.8;
			return passengerRate;
		}

	}

	@Override
	public int calcToll() {
		calcDistance();
		return (int) (basicPrice + (calcDistance() * distanceRate * calcPassengerRate(noOfPassengers)));
	}

	@Override
	public String toString() {
		return "bus ";
	}

	@Override
	public String showMovementInfo() {
		String entryTime = this.getEntryTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd:HH:mm"));
		String exitTime = this.calcExitTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd:HH:mm"));
		return this.toString() + this.getVehicleNum() + " " + this.getNoOfPassengers() + "인승 " + entryTime + " "
				+ this.getEntryLocation() + "->" + this.getExitLocation() + " " + exitTime + " " + this.calcToll()
				+ "원";
	}

	@Override
	public String showVehicleInfo() {
		String entryTime = this.getEntryTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd:HH:mm"));
		return this.toString() + this.getVehicleNum() + " " + this.getNoOfPassengers() + "인승 " + entryTime + " "
				+ this.getEntryLocation() + "->" + this.getExitLocation() + " 시속:" + (int) this.getRate() + "km 위치:"
				+ (int) this.getCurrentLocation() + "km";
	}
}

class Truck extends Vehicle {
	private static int basicPrice;
	private static int distanceRate;
	private int weight;
	private double weightRate;

	public Truck(int carNum, int weight) {
		this.setVehicleNum(carNum);
		weightRate = this.calcWeightRate();
	}

	// instance variable을 위한 get/set 메소드
	public static int getBasicPrice() {
		return basicPrice;
	}

	public int getWeight() {
		return weight;
	}

	public double getWeightRate() {
		return weightRate;
	}

	public int getBasicCharge() {
		return basicPrice;
	}

	public int getDistanceRate() {
		return distanceRate;
	}

	public static void setDistanceRate(int distanceRate) {
		Truck.distanceRate = distanceRate;
	}

	public static void setBasicCharge(int basicPrice) {
		Truck.basicPrice = basicPrice;
	}

	// 중량에 따른 중량 요율을 반환하는 함수
	public double calcWeightRate() {
		if (weight >= 4) {
			return 1.2;
		} else if (weight >= 2) {
			return 1;
		} else {
			return 0.8;
		}
	}

	@Override
	public int calcToll() {
		calcDistance();
		return (int) (basicPrice + (calcDistance() * distanceRate * weightRate));
	}

	@Override
	public String toString() {
		return "truck ";
	}

	@Override
	public String showVehicleInfo() {
		String entryTime = this.getEntryTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd:HH:mm"));
		return this.toString() + this.getVehicleNum() + " " + this.getWeight() + "ton " + entryTime + " "
				+ this.getEntryLocation() + "->" + this.getExitLocation() + " 시속:" + (int) this.getRate() + "km 위치:"
				+ this.getCurrentLocation() + "km";
	}

	@Override
	public String showMovementInfo() {
		String entryTime = this.getEntryTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd:HH:mm"));
		String exitTime = this.calcExitTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd:HH:mm"));
		return this.toString() + this.getVehicleNum() + " " + this.getWeight() + "ton " + entryTime + " "
				+ this.getEntryLocation() + "->" + this.getExitLocation() + " " + exitTime + " " + this.calcToll()
				+ "원";
	}
}

