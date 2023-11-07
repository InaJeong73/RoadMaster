
import java.time.LocalDateTime;
import java.util.GregorianCalendar;

class Date {
	private int year;
	private int month;
	private int day;
	private int time;
	private int minute;

	public Date(int year, int month, int day, int time, int minute) {
		year = this.year;
		month = this.month;
		day = this.day;
		time = this.time;
		minute = this.minute;
	}

	// 유효한 날짜이면 해당 날짜를 LocalDateTime형으로 반환해주고 유효한 날짜가 아니면 null을 반환하는 함수
	public static LocalDateTime of(int year, int month, int day, int time, int minute) {
		boolean isvalid = true;
		GregorianCalendar gc = new GregorianCalendar();
		if (month > 12 || month < 1) {
			isvalid = false;
		}
		if (year < 1) {
			isvalid = false;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			if (day > 31 || day < 0) {
				isvalid = false;
			}
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			if (day > 30 || day < 0) {
				isvalid = false;
			}
			break;
		case 2:
			if (gc.isLeapYear(year) == true) {// 윤년
				if (day < 0 || day > 29) {
					isvalid = false;
				}
			} else {// 평년
				if (day < 0 || day > 28) {
					isvalid = false;
				}
			}
			break;
		default:
			break;
		}
		if (time >= 24 || time < 0) {
			isvalid = false;
		}
		if (minute >= 61 || minute < 0) {
			isvalid = false;
		}

		if (isvalid == true) {
			LocalDateTime d = LocalDateTime.of(year, month, day, time, minute);
			return d;
		} else {
			return null;
		}
	}

}

