package com.example.ass2.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ass2.DataEvents
import com.example.ass2.R
import com.example.ass2.Weather
import com.example.ass2.models.MaxData
import com.example.ass2.models.MinData
import com.example.ass2.models.dataState
import com.example.ass2.ui.theme.WeatherUiState
import com.example.ass2.ui.theme.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun HomeScreen(
    state:List<Weather>,
    context: Context,
    weatherViewModel: WeatherViewModel,
    onEvents: (DataEvents)->Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val weatherUiState=weatherViewModel.weatherUiState
    when (weatherUiState) {
        is WeatherUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize(),context,state,weatherViewModel)
        is WeatherUiState.Success -> ResultScreen(
            onEvents,weatherUiState.maxData,weatherUiState.minData, modifier = modifier.fillMaxWidth()
        )
        is WeatherUiState.Error -> ErrorScreen( modifier = modifier.fillMaxSize())
        is WeatherUiState.DbSuccess->DbResultScreen(date = weatherUiState.date, maxTemp = weatherUiState.maxData, minTemp = weatherUiState.minData,modifier=Modifier)
    }
}



//Getting current date(currDate -1 because current data not availaible at api)
fun getCurrentDateMinusOne(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val calendar = Calendar.getInstance()

    // Subtract one day from today's date
    calendar.add(Calendar.DATE, -1)

    // Format the date to "yyyy-MM-dd" format
    return dateFormat.format(calendar.time)
}

fun getAdjustedDate(date: String): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val calendar = Calendar.getInstance()

    // Parse the provided date string
    val providedDate = try {
        calendar.time = dateFormat.parse(date)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.time // Reset time to start of the day
    } catch (e: Exception) {
        return "" // Date parsing failed, return empty string
    }

    // Get today's date
    val today = Calendar.getInstance()
    today.add(Calendar.DATE, -1) // Subtract one day from today's date

    // Check if the provided date is less than today's date
    return if (providedDate <= today.time) {
        dateFormat.format(providedDate) // Return the original date
    } else {
        // If the provided date is not less than today's date, subtract 10 years
        calendar.time = today.time
        calendar.add(Calendar.YEAR, -10)
        dateFormat.format(calendar.time)
    }
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val networkInfo = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

    return networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
}

