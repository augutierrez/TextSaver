package com.example.mylearningproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.mylearningproject.saver.SavedText
import com.example.mylearningproject.saver.TextDetails
import com.example.mylearningproject.saver.TextViewModel
import com.example.mylearningproject.ui.theme.MyLearningProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyLearningProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TextSaverApp()
                }
            }
        }
    }
}

@Composable
fun TextSaverApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "texts") {
        composable("texts") {
            Texts { id -> navController.navigate("texts/$id") }
        }
        composable(
            "texts/{text_id}", arguments = listOf(navArgument("text_id") {
                type = NavType.IntType
            }),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "www.textsaverapp.details.com/{text_id}"
                },
            )
        ) { navBackStackEntry ->
            val id =
                navBackStackEntry.arguments?.getInt("text_id") // another way of getting ID, but VM had access through sharehandle
            TextDetails()
        }
    }
}

@Composable
fun MainCard(content: @Composable () -> Unit) {
    val padding = 24.dp
    val cornerShape = RoundedCornerShape(16.dp)
    Box(
        Modifier
            .fillMaxSize()
            .padding(padding)
            .clip(cornerShape)
            .background(Color.Gray), contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .padding(horizontal = padding)
                .clip(cornerShape)
                .background(Color.DarkGray)
                .padding(vertical = padding * 2),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }

}

@Composable
fun Texts(onItemClick: (id: Int) -> Unit) {
    val vm = viewModel<TextViewModel>()
    val list = vm.listOfSavedText.collectAsState().value
    MainCard {
        val cornerShape = RoundedCornerShape(16.dp)
        LazyColumn(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(20.dp)
        ) {
            itemsIndexed(list) { index, item ->
                TextItem(
                    Modifier
                        .clip(cornerShape)
                        .border(BorderStroke(1.dp, Color.Gray), shape = cornerShape)
                        .background(Color.LightGray), item, index == list.lastIndex, onItemClick
                )
            }
        }
    }

}

@Composable
private fun TextItem(
    modifier: Modifier,
    item: SavedText,
    isLastIndex: Boolean,
    onClick: (id: Int) -> Unit,
) {
    var isPinned by remember { mutableStateOf(false) }
    Box(
        modifier
            .fillMaxWidth()
            .clickable { onClick(item.id) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = item.text,
            modifier = Modifier
                .padding(vertical = 5.dp),
        )
//        Checkbox(
//            isPinned,
//            onCheckedChange = { isChecked -> isPinned = isChecked },
//            Modifier
//                .align(Alignment.CenterEnd)
//                .padding(end = 5.dp)
//        )
    }
    if (!isLastIndex) {
        Spacer(modifier = Modifier.height(1.dp))
    }
}
