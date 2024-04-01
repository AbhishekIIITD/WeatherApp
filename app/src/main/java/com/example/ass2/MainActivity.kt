package com.example.ass2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Database
import androidx.room.Room
import com.example.ass2.ui.HomeScreen
import com.example.ass2.ui.theme.Ass2Theme
import com.example.ass2.ui.theme.WeatherDataViewModel
import com.example.ass2.ui.theme.WeatherViewModel

class MainActivity : ComponentActivity() {

    private val db by lazy{
        Room.databaseBuilder(
            applicationContext,
            InventoryDatabase::class.java,
            "WeatherDb"
        ).build()
    }

    private val viewModel by viewModels<WeatherDataViewModel> (
        factoryProducer={
            object :ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return WeatherDataViewModel(db.Dao) as T
                }
            }
        }
    )
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContent {
            Ass2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val weatherViewModel: WeatherViewModel= viewModel()


                    val state by viewModel.data.collectAsState()
                    HomeScreen(state,applicationContext,weatherViewModel = weatherViewModel,viewModel::OnEvent,Modifier)
                }
            }
        }
    }
}

