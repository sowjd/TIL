/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cupcake

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cupcake.data.DataSource
import com.example.cupcake.ui.OrderSummaryScreen
import com.example.cupcake.ui.OrderViewModel
import com.example.cupcake.ui.SelectOptionScreen
import com.example.cupcake.ui.StartOrderScreen

// @StringRes: string resource를 참조한다
enum class CupcakeScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Flavor(title = R.string.choose_flavor),
    Pickup(title = R.string.choose_pickup_date),
    Summary(title = R.string.order_summary)
}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@Composable
fun CupcakeAppBar(
    currentScreen: CupcakeScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun CupcakeApp(
    viewModel: OrderViewModel = viewModel(),
    // rememberNavController()를 호출해서 NavHostController를 가져올 수 있다.
    // 경로 이동을 담당한다.
    navController: NavHostController = rememberNavController()
) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = CupcakeScreen.valueOf(
        backStackEntry?.destination?.route ?: CupcakeScreen.Start.name
    )

    Scaffold(
        topBar = {
            CupcakeAppBar(
                currentScreen,
                // back stack이 있을 때만 뒤로가기 화살표를 표시한다.
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        /*
        NavController: 화면 간 이동을 담당합니다.
        NavGraph: 이동할 composable 화면을 mapping합니다.
        NavHost: NavGraph의 현재 화면을 표시하는 컨테이너 역할을 하는 컴포저블입니다.

        NavHostController:
         */
        NavHost(
            navController = navController, // NavHostController 인스턴스, navigate()를 호출하여 다른 화면으로 이동할 수 있다.
            startDestination = CupcakeScreen.Start.name, // NavHost를 처음 표시할 때 표시되는 화면을 정의한다.
            modifier = Modifier.padding(innerPadding) // NavHost도 composable function이므로 Modifier 사용할 수 있다.
        ) {
            // NavGraph에 Composable 화면을 mapping
            composable(route = CupcakeScreen.Start.name) { // route: 경로 이름
                // 경로에 표시할 컴포저블을 호출한다.
                StartOrderScreen(
                    quantityOptions = DataSource.quantityOptions, // quantityOptions는 singleton 객체
                    onNextButtonClicked = {
                        viewModel.setQuantity(it) // cupcake 개수
                        // navigate()를 사용하면 back stack에 화면이 쌓인다.
                        // 뒤로가기 버튼으로 화면을 삭제할 수 있다.
                        navController.navigate(CupcakeScreen.Flavor.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }

            composable(route = CupcakeScreen.Flavor.name) {
                val context = LocalContext.current // LocalContext.current는 Android의 Context이다.
                SelectOptionScreen(
                    subtotal = uiState.price,
                    // R.string.*의 문자열로 리스트 만들어서 리턴
                    options = DataSource.flavors.map { id -> context.resources.getString(id) },
                    // onSelectionChanged에 전달된 값(it)을 setFlavor() 에 전달
                    onSelectionChanged = { viewModel.setFlavor(it) },
                    onCancelButtonClicked = { cancelOrderAndNavigateToStart(viewModel, navController) },
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Pickup.name) },
                    modifier = Modifier.fillMaxHeight()
                )
            }

            composable(route = CupcakeScreen.Pickup.name) {
                SelectOptionScreen(
                    subtotal = uiState.price,
                    options = uiState.pickupOptions,
                    onSelectionChanged = { viewModel.setDate(it) },
                    onCancelButtonClicked = { cancelOrderAndNavigateToStart(viewModel, navController) },
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Summary.name) },
                    modifier = Modifier.fillMaxHeight()
                )
            }

            composable(route = CupcakeScreen.Summary.name) {
                OrderSummaryScreen(
                    orderUiState = uiState,
                    onCancelButtonClicked = { cancelOrderAndNavigateToStart(viewModel, navController) },
                    onSendButtonClicked = { subject: String, summery: String -> },
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

private fun cancelOrderAndNavigateToStart(
    viewModel: OrderViewModel,
    /*
    NavHostController는 경로 이동을 담당하는데, 각 화면마다 만들지 않고 한 곳(CupcakeApp)에서 만들고 파라미터로 전달한다.
    장점은
    1) Navigation logic이 한 곳에 있기 때문에 유지관리에 좋다.
    2) 개별 화면에서 navigation을 하지 않음으로써 버그를 방지할 수 있다. (각 개별화면은 navigation logic을 모른다.)
    3) Navigation을 사용할지 말지 선택해야 할 경우 여기서 한번에 설정할 수 있다.

    이렇게 구현했을 때 해줘야 하는 작업은 사용자가 화면 전환을 일으키는 버튼을 클릭할 때 실행할 컴포저블 함수를 전달해줘야 한다.
     */
    navController: NavHostController
) {
    viewModel.resetOrder()
    navController.popBackStack(
        route = CupcakeScreen.Start.name, // route: 돌아갈 경로 (The topmost destination to retain.)
        inclusive = false // Whether the given destination should also be popped.
    )
}