/**
 * In loading screen i am taking the necessary input from the user like lat,long and date
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier,context: Context, state: List<Weather>, weatherViewModel: WeatherViewModel) {

    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var date by remember {
        mutableStateOf("2024-03-21") //using a sample date if user do not provide any input
    }
    val backgroundImage = painterResource(id = R.drawable.skyillustrate)

    Surface(color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier){
            Image(
                painter = backgroundImage,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TextField(
                    value = latitude,
                    onValueChange = { latitude = it },
                    label = { Text("Latitude") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = longitude,
                    onValueChange = { longitude = it },
                    label = { Text("Longitude") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Pass the date state to MyContent
                MyContent(date) { selectedDate ->
                    date = selectedDate
                }

                Button(
                    onClick = {
                        if (latitude.isNotEmpty() && longitude.isNotEmpty() && date.isNotEmpty() && date.isNotEmpty()) {
                            val lat = latitude.toDouble()
                            val lon = longitude.toDouble()

                            val newDate= getAdjustedDate(date)
                            val currDate= getCurrentDateMinusOne()
                            //if date is same as returned it means the date  is in past ,hence no calculation required
                            if(newDate==date){
                                if(isNetworkAvailable(context)){
                                    weatherViewModel.getCurrentWeather(lat,lon,date,date)
                                }else{
                                    val weather=state.find { it.date == newDate }
                                    if(weather==null){
                                        weatherViewModel.weatherUiState=WeatherUiState.Error
                                    }else{
                                        weatherViewModel.weatherUiState=WeatherUiState.DbSuccess(date,weather.maxTempData,weather.minTempData)
                                    }
                                }

                            }else{
                                if(isNetworkAvailable(context)){
                                    weatherViewModel.getCurrentWeather(lat,lon,newDate,currDate)
                                }else{
                                    val weather=state.find { it.date == currDate }
                                    if(weather==null){
                                        weatherViewModel.weatherUiState=WeatherUiState.Error
                                    }else{
                                        weatherViewModel.weatherUiState=WeatherUiState.DbSuccess(newDate,weather.maxTempData,weather.minTempData)
                                    }
                                }
                            }




                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                ) {
                    Text("Get Weather")
                }
            }
        }
    }
}


//Took from gfg Used to show a calender to user for choosing a date and it make it easy for us to
//take care of date input

@Composable
fun MyContent(selectedDate: String, onDateSelected: (String) -> Unit) {

    // Fetching the Local Context
    val mContext = LocalContext.current

    // Declaring integer values
    // for year, month and day
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    // Declaring a string value to
    // store date in string format
    val mDate = remember { mutableStateOf("") }

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value =if(mMonth+1<10) "$mYear-0${mMonth + 1}-$mDayOfMonth" else "$mYear-${mMonth + 1}-$mDayOfMonth"
            // Update the selected date state
            onDateSelected(mDate.value)
        }, mYear, mMonth, mDay
    )

    Column(modifier = Modifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        // Creating a button that on
        // click displays/shows the DatePickerDialog
        Button(onClick = {
            mDatePickerDialog.show()
        }, colors = ButtonDefaults.buttonColors(Color(0XFF0F9D58)), modifier = Modifier.fillMaxWidth()) {
            Text(text = "Select Date", color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Selected Date: $selectedDate", fontSize = 30.sp, textAlign = TextAlign.Center)
    }
}




//Handling all the error by showing a error screen to the user and implementing all the logic needed
@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}


@Composable
fun ResultScreen(onEvents: (DataEvents)->Unit, maxData: MaxData?, minData: MinData?, modifier: Modifier = Modifier) {
    if (maxData == null||minData==null) {
        ErrorScreen()
    } else {
        var temperature = maxData.daily.temperature_2m_max[0]
        val notNullMinTemp=minData.daily.temperature_2m_min.filterNotNull()
        val notNullMaxTemp=maxData.daily.temperature_2m_max.filterNotNull()
        val minTemp=notNullMinTemp.average()
        val maxTemp=notNullMaxTemp.average()
        val firstTime = maxData.daily.time.lastOrNull()
        val date = if (!firstTime.isNullOrEmpty()) {
            firstTime.split("T")[0] // Extract date part (before 'T' character)
        } else {
            null
        }

        if(minTemp!=null && maxTemp!=null){
            temperature=(minTemp+maxTemp)/2
            onEvents(DataEvents.addData(Weather(0,date, minTempData = minTemp, maxTempData = maxTemp)))
        }


        //Setting the background based on average of minTemp and maxTemp
        val backgroundImg = when {
            temperature < 10 -> R.drawable.less_10
            temperature > 35 -> R.drawable.more_35
            else -> R.drawable.bet_10_35
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = backgroundImg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .align(Alignment.Center)
                    .background(Color.Black.copy(alpha = 0.7f)), // 70% transparency
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //converting the temps to int and then string to show on screen

                Text(text = "Minimum Temperature : ", modifier=Modifier, color = Color.White, style = TextStyle(fontSize = 25.sp))
                Text(text = minTemp.toInt().toString(),modifier=Modifier, color = Color.White, style = TextStyle(fontSize = 40.sp))
                Spacer(modifier = Modifier.padding(12.dp))
                Text(text = "Maximum Temperature : ",modifier=Modifier, color = Color.White, style = TextStyle(fontSize = 25.sp))
                Text(text = maxTemp.toInt().toString(),modifier=Modifier, color = Color.White, style = TextStyle(fontSize = 40.sp))

            }
        }
    }
}

@Composable
fun DbResultScreen(date: String, maxTemp: Double, minTemp: Double, modifier: Modifier = Modifier) {
    // Calculate average temperature
    val temperature = (maxTemp + minTemp) / 2

    // Setting the background based on average temperature
    val backgroundImg = when {
        temperature < 10 -> R.drawable.less_10
        temperature > 35 -> R.drawable.more_35
        else -> R.drawable.bet_10_35
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = backgroundImg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .align(Alignment.Center)
                .background(Color.Black.copy(alpha = 0.7f)), // 70% transparency
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Show minimum temperature
            Text(
                text = "Minimum Temperature : ",
                modifier = Modifier,
                color = Color.White,
                style = TextStyle(fontSize = 25.sp)
            )
            Text(
                text = minTemp.toInt().toString(),
                modifier = Modifier,
                color = Color.White,
                style = TextStyle(fontSize = 40.sp)
            )

            Spacer(modifier = Modifier.padding(12.dp))

            // Show maximum temperature
            Text(
                text = "Maximum Temperature : ",
                modifier = Modifier,
                color = Color.White,
                style = TextStyle(fontSize = 25.sp)
            )
            Text(
                text = maxTemp.toInt().toString(),
                modifier = Modifier,
                color = Color.White,
                style = TextStyle(fontSize = 40.sp)
            )
        }
    }
}

