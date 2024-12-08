package feature.dashboard.di

import base.domain.extention.factoryCastOf
import feature.dashboard.domain.usecase.GetDashboardDataUseCase
import feature.dashboard.domain.usecase.GetDashboardDataUseCaseImpl
import feature.dashboard.presentation.DashboardProcessor
import feature.dashboard.presentation.DashboardPublisher
import feature.dashboard.presentation.DashboardReducer
import feature.dashboard.presentation.DashboardRepeater
import feature.dashboard.presentation.DashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun featureDashboardModule(): Module = module {
    viewModelOf(::DashboardViewModel)
    factoryCastOf(::GetDashboardDataUseCaseImpl, GetDashboardDataUseCase::class)
    factoryOf(::DashboardProcessor)
    factoryOf(::DashboardReducer)
    factoryOf(::DashboardPublisher)
    factoryOf(::DashboardRepeater)
}
