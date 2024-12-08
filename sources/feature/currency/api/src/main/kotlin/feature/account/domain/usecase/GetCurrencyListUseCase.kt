package feature.account.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetCurrencyListUseCase : () -> Flow<Result<List<String>>>