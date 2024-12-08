package feature.dashboard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import base.compose.local.LocalNavigation
import base.compose.navigation.Back
import base.compose.navigation.navigateTo
import base.compose.theme.PreviewAppTheme
import base.compose.theme.PreviewWithThemes
import feature.account.domain.entity.Account
import feature.dashboard.R
import feature.dashboard.presentation.NumberFormatter.formatCurrency
import feature.dashboard.presentation.NumberFormatter.formatNumber
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = koinViewModel()) {
    val state: DashboardState by viewModel.state.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    LaunchedEffect(key1 = viewModel) {
        viewModel.event.collect {
            when (it) {
                is DashboardEvent.ShowMessage -> {
                    showDialog = true
                    message = it.message
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("OK")
                }
            },
            text = { Text(message) }
        )
    }

    Screen(state, viewModel::process)
}

@Composable
private fun Screen(
    state: DashboardState,
    processor: (DashboardIntent) -> Unit = {},
) {
    Scaffold(
        topBar = { Toolbar() },
    ) { innerPadding ->
        Body(
            innerPadding = innerPadding,
            state = state,
            processor = processor,
        )
    }
}

@Composable
private fun Body(
    innerPadding: PaddingValues,
    state: DashboardState,
    processor: (DashboardIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentAlignment = Alignment.Center,
    ) {
        when (state.contentState) {
            is ContentState.Loading -> {
                CreateLoading()
            }

            is ContentState.Content -> {
                CreateContent(state = state, processor = processor)
            }

            is ContentState.Error -> {
                CreateError(state = state)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Toolbar() {
    val navController = LocalNavigation.current
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text("Currency converter")
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigateTo(Back)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.dashboard_ic_close),
                    contentDescription = stringResource(id = R.string.dashboard_close_button)
                )
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CreateContent(
    state: DashboardState,
    processor: (DashboardIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "MY BALANCES",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
        ) {
            state.accountList.forEach { element ->
                SuggestionChip(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = { /* Handle click */ },
                    label = { Text(text = element.amount.formatCurrency(element.currency)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "CURRENCY EXCHANGE",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        SellView(state = state, processor = processor)
        ReceiveView(state = state, processor = processor)

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            onClick = {
                processor(DashboardIntent.Exchange)
            }) {
            Text("Submit")
        }
    }
}

@Composable
private fun SellView(
    state: DashboardState,
    processor: (DashboardIntent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Red),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.dashboard_ic_arrow_upward),
                contentDescription = "Sell Icon",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Sell",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        MinimalTextField(
            value = state.amount.toString(),
            onValueChange = {
                processor(DashboardIntent.ChangeAmount(it))
            },
        )

        Spacer(modifier = Modifier.width(4.dp))

        DropdownMenuSelector(
            currency = state.fromCurrency ?: "",
            currencyList = state.fromCurrencyList,
        ) {
            processor(DashboardIntent.ChangeFromCurrency(it))
        }
    }
}

@Composable
private fun ReceiveView(
    state: DashboardState,
    processor: (DashboardIntent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Green),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.dashboard_ic_arrow_downward),
                contentDescription = "Sell Icon",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Receive",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = state.toAmount.formatNumber()
        )

        Spacer(modifier = Modifier.width(4.dp))

        DropdownMenuSelector(
            currency = state.toCurrency ?: "",
            currencyList = state.toCurrencyList,
        ) {
            processor(DashboardIntent.ChangeToCurrency(it))
        }
    }
}

@Composable
fun MinimalTextField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = Modifier.padding(end = 8.dp),
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.End,
            fontSize = 16.sp
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
    )
}

@Composable
fun DropdownMenuSelector(
    currency: String,
    currencyList: List<String>,
    onCurrencySelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
            .clickable { expanded = true }
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.wrapContentSize()
        ) {
            Text(
                text = currency,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(end = 4.dp)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown arrow",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencyList.forEach { currency ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onCurrencySelected(currency)
                    },
                    text = {
                        Text(currency)
                    }
                )
            }
        }
    }
}

@Composable
private fun CreateError(
    state: DashboardState,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = "Network error",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun CreateLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            trackColor = MaterialTheme.colorScheme.secondary,
        )
    }
}

@PreviewWithThemes
@Composable
fun PreviewScreen() {
    PreviewAppTheme {
        Screen(
            DashboardState(
                contentState = ContentState.Content,
                accountList = listOf(
                    Account(
                        amount = 100.toBigDecimal(),
                        currency = "EUR"
                    ),
                    Account(
                        amount = 200.toBigDecimal(),
                        currency = "USD"
                    ),
                )
            )
        )
    }
}
