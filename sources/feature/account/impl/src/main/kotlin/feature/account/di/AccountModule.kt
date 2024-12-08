package feature.account.di

import base.domain.extention.factoryCastOf
import base.domain.extention.singleCalsOf
import feature.account.data.repository.AccountRepositoryImpl
import feature.account.domain.repository.AccountRepository
import feature.account.domain.usecase.GetAccountListListUseCaseImpl
import feature.account.domain.usecase.GetAccountListUseCase
import feature.account.domain.usecase.TransferMoneyUseCase
import feature.account.domain.usecase.TransferMoneyUseCaseImpl
import org.koin.core.module.Module
import org.koin.dsl.module

fun featureAccountModule(): Module = module {
    factoryCastOf(::GetAccountListListUseCaseImpl, GetAccountListUseCase::class)
    factoryCastOf(::TransferMoneyUseCaseImpl, TransferMoneyUseCase::class)
    singleCalsOf(::AccountRepositoryImpl, AccountRepository::class)
}