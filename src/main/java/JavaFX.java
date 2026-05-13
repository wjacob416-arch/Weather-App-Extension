import com.sun.javafx.stage.EmbeddedWindow;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import weather.Period;
import weather.WeatherAPI;
import java.util.ArrayList;




public class JavaFX extends Application {
	TextField temperature,weather;
	Button threeDayButton,WindButton,FCconvert;
	HBox buttonContainer;
	Scene scene;

	public static void main(String[] args) {

		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Chicago Weather Application");
		//int temp = WeatherAPI.getTodaysTemperature(77,70);
		ArrayList<Period> forecast = WeatherAPI.getForecast("LOT",77,70);
		if (forecast == null){
			throw new RuntimeException("Forecast did not load");
		}

		temperature = new TextField();
		weather = new TextField();

		temperature.setText("Today's weather is: "+String.valueOf(forecast.get(0).temperature));
		weather.setText(forecast.get(0).shortForecast);

		Label weatherLabel = new Label("Chicago");
		weatherLabel.getStyleClass().add("title-label");

		Label tempForecastLabel = new Label(forecast.get(0).temperature + ": " + forecast.get(0).shortForecast);
		tempForecastLabel.getStyleClass().add("subtitle-label");

		Label welcomeLabel = new Label("Welcome to the Chicago Weather App");
		welcomeLabel.getStyleClass().add("headline-label");

		VBox headerContainer = new VBox(8, welcomeLabel, weatherLabel, tempForecastLabel);
		headerContainer.setAlignment(Pos.CENTER);
		headerContainer.getStyleClass().add("header");

		VBox weatherLabelContainer = new VBox(headerContainer);
		weatherLabelContainer.setAlignment(Pos.CENTER);

		VBox SevenDayLayout = new VBox();
		SevenDayLayout.setSpacing(8);
		SevenDayLayout.getStyleClass().add("forecast-list");

		for (int i = 0; i < 7; i++) {
			String dayForecast = "Day " + (i + 1) + ": " + forecast.get(i).shortForecast
					+ ", Temp: " + forecast.get(i).temperature;
			Label forecastLabel = new Label(dayForecast);
			forecastLabel.getStyleClass().add("forecast-item");
			SevenDayLayout.getChildren().add(forecastLabel);
		}

		String[] sunTimes = WeatherAPI.getSunriseSunset(41.85, -87.65);
		String sunriseStr = (sunTimes != null) ? sunTimes[0] : "N/A";
		String sunsetStr  = (sunTimes != null) ? sunTimes[1] : "N/A";

		Label sunriseLabel = new Label("Sunrise: " + sunriseStr);
		sunriseLabel.getStyleClass().add("stat-label");

		Label sunsetLabel = new Label("Sunset: " + sunsetStr);
		sunsetLabel.getStyleClass().add("stat-label");

		int precipitationChance = forecast.get(0).probabilityOfPrecipitation.value;
		Label precipitationLabel = new Label("Today's Precipitation Chance: " + precipitationChance + "%");
		precipitationLabel.getStyleClass().add("stat-label");

		VBox weatherLayout = new VBox(12, SevenDayLayout, precipitationLabel, sunriseLabel, sunsetLabel);
		weatherLayout.getStyleClass().add("card");



		threeDayButton = new Button("3-Day Data");
		threeDayButton.getStyleClass().addAll("button", "button-gold");
		threeDayButton.setOnAction(e -> showThreeDayData(forecast));

		WindButton = new Button("Search Wind Data");
		WindButton.getStyleClass().addAll("button", "button-blue");
		WindButton.setOnAction(e -> showWindSpeedScene(forecast));

		FCconvert = new Button("Temperature Convert");
		FCconvert.getStyleClass().addAll("button", "button-green");
		FCconvert.setOnAction(e -> ShowTemperatureConversion());

		buttonContainer = new HBox(15, threeDayButton, WindButton, FCconvert);
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPadding(new Insets(16, 0, 20, 0));
		buttonContainer.getStyleClass().addAll("button-container", "button-panel");


		VBox layout = new VBox(18, weatherLabelContainer, weatherLayout, buttonContainer);
		layout.setAlignment(Pos.TOP_CENTER);
		layout.setPadding(new Insets(10));

		ScrollPane scrollPane = new ScrollPane(layout);
		scrollPane.setFitToWidth(true);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

		BorderPane rootLayout = new BorderPane();
		rootLayout.setCenter(scrollPane);
		rootLayout.setPadding(new Insets(15));

		scene = new Scene(rootLayout, 820, 760);
		scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	private void showThreeDayData(ArrayList<Period> forecast) {
		if(forecast != null && forecast.size() >= 3){
			VBox threeDayLayout = new VBox();
			threeDayLayout.setSpacing(10); // Adjust spacing between items if needed
			threeDayLayout.setStyle("-fx-background-color: #B0E0E6;"); // Set background color for the VBox
			for(int i = 0; i < 3;i++){
				String dayForecast = "Days " + (i + 1) + ": " + forecast.get(i).shortForecast
						+ ", Temp: " + forecast.get(i).temperature;
				Label forecastLabel = new Label(dayForecast);
				forecastLabel.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

				// Alternate the background color for the labels, staying within the blue color range
				String bgColor = (i % 2 == 0) ? "#B0E0E6" : "#ADD8E6";  // Alternating between two blue shades
				forecastLabel.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-color: " + bgColor + ";");

				threeDayLayout.getChildren().add(forecastLabel);
			}
			Image image = new Image("file:1778999.jpg");
			ImageView imageView = new ImageView(image);

			imageView.setPreserveRatio(true); // Maintain the aspect ratio of the image
			imageView.setFitWidth(500); // Set the width to the original width
			imageView.setFitHeight(500); // Set the height to the original height


			// Add the image at the bottom of the VBox
			threeDayLayout.getChildren().add(imageView);

			Scene threeDayScene = new Scene(threeDayLayout, 700, 700);
			Stage newStage = new Stage();
			newStage.setTitle("3-Day Forecast");
			newStage.setScene(threeDayScene);
			newStage.show();
		}
		else{
			temperature.setText("Failed to load 3-day forecast.");
		}
	}
	private void showWindSpeedScene(ArrayList<Period> forecast) {
		if(forecast != null && !forecast.isEmpty()){
			VBox windSpeedLayout = new VBox(20);
			windSpeedLayout.setStyle("-fx-background-color: #90EE90;"); //light green background

			String windSpeed = forecast.get(0).windSpeed;
			String windDirection = forecast.get(0).windDirection;
			Label windSpeedLabel = new Label("Wind Speed: " + windSpeed);
			Label windDirectionLabel = new Label("Wind Direction: " + windDirection);
			windSpeedLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #000000; -fx-padding: 10px;");
			windDirectionLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #000000; -fx-padding: 10px;");

			Image compassImage = new Image("file:compass-1413032.jpg");
			ImageView compassImageView = new ImageView(compassImage);

			compassImageView.setFitWidth(250);  // Adjust width as needed
			compassImageView.setFitHeight(250); // Adjust height as needed

			compassImageView.setTranslateY(20); // move image down

			double rotationAngle = 0;
			switch (windDirection.toUpperCase()) {  // Using toUpperCase to be case-insensitive
				case "N":
					rotationAngle = 0;   // North
					break;
				case "NE":
					rotationAngle = 45;  // North-East
					break;
				case "E":
					rotationAngle = 90;  // East
					break;
				case "SE":
					rotationAngle = 135; // South-East
					break;
				case "S":
					rotationAngle = 180; // South
					break;
				case "SW":
					rotationAngle = 225; // South-West
					break;
				case "W":
					rotationAngle = 270; // West
					break;
				case "NW":
					rotationAngle = 315; // North-West
					break;
				default:
					rotationAngle = 0;   // Default to North if an unexpected value is found
					break;
			}

			compassImageView.setRotate(rotationAngle);

			windSpeedLayout.getChildren().addAll(windSpeedLabel, windDirectionLabel,compassImageView);
			Scene windSpeedScene = new Scene(windSpeedLayout, 700, 700);
			Stage windSpeedStage = new Stage();
			windSpeedStage.setTitle("Wind Speed Information");
			windSpeedStage.setScene(windSpeedScene);
			windSpeedStage.show();
		}
		else{
			temperature.setText("Failed to load wind data.");
		}

	}
	private void ShowTemperatureConversion() {
		Label resultLabel = new Label("Result: ");
		resultLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #FF6347; -fx-padding: 20px;");

		HBox buttonLayout = new HBox(15); // 15px gap between buttons
		buttonLayout.setStyle("-fx-alignment: center;");

		VBox temperatureConversionLayout = new VBox(10);
		temperatureConversionLayout.setStyle("-fx-background-color: #FFFFE0;");

		TextField temperatureField = new TextField();
		temperatureField.setPromptText("Enter Temperature");
		temperatureField.setStyle("-fx-font-size: 20px; -fx-padding: 10px; -fx-background-color: #f0f8ff; -fx-border-radius: 10px;");

		Button ConvertCelsiusButton = new Button("Convert Celsius");
		ConvertCelsiusButton.setStyle("-fx-font-size: 20px; -fx-padding: 15px 30px; -fx-background-color: #FF6347; -fx-border-radius: 10px;");
		ConvertCelsiusButton.setOnAction(e -> {
			try {
				double fahrenheit = Double.parseDouble(temperatureField.getText());
				double celsius = (fahrenheit - 32) * 5 / 9;
				String result = String.format("%.2f", celsius);
				resultLabel.setText("Result: " + result + " °C");
			} catch (NumberFormatException ex) {
				resultLabel.setText("Invalid input. Enter a number.");
			}
		});

		Button ConvertFahrenheitButton = new Button("Convert Fahrenheit");
		ConvertFahrenheitButton.setStyle("-fx-font-size: 20px; -fx-padding: 15px 30px; -fx-background-color: #4682B4; -fx-border-radius: 10px;");
		ConvertFahrenheitButton.setOnAction(e -> {
			try {
				double celsius = Double.parseDouble(temperatureField.getText());
				double fahrenheit = (celsius * 9 / 5) + 32;
				String result = String.format("%.2f", fahrenheit);
				resultLabel.setText("Result: " + result + " °F");
			} catch (NumberFormatException ex) {
				resultLabel.setText("Invalid input. Enter a number.");
			}
		});
		buttonLayout.getChildren().addAll(ConvertCelsiusButton, ConvertFahrenheitButton);

		temperatureConversionLayout.getChildren().addAll(
				temperatureField, buttonLayout, resultLabel);

		Scene TemperatureConversionsScene = new Scene(temperatureConversionLayout, 700, 700);
		Stage temperatureConversionStage = new Stage();
		temperatureConversionStage.setTitle("Temperature Conversion");
		temperatureConversionStage.setScene(TemperatureConversionsScene);
		temperatureConversionStage.show();
	}





}