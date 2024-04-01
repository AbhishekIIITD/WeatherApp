# Weather App

The Weather App is a simple Android application that allows users to check the weather forecast based on their location. It utilizes the OpenMeteo API to fetch weather data and provides users with information such as minimum and maximum temperature for a given date.

## Features
- **Real-Time Weather Data:** Fetches real-time weather data from the OpenMeteo API.
- **Database Integration:** Uses Room Persistence Library to store weather data locally in a SQLite database.
- **Internet Connectivity Check:** Utilizes the `isNetworkAvailable` function to check internet connectivity before making API calls.
- **Four Screens:** Consists of four main screens:
  - **Loading Screen:** Allows users to input latitude, longitude, and date to fetch weather data.
  - **Result Screen:** Displays the weather forecast for the given date if internet connection is available.
  - **Error Screen:** Displays an error message if there is an issue fetching data from the API or the database.
  - **DB Result Screen:** Displays the weather forecast from the local database if internet connection is not available.
- **Input Validation:** Validates user inputs for latitude, longitude, and date to ensure correct data entry.
- **Gson Converter Factory:** Uses GsonConverterFactory to convert API responses to Kotlin data classes (Weather).

## Technologies Used
- Kotlin
- Android Jetpack Components (ViewModel, Room Database, LiveData)
- Retrofit for API calls
- Gson for JSON parsing
- Compose for UI development
- Gradle for dependency management

## For TA

### For Question 1:
- **Utilizing the API and downloading the data:**  
  The project utilizes the OpenMeteo API to fetch weather data. Retrofit is used to make HTTP requests to the API endpoints, and GsonConverterFactory is used to parse the JSON responses into Kotlin data classes. This functionality is implemented in the WeatherViewModel class, where the `getCurrentWeather` function fetches weather data from the API based on user input (latitude, longitude, date).
- **Creation of the UI:**  
  The UI is created using Jetpack Compose, a modern toolkit for building native Android UI. Composable functions are used to define the various screens of the app, such as the Loading Screen, Result Screen, Error Screen, and DB Result Screen. Each screen is designed to provide a user-friendly interface for inputting data, displaying weather information, handling errors, and showing database results.
- **Parsing of JSON files:**  
  GsonConverterFactory is used to automatically parse the JSON responses from the API into Kotlin data classes. These data classes represent the structure of the weather data, making it easy to work with the parsed data within the application.
- **Proper output and running code:**  
  The app is designed to provide accurate weather information based on user input. Proper error handling is implemented to handle cases where data cannot be fetched from the API or the database.
- **Validation of user input, proper error messages, and running app:**  
  Input validation is implemented in the Loading Screen to ensure that the user provides valid latitude, longitude, and date values before fetching weather data. Error messages are displayed to the user in case of any issues with fetching data or validating input. The app runs smoothly on Android devices, providing a seamless user experience.

### For Question 2:
- **Creation of database and schema:**  
  The project uses Room Persistence Library to create and manage a SQLite database. The database schema is defined using Room entities, representing the structure of the weather data to be stored locally.
- **Insertion of data into the database and sending queries:**  
  Weather data fetched from the API is inserted into the local database using Room DAO (Data Access Object) functions. Queries are sent to the database to retrieve weather data based on specific criteria, such as date.
- **Identification of cases where calculation is necessary, and computing it:**  
  Cases where calculation is necessary, such as computing average temperature, are identified based on the requirements of the application. These calculations are performed within the ViewModel or repository layer of the app, ensuring separation of concerns and maintainability.
- **Checking if network connectivity and its associated logic:**  
  The project includes a function (`isNetworkAvailable`) to check for network connectivity before making API calls. Proper logic is implemented to handle scenarios where internet connection is not available, such as fetching data from the local database instead of the API.
- **Proper error messages, correct output, and running app:**  
  Error messages are displayed to the user in case of any issues with database operations or network connectivity. The app provides correct weather information to the user, whether fetched from the API or retrieved from the local database. It runs smoothly on Android devices, handling various scenarios gracefully and providing a reliable user experience.

## Screenshots

## How to Use
1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Build and run the app on an Android emulator or physical device.
4. Input latitude, longitude, and date in the Loading Screen and tap on "Get Weather" to fetch weather data.
5. View the weather forecast on the Result Screen if internet connection is available. In case of no internet connection, the app will display the weather forecast from the local database on the DB Result Screen. If there is an error fetching data, the Error Screen will be displayed.

## Contributions
Contributions to the Weather App are welcome! Feel free to fork the repository, make improvements, and submit pull requests.

## License
The Weather App is open-source software licensed under the MIT License.
