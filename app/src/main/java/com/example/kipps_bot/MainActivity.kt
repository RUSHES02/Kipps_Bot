package com.example.kipps_bot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kipps_bot.ui.theme.AppBar
import com.example.kipps_bot.ui.theme.Kipps_BotTheme
import com.example.kipps_bot.ui.theme.PrimaryColor

class MainActivity : ComponentActivity() {
	@OptIn(ExperimentalMaterial3Api::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			
			val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
			var showBottomSheet by remember { mutableStateOf(false) }
			
			if (showBottomSheet) {
				ModalBottomSheet(
					modifier = Modifier
						.wrapContentHeight(),
					onDismissRequest = { showBottomSheet = false },
					sheetState = sheetState
				) {
					ChatBot(
						modifier = Modifier
							.fillMaxWidth()
					)
				}
			}
				Kipps_BotTheme {
				Scaffold(
					modifier = Modifier
						.fillMaxSize(),
					topBar = {
						AppBar(
							title = "Kipps Chatbot"
						)
					}
				) { innerPadding ->
					Surface (
						modifier = Modifier
							.padding(innerPadding)
					) {
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.fillMaxHeight()
								.windowInsetsPadding(WindowInsets.navigationBars)
								.statusBarsPadding()
								.padding(30.dp)
						){
							FloatingActionButton(
								modifier = Modifier
									.align(Alignment.BottomEnd),
								containerColor = PrimaryColor,
								onClick = {
									showBottomSheet = !showBottomSheet
								},
							) {
								Icon(
									imageVector = ImageVector.vectorResource(R.drawable.ic_chats),
									contentDescription = "Chat with Kipps",
									tint = Color.White
								)
							}
						}
						
					}
				}
			}
		}
	}
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
	Kipps_BotTheme {
		ChatBot()
	}
}