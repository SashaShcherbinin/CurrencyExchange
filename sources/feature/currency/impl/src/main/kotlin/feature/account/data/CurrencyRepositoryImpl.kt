package feature.account.data

import base.storage.common.cashe.CachePolicy
import base.storage.common.storage.LocalStorage
import feature.account.domain.entity.ExchangeRequest
import feature.account.domain.entity.ExchangeResult
import feature.account.data.dto.ExchangeDto
import feature.account.data.mapper.toDomain
import feature.account.data.service.CurrencyApi
import feature.account.domain.repository.CurrencyRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit

class CurrencyRepositoryImpl(private val httpClient: HttpClient) : CurrencyRepository {

    private val exchangeLocalStorage = LocalStorage<Unit, ExchangeDto>(
        maxElements = 1,
        cachePolicy = CachePolicy.create(10, TimeUnit.SECONDS),
        network = {
            httpClient.get {
                url(CurrencyApi.GET_EXCHANGE_RATES)
            }.body<ExchangeDto>()
        },
    )

    override fun exchange(exchangeRequest: ExchangeRequest): Flow<Result<ExchangeResult>> =
        exchangeLocalStorage.get(Unit).map { dtoResult ->
            dtoResult.map { exchangeDto ->
                exchangeDto.toDomain(
                    fromCurrency = exchangeRequest.fromCurrency,
                    toCurrency = exchangeRequest.toCurrency,
                    amount = exchangeRequest.amount,
                )
            }
        }

    override fun getCurrencyList(): Flow<Result<List<String>>> =
        exchangeLocalStorage.get(Unit).map { dtoResult ->
            dtoResult.map { exchangeDto ->
                exchangeDto.rates.keys.toList()
            }
        }
}
