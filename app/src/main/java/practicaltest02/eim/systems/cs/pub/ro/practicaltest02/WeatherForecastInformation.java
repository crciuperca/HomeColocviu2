package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

public class WeatherForecastInformation {
    private String humidity;
    private String temperature;

    public WeatherForecastInformation(String humidity, String temperature) {
        this.humidity = humidity;
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
