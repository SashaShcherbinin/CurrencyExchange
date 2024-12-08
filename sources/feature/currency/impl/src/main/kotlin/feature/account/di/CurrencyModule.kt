package feature.account.di

import base.domain.extention.factoryCastOf
import base.domain.extention.singleCalsOf
import feature.account.data.CurrencyRepositoryImpl
import feature.account.domain.repository.CurrencyRepository
import feature.account.domain.usecase.ExchangeCurrencyUseCase
import feature.account.domain.usecase.ExchangeCurrencyUseCaseImpl
import feature.account.domain.usecase.GetCurrencyListUseCase
import feature.account.domain.usecase.GetCurrencyListUseCaseImpl
import org.koin.core.module.Module
import org.koin.dsl.module

fun featureCurrencyModule(): Module = module {
    factoryCastOf(::ExchangeCurrencyUseCaseImpl, ExchangeCurrencyUseCase::class)
    factoryCastOf(::GetCurrencyListUseCaseImpl, GetCurrencyListUseCase::class)
    singleCalsOf(::CurrencyRepositoryImpl, CurrencyRepository::class)
}