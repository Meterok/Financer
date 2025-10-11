package com.potaninpm.feature_auth.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.potaninpm.feature_auth.R
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(
    onAuthSuccess: () -> Unit
) {
    WelcomeScreenContent(onAuthSuccess = onAuthSuccess)
}

@Composable
private fun WelcomeScreenContent(
    onAuthSuccess: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(R.string.sign_in, R.string.sign_up)
    val pagerState = rememberPagerState { tabs.size }

    LaunchedEffect(pagerState.currentPage) {
        selectedTab = pagerState.currentPage
    }

    Scaffold(
        topBar = {
            // пока так
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(bottom = innerPadding.calculateBottomPadding(), top = innerPadding.calculateTopPadding())
        ) {
            Column {
                TabRow(
                    selectedTabIndex = selectedTab,
                    indicator = { tabPositions ->
                        if (pagerState.currentPage < tabPositions.size) {
                            Box(
                                modifier = Modifier
                                    .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                                    .clip(MaterialTheme.shapes.large)
                                    .border(border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary), MaterialTheme.shapes.large)
                                    .background(Color.Transparent)
                                    .fillMaxSize()
                            )
                        }
                    },
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, title ->
                        val selected = selectedTab == index
                        Tab(
                            selectedContentColor = MaterialTheme.colorScheme.onSurface,
                            unselectedContentColor = Color.Gray,
                            modifier = Modifier
                                .zIndex(1f)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .clip(MaterialTheme.shapes.medium),
                            selected = selected,
                            onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                            text = { Text(text = stringResource(title)) },

                        )
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) { page ->
                    when (page) {
                        0 -> LoginScreen(onAuthSuccess = onAuthSuccess)
                        1 -> RegisterScreen(onAuthSuccess = onAuthSuccess)
                    }
                }
            }
        }
    }
}