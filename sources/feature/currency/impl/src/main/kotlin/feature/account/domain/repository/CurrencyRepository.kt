package feature.account.domain.repository

import feature.account.domain.entity.ExchangeRequest
import feature.account.domain.entity.ExchangeResult
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {

    fun exchange(exchangeRequest: ExchangeRequest): Flow<Result<ExchangeResult>>

    fun getCurrencyList(): Flow<Result<List<String>>>
}