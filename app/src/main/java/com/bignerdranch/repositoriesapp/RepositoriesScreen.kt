package com.bignerdranch.repositoriesapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

@Composable
fun RepositoriesScreen(repos: LazyPagingItems<Repository>) {
    LazyColumn(
        contentPadding = PaddingValues(
            vertical = 8.dp,
            horizontal = 8.dp
        )
    ) {
        items(repos.itemCount) { index ->
            repos[index]?.let {
                RepositoryItem(
                    index = index + 1,
                    item = it
                )
            }
        }
        val refreshLoadState = repos.loadState.refresh
        val appendLoadState = repos.loadState.append
        when {
            refreshLoadState is LoadState.Loading -> {
                item {
                    LoadingItem(
                        Modifier.fillParentMaxSize())
                }
            }
            refreshLoadState is LoadState.Error -> {
                val error = refreshLoadState.error
                item {
                    ErrorItem(
                        message = error.localizedMessage
                            ?: "",
                        modifier =
                        Modifier.fillParentMaxSize(),
                        onClick = { repos.retry() }
                    )
                }
            }
            appendLoadState is LoadState.Loading -> {
                item {
                    LoadingItem(
                        Modifier.fillMaxWidth())
                }
            }
            appendLoadState is LoadState.Error -> {
                val error = appendLoadState.error
                item {
                    ErrorItem(
                        message = error.localizedMessage
                            ?: "",
                        onClick = { repos.retry() })
                }
            }
        }
    }
}

@Composable
fun RepositoryItem(index: Int, item: Repository) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.padding(8.dp).height(120.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = index.toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .weight(0.2f)
                    .padding(8.dp))
            Column(modifier = Modifier.weight(0.8f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodySmall)
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3)
            }
        }
    }
}

@Composable
fun LoadingItem(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment =
        Alignment.CenterHorizontally
    ) { CircularProgressIndicator() }
}

@Composable
fun ErrorItem(
    message: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit) {
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement =
        Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            maxLines = 2,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Red)
        Button(
            onClick = onClick,
            modifier = Modifier.padding(8.dp)
        ) { Text(text = "Try again") }
    }
}