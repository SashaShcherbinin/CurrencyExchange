package feature.account.domain.usecase

import feature.account.domain.entity.ExchangeRequest
import feature.account.domain.entity.ExchangeResult
import kotlinx.coroutines.flow.Flow

interface ExchangeCurrencyUseCase : (ExchangeRequest) -> Flow<Result<ExchangeResult>>