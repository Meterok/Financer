package com.potaninpm.feature_home.presentation.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.potaninpm.feature_home.R
import com.potaninpm.feature_home.domain.model.NewsArticle
import com.potaninpm.feature_home.domain.model.SearchResults
import com.potaninpm.feature_home.domain.model.Ticker
import com.potaninpm.feature_home.presentation.components.NewsCard
import com.potaninpm.feature_home.presentation.components.TickerCard
import com.potaninpm.feature_home.presentation.components.searchBar.SearchBar
import com.potaninpm.feature_home.presentation.viewModels.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = koinViewModel()
) {
    val focusRequester = remember { FocusRequester() }

    val query by searchViewModel.query.collectAsState()
    val results by searchViewModel.searchResults.collectAsState()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column {
        SearchScreenContent(
            query = query,
            onQueryChange = { newQuery ->
                if (newQuery.isNotEmpty()) {
                    searchViewModel.setQuery(newQuery)
                }
            },
            results = results,
            focusRequester = focusRequester,
        )
    }
}

@Composable
private fun SearchScreenContent(
    query: String,
    onQueryChange: (String) -> Unit,
    results: SearchResults,
    focusRequester: FocusRequester
) {
    var searchText by remember {
        mutableStateOf(query)
    }

    val state = rememberScrollState()

    Scaffold(
        topBar = {

        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(top = 12.dp)
                .padding(horizontal = 16.dp)
        ) {
            SearchBar(
                query = searchText,
                onQueryChange = onQueryChange,
                focusRequester = focusRequester
            )

            Column(
                modifier = Modifier
                    .verticalScroll(state)
            ) {
                TickersListSearch(
                    tickers = results.tickers
                )

                Spacer(modifier = Modifier.height(12.dp))

                NewsListSearch(
                    news = results.news
                )
            }
        }
    }
}

@Composable
fun NewsListSearch(news: List<NewsArticle>) {
    if (news.isNotEmpty()) {
        Text(
            text = "Новости",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
        )

        news.forEach { new ->
            NewsCard(
                article = new,
                onClick = {

                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun TickersListSearch(
    tickers: List<Ticker>
) {
    if (tickers.isNotEmpty()) {
        Text(
            text = "Тикеры",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier
                .padding(top = 16.dp)
        )

        Card(
            modifier = Modifier
                .padding(top = 12.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            tickers.forEach { ticker ->
                Log.i("INFOG2", "Ticker: $ticker")

                if (ticker.currentPrice != 0.0f) {
                    TickerInfoSearch(
                        ticker = ticker
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun TickerInfoSearch(ticker: Ticker) {
    val priceColor = if (ticker.change >= 0) Color(0xFF05B000) else Color.Red

    val painter = if (ticker.logoUrl.isNullOrEmpty()) {
        painterResource(id = R.drawable.error_24px)
    } else {
        rememberAsyncImagePainter(ticker.logoUrl)
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(35.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ticker.symbol,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                )
                Image(
                    painter = painterResource(if (ticker.change >= 0) R.drawable.arrow_drop_up_24px else R.drawable.arrow_drop_down_24px),
                    contentDescription = null,
                )
            }
            ticker.companyName?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = String.format("%.2f", ticker.currentPrice) + " " + ticker.currency,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            )
            Text(
                text = if (ticker.change >= 0) "${"+%.2f".format(ticker.change)}%" else "${"%.2f".format(ticker.change)}%",
                color = priceColor,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

