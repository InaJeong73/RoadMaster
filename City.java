
public class City {
	private String cityName; // 도시명
	private int distanceFromSeoul; //서울로부터의 거리

	public City(String cityName, int distanceFromSeoul) {
		this.cityName = cityName;
		this.distanceFromSeoul = distanceFromSeoul;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getDistanceFromSeoul() {
		return distanceFromSeoul;
	}

	public void setDistanceFromSeoul(int distanceFromSeoul) {
		this.distanceFromSeoul = distanceFromSeoul;
	}

	@Override
	public String toString() {
		return cityName;
	}

}
